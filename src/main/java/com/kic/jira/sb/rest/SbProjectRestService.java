package com.kic.jira.sb.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	public List<Map<String, String>> getProjectList()  throws Exception {
		logger.debug("###[START] get SmartBuilder ProjectList ###");
		
    	List<Map<String, String>> projectList = new ArrayList<Map<String, String>> ();
    	Map<String, String> projectMap = new HashMap<String, String>();
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
    	logger.debug("###[END] get SmartBuilder ProjectList ###");
		
		return projectList;
	}
}
