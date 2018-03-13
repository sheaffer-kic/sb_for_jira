var SB_CONFIG = {	
	sb_project: function (contextPath) {	
		var url = contextPath + "/rest/sb/1.0/config/sw/project/cf/list";
		$.ajax({
			type: 'GET',
			url: url ,
			async: false,
			success: function(data, textStatus, response) {
				$.each(data, function(key) {
					var info = data[key];
					$('#cfId').append(AJS.$('<option>', { 
				        value: info.id,
				        text : info.key 
				    }));									
				});
				$('#cfId').auiSelect2();
			},
			error :function(response, textStatus, errorThrown) {
				alert("code :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}		
		});			
	}
};

AJS.toInit(function(){
	var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
	SB_CONFIG.sb_project(contextPath);

	AJS.$("#delete-sb-submit-button").click(function(e) {
		var id = AJS.$('#id').val();
		fn_delete(id);
	});	
	
	AJS.$('#sb-submit-form').on('aui-valid-submit', function(event) {
	    event.preventDefault();

		var id = AJS.$('#id').val();
		
		if(id==0){
			//alert("insert");
			fn_insert();	
		}else{
			//alert("fn_update");
			fn_update(id);
		}	    
	});	
});
	


function fn_insert() {
	console.log("insert sb");
	
	// JSON형식으로 변환 할 오브젝트
	var obj = new Object();
	   
	// form의 값을 오브젝트에 저장
	obj.sbId = AJS.$('#sbId').val();		
	obj.sbPassword = AJS.$('#sbPassword').val();	
	obj.url = AJS.$('#url').val();		

	var sendData = "sendData="+JSON.stringify(obj);

	console.log("======JSON  new Object(); sendData=====>"+sendData);
	
	var url = contextPath + "/secure/admin/SbConfig!insert.jspa";
	
	AJS.$.ajax({
		type: 'POST',		
		url: url,
		data: sendData,		
		success: function(data, textStatus, response) {
			AJS.messages.success("#aui-message-bar", {
			    title: 'Success!',
			    body: '<p> Save Smart Builder Configuration</p>'
			});	
			location.href= contextPath + "/secure/admin/SbConfig!default.jspa";
		},
		error :function(response, textStatus, errorThrown) {
			console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
		}				
	});		
}

function fn_update(id) {
	console.log("update sb");
	
	// JSON형식으로 변환 할 오브젝트
	var obj = new Object();
	   
	// form의 값을 오브젝트에 저장
	obj.sbId = AJS.$('#sbId').val();		
	obj.sbPassword = AJS.$('#sbPassword').val();	
	obj.url = AJS.$('#url').val();		
	obj.id = id;
	
	var sendData = "sendData="+JSON.stringify(obj);

	console.log("======JSON  new Object(); sendData=====>"+sendData);
	//alert("======JSON  new Object(); sendData=====>"+sendData);
		
	var url = contextPath + "/secure/admin/SbConfig!update.jspa";
	
	AJS.$.ajax({
		type: 'POST',		
		url: url,
		data: sendData,		
		success: function(data, textStatus, response) {
			AJS.messages.success("#aui-message-bar", {
			    title: 'Success!',
			    body: '<p> Update Smart Builder Configuration</p>'
			});	
			
			location.href= contextPath + "/secure/admin/SbConfig!default.jspa";
			
		
		},
		error :function(response, textStatus, errorThrown) {
			console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
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
		
		var sendData = "sendData="+JSON.stringify(obj);

		console.log("======JSON  new Object(); sendData=====>"+sendData);	
		//alert("======JSON  new Object(); sendData=====>"+sendData);
		var url = contextPath + "/secure/admin/SbConfig!delete.jspa";
		
		AJS.$.ajax({
			type: 'POST',		
			url: url,
			data: sendData,		
			success: function(data, textStatus, response) {
				AJS.messages.success("#aui-message-bar", {
				    title: 'Success!',
				    body: '<p> Delete Smart Builder Configuration</p>'
				});
				
				location.href= contextPath + "/secure/admin/SbConfig!default.jspa";


				
				
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});	
	//}
}