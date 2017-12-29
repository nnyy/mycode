<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../comm/comm.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript">
	$(function() {
		$("#regform").validate({
			rules : {
				connName : "required",
				dbtype : "required",
				driverClass : "required",
				user : "required",
				pwd : "required",
				url : "required"
			},
			messages : {
				connName : "连接名称不能为空",
				dbtype : "数据库类必选",
				driverClass : "驱动类不能为空",
				url : "连接字符器不能为空",
				user : "*",
				pwd : "*"
			},
			submitHandler:function(form){
				var validator = $(form).validate();
				var f = validator.form();
				if(f){
					var url = root + '/service/hcloud/conn/save', data=$(form).serialize();
					$.post(url,data,function(rst){
						if(rst.status=="1"){
							var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							parent.layer.close(index); //再执行关闭  
							parent.$("#rst").val("1");
							parent.$("#conn").val(rst.connname);
						}else if(rst.status=="2"){
							var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							parent.layer.close(index); //再执行关闭  
							parent.$("#rst").val("2");
						}else{
							layer.open({
								content : "保存失败！",
								btn : "确定"
							});
						}
					});
				}
			}
		});
	});
</script>
</head>
<body>
	<form class="layui-form" id="regform">
		<div class="layui-form-item">
			<label class="layui-form-label">连接名称:</label>
			<div class="layui-input-block">
				<input type="text" id="connName" name="connName"
					placeholder="输入连接名称" autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">选择数据库:</label>
			<div class="layui-input-block">
				<select id="dbtype" name="dbtype">
					<option value="1">sqlserver2008</option>
					<option value="4">pheonix</option>
					<option value="5">hive</option>
					<option value="2">oracle11g</option>
					<option value="3">mysql</option>
					<option value="6">DB2</option>
					<option value="7">postgreSQL</option>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">驱动类:</label>
			<div class="layui-input-block">
				<input type="text" id="driverClass" name="driverClass"
					placeholder="请输入驱动" autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">连接字符串:</label>
			<div class="layui-input-block">
				<input type="text" id="url" name="url" placeholder="请输入jdbc连接字符串"
					autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">用户名:</label>
			<div class="layui-input-block">
				<input type="text" id="user" name="user" placeholder="请输入用户名"
					autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">密码:</label>
			<div class="layui-input-block">
				<input type="text" id="pwd" name="pwd" placeholder="输入密码"
					autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button type="submit" class="layui-btn" id="savebtn">保存</button>
				<button type="reset" class="layui-btn" id="reset">重置</button>
			</div>
		</div>
	</form>
	<script type="text/javascript"
		src="${ctxPath }/resource/js/layui/layui.all.js"></script>
	<script type="text/javascript">
		;
		!function() {
			var form = layui.form;
		}();
	</script>
</body>
</html>