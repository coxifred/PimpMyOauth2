package pimpmyoauth.beans;

import java.util.UUID;

public class Token {
	
	String access_token;
	Integer expires_in;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
	
	public void generate(Integer expiration)
	{
		access_token=UUID.randomUUID().toString();
		this.expires_in=expiration;
	}

}
