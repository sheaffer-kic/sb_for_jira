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
				
				//JSONArray sbList = new JSONArray(jsonResponse.getString("list"));
				for (int i=0; i < sbList.length(); i++) {
					JSONObject jj = sbList.getJSONObject(i);     	
					ObjectMapper mapper = new ObjectMapper();
		     	
					TypeReference<HashMap<String,Object>> typeRef 
						= new TypeReference<HashMap<String,Object>>() {};
					Map<String,String> o = mapper.readValue(jj.toString(), typeRef);
					logList.add(o);
				}
				
				//#set ($href = "$sbConfig.url?tagName=$tagName&projectId=$projectId")
				
				List<List<Map<String, String>>> splitLogList = Lists.partition(logList, 8);//sbconfig add
				rtnMap.put("sbList", splitLogList);
				rtnMap.put("url", sbConfigVo.getUrl() + "/external.workstep.do");

		} catch(Exception e) {
		    e.printStackTrace();
		    rtnMap.put("sbError", e.getMessage());
		}
		return rtnMap;
	}	
	
	
	
	
//	
//	@Override
//	public Map<String, Object> getContextMap_ori(Map<String, Object> context) {	
//
//		Map<String, Object> rtnMap = new HashMap<String, Object>();
//		try {
//				//get issue, SBprojectId(CustomField)
//				Issue currentIssue = (Issue)context.get("issue");
//				String issue_key = currentIssue.getKey();	
//				
//				List<CustomField> customFieldList = (List)customFieldManager.getCustomFieldObjectsByName("SB-ProjectId");//id value.. sbconfig add
//			
//				String projectId = "";
//				for(int i=0; i<customFieldList.size(); i++){
//					//System.out.println("getCustomFieldValue==================>"+currentIssue.getCustomFieldValue(customFieldList.get(i)));
//					projectId = currentIssue.getCustomFieldValue(customFieldList.get(i)).toString();	//for문 다 돌지말것		
//				}	
//				
//				//projectId 없을때 처리
//				if(projectId == null || projectId.equals("")){
//					rtnMap.put("sbError", "Please register : Adminstrations > Smart Builder > Smart Builder Configuration");//i18
//					return rtnMap;					
//				}	
//				
//				rtnMap.put("tagName", issue_key);
//				rtnMap.put("projectId", projectId);
//			
//				//get admin> smart builder config
//				SbConfigVo sbConfigVo = sbConfigDAO.selectSbConfig();
//				
//				//sb config 없을때 처리
//				if(sbConfigVo.getUrl() == null || sbConfigVo.getUrl().equals("")){
//					rtnMap.put("sbError", "Please register : Adminstrations > Smart Builder > Smart Builder Configuration");//i18
//					return rtnMap;					
//				}				
//				rtnMap.put("sbConfig", sbConfigVo);
//				
//				sbConfigVo.getID();
//				String sbId = sbConfigVo.getSbId();
//				String sbPassword = sbConfigVo.getSbPassword();
//				String url = sbConfigVo.getUrl();
//		 
//				URL obj = new URL(url+"/rest/projectresult/jira");
//
//				JSONObject jObj = new JSONObject();
//				jObj.put("tagName", issue_key);
//				jObj.put("projectId", projectId);
//				jObj.put("userId", sbId);
//				jObj.put("userPw", sbPassword);	
//				
//				String urlParameters = jObj.toString();
//				
//				System.out.println("urlParameters jObj.toString()=======>"+urlParameters);
//
//				HttpURLConnection con = (HttpURLConnection)obj.openConnection();	
//
//				con.setRequestMethod("POST");
//				con.setRequestProperty("Content-Type", "application/json");
//		        con.setDoOutput(true);
//		        
//		        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//		        wr.writeBytes(urlParameters);
//		        wr.flush();
//		        
//		        //https
//		        if ((con.getResponseCode()+"").indexOf("2") != 0) {
//		            rtnMap.put("sbError", "Failed : HTTP error code : "+ con.getResponseCode());
//		            
//		            wr.close();
//		            con.disconnect();
//					return rtnMap;          
//		            
//		        }		        
//				
//				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
//				
//				String inputLine;
//				StringBuffer response = new StringBuffer();	            
//				String rtndata = "";
//				
//				while ((inputLine = in.readLine()) != null) {
//				    response.append(inputLine);	                
//				}
//				in.close();
//				rtndata = response.toString();
//				System.out.println("response json data =========> "+response.toString());	
//			
//				JSONObject jsonObj = new JSONObject(rtndata);	
//				
//				String result = jsonObj.get("result").toString();				
//				
//				if(result.equals("fail")){
//					String resultMsg = jsonObj.get("message").toString();
//					rtnMap.put("sbError", resultMsg);
//					return rtnMap; 
//				}
//				
//				JSONArray sbList = (JSONArray)jsonObj.get("list");
//				//System.out.println("rtndataArry sbList.length=====>"+sbList.length());
//				
//				List<Map<String, String>> logList = new ArrayList<Map<String, String>>();
//					
//				for(int i=0;i<sbList.length();i++){	//util처리 vo처리 일반화		
//					JSONObject sb = (JSONObject) sbList.get(i);					
//
//					 Map<String, String> logMap = new HashMap<String, String>();
//					 
//					 logMap.put("projectId", sb.getString("projectId"));
//					 logMap.put("projResult", sb.getString("projResult"));
//					 logMap.put("projResultId", sb.getString("projResultId"));
//					 logMap.put("rowNum", sb.getString("rowNum"));
//					 logMap.put("runTime", sb.getString("runTime"));	
//					 logMap.put("startTime", sb.getString("startTime"));
//				/*
//					 logMap.put("build_num", sb.getString("build_num"));
//					 logMap.put("build_result", sb.getString("build_result"));
//					 logMap.put("build_desc", sb.getString("build_desc"));
//					 logMap.put("builder", sb.getString("builder"));
//					 logMap.put("issueid", sb.getString("issueid"));
//					 logMap.put("start_date", sb.getString("start_date"));
//					 logMap.put("end_date", sb.getString("end_date"));
//					 logMap.put("update_date", sb.getString("update_date"));
//					 logMap.put("update_id", sb.getString("update_id"));		        	 
//				*/		 
//					 logList.add(logMap);					
//				}				
//					
//				List<List<Map<String, String>>> splitLogList = Lists.partition(logList, 8);//sbconfig add
//				rtnMap.put("sbList", splitLogList);	
//				
//	            wr.close();
//	            con.disconnect();
//
//		} catch(Exception e) {
//		    e.printStackTrace();
//		    rtnMap.put("sbError", e.getMessage());
//		}
//		return rtnMap;
//	}	

}