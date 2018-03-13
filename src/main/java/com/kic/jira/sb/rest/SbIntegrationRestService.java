package com.kic.jira.sb.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

@Scanned 
@Path("integration")
public class SbIntegrationRestService {

	
	//http://xxx:prot/context/rest/sb/1.0/integration/test
	@GET
    @Path("/test")
	@Produces({MediaType.APPLICATION_JSON})	
	public Map<String, String> test() throws Exception {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("test", "test valeu");
		rtnMap.put("aaa", "aaa.value");
		
		return rtnMap;
	}
}
