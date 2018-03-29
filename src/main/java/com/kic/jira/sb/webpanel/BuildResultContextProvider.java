package com.kic.jira.sb.webpanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.util.json.JSONArray;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.ContextProvider;
import com.google.common.collect.Lists;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.util.SbPluginUtil;
import com.kic.jira.sb.vo.SbConfigVo;

public class BuildResultContextProvider implements ContextProvider{
	private static final Logger logger = LoggerFactory.getLogger(BuildResultContextProvider.class);

	private static final String SB_BUILD_RESULT_REST = "/rest/projectresult/jira";
	
	private final SbConfigService sbConfigService;
	private final CustomFieldManager customFieldManager;
	
	@Inject
	public BuildResultContextProvider(SbConfigService sbConfigService, 
									  @ComponentImport CustomFieldManager customFieldManager) {
		this.sbConfigService = sbConfigService; 
		this.customFieldManager = customFieldManager;
	}	
	
	@Override
	public void init(Map<String, String> params) throws PluginParseException {
		// TODO Auto-generated method stub		
	}

	@Override
	public Map<String, Object> getContextMap(Map<String, Object> context) {	
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
				//get issue, SBprojectId(CustomField)
				Issue currentIssue = (Issue)context.get("issue");
				String issue_key = currentIssue.getKey();	
			
				SbConfigVo sbConfigVo = sbConfigService.getSelectSbConfig();	
				if(sbConfigVo.getID() == 0){
					rtnMap.put("sbError", "Please register : Adminstrations > Smart Builder > Smart Builder Configuration");//i18
					return rtnMap;					
				}
	
				String sbProjectName = customFieldManager
									   .getCustomFieldObject(sbConfigVo.getSbCfId())
									   .getValueFromIssue(currentIssue);

				logger.debug("sbProjName : " + sbProjectName + ", cfName : " + sbConfigVo.getSbCfName());
				if (sbProjectName == null) {
					//rtnMap.put("sbError", "Please register : Adminstrations > Smart Builder > Smart Builder Configuration");//i18
					rtnMap.put("sbError", "Check your " + sbConfigVo.getSbCfName().substring(sbConfigVo.getSbCfName().indexOf("|") + 1) + " value of Issue");//i18
					return rtnMap;	
				}
			
				JSONObject jObj = new JSONObject();
				jObj.put("tagName", issue_key);
				jObj.put("projectName", sbProjectName);
				jObj.put("userId", sbConfigVo.getSbId());
				jObj.put("userPw", sbConfigVo.getSbPassword());	

				Map<String, Object> httpMap = new HashMap<String, Object>();
				
				///jobUrl
				httpMap.put("jobUrl", sbConfigVo.getUrl() );
				httpMap.put("sendData", jObj);
				HttpResponse response = SbPluginUtil.postHttpResponseForSb(httpMap, SB_BUILD_RESULT_REST);

		        int respCode = response.getStatusLine().getStatusCode();   
		        String responseBody = EntityUtils.toString(response.getEntity());
		        logger.debug("statuscode : " + respCode);
		        
		        if (respCode / 100 != 2) {//200 번대가 아니면 오류      	
		        	rtnMap.put("sbError", "ResponseCode : " + respCode + " [" + responseBody + "]");
		        	return rtnMap;
		        }

		        JSONObject jsonObj = new JSONObject(responseBody);			       
				String result = jsonObj.get("result").toString();								
				if(result.equals("fail")){
					String resultMsg = jsonObj.getString("message");
					rtnMap.put("sbError", resultMsg);
					return rtnMap; 
				}
				
				JSONArray sbList = new JSONArray(jsonObj.getString("list"));
				List<Map<String, String>> logList = new ArrayList<Map<String, String>>();

				for (int i=0; i < sbList.length(); i++) {
					JSONObject jj = sbList.getJSONObject(i);     	
					ObjectMapper mapper = new ObjectMapper();
		     	
					TypeReference<HashMap<String,Object>> typeRef 
						= new TypeReference<HashMap<String,Object>>() {};
					Map<String,String> o = mapper.readValue(jj.toString(), typeRef);
					logList.add(o);
				}
				
				List<List<Map<String, String>>> splitLogList = Lists.partition(logList, 8);//sbconfig add
				rtnMap.put("sbList", splitLogList);
				rtnMap.put("url", sbConfigVo.getUrl() + "/external.workstep.do");

		} catch(Exception e) {
		    e.printStackTrace();
		    rtnMap.put("sbError", e.getMessage());
		}
		return rtnMap;
	}	

}