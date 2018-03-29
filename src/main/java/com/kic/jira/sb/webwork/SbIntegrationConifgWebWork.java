package com.kic.jira.sb.webwork;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webwork.action.ServletActionContext;

import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.service.SbIntegrationConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

@Scanned
public class SbIntegrationConifgWebWork extends JiraWebActionSupport {
	private static final long serialVersionUID = 8163482301218352570L;//-4417299931960575349L;
	private static final Logger logger = LoggerFactory.getLogger(SbIntegrationConifgWebWork.class);
	
	private List<SbInteConfig> sbInteConfigList;
	
	private String projectKey;
	
	private final SbIntegrationConfigService sbIntegrationConfigService;
	
	public SbIntegrationConifgWebWork(SbIntegrationConfigService sbIntegrationConfigService) {
		this.sbIntegrationConfigService = sbIntegrationConfigService;
	}

	
	public String doDefault() {			
		HttpServletRequest request = ServletActionContext.getRequest();
		String projectKey = request.getParameter("projectKey");		
		setProjectKey(projectKey);
		
		return "success";	
	}

	
	public List<SbInteConfig> getSbInteConfigList() {
		return this.sbInteConfigList;
	}

	/*
	 * set ProjectKey
	 */
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
 
    /*
     * get ProjectKey
     */
    public String getProjectKey() {
        return this.projectKey;
    }
    
	public void doSave() {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		try {
		
			String receiveData = SbPluginUtil.getRequestJsonData(request);
			JSONObject jsonObj = new JSONObject(receiveData.trim());
		
			
			ObjectMapper mapper = new ObjectMapper();
			SbIntegrationConfigVo vo  = mapper.readValue(jsonObj.toString(), SbIntegrationConfigVo.class);
			
			if(jsonObj.has("id")){
				int id = jsonObj.getInt("id");
				sbIntegrationConfigService.setUpdateSbIntegrationConfig(vo, id);
			}else{
				sbIntegrationConfigService.setInsertSbIntegrationConfig(vo);
			}

		} catch (Exception e) {
			logger.debug(e.getMessage());
			
		}			
	}
	
	public void doDelete() {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
		
			String receiveData = SbPluginUtil.getRequestJsonData(request);
			JSONObject jsonObj = new JSONObject(receiveData.trim());
			
			int id = jsonObj.getInt("id");			
			sbIntegrationConfigService.setDeleteSbIntegrationConfig(id);
			
		} catch (Exception e) {
			logger.debug(e.getMessage());			
		}		
		
	}
	
}
