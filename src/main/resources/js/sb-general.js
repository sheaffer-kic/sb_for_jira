var SB_INCLUDER = {
	_cacheBuild: {},
	$contextObject: null,
	
	sb_project: function (flag, value, $contextObject) {	
		SB_INCLUDER.$contextObject = $contextObject;
		console.log("flag : " + flag + ", value : " + value);  //flag 와  value 를 가지고 projectType 판별 후  cf(customfield_10007) 를 가지고 온다
		
		var url = contextPath + "/rest/sb/1.0/build/project/list";
		var obj = new Object();
		obj.flag = flag;
		obj.value = value;
		
		console.log("dd : " + JSON.stringify(obj));
		$.ajax({
			type: 'POST',
			url: url ,
			data: JSON.stringify(obj),
			async: false,
    		contentType: 'application/json',
            dataType: 'json',
			success: function(data, textStatus, response) {
				console.log("result : " + JSON.stringify(data));
				if (data.result == "ok") {
					var cf = data.cfId;					
					$("#" + cf).auiSelect2({
						placeholder: "Select Build Project",
						 allowClear: true,
						data: data.sbList					
					});		
				} else if (data.result == "fail") {
					alert (data.message);
				}
			},
			error :function(response, textStatus, errorThrown) {
				alert("code22 :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}		
		});			
	},

	sb_build: function(issueId, $contextObject) {
		//var actionId = "action_id_41";
		var actionId = "";
		if (SB_INCLUDER._cacheBuild[issueId] == null) {
            SB_INCLUDER._cacheBuild[issueId] = {};
            
    		var url = contextPath + "/rest/sb/1.0/build/action/id/" + issueId;
    		//var actionId = "action_id_41";
    		$.ajax({
    			type: 'get',
    			url: url ,
    			async: false,
    			success: function(data, textStatus, response) {    				
    				if (data.result == "ok") {
    					actionId = "action_id_" + data.actionId;
    				}     				
    				SB_INCLUDER._cacheBuild[issueId] = actionId;    				
    			},
    			error :function(response, textStatus, errorThrown) {
    				alert("code33 :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
    			}		
    		});    		
		}
		
		actionId = SB_INCLUDER._cacheBuild[issueId] ;
		console.log(">> actionID : " + actionId);
		if (actionId != "") {
			SB_INCLUDER.sb_build_execute(actionId, issueId, $contextObject );
		}
		
	}, 
	
	sb_build_execute: function (actionId, issueId, $contextObject) {
		SB_INCLUDER.$contextObject = $contextObject;
		
		$("#" + actionId).click(function () {
    		var url = contextPath + "/rest/sb/1.0/build/execute";
    		var obj = new Object();
    		obj.issueId = issueId;

    		var reload = false;
    		$.ajax({
    			type: 'POST',
    			url: url ,
    			async: false,
    			data: JSON.stringify(obj),		
    		    dataType: "json",     
    		    contentType: "application/json; charset=utf-8",     			
    		    //contentType: "application/json",
    			success: function(data, textStatus, response) {  
    				if (data.result == "ok") {
    					obj = new Object();
    					obj.issueKey = data.issueKey;
    					obj.flag = "I"; //빌드중 (ㅑ), 빌드성공(ㅖ), 빌드실패(F) 3가지가 있으면, 여기서는 빌드중
    					url = contextPath + "/rest/sb/1.0/build/update/result";

    					$.ajax({
    		    			type: 'POST',
    		    			url: url ,
    		    			async: false,
    		    			data: JSON.stringify(obj),		
    		    		    dataType: "json",     
    		    		    contentType: "application/json; charset=utf-8", 
    		    		    success:function (data, textStatus, response) {      		    		    	
    		    		    	if (data.result == "ok") {
    		    		    		alert("Build Request Success !!!!");
    		    		    		reload = true;	
    		    		    	} else {
    		    		    		alert ("error : " + data.message);
    		    		    	}
    		    		    	
    		    		    }, 
    		    			error :function(response, textStatus, errorThrown) {
    		    				alert("code :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
    		    			}    		    		    
    		    		    
    					});
    					//reload = true;
    				} else {
    					alert ("error :" + data.message);
    				}
    			},
    			error :function(response, textStatus, errorThrown) {
    				alert("code :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
    			}		
    		});  
	
    		if (reload) location.reload();
    		return false;
		});
		
	}
};

(function ($) {
    AJS.toInit(function () {
    	var contextPath =$("meta[name='ajs-context-path']").attr('content');
    	
		JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (e, $context, reason) {
			//Dashboard, Board 에서
			if (reason == JIRA.CONTENT_ADDED_REASON.dialogReady) { 
				if ($context.parent('#create-issue-dialog').length ||  $context.parent('#create-subtask-dialog').length || $context.parent('#prefillable-create-issue-dialog').length) {
					//alert ("projectfield : " + $("#project-field").val());  SW-SCRUM01 (SWSCRUM01)  => SW-SCRUM01 이 프로젝트이름임..
					var project = $("#project-field").val()
					SB_INCLUDER.sb_project(1, project.substring(0, project.indexOf("(")), $(document) );  //1:projectName, 2:issueId
					return;
				}                     

//				if ($context.children('#issue-workflow-transition').length)
//					console.log("bbb transition");
//                     //JS_INCLUDER.executeIssueScripts($context.find('input[name="id"]').val(), JS_INCLUDER.CONTEXT_TRANSITION, $context);
             }
		});
    });
	
	
})(AJS.$);

