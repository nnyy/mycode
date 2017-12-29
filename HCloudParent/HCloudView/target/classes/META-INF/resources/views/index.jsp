<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="comm/comm.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>中交海德数据库迁移平台</title>
<script type="text/javascript" src="${ctxPath }/resource/js/index.js"></script>
</head>
<body class="layui-layout-body">
	<input type="hidden" id="rst" value="" />
	<input type="hidden" id="conn" value="" />
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header ayui-bg-cyan ">
			<div class="layui-logo" style="color: #fff;">海德大数据管理平台</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-left">
				<li class="layui-nav-item">
				<a href="javascript:void(0);" onclick="driverreg();">新建连接</a></li>
				<li class="layui-nav-item"><a href="javascript:void(0);" onclick="importData();">数据导入</a></li>
				<li class="layui-nav-item"><a href="javascript:void(0);" onclick="exportScript();">脚本导出</a></li>
				<li class="layui-nav-item"><a href="javascript:;">已注册连接</a>
					<dl id="conList" class="layui-nav-child">

					</dl></li>
			</ul>
		</div>
		<div class="layui-side layui-bg-green">
			<div class="layui-side-scroll">
				<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>

		<div class="layui-body">
			<!-- 内容主体区域 -->
			<div style="padding: 5px;width:100%; overflow: hidden;">
				<div
					style="width: 4.7%; height: 300px; float: left; margin-right: 2px;margin-left:20px; text-align: right;">
					<button class="layui-btn layui-btn-small" style="margin-left: 10px;" onclick="exesql();">
						<i class="layui-icon">&#xe623;</i>执行
					</button>
				</div>
				<div
					style="width: 90%;margin-left:20px; height: 300px; float: left; border: 1px solid #525252;">
					<textarea class="editable medium-editor-textarea" id="sqlEditer"
						style="height: 300px; width: auto; font-weight: bold; font-size: 15px"></textarea>
				</div>
				<div
					style="width: 100%;margin-left:20px; height: 550px; float: left; margin-top: 5px;">
					<div
						style=" width: 4.7%; height: 550px; float: left; margin-right: 2px; text-align: right; line-height: 15px;">
						<button class="layui-btn  layui-btn-small" onclick="exportcurrdata()"
							style="margin-top: 5px; margin-left: 3px;">
							<i class="layui-icon">&#xe62d;</i>导出数据
						</button>
						<button class="layui-btn  layui-btn-small"
							style="margin-top: 5px;margin-left: 3px;">
							<i class="layui-icon">&#xe62d;</i>在线分析
						</button>
					</div>
					<div
						style="width: 90%;margin-left:20px; height: 550px; float: left; border: 1px solid #525252; overflow: scroll;">
						<table id="rsttable" class="layui-table">
						</table>
					</div>
				</div>
			</div>
		</div>

		<div class="layui-footer">元数据交换平台</div>
	</div>
	<script type="text/javascript"
		src="${ctxPath }/resource/js/layui/layui.all.js"></script>
	<script type="text/javascript">
		var element = null;
		!function() {
			element = layui.element;
		}();
	</script>
</body>
</html>