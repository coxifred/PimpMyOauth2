package pimpmyoauth.core;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Vector;

import pimpmyoauth.beans.Log;
import pimpmyoauth.beans.Servlet;
import pimpmyoauth.beans.User;
import pimpmyoauth.server.Webserver;
import pimpmyoauth.threads.ThreadMemory;
import pimpmyoauth.threads.ThreadSaver;
import pimpmyoauth.threads.ThreadServletWatcher;
import pimpmyoauth.utils.Fonctions;



public class Core {

	/**
	 * Singleton code
	 */
	static Core instance;

	/**
	 * webserver port
	 */
	Integer webServerPort = 443;
	
	/**
	 * websocket port
	 */
	Integer webSocketPort = 4430;
	
	/**
	 * Complete url access
	 */
	transient String urlAccess="";
	
	/**
	 * Ip for binding server
	 */
	String webServerIp;

	/**
	 * Debug mode
	 */
	Boolean debug = false;
	
	/**
	 * Debug mode
	 */
	Boolean debugJetty = false;

	/**
	 * Webserver
	 */
	transient Webserver ws;

	/**
	 * Admin password
	 */
	String adminPassword;

	/**
	 * uuid generated in case of webserver
	 */
	UUID uuid;

	Integer maxLogEntries = 1000;

	/*
	 * for https,http2 and alpn)
	 */
	Boolean http2 = true;
	
	/**
	 * Session id
	 */
	Long sessionId=0l;

	/**
	 * List des users
	 * 
	 * @throws Exception
	 */
	Map<String, User> users = new HashMap<String, User>();
	
	/**
	 * List of servlety to watch, populated by Webserver
	 */
	List<Servlet> servlets = new ArrayList<Servlet>();

	/**
	 * Logs
	 */
	List<Log> logs = new ArrayList<Log>();

	/**
	 * Datapath
	 */
	String dataPath = "";

	

	public void launch() throws Exception {

		Fonctions.trace("INF", "Starting main core", "CORE");
		// Chargement des users
		setUsers(User.loadUsers());

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		// Creating default Directory (plugins)
		Fonctions.trace("INF", "Checking/Creating plugins directory in " + getDataPath() + "/plugins", "CORE");
		new File(getDataPath() + "/plugins").mkdirs();

		// Loading plugins
		loadPlugin();

		/**
		 * Starting webserver
		 */
		ws = new Webserver();

		Fonctions.trace("INF", "Starting webserver on port " + getWebServerPort(), "CORE");
		ws = new Webserver();
		if (getWebServerIp() == null) {
			ws.setIp(InetAddress.getLocalHost().getHostAddress());
		} else {
			ws.setIp(getWebServerIp());
		}
		ws.setPort(getWebServerPort());
		ws.startWebSocket(http2);
		Fonctions.traceBanner();
		
		Fonctions.trace("INF", "", "CORE");
		Fonctions.trace("INF", "", "CORE");
		Fonctions.trace("INF",
				"Please open this url in you favorite browser https://" + ws.getIp() + ":" + ws.getPort(), "CORE");
		urlAccess="https://" + ws.getIp() + ":" + ws.getPort();
		Fonctions.trace("INF", "", "CORE");
		Fonctions.trace("INF", "", "CORE");

		// Starting ThreadMemory
		ThreadMemory tm = new ThreadMemory();
		tm.start();

		// Starting ThreadSaver (save data)
		ThreadSaver ts = new ThreadSaver();
		ts.start();
		
		// Starting ThreadServletWatcher (statistics for servlets)
		ThreadServletWatcher tsw = new ThreadServletWatcher();
		tsw.start();

		while (ws.getWebSocketThread().isAlive()) {
			Fonctions.attendre(5000);

			if (!ts.isAlive()) {
				ts = new ThreadSaver();
				ts.start();
			}
			if (!tm.isAlive()) {
				tm = new ThreadMemory();
				tm.start();
			}
			if (!tsw.isAlive()) {
				tsw = new ThreadServletWatcher();
				tsw.start();
			}
		}

		Fonctions.trace("INF", "Ending main core", "CORE");
		System.exit(0);
	}

	private void loadPlugin() throws IOException {

	}
	
	

	public static Core getInstance() {
		if (instance == null) {
			instance = new Core();
		}
		return instance;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public static void setInstance(Core instance) {
		Core.instance = instance;
	}

	public Integer getWebServerPort() {
		return webServerPort;
	}

	public void setWebServerPort(Integer webServerPort) {
		this.webServerPort = webServerPort;
	}

	public String getWebServerIp() {
		return webServerIp;
	}

	public void setWebServerIp(String webServerIp) {
		this.webServerIp = webServerIp;
	}

	public Webserver getWs() {
		return ws;
	}

	public void setWs(Webserver ws) {
		this.ws = ws;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}



	public Boolean getHttp2() {
		return http2;
	}

	public void setHttp2(Boolean http2) {
		this.http2 = http2;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(Vector<Log> logs) {
		this.logs = logs;
	}

	public int getMaxLogEntries() {
		return maxLogEntries;
	}

	public void setMaxLogEntries(Integer maxLogEntries) {
		this.maxLogEntries = maxLogEntries;
	}

	

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public synchronized Long getNextSessionId()
	{
		if ( sessionId == null)
		{
			sessionId=0l;
		}
		sessionId++;
		return sessionId;
	}

	public Boolean getDebugJetty() {
		return debugJetty;
	}

	public void setDebugJetty(Boolean debugJetty) {
		this.debugJetty = debugJetty;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public Integer getWebSocketPort() {
		return webSocketPort;
	}

	public void setWebSocketPort(Integer webSocketPort) {
		this.webSocketPort = webSocketPort;
	}

	public List<Servlet> getServlets() {
		return servlets;
	}

	public void setServlets(List<Servlet> servlets) {
		this.servlets = servlets;
	}

	public String getUrlAccess() {
		return urlAccess;
	}

	public void setUrlAccess(String urlAccess) {
		this.urlAccess = urlAccess;
	}
	
}
