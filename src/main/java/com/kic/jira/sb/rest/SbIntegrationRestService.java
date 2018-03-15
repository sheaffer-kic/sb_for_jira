package com.kic.jira.sb.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.kic.jira.sb.vo.ActionVo;
import com.kic.jira.sb.vo.IssueStatusVo;
import com.kic.jira.sb.vo.IssueTypeVo;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;




@Scanned 
@Path("integration")
public class SbIntegrationRestService {

	private static final Logger logger = LoggerFactory.getLogger(SbIntegrationRestService.class);
	
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

	//http://localhost:2990/jira/rest/sb/1.0/integration/issueType/ST1
	@GET
    @Path("/issueType/{projectKey}")
	@Produces({MediaType.APPLICATION_JSON})		
    public List<IssueTypeVo> getProjectIssueTypeList(@PathParam("projectKey") String projectKey) throws Exception {
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
	
	//http://localhost:2990/jira/rest/sb/1.0/integration/status/ST1/10004
	@GET
    @Path("/status/{projectKey}/{issueTypeId}")
	@Produces({MediaType.APPLICATION_JSON})		
	public List<IssueStatusVo> getProjStatusOfIssueType(@PathParam("projectKey") String projectKey, @PathParam("issueTypeId") String issueTypeId) throws Exception {
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
	
	
	//http://localhost:2990/jira/rest/sb/1.0/integration/next/status/ST1/10004/13
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
	
	//http://localhost:2990/jira/rest/sb/1.0/integration/action/ST1/10001/10004
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
