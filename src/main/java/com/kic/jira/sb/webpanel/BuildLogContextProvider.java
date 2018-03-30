package com.kic.jira.sb.webpanel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import webwork.action.ServletActionContext;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.ContextProvider;

public class BuildLogContextProvider implements ContextProvider {

	private final IssueManager issueManager;
	
	@Inject
	public BuildLogContextProvider(@ComponentImport IssueManager issueManager) {
		this.issueManager = issueManager;
	}
	
	@Override
	public void init(Map<String, String> params) throws PluginParseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getContextMap(Map<String, Object> context) {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		String projResultId = request.getParameter("projResultId");
		String issueKey = request.getParameter("issueKey");
		
		rtnMap.put("projectKey", context.get("projectKey"));
		rtnMap.put("sbProjResultId", projResultId);	
		rtnMap.put("issueKey", issueKey);
		rtnMap.put("issueSummary", issueManager.getIssueObject(issueKey).getSummary());

		return rtnMap;
	}

}
