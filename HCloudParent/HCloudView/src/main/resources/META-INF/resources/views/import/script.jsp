<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../comm/comm.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>脚本导出</title>
<link rel="stylesheet" type="text/css" href="${ctxPath }/resource/css/import.css" />
<script type="text/javascript" src="${ctxPath }/resource/js/layui/layui.all.js"></script>
<script type="text/javascript" src="${ctxPath }/resource/js/biz/scrollable.js"></script>
<script type="text/javascript" src="${ctxPath }/resource/js/websocket.js"></script>
<script type="text/javascript" src="${ctxPath }/resource/js/script.js">></script>
</head>
<body>
		<div id="main">
		<form action="#" method="post">
			<div id="wizard">
				<ul id="status">
					<li class="active"><strong>1.</strong>选择导出连接</li>
					<li><strong>2.</strong>选择需要导出脚本的表</li>
					<li><strong>3.</strong>正在写入脚本</li>
					<li><strong>4.</strong>脚本写入结果</li>
				</ul>
				<div class="items">
					<div class="page">
						<h3>
							请选择导出脚本连接<br /> <em>导出连接需在系统注册，选择导出连接同时选择对应的数据库</em>
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
						<p>
							<label>脚本数据库：</label> 
							<select class="input" name="dbType" id="dbType">
								<option value="0">--请选择--</option>
								<option value="1">sqlserver2008</option>
								<option value="4">pheonix</option>
								<option value="5">hive</option>
								<option value="2">oracle11g</option>
								<option value="3">mysql</option>
								<option value="6">DB2</option>
								<option value="7">postgreSQL</option>
			
							</select>
						</p>
						<div class="btn_nav">
							<input type="button" class="next right" value="下一步"
								onclick="getTables();" />
						</div>
					</div>
					<div class="page">
						<h3>
							请选择导出脚本表<br /> <em>导出表及表数据会生成脚本</em>
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
								value="下一步&raquo;" onclick="genScript();" />
						</div>
					</div>

					<div class="page">
						<h3>
							正在执行脚本生成操作<br /> <em>脚本生成日志记录：</em>
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
							操作完成<br /> <em>脚本结果：</em>
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