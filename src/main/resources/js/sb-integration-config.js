var projKey;
var contextPath;

AJS.toInit(function(){
	//require(['aui/form-validation']);
	projKey = AJS.$("meta[name='projectKey']").attr('content');
	contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');	
	console.log("contextPath=====>"+contextPath);
	
	//Add Configuration
	AJS.$("#add-integration-button").click(function() {	
		
		AJS.dialog2("#add-dialog").show();
		var url = contextPath + "/rest/sb/1.0/integration/issueType/"+projKey;

		console.log("url=====>"+url);

		AJS.$.ajax({
			type: 'get',		
			url: url, 
			async: false, 
			//data: sendData,
			dataType: 'json',
			success: function(data, textStatus, jqXHR) {
				
				setInitForm();
				
				//var issueType_list = data.projIssueTypeList;
				
				console.log("issueType_list data=====>"+JSON.stringify(data));
				
				AJS.$.each(data, function(key) {
					var info = data[key];
					AJS.$('#dply-issue-type').append(AJS.$('<option>', { 
				        value: info.id,
				        text : info.name
				    }));
				});

				setDplyTrgtStatus(data[0].id);					

			},
			error :function(jqXHR, textStatus, errorThrown) {
				alert('error: ' + textStatus);
			}
		});

	});
	
	
	// Hides the dialog (Add Configuration_)
	AJS.$("#dialog-close-button").click(function(e) {
	    e.preventDefault();
	    AJS.dialog2("#add-dialog").hide();
	});

	// Ok 버튼(Add Configuration_)
	AJS.$("#dialog-submit-button").click(function(e) {
		e.preventDefault();
		
		var id =  AJS.$('#cfgId').val() ;
		if (id != "") {
			goConfigUpdate(id);
		} else {
			goConfigInsert();
		}
	});	
});

//form 초기화
function setInitForm() {
	AJS.$('#cfgId').val("");
	AJS.$("select[id=dply-issue-type] option").remove();
	AJS.$("select[id=dply-trgt] option").remove();
	AJS.$("select[id=dply-progress] option").remove();
	AJS.$("select[id=dply-success] option").remove();
	AJS.$("select[id=dply-fail] option").remove();
}

//onchange 이슈타입 - 배포 대상 이슈 상태 영역 세팅
function setDplyTrgtStatus(issueTypeId) {	
	var url = contextPath + "/rest/sb/1.0/integration/status/" + projKey + "/" + issueTypeId;

	AJS.$.ajax({
		type: 'get',	
		async: false, 
		url: url, 
		dataType: 'json',
		success: function(data, textStatus, jqXHR) {		
			AJS.$("select[id=dply-trgt] option").remove();
			AJS.$.each(data, function(key) {
				var info = data[key];
				AJS.$('#dply-trgt').append(AJS.$('<option>', { 
			        value: info.id + "*" + info.stepId,
			        text : info.name
			    }));
			});
			
			setDplyProgress(issueTypeId, data[0].stepId);
		},
		error :function(jqXHR, textStatus, errorThrown) {
			alert('error: ' + textStatus);
		}
	});				
}

function setDplyProgressFirst(value) {
	var arrTemp = value.split("*");
	setDplyProgress(AJS.$('#dply-issue-type').val(), arrTemp[1]);
}

//빌드를 수행할 액션
function setDplyProgress(issueTypeId, trgtStepId) {
	var projKey = AJS.$("meta[name='projectKey']").attr('content');
	var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
	
	// /netxt/status/{projectKey}/{issueTypeId}/{stepId}")
	var url = contextPath + "/rest/sb/1.0/integration/next/status/" + projKey + "/" + issueTypeId + "/" + trgtStepId;
	AJS.$.ajax({
		type: 'get',		
		url: url, 
		dataType: 'json',
		success: function(data, textStatus, jqXHR) {		
			//AJS.$("select[id=dply-trgt] option").remove();
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
			setSuccessFailStatus(data[0].id);
		},
		error :function(jqXHR, textStatus, errorThrown) {
			alert('error: ' + textStatus);
		}
	});		
}

