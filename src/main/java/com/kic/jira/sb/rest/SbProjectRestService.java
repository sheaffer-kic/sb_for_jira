package com.kic.jira.sb.rest;

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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;


@Scanned 
@Path("project")
public class SbProjectRestService {
	private static final Logger logger = LoggerFactory.getLogger(SbProjectRestService.class);
	
	
	@GET
    @Path("/list")
	@Produces({MediaType.APPLICATION_JSON})	
	public List<Map<String, Object>> getProjectList()  throws Exception {
		logger.debug("###[START] get SmartBuilder ProjectList ###");
		
    	List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>> ();
    	
    	
		
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
        
        
        
        //getHttpResponseForSb(Map<String, Object> sbMap,  //jobUrl
    	
    	
/*    	Map<String, String> projectMap = new HashMap<String, String>();
    	projectMap.put("id", "project01");
    	projectMap.put("text", "project01");    	
    	projectList.add(projectMap);
    	
    	
    	projectMap = new HashMap<String, String>();
    	projectMap.put("id", "project02");
    	projectMap.put("text", "project02");    	
    	projectList.add(projectMap);
    	
    	
    	projectMap = new HashMap<String, String>();
    	projectMap.put("id", "aaproject03");
    	projectMap.put("text", "aaproject0355");    	
    	projectList.add(projectMap);
    	logger.debug("###[END] get SmartBuilder ProjectList ###");*/
		
		return null;
	}
}
