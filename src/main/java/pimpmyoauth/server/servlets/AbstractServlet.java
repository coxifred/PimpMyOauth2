package pimpmyoauth.server.servlets;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import pimpmyoauth.beans.Servlet;
import pimpmyoauth.core.Core;
import pimpmyoauth.utils.Fonctions;

public class AbstractServlet extends HttpServlet {

	/**
	* 
	*/
	private static final long serialVersionUID = 5439683042873493759L;

	public Boolean stats(HttpServletRequest request, String servletName) {
		String origin = request.getRemoteHost();
		for (Servlet aServlet : Core.getInstance().getServlets()) {
			if (aServlet.getName().equals(servletName)) {
				aServlet.addAccess();
				aServlet.addAccessForOrigin(origin);
				break;
			}
		}
		if ("true".equals(request.getParameter("stats"))) {
			return true;
		} else {
			return false;
		}
	}

	protected Map<String, String> buildListParameters(HttpServletRequest request, String... keys) {
		Map<String, String> parameters = new HashMap<String, String>();
		for (String aString : keys) {
			parameters.put(aString, request.getParameter(aString));
			Fonctions.trace("DBG", aString + " -> " + request.getParameter(aString), "AbstractServlet");
		}
		return parameters;
	}

	protected Map<String, String> checkParameters(HttpServletRequest request, HttpServletResponse response,
			String... strings) {
		// Checking parameters
		Map<String, String> parameters = buildListParameters(request, strings);
		try {
			checkParametersNotNullAndNotEmpty(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parameters;
	}

	protected Boolean checkParametersNotNullAndNotEmpty(Map<String, String> keyValue) throws Exception {
		Boolean ok = true;
		String message = "";
		for (String aString : keyValue.keySet()) {
			if (keyValue.get(aString) == null || "".equals(keyValue.get(aString))) {
				message += aString + " parameter is empty or null,";
				ok = false;
			}
		}
		if (!ok) {
			Fonctions.trace("ERR", "Invalid parameters " + message, "AbstractServlet");
			throw new Exception("Invalid parameters " + message);
		}
		return ok;
	}
	
	protected String toGson(Object obj) {
		Gson aGson = new Gson();
		return aGson.toJson(obj);
	}

}
