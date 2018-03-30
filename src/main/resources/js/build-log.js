var SB_BUILD_LOG = {
	fn_init: function () {		
		var issueKey =  AJS.$("meta[name='issueKey']").attr('content');		
		var url = contextPath + "/rest/sb/1.0/result/list/" + issueKey;	

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
			SB_BUILD_LOG.fn_viewLog(e.target.getAttribute("sresult-id"));
		});		
		
		SB_BUILD_LOG.fn_viewStep(AJS.$("meta[name='sbProjResultId']").attr('content'));
	}
	
	,
	fn_viewStep: function (sbProjResultId) {
		//smartBuilder쪽의 step가져오면 됨.
		var url = contextPath + "/rest/sb/1.0/result/test";

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
			
		        SB_BUILD_LOG.fn_viewLog(obj.stepFirst.stepResultId);		        
		        //setTimeout("SB_BUILD_LOG.fn_viewLog('aaaaa')", 10000); 
			},
			error :function(response, textStatus, errorThrown) {
				alert("code33 :"+response.status+"\n"+"message:"+response.responseText+"\n"+"error:"+errorThrown);
			}		
		}); 
		
	}
	
	
	,
	fn_viewLog: function (sbStepResultId) {
		//선택한 스텝의 상세 로그정보를 보여준다. (smartBuilder 에서 가져와야 함)
		$("#tabs-first").empty();
		$("#tabs-first").append(sbStepResultId);
	}

};
(function ($) {
	AJS.toInit(function() {		
		var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
		SB_BUILD_LOG.fn_init();		
	});
	
	
	
})(AJS.$);