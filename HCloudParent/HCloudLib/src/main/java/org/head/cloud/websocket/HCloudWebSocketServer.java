package org.head.cloud.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/hcloud/push/{clientId}")
public class HCloudWebSocketServer {

	private static Map<String, Session> _SESSIONMAPS_ = new ConcurrentHashMap<String, Session>();

	@OnMessage
	public void onMessage(String message, Session session, @PathParam("clientId") String clientId) {
		String[] strs = message.split(";");
		Session s = _SESSIONMAPS_.get(strs[0]);
		try {
			s.getBasicRemote().sendText(strs[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("clientId") String clientId) {
		_SESSIONMAPS_.put(clientId, session);
	}

	@OnClose
	public void onClose(Session session, @PathParam("clientId") String clientId) {
		System.out.println("客户端关闭:" + clientId);
		_SESSIONMAPS_.remove(clientId);
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("客户端异常断开");
	}

}
