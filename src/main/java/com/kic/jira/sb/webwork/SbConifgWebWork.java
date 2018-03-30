package com.kic.jira.sb.webwork;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webwork.action.ServletActionContext;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.rest.BuildRestService;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbConfigVo;


@Scanned 
public class SbConifgWebWork extends JiraWebActionSupport {
	private static final long serialVersionUID = 8163482301218352570L;	
	private static final Logger logger = LoggerFactory.getLogger(SbConifgWebWork.class);
	
	private final CustomFieldManager customFieldManager;
	private final SbConfigService sbConfigService;

	
	private String projResultId;//smartbuilder build id request build-result.vm
	
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
		
		HttpServletRequest request = ServletActionContext.getRequest();
		projResultId = request.getParameter("projResultId");
		
		//System.out.println("doDefault projResultId=====>" + projResultId);
		
	
		return "success";	
	}	
	
	public String getProjResultId() {
			
		return this.projResultId;
	}	
	
	public void doInsert() {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		try {
			String receiveData = SbPluginUtil.getRequestJsonData(request);
			JSONObject jsonObj = new JSONObject(receiveData.trim());			
			ObjectMapper mapper = new ObjectMapper();
			
			SbConfigVo sbConfigVo = mapper.readValue(jsonObj.toString(), SbConfigVo.class);
			String cfId = customFieldManager
					.getCustomFieldObject(jsonObj.getString("sbCfId"))
					.getId();			
			sbConfigVo.setSbCfId(cfId);

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
			ObjectMapper mapper = new ObjectMapper();
			
			SbConfigVo sbConfigVo = mapper.readValue(jsonObj.toString(), SbConfigVo.class);
			String cfId = customFieldManager
					.getCustomFieldObject(jsonObj.getString("sbCfId"))
					.getId();			
			sbConfigVo.setSbCfId(cfId);
			int id = jsonObj.getInt("id");
			
			sbConfigService.setUpdateSbConfig(sbConfigVo, id);
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}		
	}
	
	public void doDelete() {
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
