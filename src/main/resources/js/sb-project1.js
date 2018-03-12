(function ($) {		
	AJS.toInit(function () {		
		//create issue 에 해당   (Administrator 로 이동후 상단의   Create 클릭시)
		var form = $('#issue-create');
		if (form.length) {
        	 console.log("Create issue from Administrator");        	 
        	 //alert ("issue-create-project-name : " + $("#issue-create-project-name").text()); //프로젝트 이름으로...
        	 SB_INCLUDER.sb_project("project", $("#issue-create-project-name").text(), $(document) );
        	 
        	 return;
		}        
    });
})(AJS.$);


    