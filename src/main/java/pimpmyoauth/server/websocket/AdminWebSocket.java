package pimpmyoauth.server.websocket;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;

import pimpmyoauth.utils.Fonctions;



@ServerEndpoint(value = "/echo")
public class AdminWebSocket implements WebSocketListener {

	Boolean close = false;

	@OnWebSocketClose
	public void onWebSocketClose(int statusCode, String reason) {
		close = true;
		Fonctions.trace("DBG", "onWebSocketClose " + statusCode + " " + reason, "CORE");
	}

	@OnWebSocketConnect
	public void onWebSocketConnect(Session session) {
		Fonctions.trace("DBG", "onWebSocketConnect", "CORE");
		
	}

	@OnWebSocketError
	public void onWebSocketError(Throwable e) {
		Fonctions.trace("DBG", "onWebSocketError", "CORE");

	}

	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		Fonctions.trace("DBG", "onWebSocketBinary", "CORE");
	}

	@OnMessage
	public void onWebSocketText(String message) {
		Fonctions.trace("DBG", "onWebSocketText " + message, "CORE");
		if (!close) {}


	}

	

	public Boolean getClose() {
		return close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}



}
