var SB_CONFIG = {	

	fn_init: function () {
		SB_CONFIG.fn_setEvent();		
	}
	,
	fn_setEvent: function() {
		AJS.$("a").click(function(e) {
			var projResultId = this.id;
			//alert(resultId);
			location.href= contextPath + "/secure/admin/SbConfig!default.jspa?projResultId="+projResultId;		
		});	

	}
};

(function ($) {
	AJS.toInit(function(){
		var contextPath = AJS.$("meta[name='ajs-context-path']").attr('content');
		SB_CONFIG.fn_init();
	});
})(AJS.$);

