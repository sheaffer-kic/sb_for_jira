(function ($) {
    AJS.toInit(function () {    	
		//[start] top Issues  에서 이슈 직접 선택 (보기)    	
		var key = $("#key-val").attr("rel");
		if (key) {  
			console.log("###### VIEW ISSUE KEY-val : "+ key);
			SB_INCLUDER.sb_build(key, $(document) );
		}
		//[end] top Issues  에서 이슈 직접 선택 (보기)
		
		
		//[start] //이슈선택(왼쪽) > 이슈정보 노출(오른쪽)
		JIRA.bind(JIRA.Events.ISSUE_REFRESHED,function(event, issueKey) { //이슈선택(왼쪽) > 이슈정보 노출(오른쪽)			
			SB_INCLUDER.sb_build(issueKey, $(document) );
		});
		//[end] //이슈선택(왼쪽) > 이슈정보 노출(오른쪽)
		
		JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (event, $context, reason) {
			//create or edit
			if (reason == "dialogReady") {
				var chkValue = 0; //create
				var issueId =  $("form[name='jiraform']").find('input[name="id"]').val();
				console.log("issueId : " + issueId);
				if (issueId) { //값있음  
					chkValue = 1; //edit
				} else { //값없음
					issueId =  $("form[id='issue-workflow-transition']").find('input[name="id"]').val();
					if (issueId) chkValue = 1;
					
				}
				
				if (chkValue ==1) { //EDIT
					//issueId : 이슈 아이디
					SB_INCLUDER.sb_project(2, issueId, $(document) ); //1:projectName, 2:issueId
				}				
			}
		});	
		
    });
})(AJS.$);
