package pimpmyoauth.server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pimpmyoauth.utils.Fonctions;

public class ApiServlet extends AbstractServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8546977083372271300L;

	public ApiServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Fonctions.trace("DBG", "doGet from ApiServlet", "ApiServlet");
		stats(request,"apiServlet");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		Fonctions.trace("DBG", "doPost from ApiServlet", "ApiServlet");
		stats(request,"apiServlet");
	}

}