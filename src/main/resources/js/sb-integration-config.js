var projKey;
var contextPath;
var getObjData;

AJS.toInit(function(){
	//require(['aui/form-validation']);
	projKey = AJS.$("meta[name='projectKey']").attr('content');
	contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');	
	console.log("contextPath=====>"+contextPath);
	
	//Add Configuration
	AJS.$("#add-integration-button").click(function() {			
		setShowDialog();
	});	
	
	// Hides the dialog (Add Configuration_)
	AJS.$("#dialog-close-button").click(function(e) {
	    e.preventDefault();
	    AJS.dialog2("#add-dialog").hide();
	});

	// save 버튼(Add Configuration_)
	AJS.$("#dialog-submit-button").click(function(e) {
		e.preventDefault();		
		goConfigSave();
	});	
});

//form 초기화
function setInitForm() {	
	if(AJS.$('#id').val()==""){		
		getObjData = null;
	}
	AJS.$('#id').val("");
	AJS.$("select[id=dply-issue-type] option").remove();
	AJS.$("select[id=dply-trgt] option").remove();
	AJS.$("select[id=dply-progress] option").remove();
	AJS.$("select[id=dply-success] option").remove();
	AJS.$("select[id=dply-fail] option").remove();
}

//팝업화면세팅
function setShowDialog(){
	AJS.dialog2("#add-dialog").show();
	var url = contextPath + "/rest/sb/1.0/integration/issueType/"+projKey;

	console.log("url=====>"+url);

	AJS.$.ajax({
		type: 'get',		
		url: url, 
		async: false, 
		//data: sendData,
		dataType: 'json',
		async: false,
		success: function(data, textStatus, jqXHR) {
			
			setInitForm();
			
			console.log("issueType_list data=====>"+JSON.stringify(data));
			
			AJS.$.each(data, function(key) {
				var info = data[key];
				AJS.$('#dply-issue-type').append(AJS.$('<option>', { 
			        value: info.id,
			        text : info.name
			    }));
			});

			
			
			if(getObjData==null){
				setDplyTrgtStatus(data[0].id);
			}else{
				var issueType = getObjData.issueType;
				AJS.$('#dply-issue-type').val(issueType).prop('selected', true);
				setDplyTrgtStatus(issueType);
			}
		},
		error :function(jqXHR, textStatus, errorThrown) {
			console.log('error: ' + textStatus);
		}
	});	
}

//onchange 이슈타입 - 배포 대상 이슈 상태 영역 세팅
function setDplyTrgtStatus(issueTypeId) {
	
	//초기화
	AJS.$("select[id=dply-trgt] option").remove();
	AJS.$("select[id=dply-progress] option").remove();
	AJS.$("select[id=dply-success] option").remove();
	AJS.$("select[id=dply-fail] option").remove();	
	
	var url = contextPath + "/rest/sb/1.0/integration/status/" + projKey + "/" + issueTypeId;

	AJS.$.ajax({
		type: 'get',	
		async: false, 
		url: url, 
		dataType: 'json',
		async: false,
		success: function(data, textStatus, jqXHR) {		
			AJS.$("select[id=dply-trgt] option").remove();
			AJS.$.each(data, function(key) {
				var info = data[key];
				AJS.$('#dply-trgt').append(AJS.$('<option>', { 
			        value: info.id + "*" + info.stepId,
			        text : info.name
			    }));
			});
			
			if(getObjData==null){
				setDplyProgress(issueTypeId, data[0].stepId);
			}else{
				var trgt = getObjData.buildTargetId;
				var trgtStepId = getObjData.buildStepId;
				
				AJS.$('#dply-trgt').val(trgt+"*"+trgtStepId).prop('selected', true);
				setDplyProgress(issueTypeId, trgtStepId);
			}
			
		},
		error :function(jqXHR, textStatus, errorThrown) {
			console.log('error: ' + textStatus);
		}
	});				
}

//빌드대상 이슈상태 onchange시 사용
function setDplyProgressFirst(value) {	
	var arrTemp = value.split("*");
	setDplyProgress(AJS.$('#dply-issue-type').val(), arrTemp[1]);
}

//빌드를 수행할 액션
function setDplyProgress(issueTypeId, trgtStepId) {
	
	//초기화
	//AJS.$("select[id=dply-trgt] option").remove();
	AJS.$("select[id=dply-progress] option").remove();
	AJS.$("select[id=dply-success] option").remove();
	AJS.$("select[id=dply-fail] option").remove();
	
	var url = contextPath + "/rest/sb/1.0/integration/next/status/" + projKey + "/" + issueTypeId + "/" + trgtStepId;
	AJS.$.ajax({
		type: 'get',		
		url: url, 
		dataType: 'json',
		async: false,
		success: function(data, textStatus, jqXHR) {		
			AJS.$.each(data, function(key) {
				var info = data[key];				
				AJS.$('#dply-progress').append(AJS.$('<option>', { 
			        value: info.id + "*" + info.actionId,
			        text : info.name
			    }));	
			
			});			
			
			if(getObjData==null){
				setSuccessFailStatus(data[0].id);
			}else{
				var progress = getObjData.buildProgressId;
				var progressAction = getObjData.buildProgressAction;				
				var concatProgress = progress+"*"+progressAction;
				AJS.$('#dply-progress').val(concatProgress).prop('selected', true);
				setSuccessFailStatus(concatProgress);
			}
			
		},
		error :function(jqXHR, textStatus, errorThrown) {
			console.log('error: ' + textStatus);
		}
	});		
}

