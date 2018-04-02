var SB_BUILD_LOG = {
	fn_init: function () {		
		var issueKey =  AJS.$("meta[name='issueKey']").attr('content');		
		var url = contextPath + "/rest/sb/1.0/result/list/" + issueKey;	

		timeoutId = "";
		$.ajax({
			type: 'GET',
			url: url ,
			async: false,
			success: function(data, textStatus, response) {
				$("#sb-log-top").empty();
				$('#sb-log-top').append(kic.sb.build.log.list(data, ''));

				var iLen = data.sbList.length;
				SB_BUILD_LOG.fn_setEvent(data.sbList.length, data.sbList[0].length, data.sbList[iLen-1].length);				
				
			},
			error :function(response, textStatus, errorThrown) {
				alert("code33 :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}		
		}); 			
	}

	,
	fn_setEvent: function (iLen1, iLen2, iLen3) {
		for (var i=0; i < iLen1-1; i++) {
			for (var j=0; j < iLen2; j++) {
				var s = i + "";
				$("#sb-log" + s.concat(j)).click(function () {
					var obj = $("#" + this.id);
					SB_BUILD_LOG.fn_viewStep(obj.attr("sb-proj-result-id"));
				});				
			}
		}

		for (var i=iLen1-1; i < iLen1; i++) {
			for (var j=0; j < iLen3; j++) {
				var s = i + "";
				$("#sb-log" + s.concat(j)).click(function () {
					var obj = $("#" + this.id);					
					SB_BUILD_LOG.fn_viewStep(obj.attr("sb-proj-result-id"));
				});
			}
		}		

		AJS.$('#sb-steps-tabs').on('tabSelect', function(e) {
			clearTimeout(timeoutId);
			$("#tabs-first").empty();
			SB_BUILD_LOG.fn_viewLog(e.target.getAttribute("sresult-id"), 1);
		});		
		
		SB_BUILD_LOG.fn_viewStep(AJS.$("meta[name='sbProjResultId']").attr('content'));
	}
	
	,
	fn_viewStep: function (sbProjResultId) {
		//smartBuilder쪽의 step가져오면 됨.  (SbResultRestService.java => test method)
		var url = contextPath + "/rest/sb/1.0/result/test/" + sbProjResultId;

		$("#sb-title").empty();
		$("#sb-title").append(" (" + sbProjResultId + ")");
		$.ajax({
			type: 'GET',
			url: url ,
			async: false,
			success: function(data, textStatus, response) {
				var obj = new Object();			    
			    obj.stepFirst = data[0];
			    obj.stepList = data.splice(1);
			    
			    $("#sb-tab1").empty();
			    $('#sb-tab1').append(kic.sb.build.log.content(obj, ''));
			
			    clearTimeout(timeoutId);
			    $("#tabs-first").empty();
			    SB_BUILD_LOG.fn_viewLog(obj.stepFirst.stepResultId, 1);
		        
			},
			error :function(response, textStatus, errorThrown) {
				alert("code33 :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}		
		}); 
		
	}

	,
	fn_viewLog: function (sbStepResultId, pageIdx) {
		//smartBuilder 에서 로그정보 가져와야 함.
		$("#tabs-first").append(sbStepResultId  + ", pageIdx : " + pageIdx + "<br/>");
		
		/* 50개씩 로그정보 가져오기.. 3000 : 3초 마다 다시 호출됨. */
		pageIdx++;
		timeoutId = setTimeout("SB_BUILD_LOG.fn_viewLog('" + sbStepResultId + "','" + pageIdx + "')", 3000); //3초
	}
};

(function ($) {
	AJS.toInit(function() {		
		var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
		var timeoutId = "";
		
		SB_BUILD_LOG.fn_init();		
	});
	
	
	
})(AJS.$);