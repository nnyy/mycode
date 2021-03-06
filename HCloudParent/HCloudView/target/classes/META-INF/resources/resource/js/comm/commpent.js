/**
 * 动态创建组件js
 */
"use strict";
var tabSize = tabSize || {};
tabSize.init = function(id) {
	var i, self, table = document.getElementById(id), header = table.rows[0], tableX = header.clientWidth, length = header.cells.length;

	for (i = 0; i < length; i++) {
		header.cells[i].onmousedown = function() {
			self = this;
			if (event.offsetX > self.offsetWidth - 10) {
				self.mouseDown = true;
				self.oldX = event.x;
				self.oldWidth = self.offsetWidth;
			}
		};
		header.cells[i].onmousemove = function() {
			if (event.offsetX > this.offsetWidth - 10) {
				this.style.cursor = 'col-resize';
			} else {
				this.style.cursor = 'default';
			}
			if (self == undefined) {
				self = this;
			}
			if (self.mouseDown != null && self.mouseDown == true) {
				self.style.cursor = 'default';
				if (self.oldWidth + (event.x - self.oldX) > 0) {
					self.width = self.oldWidth + (event.x - self.oldX);
				}
				self.style.width = self.width;
				table.style.width = tableX + (event.x - self.oldX) + 'px';
				self.style.cursor = 'col-resize';
			}
		};
		table.onmouseup = function() {
			if (self == undefined) {
				self = this;
			}
			self.mouseDown = false;
			self.style.cursor = 'default';
			tableX = header.clientWidth;
		};
	}
};

$.extend({
	down : function(fileName, url) {
		$('<form action="' + url + '" method="get">' + // action请求路径及推送方法
		'<input type="hidden"  name="fileName" value="' + fileName + '"/>' + // 文件路径
		'</form>').appendTo('body').submit().remove();
	}
});