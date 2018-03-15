package com.kic.jira.sb.webwork;

import java.io.BufferedReader;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webwork.action.ServletActionContext;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbConfigVo;


@Scanned 
public class SbConifgWebWork extends JiraWebActionSupport {
	private static final long serialVersionUID = 8163482301218352570L;	
	private static final Logger logger = LoggerFactory.getLogger(SbConifgWebWork.class);
	
	private SbConfigVo sbConfigContent;
	
	private final I18nResolver i18nResolver;
	private final CustomFieldManager customFieldManager;
	private final SbConfigService sbConfigService;
	
	public SbConifgWebWork(@ComponentImport I18nResolver i18nResolver,
						   @ComponentImport CustomFieldManager customFieldManager,
						   SbConfigService sbConfigService) {
		this.i18nResolver = i18nResolver;
		this.customFieldManager = customFieldManager;
		this.sbConfigService = sbConfigService;
	}
	
	
	/* 
	 * (non-Javadoc)
	 * @see webwork.action.ActionSupport#doDefault()
	 */
	public String doDefault() {		
		try {

			sbConfigContent = sbConfigService.getSelectSbConfig();
			
			//System.out.println("doDefault()==========================>1");
			//System.out.println("doDefault() sbConfigContent.sbId==========================>"+sbConfigContent.getSbId());
			//System.out.println("sbConfig.getSbId()==========================>"+sbConfig.getSbId());
			/*
			if(sbConfig==null){
				System.out.println("sbConfig==null==========================>");
				sbConfig.setSbId("1");
				sbConfig.setSbPassword("2");
				sbConfig.setUrl("3");	
			}*/
			
			
			//System.out.println("doDefault()==========================>2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "success";	

	}	
	
	public SbConfigVo getSbConfigContent() {
		
		if(sbConfigContent.getID() == 0){
			//System.out.println("sbConfigContent==null==========================>");
			sbConfigContent.setSbId("");
			sbConfigContent.setSbPassword("");
			sbConfigContent.setUrl("");
			sbConfigContent.setSbCfId("");	
			sbConfigContent.setSbCfName("");
			sbConfigContent.setJiraId("");
			sbConfigContent.setJiraPassword("");			
		}
		
		return this.sbConfigContent;
	}	
	
	public void doInsert() {
		//System.out.println("doInsert()==========================>1");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//System.out.println("doInsert()==========================>2");
		
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
			
			//System.out.println();
			//System.out.println("doInsert()==========================>3");
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}	
	}

	public void doUpdate() {
		//System.out.println("doUpdate()==========================>1");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		//System.out.println("doUpdate()==========================>2");

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
			
			System.out.println("doUpdate()=====>id: "+id);
			System.out.println("doUpdate()=====>sbConfigVo.getSbId(): "+sbConfigVo.getSbId());
			System.out.println("doUpdate()=====>sbConfigVo.getSbPassword(): "+sbConfigVo.getSbPassword());
			System.out.println("doUpdate()=====>sbConfigVo.getUrl(): "+sbConfigVo.getUrl());					
			System.out.println("doUpdate()=====>sbConfigVo.getJiraId(): "+sbConfigVo.getJiraId());
			
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
