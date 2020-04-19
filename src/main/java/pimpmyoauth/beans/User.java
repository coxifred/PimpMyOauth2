package pimpmyoauth.beans;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

import pimpmyoauth.core.Core;
import pimpmyoauth.utils.Fonctions;

public class User {

	String description;
	String email;
	String name;
	String password;

	transient String remoteAdress;

	public User() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void saveUser() {
		XStream aStream = new XStream();
		try {
			File aFile = new File(System.getProperty("dataPath", Core.getInstance().getDataPath()));
			Fonctions.trace("DBG", "Saving file to " + aFile.getAbsolutePath() + "/pimpMyOAuth2_user_" + name + ".xml",
					"CORE");
			aStream.toXML(this, new FileWriter(aFile.getAbsolutePath() + "/pimpMyOAuth2_user_" + name + ".xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, User> loadUsers() {
		Map<String, User> userList = new HashMap<String, User>();
		userList.put("admin",getAdminUser());
		XStream aStream = new XStream();
		File aFile = new File(System.getProperty("dataPath", Core.getInstance().getDataPath()));
		if (aFile.exists()) {
			for (File theFile : aFile.listFiles()) {
				if (theFile.getName().startsWith("pimpMyOAuth2") && theFile.getName().endsWith(".xml")) {
					try {
						User aUser = (User) aStream.fromXML(new FileReader(theFile));
						userList.put(aUser.getName(), aUser);

					} catch (Exception e) {
						Fonctions.trace("ERR", "Couldn't reload " + theFile + e.getMessage(), "CORE");
					}
				}
			}

		} else {
			Fonctions.trace("DEAD",
					"Couldn't evaluate dataPath [" + aFile
							+ "], is it empty? Please set dataPath on a valid path in aCore.xml or in -DdataPath=...",
					"CORE");
		}

		return userList;

	}
	
	public static User getAdminUser()
	{
		User admin=new User();
		admin.setDescription("Administrator");
		admin.setName("admin");
		admin.setPassword(Core.getInstance().getAdminPassword());
		return admin;
		
	}

	public void deleteUser() {
		File aFile = new File(name + ".xml");
		aFile.delete();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemoteAdress() {
		return remoteAdress;
	}

	public void setRemoteAdress(String remoteAdress) {
		this.remoteAdress = remoteAdress;
	}

	public static boolean isAuthentified(String user, String passwd) {
		User aUser=Core.getInstance().getUsers().get(user);
		if ( aUser != null && passwd != null && passwd.equals(aUser.getPassword()))
		{
			return true;
		}
		return false;
	}

}
