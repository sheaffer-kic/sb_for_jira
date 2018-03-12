package com.kic.jira.sb.rest;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.util.json.JSONArray;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;


//@AnonymousAllowed
@Scanned 
@Path("test")
public class TestRestService {
	
	private static final Logger logger = LoggerFactory.getLogger(TestRestService.class);
	
	//localhost:2990/context/rest/sb/1.0/test/list/
	@GET
    @Path("/list")
	@Produces({MediaType.APPLICATION_JSON})	
	public List<Map<String, Object>> getList()  throws Exception {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>  ();
		
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                 return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}};

		HostnameVerifier hostnameVerifier = new HostnameVerifier(){
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
		};
     
		//Install the all-trusting trust manager
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
    
		JSONObject json = new JSONObject();
		json.put("tagName", "TEST-1");
		json.put("projectId", "7");
		json.put("userId", "dev02");
		json.put("userPw", "qwe1234$");
		
		//URI uriProj = URI.create("https://rulrura.com/jira/rest/api/2/issue");
		URI uriProj = URI.create("https://kmd.kicco.com/sb.view/rest/projectresult/jira");
		String uriHost = uriProj.getHost();
		int uriPort = uriProj.getPort();
		String uriScheme = uriProj.getScheme();
		HttpHost host = new HttpHost(uriHost, uriPort, uriScheme);
		
		CloseableHttpClient httpClient = HttpClients.custom()
             .setSSLSocketFactory(sslsf)
             //.setDefaultCredentialsProvider(credsProvider)
             .build();
     
		
		HttpClientContext localContext = HttpClientContext.create();		
		HttpPost postMethod = new HttpPost(uriProj);
		postMethod.setEntity(new ByteArrayEntity(json.toString().getBytes(), ContentType.APPLICATION_JSON));	
		HttpResponse response = httpClient.execute(host, postMethod, localContext);
		
		System.out.println("response statusLine :: " + response.getStatusLine());
		System.out.println("statuscode : " + response.getStatusLine().getStatusCode());
     
		String responseBody = EntityUtils.toString(response.getEntity());
		System.out.println("responbBody : " + responseBody);	
     
		JSONObject jsonResponse = new JSONObject(responseBody);
		System.out.println(jsonResponse.get("list"));
     
		JSONArray sbList = new JSONArray(jsonResponse.getString("list"));
		for (int i=0; i < sbList.length(); i++) {
			JSONObject jj = sbList.getJSONObject(i);     	
			ObjectMapper mapper = new ObjectMapper();
     	
			TypeReference<HashMap<String,Object>> typeRef 
				= new TypeReference<HashMap<String,Object>>() {};
			Map<String,Object> o = mapper.readValue(jj.toString(), typeRef);
			System.out.println("o :: " + o.toString());
     	
			rtnList.add(o);
		}
  
		return rtnList;
	}
	
/*	@GET
    @Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})	
	public Map<String, String> getCustomerById(@PathParam("id") int id) {
		
	}*/

}