//onchange 배포중 이슈 상태 -   배포 성공 시 수행할 액션, 배포 실패 시 수행 할 액션 영역 세팅
function setSuccessFailStatus(value) {
	var arrTemp = value.split("*");
	var issueTypeId =  AJS.$('#dply-issue-type').val() ;
	
	//ur : http://localhost:9080/rest/jenkins/1/action/NK/10002/10306	
	var url = contextPath + "/rest/sb/1.0/integration/action/" + projKey + "/" + issueTypeId + "/" + arrTemp[0];
	
	AJS.$.ajax({
		type: 'get',		
		url: url, 
		dataType: 'json',
		success: function(data, textStatus, jqXHR) {		
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
		},
		error :function(jqXHR, textStatus, errorThrown) {
			alert('error: ' + textStatus);
		}
	});		
}


//저장
function goConfigInsert() {
	console.log(" ::>>>>>> insert ::>>>>>> ");
	
	var obj = new Object();	   
	// form의 값을 오브젝트에 저장
	obj.projectKey =  projKey;
	
	var issueType =  AJS.$('#dply-issue-type').val();
	obj.issueType =  issueType;				
	obj.issueTypeName = AJS.$("#dply-issue-type option[value='"+issueType+"']").text();
	
	var trgt = AJS.$('#dply-trgt').val();
	obj.trgt = trgt.split("*")[0];
	obj.trgtName = AJS.$("#dply-trgt option[value='"+trgt+"']").text();
	obj.trgtStepId = trgt.split("*")[1];
	
	var progress = AJS.$('#dply-progress').val();
	obj.progress = progress.split("*")[0];
	obj.progressName = AJS.$("#dply-progress option[value='"+progress+"']").text();
	obj.progressAction = progress.split("*")[1];
	
	var success = AJS.$('#dply-success').val();
	obj.success = success;
	obj.successName = AJS.$("#dply-success option[value='"+success+"']").text();				
	
	var fail = AJS.$('#dply-fail').val();
	obj.fail = fail;
	obj.failName = AJS.$("#dply-fail option[value='"+fail+"']").text();
	
	//obj.arrTrgt = trgt.split("*");
	//obj.arrProgress = progress.split("*");
	
	var url = contextPath + "/secure/SbIntegrationConfig!insert.jspa";

	console.log("obj ::>>>>>> " + JSON.stringify(obj));	
	
	AJS.$.ajax({
		type: 'POST',		
		url: url,
		data: JSON.stringify(obj),
		//dataType: "json",
        contentType: "application/json; charset=utf-8",		
		success: function(data, textStatus, response) {
			AJS.dialog2("#add-dialog").hide();
			location.href= contextPath + "/secure/SbIntegrationConfig!default.jspa?projectKey=" + projKey;
		},
		error :function(response, textStatus, errorThrown) {
			alert("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
		}
		
	});	

}


//삭제
function fn_delete(id) {
	//if (confirm("삭제 하시겠습니까?")) {
		
		// JSON형식으로 변환 할 오브젝트
		var obj = new Object();
		
		// form의 값을 오브젝트에 저장
		obj.id = id;
		
		var url = contextPath + "/secure/SbIntegrationConfig!delete.jspa";
		
		AJS.$.ajax({
			type: 'POST',		
			url: url,
			data: JSON.stringify(obj),
			//dataType: "json",
	        contentType: "application/json; charset=utf-8",		
			success: function(data, textStatus, response) {
				/*
				AJS.messages.success("#aui-message-bar", {
				    title: 'Success!',
				    body: '<p> Delete Smart Builder Configuration</p>'
				});
				*/
				location.href= contextPath + "/secure/SbIntegrationConfig!default.jspa?projectKey=" + projKey;
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});	
		
		
	//}
}
