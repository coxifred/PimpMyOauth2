package pimpmyoauth.beans;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuthorizationCode {

	String clientId;
	String state;
	String authorizationCode;
	String scopes = "api";
	LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(2);
	String redirectUri;
	
	public AuthorizationCode(String clientId,String state,String redirectUri)
	{
		this.clientId=clientId;
		this.state=state;
		this.redirectUri=redirectUri;
		authorizationCode=UUID.randomUUID().toString();
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	private void generateCode()
	{
		
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
