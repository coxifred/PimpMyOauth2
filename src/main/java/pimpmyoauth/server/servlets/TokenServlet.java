package pimpmyoauth.server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pimpmyoauth.beans.Token;
import pimpmyoauth.utils.Fonctions;

public class TokenServlet extends AbstractServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8546977083372271300L;

	public TokenServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Fonctions.trace("DBG", "doGet from TokenServlet", "TokenServlet");
		stats(request,"tokenServlet");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Fonctions.trace("DBG", "doPost from TokenServlet", "TokenServlet");
		stats(request,"tokenServlet");
		Token aToken=new Token();
		aToken.generate(3600);
		response.getWriter().write(toGson(aToken));
	}
//	class SigninServlet extends HttpServlet {
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//		// redirect to google for authorization
//		StringBuilder oauthUrl = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
//				.append("?client_id=").append(clientId) // the client id from the api console registration
//				.append("&response_type=code").append("&scope=openid%20email") // scope is the api permissions we
//																				// are requesting
//				.append("&redirect_uri=http://localhost:8089/callback") // the servlet that google redirects to
//																		// after authorization
//				.append("&state=this_can_be_anything_to_help_correlate_the_response%3Dlike_session_id")
//				.append("&access_type=offline") // here we are asking to access to user's data while they are not
//												// signed in
//				.append("&approval_prompt=force"); // this requires them to verify which account to use, if they are
//													// already signed in
//
//		resp.sendRedirect(oauthUrl.toString());
//	}
//}
//
//class CallbackServlet extends HttpServlet {
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		// google redirects with
//		// http://localhost:8089/callback?state=this_can_be_anything_to_help_correlate_the_response%3Dlike_session_id&code=4/ygE-kCdJ_pgwb1mKZq3uaTEWLUBd.slJWq1jM9mcUEnp6UAPFm0F2NQjrgwI&authuser=0&prompt=consent&session_state=a3d1eb134189705e9acf2f573325e6f30dd30ee4..d62c
//
//		// if the user denied access, we get back an error, ex
//		// error=access_denied&state=session%3Dpotatoes
//
//		if (req.getParameter("error") != null) {
//			resp.getWriter().println(req.getParameter("error"));
//			return;
//		}
//
//		// google returns a code that can be exchanged for a access token
//		String code = req.getParameter("code");
//
//		// get the access token by post to Google
//		String body = post("https://accounts.google.com/o/oauth2/token",
//				ImmutableMap.<String, String>builder().put("code", code).put("client_id", clientId)
//						.put("client_secret", clientSecret).put("redirect_uri", "http://localhost:8089/callback")
//						.put("grant_type", "authorization_code").build());
//
//		// ex. returns
////{
////   "access_token": "ya29.AHES6ZQS-BsKiPxdU_iKChTsaGCYZGcuqhm_A5bef8ksNoU",
////   "token_type": "Bearer",
////   "expires_in": 3600,
////   "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA5ZmE5NmFjZWNkOGQyZWRjZmFiMjk0NDRhOTgyN2UwZmFiODlhYTYifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiZW1haWwiOiJhbmRyZXcucmFwcEBnbWFpbC5jb20iLCJhdWQiOiI1MDgxNzA4MjE1MDIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdF9oYXNoIjoieUpVTFp3UjVDX2ZmWmozWkNublJvZyIsInN1YiI6IjExODM4NTYyMDEzNDczMjQzMTYzOSIsImF6cCI6IjUwODE3MDgyMTUwMi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImlhdCI6MTM4Mjc0MjAzNSwiZXhwIjoxMzgyNzQ1OTM1fQ.Va3kePMh1FlhT1QBdLGgjuaiI3pM9xv9zWGMA9cbbzdr6Tkdy9E-8kHqrFg7cRiQkKt4OKp3M9H60Acw_H15sV6MiOah4vhJcxt0l4-08-A84inI4rsnFn5hp8b-dJKVyxw1Dj1tocgwnYI03czUV3cVqt9wptG34vTEcV3dsU8",
////   "refresh_token": "1/Hc1oTSLuw7NMc3qSQMTNqN6MlmgVafc78IZaGhwYS-o"
////}
//
//		JSONObject jsonObject = null;
//
//		// get the access token from json and request info from Google
//		try {
//			jsonObject = (JSONObject) new JSONParser().parse(body);
//		} catch (ParseException e) {
//			throw new RuntimeException("Unable to parse json " + body);
//		}
//
//		// google tokens expire after an hour, but since we requested offline access we
//		// can get a new token without user involvement via the refresh token
//		String accessToken = (String) jsonObject.get("access_token");
//
//		// you may want to store the access token in session
//		req.getSession().setAttribute("access_token", accessToken);
//
//		// get some info about the user with the access token
//		String json = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=")
//				.append(accessToken).toString());
//
//		// now we could store the email address in session
//
//		// return the json of the user's basic info
//		resp.getWriter().println(json);
//	}
//}
//
//// makes a GET request to url and returns body as a string
//public String get(String url) throws ClientProtocolException, IOException {
//	return execute(new HttpGet(url));
//}
//
//// makes a POST request to url with form parameters and returns body as a string
//public String post(String url, Map<String, String> formParameters) throws ClientProtocolException, IOException {
//	HttpPost request = new HttpPost(url);
//
//	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//
//	for (String key : formParameters.keySet()) {
//		nvps.add(new BasicNameValuePair(key, formParameters.get(key)));
//	}
//
//	request.setEntity(new UrlEncodedFormEntity(nvps));
//
//	return execute(request);
//}
//
//// makes request and checks response code for 200
//private String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
//	HttpClient httpClient = new DefaultHttpClient();
//	HttpResponse response = httpClient.execute(request);
//
//	HttpEntity entity = response.getEntity();
//	String body = EntityUtils.toString(entity);
//
//	if (response.getStatusLine().getStatusCode() != 200) {
//		throw new RuntimeException(
//				"Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
//	}
//
//	return body;
//}
}