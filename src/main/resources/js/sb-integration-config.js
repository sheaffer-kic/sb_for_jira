var projKey;

var SB_INTE = {	
	fn_list: function () {
		var url = contextPath + "/rest/sb/1.0/config/integration/list/" + projKey;
		
		$.ajax({
			type: 'GET',		
			url: url,
			async:false,
			success: function(data, textStatus, response) {
				var obj = new Object();
				obj.sbInteConfigList = data;				
				$('#tbody').html(kic.sb.inte.config.list(obj, ''));				
				SB_INTE.fn_setEvent(data.length);
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});		
	}

	,
	fn_setEvent: function (iLen) {
		for (var i=0; i < iLen; i++) {
			$("#edit" + i).click(function () {
				var obj = $("#" + this.id);
				SB_INTE.fn_view(obj.attr("id-value"));
			});
			
			$("#delete" + i).click(function () {
				var obj = $("#" + this.id);
				SB_INTE.fn_delete(obj.attr("id-value"));
			});		
		}	
	}	
	
	
	,
	fn_view: function (id) {
		//AJS.$('#id').val(id);		
		var url = contextPath + "/rest/sb/1.0/integration/select/" + id
		
		AJS.$.ajax({
			type: 'get',		
			url: url,
			//data: JSON.stringify(obj),
			dataType: "json",
	        contentType: "application/json; charset=utf-8",	
	        async: false,
			success: function(data, textStatus, response) {
				//console.log("data ::>>>>>> " + JSON.stringify(data));
				viewData = data;
				var objIssueType = new Object();
				objIssueType.id = data.issueType;
				objIssueType.name = data.issueTypeName;				
				var selectedIssueType = [];				
				selectedIssueType.push(objIssueType);
				
				var obj = new Object();
				obj.issueTypeList = selectedIssueType;
				obj.id = id;
				
				$('#add-dialog').html(kic.sb.inte.config.form(obj, ''));
				SB_INTE.setShowDialog();
				$("#dply-issue-type").trigger("change");
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});		
	}
	
	,
	fn_delete: function (id) {		
		var obj = new Object();
		obj.id = id;		
		var url = contextPath + "/secure/SbIntegrationConfig!delete.jspa";
		
		AJS.$.ajax({
			type: 'POST',		
			url: url,
			data: JSON.stringify(obj),
			//dataType: "json",
	        contentType: "application/json; charset=utf-8",	
	        async: false,
			success: function(data, textStatus, response) {
//				AJS.messages.success("#aui-message-bar", {
//				    title: 'Success!',
//				    body: '<p> Delete SmartBuild Integration Configuration</p>'
//				});
				location.reload();
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});			
	}	
	
	,
	//중복체크
	fn_dupCheck: function (projKey, issueType) {
		var url = contextPath + "/rest/sb/1.0/integration/dupCheck/" + projKey +"/"+ issueType;
		var isDup = "N";
		
		AJS.$.ajax({
			type: 'get',		
			url: url,
			dataType: "json",
	        contentType: "application/json; charset=utf-8",	
	        async: false,
			success: function(data, textStatus, response) {
				if(data.projectKey != null && data.issueType != null){
					isDup = "Y";
					AJS.messages.error("#aui-message-bar-dialog", {
					    title: 'Fail!',
					    body: '<p> Duplication Smart Builder Configuration </p>',
					    fadeout : true
					});				
				}			
			},
			error :function(response, textStatus, errorThrown) {
				AJS.messages.error("#aui-message-bar-dialog", {
				    title: 'Fail!',
				    body: '<p> ' + response.responseText + '</p>',
				    fadeout : true
				});					
				isDup = "Y";
			}				
		});
		
		return isDup;
	}	
	
	,
	setShowDialog: function (){	
		AJS.dialog2("#add-dialog").show();	
		
		AJS.$("#dialog-close-button").click(function(e) {
		    e.preventDefault();
		    AJS.dialog2("#add-dialog").hide();
		});
		
		AJS.$("#dialog-submit-button").click(function(e) {
			AJS.$('#valid-submit-form').submit();
		});
	
		AJS.$('#valid-submit-form').on('aui-valid-submit', function(e) {
		    e.preventDefault();
		    SB_INTE.fn_goConfigSave();		
			
		});
		
/*		AJS.formValidation.register(['validationFunction'], function(field) {
			if (field.el.value == "") {
				field.invalidate(AJS.format('', field.args('validationFunction')));
		    	field.invalidate(AJS.format('This is a required field22', field.args('validationFunction')));
		    } else {
		        field.validate();
		    }
		});	*/	
		
		$("#dply-issue-type").change(function () {
			//빌드/배포 대상 이슈 상태 영역 세팅
			SB_INTE.fn_setDplyTrgtStatus(this.value);
		});
				
		$("#dply-trgt").change(function () {
			// 빌드/배포 중 이슈 상태
			var arrTemp = this.value.split("*");
			SB_INTE.fn_setDplyProgress(arrTemp[1]);
		});
		
		$("#dply-progress").change(function () {
			// 빌드/배포  성공 및 실패시 수행 할 액션
			var arrTemp = this.value.split("*");
			SB_INTE.fn_setSuccessFailStatus(arrTemp[0]);
		});		
		
	}
	
	, 
	fn_goConfigSave: function () {
		var obj = new Object();	   
		obj.projectKey =  projKey;
		
		var issueType =  AJS.$('#dply-issue-type').val();
		obj.issueType =  issueType;				
		obj.issueTypeName = AJS.$("#dply-issue-type option[value='"+issueType+"']").text();
		
		var trgt = AJS.$('#dply-trgt').val();
		obj.buildTargetId = trgt.split("*")[0];
		obj.buildTargetName = AJS.$("#dply-trgt option[value='"+trgt+"']").text();
		obj.buildStepId = trgt.split("*")[1];	
		
		var progress = AJS.$('#dply-progress').val();
		obj.buildProgressId = progress.split("*")[0];
		obj.buildProgressName = AJS.$("#dply-progress option[value='"+progress+"']").text();
		obj.buildProgressAction = progress.split("*")[1];
		
		var success = AJS.$('#dply-success').val();
		obj.buildSuccessId = success;
		obj.buildSuccessName = AJS.$("#dply-success option[value='"+success+"']").text();						
		
		var fail = AJS.$('#dply-fail').val();
		obj.buildFailId = fail;
		obj.buildFailName = AJS.$("#dply-fail option[value='"+fail+"']").text();
		
		var id =  AJS.$('#id').val() ;
		var isDup = "N";
		if (id != "") {
			obj.id = id;		
		}else{
			//중복체크
			isDup = SB_INTE.fn_dupCheck(projKey, issueType);
		}
		
		if(isDup == "Y") return;
		
		url = contextPath + "/secure/SbIntegrationConfig!save.jspa";		
		AJS.$.ajax({
			type: 'POST',		
			url: url,
			data: JSON.stringify(obj),
	        contentType: "application/json; charset=utf-8",	
	        async: false,
			success: function(data, textStatus, response) {
//				AJS.messages.success("#aui-message-bar", {
//				    title: 'Success!',
//				    body: '<p> Save Smart Builder Configuration</p>'
//				});
				AJS.dialog2("#add-dialog").hide();
				location.reload();
			},
			
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}			
		});
	}
	
	,
	fn_setDefaultData: function () {	//초기 (fn_add)	
		//issueType 가져오기..
		var url = contextPath + "/rest/sb/1.0/integration/issueType/"+projKey;
		viewData = "";
		
		AJS.$.ajax({
			type: 'get',		
			url: url, 
			dataType: 'json',
			async: false,
			success: function(data, textStatus, jqXHR) {			
				var obj = new Object();
				obj.issueTypeList = data;
				obj.id = "";
				
				$('#add-dialog').html(kic.sb.inte.config.form(obj, ''));
				SB_INTE.setShowDialog();
				$("#dply-issue-type").trigger("change");
			},
			
			error :function(jqXHR, textStatus, errorThrown) {
				console.log('error: ' + textStatus);
			}
		});	

	}
	
	,
	fn_setDplyTrgtStatus: function (issueTypeId) { // 빌드/배포 대상 이슈 상태
		var url = contextPath + "/rest/sb/1.0/integration/status/" + projKey + "/" + issueTypeId;

		AJS.$.ajax({
			type: 'get',	
			url: url, 
			dataType: 'json',
			async: false,
			success: function(data, textStatus, jqXHR) {	
				AJS.$("select[id=dply-trgt] option").remove();
				AJS.$("select[id=dply-progress] option").remove();
				AJS.$("select[id=dply-success] option").remove();
				AJS.$("select[id=dply-fail] option").remove();		
				
				AJS.$.each(data, function(key) {
					var info = data[key];
					AJS.$('#dply-trgt').append(AJS.$('<option>', { 
				        value: info.id + "*" + info.stepId,
				        text : info.name
				    }));
				});
				
				$("#dply-trgt").val(viewData.buildTargetId + "*" + viewData.buildStepId);
				$("#dply-trgt").trigger("change");
			},
			
			error :function(jqXHR, textStatus, errorThrown) {
				console.log('error: ' + textStatus);
			}
		});			
	}
	
	,
	fn_setDplyProgress: function (trgtStepId) { // 빌드/배포 중 이슈 상태
		var issueTypeId = $("#dply-issue-type").val();
		
		var url = contextPath + "/rest/sb/1.0/integration/next/status/" + projKey + "/" + issueTypeId + "/" + trgtStepId;
		AJS.$.ajax({
			type: 'get',		
			url: url, 
			dataType: 'json',
			async: false,
			success: function(data, textStatus, jqXHR) {	
				AJS.$("select[id=dply-progress] option").remove();
				AJS.$("select[id=dply-success] option").remove();
				AJS.$("select[id=dply-fail] option").remove();

				AJS.$.each(data, function(key) {				
					var info = data[key];							
					AJS.$('#dply-progress').append(AJS.$('<option>', { 
				        value: info.id + "*" + info.actionId,
				        text : info.name
				    }));						
				});
				$("#dply-progress").val(viewData.buildProgressId + "*" + viewData.buildProgressAction);					
				$("#dply-progress").trigger("change");				
			},
			
			error :function(jqXHR, textStatus, errorThrown) {
				console.log('error: ' + textStatus);
			}
		});			
	}
	
	,
	fn_setSuccessFailStatus: function (statusId) { // 빌드/배포 성공 및 실패중 수행 할 액션
		var issueTypeId = $("#dply-issue-type").val();
		
		var url = contextPath + "/rest/sb/1.0/integration/action/" + projKey + "/" + issueTypeId + "/" + statusId;		
		AJS.$.ajax({
			type: 'get',		
			url: url, 
			dataType: 'json',
			async: false,
			success: function(data, textStatus, jqXHR) {
				//초기화
				AJS.$("select[id=dply-success] option").remove();
				AJS.$("select[id=dply-fail] option").remove();
				
				AJS.$.each(data, function(key) {
					var info = data[key];
					AJS.$('#dply-success').append(AJS.$('<option>', { 
				        value: info.id,
				        text : info.name 
				    }));
					
					AJS.$('#dply-fail').append(AJS.$('<option>', { 
				        value: info.id,
				        text : info.name
				    }));				
				});
			
				$("#dply-success").val(viewData.buildSuccessId);
				$("#dply-fail").val(viewData.buildFailId);
				
				$("#dply-success").trigger("change");
				$("#dply-fail").trigger("change");				
			},
			
			error :function(jqXHR, textStatus, errorThrown) {
				console.log('error: ' + textStatus);
			}
		});			
	}
};

(function ($) {
	AJS.toInit(function(){
		projKey = AJS.$("meta[name='projectKey']").attr('content');
		var viewData = "";
		var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
		SB_INTE.fn_list();
		
		//Add Configuration
		AJS.$("#add-integration-button").click(function() {		
			SB_INTE.fn_setDefaultData();		
		});			
		
	});
})(AJS.$);

