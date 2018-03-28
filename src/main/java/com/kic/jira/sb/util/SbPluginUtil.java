package com.kic.jira.sb.util;

import java.io.BufferedReader; 
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbPluginUtil {
	private static final Logger logger = LoggerFactory.getLogger(SbPluginUtil.class);

	public static String getRequestJsonData(HttpServletRequest request)  {
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) jb.append(line);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		 
		return jb.toString();
	}
	
	public static HttpResponse getHttpResponseForSb(Map<String, Object> httpMap, String restPath) throws Exception  {		
	     // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        HostnameVerifier hostnameVerifier = new HostnameVerifier(){
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
        };
        
        // Install the all-trusting trust manager
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

		URI uri = URI.create((String)httpMap.get("jobUrl") + restPath); //https://kmd.kicco.com/sb.view  + "/rest/project/list/for/external"
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
			
		CloseableHttpClient httpClient = HttpClients.custom()
	            .setSSLSocketFactory(sslsf)
	            //.setDefaultCredentialsProvider(credsProvider)
	            .build();
	        
			
		HttpClientContext localContext = HttpClientContext.create();		
    	HttpGet getMethod = new HttpGet(uri);
	    //postMethod.setEntity(new ByteArrayEntity(json.toString().getBytes(), ContentType.APPLICATION_JSON));	
	    HttpResponse response = httpClient.execute(host, postMethod, localContext);

			
		return response;
	}
	
	
	
	public static HttpResponse postHttpResponseForSb(Map<String, Object> httpMap, String restPath) throws Exception  {		
	     // Create a trust manager that does not validate certificate chains
       TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
               public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                   return null;
               }
               public void checkClientTrusted(X509Certificate[] certs, String authType) {
               }
               public void checkServerTrusted(X509Certificate[] certs, String authType) {
               }
           }
       };

       HostnameVerifier hostnameVerifier = new HostnameVerifier(){
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
       };
       
       // Install the all-trusting trust manager
       SSLContext sslContext = SSLContext.getInstance("SSL");
       sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

       SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

		URI uri = URI.create((String)httpMap.get("jobUrl") + restPath); //https://kmd.kicco.com/sb.view  + "/rest/project/list/for/external"
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
			
		CloseableHttpClient httpClient = HttpClients.custom()
	            .setSSLSocketFactory(sslsf)
	            //.setDefaultCredentialsProvider(credsProvider)
	            .build();
	        
			
		HttpClientContext localContext = HttpClientContext.create();		
		HttpPost postMethod = new HttpPost(uri);
		//postMethod.setHeader("Content-Type","application/json;charset=utf-8");
	    postMethod.setEntity(new ByteArrayEntity(httpMap.get("sendData").toString().getBytes(), ContentType.APPLICATION_JSON));		

	    HttpResponse response = httpClient.execute(host, postMethod, localContext);

			
		return response;
	}	
	
}
