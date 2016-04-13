window.onload = initTime;

function initTime(){

	var time = $("#time").val();
	var zone = $("#zone").val();
	time = time * 1000;
	var now = new Date();
	now.setTime(time);
	
	var y = now.getUTCFullYear();
	var m = now.getUTCMonth()+1;
	var d = now.getUTCDate();
	
	var setTime = "00:00:00";
	document.getElementById("f_date").value = y+"-"+m+"-"+d;
	document.getElementById("f_time").value = setTime;
	
}

