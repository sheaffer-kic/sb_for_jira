package com.kic.jira.sb.webwork;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.service.SbIntegrationConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

import webwork.action.ServletActionContext;

@Scanned
public class SbIntegrationConifgWebWork extends JiraWebActionSupport {
	private static final long serialVersionUID = 8163482301218352570L;//-4417299931960575349L;
	private static final Logger logger = LoggerFactory.getLogger(SbIntegrationConifgWebWork.class);
	
	private List<SbInteConfig> sbInteConfigList;
	
	private String projectKey;
	
	private final I18nResolver i18nResolver;
	private final SbIntegrationConfigService sbIntegrationConfigService;
	
	public SbIntegrationConifgWebWork(@ComponentImport I18nResolver i18nResolver,
			SbIntegrationConfigService sbIntegrationConfigService) {
		this.i18nResolver = i18nResolver;
		this.sbIntegrationConfigService = sbIntegrationConfigService;
	}

	
	public String doDefault() {	
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String projectKey = request.getParameter("projectKey");		
		setProjectKey(projectKey);
		
		System.out.println("sbproject config de default");
		try {

			sbInteConfigList = sbIntegrationConfigService.getListSbIntegrationConfig();
			System.out.println("## sbInteConfigListsize : ====>" + sbInteConfigList.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("doDefault ====>>>>> e.printStackTrace()=======================>"+e.toString());
		}

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
		System.out.println("doSave()==========================>1");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		System.out.println("doSave()==========================>2");		
		
		try {
		
			String receiveData = SbPluginUtil.getRequestJsonData(request);
			JSONObject jsonObj = new JSONObject(receiveData.trim());
			
			System.out.println("json receiveData===>"+receiveData);
			
			
			String projectKey = jsonObj.getString("projectKey"); //프로젝트 키
			String issueType = jsonObj.getString("issueType"); //이슈타입
			String issueTypeName = jsonObj.getString("issueTypeName"); //이슈타입 이름
			int buildTargetId = jsonObj.getInt("trgt"); //빌드대상 상태
			String buildTargetName = jsonObj.getString("trgtName"); //빌드대상 상태 이름
			int buildStepId = jsonObj.getInt("trgtStepId"); //buildTargetId 상태에 해당하는 stepId
			int buildProgressId = jsonObj.getInt("progress"); //빌드중 이슈 상태
			String buildProgressName = jsonObj.getString("progressName"); //빌드중 이슈 상태 이름
			int buildProgressAction = jsonObj.getInt("progressAction"); //이 액션(트랜지션)을 수행하면 buildProgressId 상태가 됨.
			int buildSuccessId = jsonObj.getInt("success"); //빌드 성공시 수행할 트랜지션 
			String buildSuccessName = jsonObj.getString("successName");//빌드 성공시 수행할 트랜지션 이름
			int buildFailId = jsonObj.getInt("fail"); //빌드 실패시 수행할 트랜지션
			String buildFailName = jsonObj.getString("failName");//빌드 실패시 수행할 트랜지션 이름

			SbIntegrationConfigVo sbIntegrationConfigVo = new SbIntegrationConfigVo();			
			
			sbIntegrationConfigVo.setProjectKey(projectKey);
			sbIntegrationConfigVo.setIssueType(issueType);
			sbIntegrationConfigVo.setIssueTypeName(issueTypeName);
			sbIntegrationConfigVo.setBuildTargetId(buildTargetId);
			sbIntegrationConfigVo.setBuildTargetName(buildTargetName);
			sbIntegrationConfigVo.setBuildStepId(buildStepId);
			sbIntegrationConfigVo.setBuildProgressId(buildProgressId);
			sbIntegrationConfigVo.setBuildProgressName(buildProgressName);
			sbIntegrationConfigVo.setBuildProgressAction(buildProgressAction);
			sbIntegrationConfigVo.setBuildSuccessId(buildSuccessId);
			sbIntegrationConfigVo.setBuildSuccessName(buildSuccessName);
			sbIntegrationConfigVo.setBuildFailId(buildFailId);
			sbIntegrationConfigVo.setBuildFailName(buildFailName);
			
			
			if(jsonObj.has("id")){
				System.out.println("update==========================>");
				int id = jsonObj.getInt("id");
				sbIntegrationConfigService.setUpdateSbIntegrationConfig(sbIntegrationConfigVo, id);
			}else{
				System.out.println("insert==========================>");
				sbIntegrationConfigService.setInsertSbIntegrationConfig(sbIntegrationConfigVo);
			}
			
			//System.out.println();
			System.out.println("doSave()==========================>3");
		} catch (Exception e) {
			//logger.debug(e.getMessage());
			System.out.println("doSave ====>>>>> e.printStackTrace()=======================>"+e.toString());
		}			
	}
	
	public void doDelete() {
		System.out.println("doDelete()==========================>1");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		System.out.println("doDelete()==========================>2");		
		
		try {
		
			String receiveData = SbPluginUtil.getRequestJsonData(request);
			JSONObject jsonObj = new JSONObject(receiveData.trim());
			
			System.out.println("json receiveData===>"+receiveData);
			
			int id = jsonObj.getInt("id");
			
			sbIntegrationConfigService.setDeleteSbIntegrationConfig(id);	
			
			//System.out.println();
			System.out.println("doDelete()==========================>3");
		} catch (Exception e) {
			//logger.debug(e.getMessage());
			System.out.println("doDelete ====>>>>> e.printStackTrace()=======================>"+e.toString());
		}		
		
	}
	
}
