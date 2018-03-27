package com.kic.jira.sb.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.vo.SbConfigVo;

@Scanned 
@Path("config")
public class SbConfigRestService {
	private static final Logger logger = LoggerFactory.getLogger(SbConfigRestService.class);
	
	private final SbConfigService sbConfigService;
	private final ProjectManager projectManager;
	
	public SbConfigRestService(SbConfigService sbConfigService,
							   @ComponentImport ProjectManager projectManager) {
		this.sbConfigService = sbConfigService;
		this.projectManager = projectManager;
	}	
	
	
	
	@GET
    @Path("/sw/project/cf/list")
	@Produces({MediaType.APPLICATION_JSON})	
	public Map<String, Object> getCfListOfSwProject(@Context HttpServletRequest req) throws Exception { 		
		String searchValue = req.getParameter("searchValue");
		int page = Integer.parseInt(req.getParameter("page"))-1;
		int pageLimit = Integer.parseInt(req.getParameter("pageLimit"));
		int startIdx = page * pageLimit;
		
		CustomFieldManager cfm = ComponentAccessor.getCustomFieldManager();
		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		List<Project> projectList = projectManager.getProjects();
		List<String> projIssueTypeList = new ArrayList<String> ();		
		List<Map<String, String>> cfList = new ArrayList<Map<String, String>>();
		
		for (Project project : projectList) {
			Collection<IssueType> issueTypes = project.getIssueTypes();
			for (IssueType it : issueTypes) {
	        	projIssueTypeList.add(it.getId());
	        }
			
			List<CustomField> projectFields = cfm.getCustomFieldObjects(project.getId(), projIssueTypeList);			
			for (CustomField cf : projectFields) {
				logger.debug("cfName : " + cf.getName());
				System.out.println("cf ::: " + cf.getName() + ", id : " + cf.getId() + ", getNameKey : " + cf.getNameKey());
				Map<String, String> cfMap = new HashMap<String, String>();

				if (cf.getName().contains(searchValue)) {
					cfMap.put("id", cf.getId() + "|" + cf.getName());
					//cfMap.put("id", cf.getName());
					cfMap.put("cf_name", cf.getName());
					cfList.add(cfMap);
				}
			}			
		}

		//중복제거.. (java8 의 Lambada)
		cfList = cfList.parallelStream().distinct().collect(Collectors.toList());
		int totCount = cfList.size();
		rtnMap.put("totCount", totCount);
		
		int lastIdx = startIdx + pageLimit;
		if (totCount <= lastIdx) lastIdx = totCount;
		cfList = cfList.subList(startIdx, lastIdx);
		rtnMap.put("cfList", cfList);
		return rtnMap;
	}
	
	
	//localhost:2990/jira/rest/sb/1.0/config/info
	@GET
    @Path("/info")
	@Produces({MediaType.APPLICATION_JSON})	
	public SbConfigVo getSbConfigInfo() throws Exception {		
		SbConfigVo vo = sbConfigService.getSelectSbConfig();
		if(vo.getID() == 0){
			vo.setSbId("");
			vo.setSbPassword("");
			vo.setUrl("");
			vo.setSbCfId("");	
			vo.setSbCfName("");
			vo.setJiraId("");
			vo.setJiraPassword("");			
		}
		
		return vo;		
	}
	
	
	
//	
//	@GET
//    @Path("/list")
//	@Produces({MediaType.APPLICATION_JSON})	
//	public Map<String, String> test() throws Exception {
//		Map<String, String> rtnMap = new HashMap<String, String>();
//		String projectKey = "SCRUM01";
//		
//		//Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
//		
//		System.out.println("projectManager ::: "+ projectManager);
//		
//		
//		//Project project = ComponentAccessor.getProjectManager().getProjectObjByKey(projectKey);
//		//ScreenManager aa = null;
//		
//		//ComponentAccessor.get
//		
//		List<Project> projectList = projectManager.getProjects();
//		List<String> projIssueTypeList = new ArrayList<String> ();
//		CustomFieldManager cfm = ComponentAccessor.getCustomFieldManager(); 
//		
//		for (Project project : projectList) {
//			Collection<IssueType> issueTypes = project.getIssueTypes();
//			for (IssueType it : issueTypes) {
//	        	projIssueTypeList.add(it.getId());
//	        }
//			
//			List<CustomField> projectFields = cfm.getCustomFieldObjects(project.getId(), projIssueTypeList);
//			
//			for (CustomField cf : projectFields) {
//				System.out.println("cf ::: " + cf.getName() + ", id : " + cf.getId() + ", getNameKey : " + cf.getNameKey());
//			}
//			
//			System.out.println("-----------");
//		}
//		
//		
//		//List<CustomField> projectFields = cfm.getCustomFieldObjects(projectId, projIssueTypeList);
//		
//		//project.getProjectTypeKey().getKey().equals("software")){//파람으로
//		
////		CustomFieldManager cfm = ComponentAccessor.getCustomFieldManager(); 
////		Project project = projectManager.getProjectObjByKey(projectKey); 
////		long projectId = project.getId(); 
//		
//        //List<IssueTypeVo> projIssueTypeList = new ArrayList<IssueTypeVo>();
//
///*        // long projectId = project.getId();
//        Collection<IssueType> issueTypes = project.getIssueTypes();
//        List<String> projIssueTypeList = new ArrayList<String> ();
//        for (IssueType it : issueTypes) {
//        	projIssueTypeList.add(it.getId());
////            IssueTypeVo vo = new IssueTypeVo();
////            vo.setId(it.getId());
////            vo.setName(it.getName());
////            projIssueTypeList.add(vo);
//        }
//        
//        System.out.println("issueTypes : " + projIssueTypeList.toString());
//        //cfm.getCustomFieldObject
//		List<CustomField> projectFields = cfm.getCustomFieldObjects(projectId, projIssueTypeList);
//		System.out.println(">>> @@@@@ : " + projectFields.size());
//		
//		for (CustomField cf : projectFields) {
//			System.out.println("cf ::: " + cf.getName() + ", id : " + cf.getId() + ", getNameKey : " + cf.getNameKey());
//		}*/
//		rtnMap.put("projectKey", projectKey);
//		
//		
//		return rtnMap;
//	}
	
}
