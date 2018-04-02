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

	itemFormatResult: function(item) {
		var markup = "<div>" +  item.cf_name + "</div>";
	    return markup;
	},
	
	itemFormatSelection: function(item) {
		var value = item.id;
		$("#cfId").val( value.substring(0, value.indexOf("|")));
		return item.cf_name;
	},
	
	fn_update: function (id) {
		var obj = new Object();	   
		obj.id = AJS.$('#id').val();
		obj.sbId = AJS.$('#sbId').val();		
		obj.sbPassword = AJS.$('#sbPassword').val();	
		obj.url = AJS.$('#url').val();
		obj.sbCfId = AJS.$('#cfId').val();
		obj.sbCfName = AJS.$('#cfName').val();
		obj.jiraId = AJS.$('#jiraId').val();		
		obj.jiraPassword = AJS.$('#jiraPassword').val();	
		var url = contextPath + "/secure/admin/SbConfig!update.jspa";	
		
		$.ajax({
			type: 'POST',		
			url: url,
			data: JSON.stringify(obj),		
	        //dataType: "json",     
	        contentType: "application/json; charset=utf-8",        
			success: function(data, textStatus, response) {
/*				AJS.messages.success("#aui-message-bar", {
				    title: 'Success!',
				    body: '<p> Update Smart Builder Configuration</p>'
				});	*/
				
				location.reload();			
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});		
	},
	
	fn_insert: function () {		
		var obj = new Object();	   
		obj.sbId = AJS.$('#sbId').val();		
		obj.sbPassword = AJS.$('#sbPassword').val();	
		obj.url = AJS.$('#url').val();
		obj.sbCfId = AJS.$('#cfId').val();
		obj.sbCfName = AJS.$('#cfName').val();
		obj.jiraId = AJS.$('#jiraId').val();		
		obj.jiraPassword = AJS.$('#jiraPassword').val();	
		var url = contextPath + "/secure/admin/SbConfig!insert.jspa";		
		//console.log("obj :: " + JSON.stringify(obj));
		
		AJS.$.ajax({
			type: 'POST',		
			url: url,
			data: JSON.stringify(obj),		
	        //dataType: "json",     
	        contentType: "application/json; charset=utf-8",        
			success: function(data, textStatus, response) {
/*				AJS.messages.success("#aui-message-bar", {
				    title: 'Success!',
				    body: '<p> Save Smart Builder Configuration</p>'
				});	*/
				location.reload();
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});		
	},
	
	fn_delete: function (id) {		
		var obj = new Object();			
		obj.id = id;			
		var sendData = "sendData="+JSON.stringify(obj);

		var url = contextPath + "/secure/admin/SbConfig!delete.jspa";
		
		AJS.$.ajax({
			type: 'POST',		
			url: url,
			data: sendData,		
			success: function(data, textStatus, response) {
/*					AJS.messages.success("#aui-message-bar", {
					    title: 'Success!',
					    body: '<p> Delete Smart Builder Configuration</p>'
					});*/				
				location.reload();
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});	
	},

	
	fn_setBuildResultStep: function () {
		var url = contextPath + "/rest/sb/1.0/build/resultStep";
		var obj = new Object();	
		obj.projResultId = AJS.$('#projResultId').val();
		console.log("obj ::::::::> " + JSON.stringify(obj));
		
		AJS.$.ajax({
			type: 'POST',		
			url: url,
			data: JSON.stringify(obj),	
	        //dataType: "json",     
	        contentType: "application/json; charset=utf-8", 			
			success: function(data, textStatus, response) {
				//console.log("data=====>"+JSON.stringify(data));
				console.log("data.list.length=====>"+data.list.length);

				var active = "";
				var tabTag = "";
				for (var i=0; i < data.list.length; i++) {
					if(i==0){
						active = "active-tab";
					}
					tabTag = tabTag + "<li class='menu-item "+active+"'>"+
					"<a href='#tabs"+i+"'>"+data.list[i].stepName+"</a>"+
					"</li>";
					
					active = "";
				}

				console.log("tabTag=====>"+tabTag);
				
				//AJS.$("ul").html(tabTag);	
		
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});	
	},	

	fn_init: function () {
		var url = contextPath + "/rest/sb/1.0/config/info";
		$.ajax({
			type: 'GET',		
			url: url,
			success: function(data, textStatus, response) {
				var obj = new Object();
				obj.sbConfigContent = data;
				$('#tbody').html(kic.sb.config.form(obj, ''));
				//location.reload();
				
				SB_CONFIG.sb_project(data.sbCfName);
				
				SB_CONFIG.fn_setEvent();
				
				
			},
			error :function(response, textStatus, errorThrown) {
				console.log("code:"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}				
		});
		
	}
	
	,
	fn_setEvent: function() {
		AJS.$("#delete-sb-submit-button").click(function(e) {
			var id = AJS.$('#id').val();
			SB_CONFIG.fn_delete(id);
		});	
		
		AJS.$('#sb-submit-form').on('aui-valid-submit', function(event) {
		    event.preventDefault();
			var id = AJS.$('#id').val();		
			if(id==0){
				SB_CONFIG.fn_insert();	
			}else{
				SB_CONFIG.fn_update(id);
			}	    
		});
	}
};

(function ($) {
	AJS.toInit(function(){
		var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
		SB_CONFIG.fn_init();
		//SB_CONFIG.fn_setBuildResultStep();
		
	});
})(AJS.$);

