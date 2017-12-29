<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../comm/comm.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="${ctxPath }/resource/css/import.css" />
<script type="text/javascript"
	src="${ctxPath }/resource/js/layui/layui.all.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/biz/scrollable.js"></script>
<script type="text/javascript"
	src="${ctxPath }/resource/js/websocket.js"></script>
<script type="text/javascript" src="${ctxPath }/resource/js/import.js"></script>

</head>
<body>
	<div id="main">
		<form action="#" method="post">
			<div id="wizard">
				<ul id="status">
					<li class="active"><strong>1.</strong>选择导出连接</li>
					<li><strong>2.</strong>选择导出表</li>
					<li><strong>3.</strong>选择不导出数据的表</li>
					<li><strong>4.</strong>选择导入连接</li>
					<li><strong>5.</strong>正在导入</li>
					<li><strong>6.</strong>导入成功</li>
				</ul>
				<div class="items">
					<div class="page">
						<h3>
							请选择导出连接<br /> <em>导出连接需在系统注册，选择导出连接同时选择对应的数据库</em>
						</h3>
						<p>
							<label>数据库连接：</label> <select class="input" name="connName"
								id="connName">
								<option value="0">--请选择--</option>
							</select>
						</p>
						<p>
							<label>导出数据库：</label> <select class="input" name="db" id="db">
								<option value="0">--请选择--</option>
							</select>
						</p>
						<div class="btn_nav">
							<input type="button" class="next right" value="下一步"
								onclick="getTables(0);" />
						</div>
					</div>
					<div class="page">
						<h3>
							请选择导出表<br /> <em>导出表及表数据用于在目标数据库</em>
						</h3>
						<div class="maindiv">
							<div class="div1">
								<select multiple="multiple" name=stbs id="stbs"
									style="width: 360px; height: 480px;">
								</select>
							</div>
							<div class="div2">
								<a class="abtn1" href="javascript:;" onclick="getSelect(0);">&gt;</a>
								<a class="abtn2" href="javascript:;" onclick="getselectAll(0);">&gt;&gt;</a>
								<a class="abtn2" href="javascript:;" onclick="backSelect(0);">&lt;</a>
								<a class="abtn2" href="javascript:;" onclick="backSelectAll(0);">&lt;&lt;</a>
							</div>
							<div class="div3">
								<select multiple="multiple" name=dtbs id="dtbs">

								</select>

							</div>
						</div>
						<div class="btn_nav">
							<input type="button" class="prev" style="float: left"
								value="&laquo;上一步" /> <input type="button" class="next right"
								value="下一步&raquo;" onclick="getTables(1)" />
						</div>
					</div>
					<div class="page">
						<h3>
							请选择不导出数据的表<br /> <em>如果选择了不导出数据，在执行导出操作时，将不导出表数据</em>
						</h3>
						<div class="maindiv">
							<div class="div1">
								<select multiple="multiple" name=stbs id="nstbs"
									style="width: 360px; height: 480px;">
								</select>
							</div>
							<div class="div2">
								<a class="abtn1" href="javascript:;" onclick="getSelect(1);">&gt;</a>
								<a class="abtn2" href="javascript:;" onclick="getselectAll(1);">&gt;&gt;</a>
								<a class="abtn2" href="javascript:;" onclick="backSelect(1);">&lt;</a>
								<a class="abtn2" href="javascript:;" onclick="backSelectAll(1);">&lt;&lt;</a>
							</div>
							<div class="div3">
								<select multiple="multiple" name=dtbs id="ndtbs">

								</select>

							</div>
						</div>
						<div class="btn_nav">
							<input type="button" class="prev" style="float: left"
								value="&laquo;上一步" /> <input type="button" class="next right"
								value="下一步&raquo;" onclick="getConnInfo(1);" />
						</div>
					</div>
					<div class="page">
						<h3>
							选择要导入的数据库连接<br /> <em>根据用户选择把导出的库的表和数据，导入到目标库</em>
						</h3>
						<p>
							<label>选择导入连接：</label> <select class="input" name="destConnName"
								id="destConnName">
								<option value="0">--请选择--</option>
							</select>
						</p>
						<p>
							<label>选择数据库：</label> <select class="input" name="destDb"
								id="destDb">
								<option value="0">--请选择--</option>
							</select>
						</p>
						<p>
							<label>是否创建数据表：</label> <input type="checkbox" checked="checked"
								id="iscreatetb" class="input" name="iscreatetb" />
						</p>
						<div class="btn_nav">
							<input type="button" class="prev" style="float: left"
								value="&laquo;上一步" /> <input type="button" class="next right"
								value="开始导入" onclick="importdata();" />
						</div>
					</div>
					<div class="page">
						<h3>
							正在执行导入操作<br /> <em>导入日志记录：</em>
						</h3>

						<textarea id="logCol" disabled
							style="width: 840px; height: 400px; font-size: 14px; resize: none; overflow: scroll;"></textarea>

						<div class="btn_nav">
							<input type="button" class="next right" id="sub"
								disabled="disabled" value="确定" />
						</div>
					</div>

					<div class="page">
						<h3>
							导入操作完成<br /> <em>导入结果：</em>
						</h3>

						<textarea id="clsTxt" disabled
							style="width: 840px; height: 400px; font-size: 14px; resize: none; overflow: scroll;"></textarea>

						<div class="btn_nav">
							<input type="button" class="next right" id="clsbtx" value="关闭" />
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>