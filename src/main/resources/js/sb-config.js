var SB_CONFIG = {	
	sb_project: function (value) {
		var url = contextPath + "/rest/sb/1.0/config/sw/project/cf/list";
		$("#cfName").auiSelect2({
		    placeholder: value.substring(value.indexOf("|") + 1),
		    minimumInputLength: 2,
		    //allowClear: true,
		    openEnter: true,
		    ajax: {
		        url: url,
		        //dataType: 'json',
		        contentType: "application/json; charset=utf-8",
		        quietMillis: 250,
		        data: function (term, page) { // page is the one-based page number tracked by Select2
		            return {
		                searchValue: term,//search term
		                page: page,
		                pageLimit: 2 
		            };
		        },
		        results: function (data, page) {
		            var more = (page * 2) < data.totCount; // whether or not there are more results available
		        	//var more = (page * 30) < data.length; // whether or not there are more results available
		            return { results: data.cfList, more: more };
		        }
		    },
		    
		    initSelection: function (element, callback) {
		        var cfName = $(element).val();
		        if (cfName !== "") {
		        	callback({"cf_name": cfName.substring(0, cfName.indexOf("|"))});
		        }
		      },
		      
		    formatResult: SB_CONFIG.itemFormatResult, // omitted for brevity, see the source of this page
		    formatSelection: SB_CONFIG.itemFormatSelection, // omitted for brevity, see the source of this page
		    dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
		    escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
		});
		$("#cfName").val(value);
	},

	itemFormatResult:function(item) {
		var markup = "<div>" +  item.cf_name + "</div>";
	    return markup;
	},
	
	itemFormatSelection:function(item) {
		var value = item.id;
		$("#cfId").val( value.substring(0, value.indexOf("|")));
		return item.cf_name;
	}
};

AJS.toInit(function(){
	var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
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
	obj.cfId = AJS.$('#cfId').val();
	obj.cfName = AJS.$('#cfName').val();
	var url = contextPath + "/secure/admin/SbConfig!insert.jspa";
	
	console.log("obj :: " + JSON.stringify(obj));
	
	AJS.$.ajax({
		type: 'POST',		
		url: url,
		data: JSON.stringify(obj),		
        //dataType: "json",     
        contentType: "application/json; charset=utf-8",        
		success: function(data, textStatus, response) {
			AJS.messages.success("#aui-message-bar", {
			    title: 'Success!',
			    body: '<p> Save Smart Builder Configuration</p>'
			});	
			//location.href= contextPath + "/secure/admin/SbConfig!default.jspa";
			location.reload();
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
	obj.id = AJS.$('#id').val();
	obj.sbId = AJS.$('#sbId').val();		
	obj.sbPassword = AJS.$('#sbPassword').val();	
	obj.url = AJS.$('#url').val();
	obj.cfId = AJS.$('#cfId').val();
	obj.cfName = AJS.$('#cfName').val();
	var url = contextPath + "/secure/admin/SbConfig!update.jspa";	
	
	$.ajax({
		type: 'POST',		
		url: url,
		data: JSON.stringify(obj),		
        //dataType: "json",     
        contentType: "application/json; charset=utf-8",        
		success: function(data, textStatus, response) {
			AJS.messages.success("#aui-message-bar", {
			    title: 'Success!',
			    body: '<p> Update Smart Builder Configuration</p>'
			});	
			
			//location.href= contextPath + "/secure/admin/SbConfig!default.jspa";
			location.reload();			
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