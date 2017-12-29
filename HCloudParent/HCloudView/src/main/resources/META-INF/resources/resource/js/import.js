/**
 * 
 */

$(function() {
	initWebSocket();
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

			} else if (i == 2) {
				var len = $("#dtbs option").length;
				if (!(len > 0)) {
					layer.msg("导出源数据表必须选择！");
					return false;
				}
			} else if (i == 4) {
				var dcname = $("#destConnName").val();
				if (dcname == "0") {
					layer.msg("导入目标数据库连接必须选择！");
					return false;
				}
				var ddb = $("#destDb").val();
				if (ddb == "0") {
					layer.msg("导入目标数据库必须选择！");
					return false;
				}
			}

		}
	});
	$("#sub").click(function() {
		websocket.close();
	});

	$("#clsbtx").click(function() {
		close();
	});

	getConnInfo(0);
	$("#connName").change({
		type : 0
	}, selectOnChange);
	$("#destConnName").change({
		type : 1
	}, selectOnChange);

	confTextArea();
});

function confTextArea() {
	var text = $("#logCol");
	text.scrollTop = text.scrollHeight;
}

function getConnInfo(p) {
	var url = root + "/service/hcloud/info/conninfo";
	$.post(url, {}, function(data) {
		if (data) {
			if (p == 0) {
				$.each(data, function(index, value) {
					var html = "<option value='" + value + "'>" + value
							+ "</option>";
					$("#connName").append(html);
				});
			} else {
				$.each(data, function(index, value) {
					var html = "<option value='" + value + "'>" + value
							+ "</option>";
					$("#destConnName").append(html);
				});
			}
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
			if (event.data.type == 0) {
				$("#db").empty();
				$("#db").append("<option value='0'>--请选择--</option>");
				$.each(data, function(index, value) {
					var html = "<option value='" + value + "'>" + value
							+ "</option>";
					$("#db").append(html);
				});
			} else {
				$("#destDb").empty();
				$("#destDb").append("<option value='0'>--请选择--</option>");
				$.each(data, function(index, value) {
					var html = "<option value='" + value + "'>" + value
							+ "</option>";
					$("#destDb").append(html);
				});
			}
		}
	});

}

function getTables(p) {
	if (p == 0) {
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

	} else {
		if ($("#dtbs option").length > 0) {
			$("#nstbs").empty();
			$("#dtbs option").each(
					function() {
						var value = $(this).val();
						var html = "<option value='" + value + "'>" + value
								+ "</option>";
						$("#nstbs").append(html);
					});
		}
	}

}

function getSelect(p) {
	if (p == 0) {
		var selectTbs = $("#stbs").val();
		if (selectTbs) {
			// $("#dtbs").empty();
			$.each(selectTbs, function(index, value) {
				var html = "<option value='" + value + "'>" + value
						+ "</option>";
				$("#dtbs").append(html);
				$("#stbs option[value='" + value + "']").remove();
			});
		} else {
			layer.msg("您没有选中任何数据!");
		}
	} else {
		var selectTbs = $("#nstbs").val();
		if (selectTbs) {
			// $("#dtbs").empty();
			$.each(selectTbs, function(index, value) {
				var html = "<option value='" + value + "'>" + value
						+ "</option>";
				$("#ndtbs").append(html);
				$("#nstbs option[value='" + value + "']").remove();
			});
		} else {
			layer.msg("您没有选中任何数据!");
		}
	}

}

function getselectAll(p) {
	// $("#dtbs").empty();
	if (p == 0) {
		$("#stbs option").each(
				function() { // 遍历所有option
					var value = $(this).val(); // 获取option值
					if (value) {
						var html = "<option value='" + value + "'>" + value
								+ "</option>";
						$("#dtbs").append(html);
						$("#stbs option[value='" + value + "']").remove();
					}
				});
	} else {
		$("#nstbs option").each(
				function() { // 遍历所有option
					var value = $(this).val(); // 获取option值
					if (value) {
						var html = "<option value='" + value + "'>" + value
								+ "</option>";
						$("#ndtbs").append(html);
						$("#nstbs option[value='" + value + "']").remove();
					}
				});
	}
}

function backSelect(p) {
	if (p == 0) {
		var selectTbs = $("#dtbs").val();
		if (selectTbs) {
			$.each(selectTbs, function(index, value) {
				var html = "<option value='" + value + "'>" + value
						+ "</option>";
				$("#stbs").append(html);
				$("#dtbs option[value='" + value + "']").remove();
			});
		} else {
			layer.msg("您没有选中任何数据!");
		}
	} else {
		var selectTbs = $("#ndtbs").val();
		if (selectTbs) {
			$.each(selectTbs, function(index, value) {
				var html = "<option value='" + value + "'>" + value
						+ "</option>";
				$("#nstbs").append(html);
				$("#ndtbs option[value='" + value + "']").remove();
			});
		} else {
			layer.msg("您没有选中任何数据!");
		}
	}
}

function backSelectAll(p) {
	if (p == 0) {
		$("#dtbs option").each(
				function() { // 遍历所有option
					var value = $(this).val(); // 获取option值
					if (value) {
						var html = "<option value='" + value + "'>" + value
								+ "</option>";
						$("#stbs").append(html);
						$("#dtbs option[value='" + value + "']").remove();
					}
				});
	} else {
		$("#ndtbs option").each(
				function() { // 遍历所有option
					var value = $(this).val(); // 获取option值
					if (value) {
						var html = "<option value='" + value + "'>" + value
								+ "</option>";
						$("#nstbs").append(html);
						$("#ndtbs option[value='" + value + "']").remove();
					}
				});
	}
}

function importdata() {
	var iscreateTable = null, sconnName = $("#connName").val(), sDb = $("#db")
			.val(), dconnName = $("#destConnName").val(), destDb = $("#destDb")
			.val(), url = root + "/service/hcloud/impandexp/import";
	if ($('#iscreatetb').is(':checked')) {
		iscreateTable = 1;
	} else {
		iscreateTable = 0;
	}

	var dtbs = "", ndtbs = "";
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

	if ($("#ndtbs option").length > 0) {
		$("#ndtbs option").each(function() { // 遍历所有option
			var value = $(this).val(); // 获取option值
			if (value) {
				ndtbs = ndtbs + value + ",";
			}
		});
	}
	$.post(url, {
		sconnName : sconnName,
		sDb : sDb,
		dtbs : dtbs,
		ndtbs : ndtbs,
		dconnName : dconnName,
		destDb : destDb,
		isCreateTb : iscreateTable
	}, function(data) {
		if (data.status == "1") {
			$("#sub").trigger("click");
			$("#clsTxt").append(data.info);
		} else {
			$("#sub").trigger("click");
			$("#clsTxt").append(data.info);
		}
	});
}

function close() {
	var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
	parent.layer.close(index);
}
