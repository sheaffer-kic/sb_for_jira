package com.kic.jira.sb.rest;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
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

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.util.json.JSONArray;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.kic.jira.sb.service.SbIntegrationConfigService;
import com.kic.jira.sb.vo.ActionVo;
import com.kic.jira.sb.vo.IssueStatusVo;
import com.kic.jira.sb.vo.IssueTypeVo;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;


//@AnonymousAllowed
@Scanned 
@Path("test")
public class TestRestService {
	
	private final SbIntegrationConfigService sbInteConfigService;
	
	public TestRestService(SbIntegrationConfigService sbInteConfigService) {
		this.sbInteConfigService = sbInteConfigService;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(TestRestService.class);
	
	
	//localhost:2990/jira/rest/sb/1.0/test/save/inte/config
	@GET
    @Path("/save/inte/config")
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, String> saveSbInteConfg() throws Exception {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("result", "ok");
		
/*		SbIntegrationConfigVo sbInteConfigVo = new SbIntegrationConfigVo();
		sbInteConfigVo.setProjectKey("SCRUM01");
		sbInteConfigVo.setIssueType("10002");
		
		sbInteConfigVo.setBuildTargetId(10003);
		sbInteConfigVo.setBuildTargetName("개발완료");
		sbInteConfigVo.setBuildStepId(3);
		
		sbInteConfigVo.setBuildProgressId(10004);
		sbInteConfigVo.setBuildProgressName("빌드중");
		sbInteConfigVo.setBuildProgressAction(41);
		
		sbInteConfigVo.setBuildSuccessId(51);
		sbInteConfigVo.setBuildSuccessName("빌드성공");
		
		sbInteConfigVo.setBuildFailId(61);
		sbInteConfigVo.setBuildFailName("빌드실패");
		
		testDAO.insertSbInteConfig(sbInteConfigVo);*/
		
		rtnMap.put("message", "now not insert !!!");
		return rtnMap;
	}
	
	//localhost:2990/jira/rest/sb/1.0/test/info/inte/config/SCRUM01/10002
	@GET
    @Path("/info/inte/config/{projectKey}/{issueType}")
	@Produces({MediaType.APPLICATION_JSON})
	public SbIntegrationConfigVo getSbInteConfg(@PathParam("projectKey") String projectKey, 
												@PathParam("issueType") String issueType) throws Exception {
		return sbInteConfigService.getSelectSbIntegrationConfig(projectKey, issueType);
	}	
	
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
	
	
	//http://localhost:2990/jira/rest/sb/1.0/test/issuetype/SCRUM01
	
	@GET
    @Path("/issuetype/{projectKey}")
	@Produces({MediaType.APPLICATION_JSON})	
	public List<IssueTypeVo> getIssueTypeByProjectKey (@PathParam("projectKey") String projectKey) {
		Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
		
		if (project == null) {
	        if (logger.isDebugEnabled())
	            logger.debug("Project is null");
	        return null;
	    }

	    List<IssueTypeVo> projIssueTypeList = new ArrayList<IssueTypeVo>();

	    // long projectId = project.getId();
	    Collection<IssueType> issueTypes = project.getIssueTypes();

	    for (IssueType it : issueTypes) {
	        IssueTypeVo vo = new IssueTypeVo();
	        vo.setId(it.getId());
	        vo.setName(it.getName());
	        projIssueTypeList.add(vo);
	    }	
	    
	    
	    return projIssueTypeList;
	}

	//http://localhost:2990/jira/rest/sb/1.0/test/status/SCRUM01/10002
	@GET
    @Path("/status/{projectKey}/{issueTypeId}")
	@Produces({MediaType.APPLICATION_JSON})		
	public List<IssueStatusVo> getProjStatusOfIssueType(@PathParam("projectKey") String projectKey, 
								@PathParam("issueTypeId") String issueTypeId) throws Exception {
		Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
		WorkflowManager workflowManager = ComponentAccessor.getWorkflowManager();  		
		long projectId = project.getId();
		JiraWorkflow jwf = workflowManager.getWorkflow(projectId, issueTypeId);		
		List<Status> statusList = jwf.getLinkedStatusObjects();	
		List<IssueStatusVo> issueTypeStatusList = new ArrayList<IssueStatusVo>();
		
		for (Status s : statusList) {
			IssueStatusVo vo = new IssueStatusVo();
			vo.setId(s.getId());
			vo.setStepId(jwf.getLinkedStep(s).getId());
			vo.setName(s.getName());
			issueTypeStatusList.add(vo);
		}
		//return Response.ok(workTypeStatusList, MediaType.APPLICATION_JSON).build();		
		return issueTypeStatusList;
	}	
	

	//http://localhost:2990/jira/rest/sb/1.0/test/next/status/SCRUM01/10002/3
	
	@GET
    @Path("/next/status/{projectKey}/{issueTypeId}/{stepId}")
	@Produces({MediaType.APPLICATION_JSON})		
	public List<IssueStatusVo> getNextStatusOfStep(@PathParam("projectKey") String projectKey, 
													     @PathParam("issueTypeId") String issueTypeId,  
													     @PathParam("stepId") int stepId) throws Exception {
		Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
		WorkflowManager workflowManager = ComponentAccessor.getWorkflowManager();  
		
		long projectId = project.getId();
		JiraWorkflow jwf = workflowManager.getWorkflow(projectId, issueTypeId);		

		List<Status> statusList = jwf.getLinkedStatusObjects();	
				
		// Collection<StepDescriptor> stepList = jwf.getStepsForTransition(act);  //해당 액션을 수행하는 단계 (step 입장에서는  out action)		
		StepDescriptor step = jwf.getDescriptor().getStep(stepId);
		//해당단계가 수행하는 액션
		List<ActionDescriptor> listAction = step.getActions();
		List<IssueStatusVo> issueTypeStatusList = new ArrayList<IssueStatusVo>();
		
		for (ActionDescriptor act : listAction) {			
			for (Status status : statusList) {
				StepDescriptor stepDesc = jwf.getLinkedStep(status);
				Collection<ActionDescriptor> actList = jwf.getActionsWithResult(stepDesc); //step 입장에서는 in action 임
				
				for (ActionDescriptor actDesc : actList) {
					if (actDesc.getName().equals(act.getName())) {
						IssueStatusVo vo = new IssueStatusVo();
						vo.setId(status.getId());
						vo.setName(status.getName());
						vo.setActionId(act.getId());
						
						issueTypeStatusList.add(vo);
						break;
					}
				}
			}

		}
		return issueTypeStatusList;
	}	
	
	
	
	/*
	 * 특정 issue type > 특정상태 > 액션 목록 가져오기.
	 * http://localhost:2990/jira/rest/sb/1.0/test/action/SCRUM01/10002/10004
	 */
	@GET
    @Path("/action/{projectKey}/{issueTypeId}/{statusId}")
	@Produces({MediaType.APPLICATION_JSON})		
	public List<ActionVo> getActionList(@PathParam("projectKey") String projectKey, 
	                                    @PathParam("issueTypeId") String issueTypeId, 
	                                    @PathParam("statusId") String statusId  ) throws Exception {
		Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
		WorkflowManager workflowManager = ComponentAccessor.getWorkflowManager();  
		
		long projectId = project.getId();

		JiraWorkflow jwf = workflowManager.getWorkflow(projectId, issueTypeId);				
		List<Status> statusList = jwf.getLinkedStatusObjects();	
		
		List<ActionVo> actionList = new ArrayList<ActionVo>();
		for (Status s : statusList) {
			if (s.getId().equals(statusId)) {
				StepDescriptor stepDesc = jwf.getLinkedStep(s);	 
				List<ActionDescriptor> listAction = stepDesc.getActions();
				
				for (ActionDescriptor act : listAction) {
					ActionVo vo = new ActionVo();
					vo.setId(act.getId());
					vo.setName(act.getName());
					actionList.add(vo);
				}
				break;
			}
		}
		//return Response.ok(actionList, MediaType.APPLICATION_JSON).build();
		
		return actionList;
	}	
}
