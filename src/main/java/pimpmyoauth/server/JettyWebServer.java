package pimpmyoauth.server;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import pimpmyoauth.beans.Servlet;
import pimpmyoauth.core.Core;
import pimpmyoauth.server.servlets.AdminServlet;
import pimpmyoauth.server.servlets.ApiServlet;
import pimpmyoauth.server.servlets.AuthorizeServlet;
import pimpmyoauth.server.servlets.CallbackServlet;
import pimpmyoauth.server.servlets.TokenServlet;
import pimpmyoauth.server.websocket.AdminWebSocket;
import pimpmyoauth.server.websocket.JettyWebSocketServlet;
import pimpmyoauth.utils.Fonctions;
import pimpmyoauth.utils.JettyLogger;



public class JettyWebServer extends Thread {

	Integer port;
	String ip;
	Server serverWeb;
	Server serverSocket;
	Boolean http2;

	public JettyWebServer(String ip, Integer port, Boolean http2) {
		this.ip = ip;
		this.port = port;
		this.http2 = http2;
		start();
	}

	public void run() {
		
		org.eclipse.jetty.util.log.Log.setLog(new JettyLogger());

		serverWeb = new Server();
		serverSocket = new Server();
		http2 = false;
		try {
			if (http2) {
				Fonctions.trace("DBG", "Starting HTTP2 server", "CORE");
				startWeb(serverWeb, serverSocket);

			} else {
				Fonctions.trace("DBG", "Starting classic server", "CORE");
				startWebSimple(serverWeb, serverSocket);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void startWeb(Server serverWeb, Server serverSocket) throws Exception, InterruptedException {

		setContextsWeb(serverWeb);
		setContextsSocket(serverSocket);

		String jettyDistKeystore = "ssl/keystore";
		String keystorePath = System.getProperty("keystore", jettyDistKeystore);
		File keystoreFile = new File(keystorePath);
		if (!keystoreFile.exists()) {
			throw new FileNotFoundException(keystoreFile.getAbsolutePath());
		}else
		{
			
		}

		// HTTP Configuration
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(port);
		http_config.addCustomizer(new SecureRequestCustomizer());

		// SSL Context Factory for HTTPS and HTTP/2
		SslContextFactory sslContextFactory = new SslContextFactory.Server();
		sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
		sslContextFactory.setKeyStorePassword("pimpMyGps");
		sslContextFactory.setKeyManagerPassword("pimpMyGps");
		sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

		// HTTP/2 Connection Factory
//		HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(http_config);
//		ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
//		alpn.setDefaultProtocol("h2");

		// SSL Connection Factory
		SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());

		// HTTP/2 Connector
//		ServerConnector http2Connector = new ServerConnector(server, ssl, alpn, h2,
//				new HttpConnectionFactory(http_config));
		ServerConnector http2Connector = new ServerConnector(serverWeb, ssl, new HttpConnectionFactory(http_config));
		http2Connector.setPort(port);
		http2Connector.setHost(ip);
		serverWeb.addConnector(http2Connector);

		// Socket
		ServerConnector connector = new ServerConnector(serverSocket);
		connector.setPort(8080);
		serverSocket.addConnector(connector);

		serverWeb.start();
		serverSocket.start();
		serverWeb.join();
		serverSocket.join();

	}

	private void startWebSimple(Server serverWeb, Server ServerSocket) throws Exception, InterruptedException {

		setContextsWeb(serverWeb);
		setContextsSocket(ServerSocket);

		SslContextFactory contextFactory = new SslContextFactory();

		String jettyDistKeystore = "ssl/keystore";
		String keyStorePath = getClass().getClassLoader().getResource(jettyDistKeystore).toExternalForm();
		
		
		contextFactory.setKeyStorePath(keyStorePath);
		contextFactory.setKeyStorePassword("pimpMyGps");
		contextFactory.setKeyManagerPassword("pimpMyGps");
		// contextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

		SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(contextFactory,
				org.eclipse.jetty.http.HttpVersion.HTTP_1_1.toString());

		HttpConfiguration config_ssl = new HttpConfiguration();
		config_ssl.setSecureScheme("https");
		config_ssl.setSecurePort(Core.getInstance().getWebServerPort());
		config_ssl.setOutputBufferSize(32786);
		config_ssl.setRequestHeaderSize(8192);
		config_ssl.setResponseHeaderSize(8192);
		HttpConfiguration sslConfiguration = new HttpConfiguration(config_ssl);
		sslConfiguration.addCustomizer(new SecureRequestCustomizer());
		HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(sslConfiguration);

		ServerConnector connector = new ServerConnector(serverWeb, sslConnectionFactory, httpConnectionFactory);
		connector.setPort(Core.getInstance().getWebServerPort());
		serverWeb.addConnector(connector);

		// Socket
		ServerConnector connectorSocket = new ServerConnector(serverSocket, sslConnectionFactory, httpConnectionFactory);
		connectorSocket.setPort(Core.getInstance().getWebSocketPort());
		serverSocket.addConnector(connectorSocket);

		serverWeb.start();
		serverSocket.start();
		serverWeb.join();
		serverSocket.join();

	}

	private void setContextsWeb(Server server) {
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		WebAppContext webAppContext = new WebAppContext();
		ServletContextHandler authorizeContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		authorizeContext.setContextPath("/authorize");
		authorizeContext.addServlet(new ServletHolder(new AuthorizeServlet()), "/*");
		Servlet authorize=new Servlet();
		authorize.setName("authorizeServlet");
		authorize.setEndPoint("/authorize");
		Core.getInstance().getServlets().add(authorize);
		
		ServletContextHandler tokenContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		tokenContext.setContextPath("/token");
		tokenContext.addServlet(new ServletHolder(new TokenServlet()), "/*");
		Servlet token=new Servlet();
		token.setName("tokenServlet");
		token.setEndPoint("/token");
		Core.getInstance().getServlets().add(token);
		
		ServletContextHandler apiContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		apiContext.setContextPath("/api");
		apiContext.addServlet(new ServletHolder(new ApiServlet()), "/*");
		Servlet api=new Servlet();
		api.setName("apiServlet");
		api.setEndPoint("/api");
		Core.getInstance().getServlets().add(api);
		
		ServletContextHandler callbackContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		callbackContext.setContextPath("/callback");
		callbackContext.addServlet(new ServletHolder(new CallbackServlet()), "/*");
		Servlet callback=new Servlet();
		callback.setName("callbackServlet");
		callback.setEndPoint("/callback");
		Core.getInstance().getServlets().add(callback);

		ServletContextHandler adminContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		adminContext.setContextPath("/admin");
		adminContext.addServlet(new ServletHolder(new AdminServlet()), "/*");
		Servlet admin=new Servlet();
		admin.setName("adminServlet");
		admin.setEndPoint("/admin");
		Core.getInstance().getServlets().add(admin);

		
		ServletContextHandler socketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
		socketContext.setContextPath("/adminwebsocket");
		socketContext.addServlet(new ServletHolder(new JettyWebSocketServlet()), "");
		

		try {
			ServerContainer jsrContainer = WebSocketServerContainerInitializer.configureContext(webAppContext);
			jsrContainer.addEndpoint(AdminWebSocket.class);
		} catch (Exception e) {

			e.printStackTrace();
		}


		String webInterfaceDir = getClass().getClassLoader().getResource("webInterface").toExternalForm();
		webAppContext.setResourceBase(webInterfaceDir);
		// webAppContext.setResourceBase("webInterface");
		contexts.setHandlers(new Handler[] { authorizeContext, webAppContext, socketContext,adminContext,tokenContext,apiContext,callbackContext });
		server.setHandler(contexts);
	}

	private void setContextsSocket(Server server) {

		ServletContextHandler socketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);

		server.setHandler(socketContext);

		// Add a websocket to a specific path spec
		ServletHolder holderSocket = new ServletHolder("ws-adminwebsocket", JettyWebSocketServlet.class);
		socketContext.addServlet(holderSocket, "/adminwebsocket/*");
		socketContext.setContextPath("/");

	}

	public Server getServerWeb() {
		return serverWeb;
	}

	public void setServerWeb(Server serverWeb) {
		this.serverWeb = serverWeb;
	}

	public Server getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(Server serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getHttp2() {
		return http2;
	}

	public void setHttp2(Boolean http2) {
		this.http2 = http2;
	}

}
