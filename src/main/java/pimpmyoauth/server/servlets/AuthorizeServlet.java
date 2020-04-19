package pimpmyoauth.server.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pimpmyoauth.beans.AuthorizationCode;
import pimpmyoauth.utils.Fonctions;

public class AuthorizeServlet extends AbstractServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8546977083372271300L;

	public AuthorizeServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Fonctions.trace("DBG", "doGet from AuthorizeServlet", "AuthorizeServlet");
		if ( ! stats(request,"authorizeServlet"))
		{
			perform(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		Fonctions.trace("DBG", "doPost from AuthorizeServlet", "AuthorizeServlet");
		stats(request,"authorizeServlet");
		perform(request,response);
	}
	
	private void perform(HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> parameters=checkParameters(request,response,"response_type","client_id","redirect_uri","scope","state");
		try {
			Fonctions.trace("DBG", "Building client authorization", "AuthorizeServlet");
			AuthorizationCode ac=new AuthorizationCode(parameters.get("client_id"), parameters.get("state"), parameters.get("redirect_uri"));
			
			response.sendRedirect("/grant.html?client_id=" + parameters.get("client_id") + "&state=" + parameters.get("state") + "&redirect_uri=" + parameters.get("redirect_uri")+"&code=" + ac.getAuthorizationCode() +"&back=" + request.getRemoteHost());
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}