package com.kic.jira.sb.rest;

/**
 * JIRA->SmartBuilder 로 요청 (빌드프로젝트 목록, 빌드)
 * 빌드결과 저장
 */
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

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.bc.issue.IssueService.TransitionValidationResult;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.UrlMode;
import com.atlassian.sal.api.user.UserManager;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.service.SbIntegrationConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbConfigVo;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;
import com.kic.jira.sb.vo.SbProjectVo;


@Scanned 
@Path("build")
public class BuildRestService {
	private static final Logger logger = LoggerFactory.getLogger(BuildRestService.class);
	
	private static final String SB_PROJECT_REST = "/rest/project/list/for/external";
	private static final String SB_BUILD_REST = "/rest/project/external/build";
	
	private static final String PROJECT_TYPE_SW = "software";
	
	private static final String BUILD_STATUS_ING = "I"; //빌드중
	private static final String BUILD_STATUS_SUCCESS = "P"; //빌드성공
	private static final String BUILD_STATUS_FAIL = "F";	//빌드실패
	
	private final ProjectManager projectManager;
	private final IssueManager issueManager;
	private final IssueService issueService;
	private final CustomFieldManager customFieldManager;
	private final com.atlassian.jira.user.util.UserManager jiraUserManager;	
	private final UserManager userManager;
	private final ApplicationProperties applicationProperties;
	
	private final SbConfigService sbConfigService;
	private final SbIntegrationConfigService sbInteConfigService;
	
	//private final TestDAO testDAO;
	
