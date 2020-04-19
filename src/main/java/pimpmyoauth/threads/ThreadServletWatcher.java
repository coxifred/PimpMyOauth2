package pimpmyoauth.threads;

import pimpmyoauth.beans.Servlet;
import pimpmyoauth.core.Core;
import pimpmyoauth.utils.Fonctions;

public class ThreadServletWatcher extends Thread {

	@Override
	public void run() {
		setName("ThreadServletWatcher");
		while (true) {
			Fonctions.trace("DBG", "Watch servlets now", "CORE");
			for (Servlet aServlet : Core.getInstance().getServlets()) {
				aServlet.check();
			}
			Fonctions.attendre(60000);
		}
	}

}
