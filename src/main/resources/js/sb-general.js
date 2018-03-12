var SB_INCLUDER = {
	_cacheBuild: {},
	$contextObject: null,
	
	sb_project: function (flag, value, $contextObject) {	
		SB_INCLUDER.$contextObject = $contextObject;
		console.log("flag : " + flag + ", value : " + value);  //flag 와  value 를 가지고 projectType 판별 후  cf(customfield_10007) 를 가지고 온다
		
		var contextPath =$("meta[name='ajs-context-path']").attr('content');
		var url = contextPath + "/rest/sb/1.0/project/list";
		$.ajax({
			type: 'get',
			url: url ,
			async: false,
			success: function(data, textStatus, response) {
				var cf = "customfield_10007";
				$("#" + cf).auiSelect2({
					placeholder: "Select Project2",
					 allowClear: true,
					//data : [{id: 0, text: 'story2'},{id: 1, text: 'bug2'},{id: 2, text: 'task2'}]
					data: data
				
				});				
				//$("#" + cf).select2('open'); //데이터영역이 열려짐)
			},
			error :function(response, textStatus, errorThrown) {
				alert("code :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}		
		});			
	},

	sb_build: function(issueId, $contextObject) {			
		if (SB_INCLUDER._cacheBuild[issueId] == null) {
            SB_INCLUDER._cacheBuild[issueId] = {};
            //alert ("null");
            
            //issueId를 가지고  actionId(action_id_41) 를 가져올지 결정한다. (rest 에서 결정, 세팅할 필요없으면 cacheBuild[issueId]이  X 값 세팅 )
            //action_id_41 :: rest 에서 가져온값.            
            //var actionId = "action_id_41";            
            var contextPath =$("meta[name='ajs-context-path']").attr('content');
    		var url = contextPath + "/rest/sb/1.0/project/list";
    		var actionId = "";
    		$.ajax({
    			type: 'get',
    			url: url ,
    			async: false,
    			success: function(data, textStatus, response) {
    				actionId = "action_id_41";
    				SB_INCLUDER._cacheBuild[issueId] = actionId;
    	            
    			},
    			error :function(response, textStatus, errorThrown) {
    				alert("code :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
    			}		
    		});    		
		}
		
		SB_INCLUDER.sb_build_execute("action_id_41", $contextObject );
		
	}, 
	
	sb_build_execute: function (actionId, $contextObject) {
		SB_INCLUDER.$contextObject = $contextObject;
		
		$("#" + actionId).click(function () {
			alert ("TTTTTTaaa3333 :: " + actionId);
			location.reload();
			return false;			
		});
		
	}
};


(function ($) {
    AJS.toInit(function () {
    	
		JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (e, $context, reason) {
			//Dashboard, Board 에서
			if (reason == JIRA.CONTENT_ADDED_REASON.dialogReady) { 
				if ($context.parent('#create-issue-dialog').length ||  $context.parent('#create-subtask-dialog').length || $context.parent('#prefillable-create-issue-dialog').length) {
					//alert ("projectfield : " + $("#project-field").val());  SW-SCRUM01 (SWSCRUM01)  => SW-SCRUM01 이 프로젝트이름임..					
					SB_INCLUDER.sb_project("project", $("#project-field").val(), $(document) );
					return;
				}                     

//				if ($context.children('#issue-workflow-transition').length)
//					console.log("bbb transition");
//                     //JS_INCLUDER.executeIssueScripts($context.find('input[name="id"]').val(), JS_INCLUDER.CONTEXT_TRANSITION, $context);
             }
		});
    });
	
	
})(AJS.$);

