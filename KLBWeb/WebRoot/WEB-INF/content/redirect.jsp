<%@ page contentType="text/html;charset=UTF-8" %>
正在跳转...
<script type="text/javascript">
if(parent.location) {
	var guide = '${klbSessionUser.guide}';
	if(guide == "1") parent.location.replace("klb-index.action");
	else parent.location.href="klb-set-1.action";
} else {
	var guide = '${klbSessionUser.guide}';
	if(guide == "1") location.replace("klb-index.action");
	else location.href="klb-set-1.action";
}
</script>
