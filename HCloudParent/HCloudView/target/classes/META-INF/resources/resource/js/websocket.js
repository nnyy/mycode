/**
 * 
 */

var websocket=null;

function initWebSocket() {
	var wsUri = "ws://" + document.location.host + '/' + root + "/hcloud/push/"
			+ sessionId;
	if (null == websocket) {
		websocket = new WebSocket(wsUri);
		websocket.onmessage = function(evt) {
			onMessage(evt)
		};
		websocket.onerror = function(evt) {
			onError(evt)
		};
		websocket.onopen = function(evt) {
			onOpen(evt)
		};
	}
}

function onMessage(evt) {
	$("#logCol").append(evt.data + "\r\n");

}

function onError(evt) {
	$("#logCol").append("通信连接出错\r\n");
}

function onOpen() {
	$("#logCol").append("通信连接通道建立\r\n");
}

function sendText(json) {
	console.log("sending text: " + json);
	websocket.send(json);
}
