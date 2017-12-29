/**
 * 首页js
 */

var setting = {
	data : {
		simpleData : {
			enable : true,
		}
	},
	async : {
		autoParam : [ 'id', 'db', 'name', 'connName', 'asyncFilter' ],
		enable : true,
		type : "post",
		url : root + "/service/hcloud/meta/cols"
	},
	callback : {
		beforeExpand : beforeExpand,
		onAsyncSuccess : onAsyncSuccess,
		onAsyncError : onAsyncError
	},
	view : {
		selectedMulti : false
	},
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	}
};
function driverreg() {
	var index = layer.open({
		type : 2,
		title : '连接参数设置',
		shadeClose : true,
		closeBtn : 0,
		shade : 0.8,
		content : root + '/driverreg',
		area : [ '600px', '450px' ],
		end : function() {
			var rst = $("#rst").val();
			if (rst == "1") {
				$("#rst").val("");
				layer.msg('数据库连接保存成功！');
				addConnection($("#conn").val());
				loadZtreeData($("#conn").val());
			} else if (rst == "2") {
				$("#rst").val("");
				layer.msg('数据库连接名称已经存在,不能重复添加相同名称的连接！');
			} else {
				layer.msg('数据库连接保存操作取消！');
			}

		}
	});
}

$(function() {
	init();
});

// 初始化编辑器
var editor;
function init() {
	var url = root + "/service/hcloud/info/conninfo";
	$
			.post(
					url,
					{},
					function(rst) {
						if (rst) {
							$
									.each(
											rst,
											function(i, value) {
												var html = "<dd name='"
														+ value
														+ "'"
														+ "><div><a href='javascript:;' style='float: left' onclick=\"getMetaData('"
														+ value
														+ "');\">"
														+ value
														+ "</a><a href='javascript:;' onclick=\"delConn('"
														+ value
														+ "',this);\">x</a></div></dd>";
												$("#conList").append(html);
											});
						}
					});

	// 初始化编辑器
	editor = new MediumEditor('.editable', {
		buttonLabels : 'fontawesome',
		toolbar : false,
		placeholder : false
	});

}

function getMetaData(value) {
	unCheckNode();
	loadZtreeData(value);
}

function unCheckNode() {
	var ztree = $.fn.zTree.getZTreeObj("treeDemo");
	if (ztree) {
		var nodes = ztree.getCheckedNodes(true);
		if (nodes && nodes.length > 0) {
			ztree.checkNode(nodes[0], false, true);
		}
	}
}

function loadZtreeData(value) {
	var ztree = $.fn.zTree.getZTreeObj("treeDemo");
	if (ztree) {
		var nodes = ztree.getNodesByParam("name", value, null);
		if (nodes && nodes.length > 0) {
			ztree.checkNode(nodes[0], true, false);
		} else {
			queryNodes(value, 1, ztree);
		}
	} else {
		queryNodes(value, 0, null);
	}
}

function queryNodes(value, flag, ztree) {
	var url = root + "/service/hcloud/meta/tables";
	$.post(url, {
		connName : value
	}, function(data) {
		if (flag == 0) {
			$.fn.zTree.init($("#treeDemo"), setting, data);
		} else {
			ztree.addNodes(null, data);
			ztree.refresh();
		}

	});

}

function addConnection(value) {
	if (value) {
		var eleObj = $("dd[name='" + value + "']");
		if (eleObj.length == 0) {
			var html = "<dd name='"
					+ value
					+ "'"
					+ "><div><a href='javascript:;' style='float: left' onclick=\"getMetaData('"
					+ value + "');\">" + value
					+ "</a><a href='javascript:;' onclick=\"delConn('" + value
					+ "',this);\">x</a></div></dd>";
			$("#conList").append(html);
		}
	}
}

function beforeExpand(treeId, treeNode) {
	if (!treeNode.isAjaxing) {
		startTime = new Date();
		treeNode.times = 1;
		ajaxGetNodes(treeNode, "refresh");
		return true;
	} else {
		layer.msg('正在请求加载数据！');
		return false;
	}
}

function onAsyncSuccess(event, treeId, treeNode, msg) {
	if (!msg || msg.length == 0) {
		return;
	}
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"), totalCount = treeNode.count;
	if (treeNode.children.length < totalCount) {
		setTimeout(function() {
			ajaxGetNodes(treeNode);
		}, 100);
	} else {
		treeNode.icon = "";
		zTree.updateNode(treeNode);
		zTree.checkNode(treeNode.children[0], true, false);// (treeNode.children[0]);
	}
}

function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus,
		errorThrown) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	layer.msg('异步请求加载数据异常！');
	treeNode.icon = "";
	zTree.updateNode(treeNode);
}