	public BuildRestService(@ComponentImport ProjectManager projectManager,
								@ComponentImport IssueManager issueManager,
								@ComponentImport IssueService issueService,
								@ComponentImport CustomFieldManager customFieldManager,
								@ComponentImport("jiraUserManager") com.atlassian.jira.user.util.UserManager jiraUserManager,
								@ComponentImport UserManager userManager,
								@ComponentImport ApplicationProperties applicationProperties,
								SbConfigService sbConfigService,
								SbIntegrationConfigService sbInteConfigService) {
		this.projectManager = projectManager;
		this.issueManager = issueManager;
		this.issueService = issueService;
		this.customFieldManager = customFieldManager;
		this.jiraUserManager = jiraUserManager;
		this.userManager = userManager;
		this.applicationProperties = applicationProperties;
		this.sbConfigService = sbConfigService;
		this.sbInteConfigService = sbInteConfigService;
	}
	
	
	//Request Build Project List From JIRA TO SmartBuilder (rest/sb/1.0/build/project/list)
	@POST
    @Path("/project/list")
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> getBuildProjectList(String param)  throws Exception {
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
	//http://localhost:2990/jira/rest/sb/1.0/build/action/id/10201
	@GET
    @Path("/action/id/{issueId}")
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
		
		SbIntegrationConfigVo sbVo = sbInteConfigService.getSelectSbIntegrationConfig(project.getKey(), issue.getIssueTypeId());
		if (sbVo.getBuildProgressAction() == 0) {
	   		rtnMap.put("result", "warn");
    		rtnMap.put("message", "This Issue doesn't do build job !!!");
    		return rtnMap;
		}
		
		rtnMap.put("actionId", sbVo.getBuildProgressAction() + "" );
		
		return rtnMap;
	}
	
	
	//Request Build From JIRA TO SmartBuilder (rest/sb/1.0/build/execute)
	@POST
    @Path("/execute")
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, String> executeBuild(String param)  throws Exception {
		Map<String, String> rtnMap = new HashMap<String, String>();
		
		JSONObject jsonObj = new JSONObject(param);
		Issue issue = issueManager.getIssueObject(jsonObj.getLong("issueId"));		
		//Project project = issue.getProjectObject();
		
		SbConfigVo sbConfigVo = sbConfigService.getSelectSbConfig();		
		//String sbProjectName = customFieldManager.getCustomFieldObject(sbConfigVo.getSbCfId()).getValue(issue).toString();
		String sbProjectName = customFieldManager.getCustomFieldObject(sbConfigVo.getSbCfId()).getValueFromIssue(issue);

		JSONObject jsonSend = new JSONObject();		
		jsonSend.put("projectName", sbProjectName);
		jsonSend.put("userId", sbConfigVo.getSbId()); 
		jsonSend.put("password", sbConfigVo.getSbPassword());		
		jsonSend.put("issueId", issue.getKey());
		jsonSend.put("feedbackUrl", applicationProperties.getBaseUrl(UrlMode.CANONICAL));
		jsonSend.put("jiraId", sbConfigVo.getJiraId());
		jsonSend.put("jiraPassword", sbConfigVo.getJiraPassword());		
		
		//logger.debug("build sendDATa : " + jsonSend.toString())	;
		
		Map<String, Object> httpMap = new HashMap<String, Object>();
		httpMap.put("jobUrl", sbConfigVo.getUrl());
		httpMap.put("sendData", jsonSend);
		
		//빌드요청 호출
		HttpResponse response = SbPluginUtil.postHttpResponseForSb(httpMap, SB_BUILD_REST);
		
        int respCode = response.getStatusLine().getStatusCode();        
        logger.debug("Build Result statuscode : " + respCode);
        
        String responseBody = EntityUtils.toString(response.getEntity());
        if (respCode / 100 != 2) {//200 번대가 아니면 오류
        	rtnMap.put("result", "fail");        	
        	rtnMap.put("message", "ResponseCode : " + respCode + " [" + responseBody + "]");
        	return rtnMap;
        }
        
        JSONObject jsonResult = new JSONObject(responseBody);
        rtnMap.put("result", jsonResult.getString("result")) ;
        rtnMap.put("message", jsonResult.getString("message"));
        //rtnMap.put("projectKey", project.getKey());
        //rtnMap.put("issueType", issue.getIssueTypeId());
        rtnMap.put("issueKey", issue.getKey());
        
        logger.debug("Build Result :: " + jsonResult.toString());
        
		return rtnMap;
	}	
	
	
	//http://localhost:2990/jira/rest/sb/1.0/build/update/result
	//Update build result (빌드중, 빌드실패, 빌드 성공)
	@POST
    @Path("/update/result")
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, String> updateBuildResult(String param)  throws Exception {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("result", "ok");
				
		JSONObject jsonObj = new JSONObject(param);
		Issue issue = issueManager.getIssueObject(jsonObj.getString("issueKey"));
		Project project = issue.getProjectObject();		
		//System.out.println("issue : " + issue.getSummary() + ", issueType : " + issue.getIssueTypeId());
		
		SbIntegrationConfigVo sbVo = sbInteConfigService.getSelectSbIntegrationConfig(project.getKey(), issue.getIssueTypeId());
		System.out.println("#### update result : " + sbVo.toString());
		System.out.println("build ING action : " + sbVo.getBuildProgressAction());
		System.out.println("build Success action : " + sbVo.getBuildSuccessId());
		System.out.println("build Fail action : " + sbVo.getBuildFailId());
		sbVo.getBuildProgressAction(); //이 액션을 수행 해야 함.
		
		String flag = jsonObj.getString("flag");
		
		System.out.println("\n\n");
		System.out.println("flag ::: " + flag);
		
		int actionId = -1;
		if (flag.equals(BUILD_STATUS_ING)) actionId = sbVo.getBuildProgressAction();  //빌드중
		else if (flag.equals(BUILD_STATUS_SUCCESS))actionId = sbVo.getBuildSuccessId(); //빌드성공
		else if (flag.equals(BUILD_STATUS_FAIL)) actionId = sbVo.getBuildFailId(); //빌드실패
		
		System.out.println("actionId ::: " + actionId);
		
		ApplicationUser user = jiraUserManager.getUserByName(userManager.getRemoteUser().getUsername());
		String issueKey = jsonObj.getString("issueKey");
		
		IssueService.IssueResult issueResult = issueService.getIssue(user, issueKey);
		MutableIssue mutableIssue = issueResult.getIssue();
		IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();	
		
		TransitionValidationResult transitionValidationResult 
			= issueService.validateTransition(user, mutableIssue.getId(), actionId, issueInputParameters); //user, issueid, actionid, issue param
		
		if (transitionValidationResult.isValid()) {
			IssueResult transitionResult = issueService.transition(user, transitionValidationResult);
			if (!transitionResult.isValid()) {
				rtnMap.put("result", "fail");				
				rtnMap.put("message", "[" + actionId  + "] Transition is not executed !!!");
			}

		} else {
			rtnMap.put("result", "fail");
			rtnMap.put("message", "Your Issue Status cannot execute [" + actionId + "]");
			//rtnMap.put("i18nResolver.getText(TRANSITION_IMPOSSIBLE));
		}
		return rtnMap;
	}		
	
	
/*	
	String url = "http://localhost:58080/sb.view"; 
	//String url = "https://kmd.kicco.com/sb.view";  //ssl  => 검토해야 함
	//String url = "http://118.36.229.198:9000/sb.view";
	String restPath = "/rest/project/external/build"; 
	
	
	HttpClient client = HttpClientBuilder.create().build();
	
	HttpPost HttpPost = new HttpPost(url + restPath);

	JSONObject json = new JSONObject();
	
	try {

		//json.put("tagName", "TEST-1");
		json.put("projectName", "프로젝트 01");
		json.put("userId", "root");
		json.put("password", "root");		
		json.put("issueId", "TEST-2");
		
		System.out.println("json: " + json.toString());
		//ContentType.APPLICATION_JSON
		
		HttpPost.setEntity(new ByteArrayEntity(json.toString().getBytes(), ContentType.APPLICATION_JSON));
		
		HttpResponse response = client.execute(HttpPost);			
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		System.out.println("statusCode ; " + statusCode);
		String responseBody = EntityUtils.toString(response.getEntity());
		
		System.out.println(">>>responsbody : " + responseBody);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		System.out.println("### error : " + e.getMessage() + ", " + e.getClass());
		
	}
	
	*/
}
