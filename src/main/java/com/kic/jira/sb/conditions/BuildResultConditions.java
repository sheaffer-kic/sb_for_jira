package com.kic.jira.sb.conditions;

import java.util.Map;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;

public class BuildResultConditions implements Condition{	
	
	@Override
	public void init(Map<String, String> params) throws PluginParseException {
	}

	@Override
	public boolean shouldDisplay(Map<String, Object> context) {
		JiraHelper jiraHelper = (JiraHelper)context.get("helper");	//context에서 처리	
		Project project = (Project) jiraHelper.getContextParams().get("project");
		
		//System.out.println("shouldDisplay() : project.getProjectTypeKey()===========>"+project.getProjectTypeKey());
		
		boolean isShow = false;//true;

		if(project.getProjectTypeKey().getKey().equals("software")){//파람으로
			isShow = true;
			//System.out.println("getProjectTypeKey software true  ====>"+ project.getProjectTypeKey().getKey());
		}
		
		return isShow;
	}
	
}
