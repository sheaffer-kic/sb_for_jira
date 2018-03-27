package com.kic.jira.sb.webwork;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webwork.action.ServletActionContext;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbConfigVo;


@Scanned 
public class SbConifgWebWork extends JiraWebActionSupport {
	private static final long serialVersionUID = 8163482301218352570L;	
	private static final Logger logger = LoggerFactory.getLogger(SbConifgWebWork.class);
	
	private final CustomFieldManager customFieldManager;
	private final SbConfigService sbConfigService;
	
	public SbConifgWebWork(@ComponentImport CustomFieldManager customFieldManager,
						   SbConfigService sbConfigService) {
		this.customFieldManager = customFieldManager;
		this.sbConfigService = sbConfigService;
	}
	
	
	/* 
	 * (non-Javadoc)
	 * @see webwork.action.ActionSupport#doDefault()
	 */
	public String doDefault() {		
		return "success";	
	}	
	
	public void doInsert() {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		try {
			String receiveData = SbPluginUtil.getRequestJsonData(request);
			JSONObject jsonObj = new JSONObject(receiveData.trim());

			String sbId = jsonObj.getString("sbId");
			String sbPassword = jsonObj.getString("sbPassword");
			String url = jsonObj.getString("url");
			String cfName = jsonObj.getString("cfName");
			String cfId = customFieldManager
								.getCustomFieldObject(jsonObj.getString("cfId"))
								.getId(); //customfield_1xxxx

			SbConfigVo sbConfigVo = new SbConfigVo();
			sbConfigVo.setSbId(sbId);
			sbConfigVo.setSbPassword(sbPassword);
			sbConfigVo.setUrl(url);			
			sbConfigVo.setSbCfName(cfName);
			sbConfigVo.setSbCfId(cfId);
			sbConfigVo.setJiraId(jsonObj.getString("jiraId"));
			sbConfigVo.setJiraPassword(jsonObj.getString("jiraPassword"));		
			
			sbConfigService.setInsertSbConfig(sbConfigVo);	

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}	
	}

	public void doUpdate() {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		try {
			String receiveData = SbPluginUtil.getRequestJsonData(request);
			JSONObject jsonObj = new JSONObject(receiveData.trim());
			
			System.out.println("jsoObje : " + jsonObj.toString());
			
			String sbId = jsonObj.getString("sbId");
			String sbPassword = jsonObj.getString("sbPassword");
			String url = jsonObj.getString("url");
			String cfName = jsonObj.getString("cfName");
			String cfId = customFieldManager
								.getCustomFieldObject(jsonObj.getString("cfId"))
								.getId();
			int id = Integer.parseInt(jsonObj.getString("id"));
			
			SbConfigVo sbConfigVo = new SbConfigVo();
			sbConfigVo.setSbId(sbId);
			sbConfigVo.setSbPassword(sbPassword);
			sbConfigVo.setUrl(url);	
			sbConfigVo.setSbCfName(cfName);
			sbConfigVo.setSbCfId(cfId);
			sbConfigVo.setJiraId(jsonObj.getString("jiraId"));
			sbConfigVo.setJiraPassword(jsonObj.getString("jiraPassword"));			

			sbConfigService.setUpdateSbConfig(sbConfigVo, id);
			
		} catch (Exception e) {
			//System.out.println("error : " + e.getMessage());
			logger.debug(e.getMessage());
		}		
	}
	
	public void doDelete() {
		//System.out.println("doDelete()==========================>1");	
		HttpServletRequest request = ServletActionContext.getRequest();		
		try {
			
			JSONObject jsonObj = new JSONObject(request.getParameter("sendData"));
			
			int id = Integer.parseInt(jsonObj.getString("id"));
			
			sbConfigService.setDeleteSbConfig(id);	
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}		
	}

}
