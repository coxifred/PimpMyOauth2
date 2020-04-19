package pimpmyoauth.threads;

import pimpmyoauth.beans.User;
import pimpmyoauth.core.Core;
import pimpmyoauth.utils.Fonctions;

public class ThreadSaver extends Thread {

	

	@Override
	public void run() {
		setName("ThreadSaver");
		while (true) {
			Fonctions.trace("DBG", "Saving data now", "CORE");
			for (User aUser : Core.getInstance().getUsers().values()) {
				aUser.saveUser();
			}

			Fonctions.attendre(60000);
		}
	}

	
}
