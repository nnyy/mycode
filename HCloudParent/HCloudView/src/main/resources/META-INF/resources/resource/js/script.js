/**
 * 脚本导出操作
 */
$(function() {
	$("#wizard").scrollable({
		onSeek : function(event, i) {
			$("#status li").removeClass("active").eq(i).addClass("active");
		},
		onBeforeSeek : function(event, i) {
			if (i == 1) {
				var cname = $("#connName").val();
				if (cname == "0") {
					layer.msg("导出数据库连接必须选择！");
					return false;
				}
				var db = $("#db").val();
				if (db == "0") {
					layer.msg("导出源数据库必须选择！");
					return false;
				}
				var dbtype = $("#dbType").val();
				if (dbtype == "0") {
					layer.msg("目标数据库类型必须选择！");
					return false;
				}

			} else if (i == 2) {
				var len = $("#dtbs option").length;
				if (!(len > 0)) {
					layer.msg("导出源数据表必须选择！");
					return false;
				}
			}
		}
	});
	$("#sub").click(function(event,p1,p2) {
		if (p1 == "1") {
			var url = root + "/service/hcloud/impandexp/downscript";
			$.down(p2, url);
			$("#clsTxt").append("文件下载操作");
			//close();
		} else {
			$("#clsTxt").append(p2);
		}
		websocket.close();
	});

	$("#clsbtx").click(function() {
		close();
	});
	getConnInfo();
	$("#connName").change({
		type : 0
	}, selectOnChange);
	initWebSocket();
});

function getConnInfo() {
	var url = root + "/service/hcloud/info/conninfo";
	$.post(url, {}, function(data) {
		if (data) {
			$.each(data, function(index, value) {
				var html = "<option value='" + value + "'>" + value
						+ "</option>";
				$("#connName").append(html);
			});
		}
	});
}

function selectOnChange(event) {
	var connName = $(this).children('option:selected').val(), url = root
			+ "/service/hcloud/meta/dbs";
	if (connName == "0") {
		layer.msg("您没有选中任何数据!");
		return;
	}
	$.post(url, {
		connName : connName
	}, function(data) {
		if (data) {
			$("#db").empty();
			$("#db").append("<option value='0'>--请选择--</option>");
			$.each(data, function(index, value) {
				var html = "<option value='" + value + "'>" + value
						+ "</option>";
				$("#db").append(html);
			});

		}
	});

}

function getTables() {
	var connName = $("#connName").val(), db = $("#db").val(), url = root
			+ "/service/hcloud/meta/tbs";
	if (connName == "0" || db == "0") {
		dtbs
		return;
	}
	$.post(url, {
		connName : connName,
		db : db
	}, function(data) {
		if (data) {
			$("#stbs").empty();
			$.each(data, function(index, value) {
				var html = "<option value='" + value + "'>" + value
						+ "</option>";
				$("#stbs").append(html);
			});
		}

	});
}

function getSelect(p) {
	var selectTbs = $("#stbs").val();
	if (selectTbs) {
		// $("#dtbs").empty();
		$.each(selectTbs, function(index, value) {
			var html = "<option value='" + value + "'>" + value + "</option>";
			$("#dtbs").append(html);
			$("#stbs option[value='" + value + "']").remove();
		});
	} else {
		layer.msg("您没有选中任何数据!");
	}
}

function getselectAll(p) {
	// $("#dtbs").empty();

	$("#stbs option").each(function() { // 遍历所有option
		var value = $(this).val(); // 获取option值
		if (value) {
			var html = "<option value='" + value + "'>" + value + "</option>";
			$("#dtbs").append(html);
			$("#stbs option[value='" + value + "']").remove();
		}
	});

}

function backSelect(p) {
	var selectTbs = $("#dtbs").val();
	if (selectTbs) {
		$.each(selectTbs, function(index, value) {
			var html = "<option value='" + value + "'>" + value + "</option>";
			$("#stbs").append(html);
			$("#dtbs option[value='" + value + "']").remove();
		});
	} else {
		layer.msg("您没有选中任何数据!");
	}

}

function backSelectAll(p) {
	$("#dtbs option").each(function() { // 遍历所有option
		var value = $(this).val(); // 获取option值
		if (value) {
			var html = "<option value='" + value + "'>" + value + "</option>";
			$("#stbs").append(html);
			$("#dtbs option[value='" + value + "']").remove();
		}
	});

}

function genScript() {
	var url = root + "/service/hcloud/impandexp/script", sconnName = $(
			"#connName").val(), sDb = $("#db").val(), dbtype = $("#dbType")
			.val();
	var dtbs = "";
	if ($("#dtbs option").length > 0) {
		$("#dtbs option").each(function() { // 遍历所有option
			var value = $(this).val(); // 获取option值
			if (value) {
				dtbs = dtbs + value + ",";
			}
		});
	} else {
		layer.msg("您没有选择任何要导入的数据表!");
		return;
	}
	$.post(url, {
		sourceConnName : sconnName,
		sourcedb : sDb,
		destDb : dbtype,
		selTbs : dtbs
	}, function(data) {
		$("#sub").trigger("click", [data.status,data.info]);
		// $("#clsTxt").append(data.info);
	});
}

function close() {
	var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
	parent.layer.close(index);
}