function ajaxGetNodes(treeNode, reloadType) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	if (reloadType == "refresh") {
		treeNode.icon = root
				+ "/resource/js/ztree/css/metroStyle/img/loading.gif";
		zTree.updateNode(treeNode);
	}
	zTree.reAsyncChildNodes(treeNode, reloadType, true);
}

function exesql() {
	var allSql = $("#sqlEditer").val(), url = root
			+ "/service/hcloud/sql/execute", treeObj = $.fn.zTree
			.getZTreeObj("treeDemo"), exeSql = "";
	var selectSqlEle = editor.getSelectedParentElement();
	if (selectSqlEle) {
		exeSql = selectSqlEle.innerText;
	} else {
		exeSql = allSql;
	}
	var treeNode = treeObj.getCheckedNodes(true);
	if (!exeSql) {
		layer.msg('请输入sql语句执行查询!');
		return;
	}

	if (treeNode.length > 0) {
		var asyncFilter = treeNode[0].asyncFilter;
		var value = null;
		if (asyncFilter == 0) {
			value = treeNode[0].name;
		} else {
			value = treeNode[0].connName;
		}
		$
				.post(
						url,
						{
							connName : value,
							sql : exeSql
						},
						function(data) {
							if (data) {
								var cols = data.cols, values = data.values, thead = $("#rsttable thead"), tbody = $("#rsttable tbody");
								if (thead) {
									$("#rsttable thead").remove();
								}
								if (tbody) {
									$("#rsttable tbody").remove();
								}
								if (cols && cols.length > 0) {
									var tbTitle = "<thead>";
									$.each(cols, function(index, value) {
										tbTitle = tbTitle + "<th>" + value
												+ "</th>";
									});
									tbTitle = tbTitle + "</thead>";
									$("#rsttable").append(tbTitle);
								}
								if (values && values.length > 0) {
									var tbody = "<tbody>"
									$
											.each(
													values,
													function(index, value) {
														if (value.length > 0) {
															tbody = tbody
																	+ "<tr>";
															for (var i = 0; i < value.length; i++) {
																tbody = tbody
																		+ "<td>"
																		+ value[i]
																		+ "</td>";
															}
															tbody = tbody
																	+ "</tr>";
														}
													});
									tbody = tbody + "</tbody>";
									$("#rsttable").append(tbody);
								}
								// 实现table的列之间拉大
								tabSize.init('rsttable');
							}
						});
	} else {
		layer.msg('请选择需要执行SQL的数据库！');
	}
}

function importData() {
	var ss = null;
	var index = layer.open({
		type : 2,
		title : '数据库迁移',
		shadeClose : true,
		closeBtn : 0,
		shade : 0.8,
		content : root + '/import',
		area : [ '1140px', '850px' ],
		success : function(layero, index) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			ss = iframeWin.websocket;
		},
		end : function() {
			layer.msg("数据库迁移操作");
			//var iframeWin = window[layero.find('iframe')[0]['name']];
			ss.close();

		}
	});
}

function exportcurrdata() {
	var selectSqlEle = editor.getSelectedParentElement(), treeObj = $.fn.zTree
			.getZTreeObj("treeDemo"), url = root
			+ "/service/hcloud/data/export/toexcel";
	var treeNode = treeObj.getCheckedNodes(true), value = "", sql = selectSqlEle.innerText;
	var asyncFilter = treeNode[0].asyncFilter;
	if (asyncFilter == 0) {
		value = treeNode[0].name;
	} else {
		value = treeNode[0].connName;
	}
	$.post(url, {
		connName : value,
		sql : sql
	}, function(data) {
		if (rst.rst == "-1") {
			layer.msg("执行查询内部出错！");
		} else {
			var url = root + "/service/hcloud/data/export/down";
			$.down(data.rst, url);
		}
	});

}

function delConn(obj, ele) {
	var url = root + "/service/hcloud/info/delconn";
	$.post(url, {
		connName : obj
	}, function(rst) {
		if (rst == "1") {
			layer.msg("连接删除成功！");
			$(ele).parent().parent().remove();
		} else {
			layer.msg("连接不存在或者删除报错！");
		}
	});

}

function exportScript() {
	var ss=null;
	var index = layer.open({
		type : 2,
		title : '数据库脚本导出',
		shadeClose : true,
		closeBtn : 0,
		shade : 0.8,
		content : root + '/script',
		area : [ '1140px', '850px' ],
		success : function(layero, index) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			ss = iframeWin.websocket;
		},
		end : function() {
			layer.msg("脚本导出操作");
			ss.close();

		}
	});
}
