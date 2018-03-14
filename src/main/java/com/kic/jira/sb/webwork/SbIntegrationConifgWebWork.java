package com.kic.jira.sb.webwork;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webwork.action.ServletActionContext;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.kic.jira.sb.service.SbIntegrationConfigService;
import com.kic.jira.sb.vo.SbIntegerationConfigVo;

public class SbIntegrationConifgWebWork extends JiraWebActionSupport {
	private static final long serialVersionUID = -4417299931960575349L;
	private static final Logger logger = LoggerFactory.getLogger(SbIntegrationConifgWebWork.class);
	
	private SbIntegerationConfigVo sbInteConfigVo;
	
	private String projectKey;
	
	private final I18nResolver i18nResolver;
	private final SbIntegrationConfigService sbProjectConfigService;
	
	public SbIntegrationConifgWebWork(@ComponentImport I18nResolver i18nResolver,
								  SbIntegrationConfigService sbProjectConfigService) {
		this.i18nResolver = i18nResolver;
		this.sbProjectConfigService = sbProjectConfigService;
	}

	
	public String doDefault() {	
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String projectKey = request.getParameter("projectKey");		
		setProjectKey(projectKey);
		
		System.out.println("sbproject config de default");
		try {

			//sbConfigContent = sbConfigService.getSelectSbConfig();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("doDefault ====>>>>> e.printStackTrace()=======================>"+e.toString());
		}

		return "success";	

	}


	public SbIntegerationConfigVo getSbInteConfigVo() {
		return sbInteConfigVo;
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
    	

}
