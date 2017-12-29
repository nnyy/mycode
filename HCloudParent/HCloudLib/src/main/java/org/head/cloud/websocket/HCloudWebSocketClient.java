package org.head.cloud.websocket;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.lang.StringUtils;

@ClientEndpoint
public class HCloudWebSocketClient {

	public HCloudWebSocketClient(URI endpointURI) {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			container.connectToServer(this, endpointURI);
		} catch (DeploymentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Session session;

	CustomMessageHandler messageHandler;

	public Session getSession() {
		return session;
	}

	@OnOpen
	public void onOpen(Session userSession) {
		System.out.println("client: opening websocket ");
		this.session = userSession;
	}

	/**
	 * Callback hook for Connection close events.
	 *
	 * @param userSession
	 *            the userSession which is getting closed.
	 * @param reason
	 *            the reason for connection close
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("client: closing websocket");
		this.session = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a client
	 * send a message.
	 *
	 * @param message
	 *            The text message
	 */
	@OnMessage
	public void onMessage(String message) {
		System.out.println("client: received message " + message);
		if (StringUtils.isNotEmpty(message)) {
			this.messageHandler.handler(message);
		}

	}

	public void addMessageHandler(CustomMessageHandler handler) {
		this.messageHandler = handler;
	}

	public void sendMessage(String msg) {
		this.session.getAsyncRemote().sendText(msg);
	}

	public static interface CustomMessageHandler {

		void handler(String msg);
	}
}
