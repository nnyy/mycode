<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String sessionid=session.getId();
	//out.print(sessionid);
%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${ctxPath }/resource/js/layui/css/layui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctxPath }/resource/js/medium-editor/css/medium-editor.css" />
<link rel="stylesheet" type="text/css"
	href="${ctxPath }/resource/js/medium-editor/css/themes/default.css" />
<link rel="stylesheet" type="text/css"
	href="${ctxPath }/resource/css/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" type="text/css"
	href="${ctxPath }/resource/js/ztree/css/metroStyle/metroStyle.css" />
<script type="text/javascript"
	src="${ctxPath }/resource/js/jquery/jquery.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/jquery/jquery.metadata.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/jquery/jquery.validate.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/jquery/additional-methods.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/jquery/messages_zh.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/medium-editor/js/medium-editor.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/ztree/jquery.ztree.all.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/comm/commpent.js"></script>
</head>
<script type="text/javascript">
	//全局js定义
	var root = '${ctxPath}', sessionId = '<%=sessionid%>';
</script>

</html>