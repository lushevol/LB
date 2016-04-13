
if (!window.fc) {
	window.fc = {};
}

(function(package){
	jQuery.extend(package, {
		iframeProxy : function(json) {
			document.getElementById("mainMidFrameId").contentWindow.location.replace(json.action);
		},
		parentProxy : function(json) {
			document.location.replace(json.action);
		}
	});
})(fc);
