package com.kic.jira.sb.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbConfigVo;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;
import com.kic.jira.sb.vo.SbProjectVo;


@Scanned 
@Path("project")
public class SbProjectRestService {
	private static final Logger logger = LoggerFactory.getLogger(SbProjectRestService.class);
	
	private static final String SB_PROJECT_REST = "/rest/project/list/for/external";
	private static final String PROJECT_TYPE_SW = "software";
	
	private final ProjectManager projectManager;
	private final IssueManager issueManager;
	private final SbConfigService sbConfigService;
	
	private final TestDAO testDAO;
	
	public SbProjectRestService(@ComponentImport ProjectManager projectManager,
								@ComponentImport IssueManager issueManager,
								SbConfigService sbConfigService,
								TestDAO testDAO) {
		this.projectManager = projectManager;
		this.issueManager = issueManager;
		this.sbConfigService = sbConfigService;
		this.testDAO = testDAO;
	}
	
	
	//SmartBuild project 목록 가져오기.
	@POST
    @Path("/list")
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> getProjectList(String param)  throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("result", "ok");
		logger.debug("###[START] get SmartBuilder ProjectList ###");

    	JSONObject json = new JSONObject(param);
    	int flag = json.getInt("flag");
    	String value = json.getString("value").trim();
    	
    	Project project = null;
    	if (flag == 1) project = projectManager.getProjectObjByName(value); //프로젝트명을 넘겼으면..
    	else project = issueManager.getIssueObject(Long.parseLong(value)).getProjectObject();  //issue 로 넘겼을때..
    	
    	//software 유형이 아닌경우는 수행 하지 않음.
    	if(!project.getProjectTypeKey().getKey().equals(PROJECT_TYPE_SW))  {
    		rtnMap.put("result", "warn");
    		rtnMap.put("message", "Project type should be software !!!");
    		return rtnMap;
    	}
    	
    	//SmartBuilder config 정보 가져오기..
    	SbConfigVo sbConfigVo = sbConfigService.getSelectSbConfig();
    	rtnMap.put("cfId", sbConfigVo.getSbCfId());
    	
        Map<String, Object> httpMap = new HashMap<String, Object>();  
        httpMap.put("jobUrl", sbConfigVo.getUrl());
        HttpResponse response = SbPluginUtil.getHttpResponseForSb(httpMap, SB_PROJECT_REST);
        
        int respCode = response.getStatusLine().getStatusCode();        
        System.out.println("statuscode : " + respCode);
        String responseBody = EntityUtils.toString(response.getEntity());
        if (respCode / 100 != 2) {//200 번대가 아니면 오류
        	rtnMap.put("result", "fail");        	
        	rtnMap.put("message", "ResponseCode : " + respCode + " [" + responseBody + "]");
        	return rtnMap;
        }

        ObjectMapper objectMapper = new ObjectMapper();        
        List<SbProjectVo> list = objectMapper.readValue(
        		responseBody,
                objectMapper.getTypeFactory().constructCollectionType(
                        List.class, SbProjectVo.class));
        
        rtnMap.put("sbList", list);
    	return rtnMap;		
	}
	
	
	//빌드수행 위한 정보 가져오기.
	//http://localhost:2990/jira/rest/sb/1.0/project/build/action/id/10201
	@GET
    @Path("/build/action/id/{issueId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, String> getBuildActionId(@PathParam("issueId") long issueId)  throws Exception {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("result", "ok");
		
		Issue issue = issueManager.getIssueObject(issueId);		
		Project project = issue.getProjectObject();
		
		System.out.println("projectKey : " + project.getKey() + ", getIssueTypeId : " + issue.getIssueTypeId());
		
    	//software 유형이 아닌경우는 수행 하지 않음.
    	if(!project.getProjectTypeKey().getKey().equals(PROJECT_TYPE_SW))  {
    		rtnMap.put("result", "warn");
    		rtnMap.put("message", "Project type should be software !!!");
    		return rtnMap;
    	}
		
		SbIntegrationConfigVo sbVo = testDAO.selectSbInteConfig(project.getKey(), issue.getIssueTypeId());
		if (sbVo.getBuildProgressAction() == 0) {
	   		rtnMap.put("result", "warn");
    		rtnMap.put("message", "This Issue doesn't do build job !!!");
    		return rtnMap;
		}
		
		rtnMap.put("actionId", sbVo.getBuildProgressAction() + "" );
		
		return rtnMap;
	}
	
	
	@POST
    @Path("/execute/build")
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, String> executeBuild(String param)  throws Exception {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("result", "ok");
		
		System.out.println(">>>> param : " + param);
		
		JSONObject jsonObj = new JSONObject(param);
		Issue issue = issueManager.getIssueObject(jsonObj.getLong("issueId"));		
		Project project = issue.getProjectObject();
		
		rtnMap.put("projectKey", project.getKey());
		rtnMap.put("issueType", issue.getIssueType().getName());
		
		//System.out.println("projectKey : " + project.getKey() + ", getIssueTypeId : " + issue.getIssueTypeId());
		
		
		
		return rtnMap;
	}	
}