//onchange 배포중 이슈 상태 -   배포 성공 시 수행할 액션, 배포 실패 시 수행 할 액션 영역 세팅
function setSuccessFailStatus(value) {
	var arrTemp = value.split("*");
	var issueTypeId =  AJS.$('#dply-issue-type').val() ;

	//초기화
	//AJS.$("select[id=dply-trgt] option").remove();
	//AJS.$("select[id=dply-progress] option").remove();
	AJS.$("select[id=dply-success] option").remove();
	AJS.$("select[id=dply-fail] option").remove();	
	
	var url = contextPath + "/rest/sb/1.0/integration/action/" + projKey + "/" + issueTypeId + "/" + arrTemp[0];
	
	AJS.$.ajax({
		type: 'get',		
		url: url, 
		dataType: 'json',
		async: false,
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
			
			if(getObjData!=null){
				var success = getObjData.buildSuccessId;
				var fail = getObjData.buildFailId;				
				AJS.$('#dply-success').val(success).prop('selected', true);
				AJS.$('#dply-fail').val(fail).prop('selected', true);	
			}
			
		},
		error :function(jqXHR, textStatus, errorThrown) {
			console.log('error: ' + textStatus);
		}
	});		
}


//저장(등록, 수정)
function goConfigSave() {
	console.log(" ::>>>>>> Save ::>>>>>> ");
	
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
	
	var id =  AJS.$('#id').val() ;
	var isDup = "N";
	if (id != "") {
		obj.id = id;		
	}else{
		//중복체크
		isDup = fn_dupCheck(projKey, issueType);
	}
	
	if(isDup != "N"){
		console.log("isDup !!!! ::>>>>>> " + isDup);	
		return;
	}
	
 	
	url = contextPath + "/secure/SbIntegrationConfig!save.jspa";

	console.log("obj ::>>>>>> " + JSON.stringify(obj));	
	
	AJS.$.ajax({
		type: 'POST',		
		url: url,
		data: JSON.stringify(obj),
		//dataType: "json",
        contentType: "application/json; charset=utf-8",	
        async: false,
		success: function(data, textStatus, response) {
			AJS.messages.success("#aui-message-bar", {
			    title: 'Success!',
			    body: '<p> Save Smart Builder Configuration</p>'
			});
			AJS.dialog2("#add-dialog").hide();
			location.href= contextPath + "/secure/SbIntegrationConfig!default.jspa?projectKey=" + projKey;
		},
		error :function(response, textStatus, errorThrown) {
			console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
		}
		
	});	
}

//중복체크
function fn_dupCheck(projKey, issueType) {

	var url = contextPath + "/rest/sb/1.0/integration/dupCheck/" + projKey +"/"+ issueType
	
	//console.log("fn_dupCheck url===>"+url);
	
	var isDup = "N";
	
	AJS.$.ajax({
		type: 'get',		
		url: url,
		//data: JSON.stringify(obj),
		dataType: "json",
        contentType: "application/json; charset=utf-8",	
        async: false,
		success: function(data, textStatus, response) {

			if(data.projectKey != null && data.issueType != null){
				isDup = "Y";
				AJS.messages.error("#aui-message-bar-dialog", {
				    title: 'Fail!',
				    body: '<p> Duplication Smart Builder Configuration</p>'
				});				
			}			
		},
		error :function(response, textStatus, errorThrown) {
			console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			isDup = "Error";
		}				
	});
	
	return isDup;
}

//보기
function fn_view(id) {	
	getObjData = null;
	AJS.$('#id').val(id);
	
	var url = contextPath + "/rest/sb/1.0/integration/select/" + id
	
	AJS.$.ajax({
		type: 'get',		
		url: url,
		//data: JSON.stringify(obj),
		dataType: "json",
        contentType: "application/json; charset=utf-8",	
        async: false,
		success: function(data, textStatus, response) {
			console.log("data ::>>>>>> " + JSON.stringify(data));	
			//값세팅	
			//fn_setValueDialog(data);
			getObjData = data;
			
			setShowDialog();
		},
		error :function(response, textStatus, errorThrown) {
			console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
		}				
	});		
	
	
}

//보기 데이터 세팅 - 안씀
function fn_setValueDialog(data){
/*
	var issueType = data.issueType;

	var trgt = data.buildTargetId;
	var trgtStepId = data.buildStepId;
	
	var progress = data.buildProgressId;
	var progressAction = data.buildProgressAction;
	
	var success = data.buildSuccessId;
	var fail = data.buildFailId;
	
	AJS.$('#dply-issue-type').val(issueType).prop('selected', true);
	
	setDplyTrgtStatus(issueType);
	AJS.$('#dply-trgt').val(trgt+"*"+trgtStepId).prop('selected', true);
	
	console.log("dply-progress get befor====>"+AJS.$('#dply-progress').val());
	setDplyProgress(issueType, trgtStepId);		
	var concatProgress = progress+"*"+progressAction;
	AJS.$('#dply-progress').val(concatProgress).prop('selected', true);
	console.log("concatProgress====>"+concatProgress);
	console.log("dply-progress get after ====>"+AJS.$('#dply-progress').val());	
	
	setSuccessFailStatus(concatProgress);
	AJS.$('#dply-success').val(success).prop('selected', true);
	AJS.$('#dply-fail').val(fail).prop('selected', true);
*/
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
	        async: false,
			success: function(data, textStatus, response) {
				AJS.messages.success("#aui-message-bar", {
				    title: 'Success!',
				    body: '<p> Delete SmartBuild Integration Configuration</p>'
				});
				location.href= contextPath + "/secure/SbIntegrationConfig!default.jspa?projectKey=" + projKey;
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});	
		
		
	//}
}
