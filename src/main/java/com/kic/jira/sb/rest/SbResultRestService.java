/**
 * smartBuilder 빌드 결과 가져오기 (이슈 오른쪽 화면...)
 */
package com.kic.jira.sb.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.google.common.collect.Lists;
import com.kic.jira.sb.service.SbResultService;
import com.kic.jira.sb.vo.SbResultVo;

@Scanned 
@Path("result")
public class SbResultRestService {
	private static final Logger logger = LoggerFactory.getLogger(SbResultRestService.class);
	
	private final SbResultService sbResultService;
	
	public SbResultRestService(SbResultService sbResultService) {
		this.sbResultService = sbResultService;
	}	
	
	
	
	//localhost:2990/jira/rest/sb/1.0/result/list
	@GET
    @Path("/list/{issueKey}")
	@Produces({MediaType.APPLICATION_JSON})	
	public Map<String, Object> getSbBuildResultList(@PathParam("issueKey") String issueKey) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		List<SbResultVo> logList = sbResultService.getSelectSbResultByIssueKey(issueKey);		
		List<List<SbResultVo>> splitLogList = Lists.partition(logList, 15);//sbconfig add
		rtnMap.put("sbList", splitLogList);
		
		return rtnMap;
	}
	
	
	//localhost:2990/jira/rest/sb/1.0/result/test
	@GET
    @Path("/test")
	@Produces({MediaType.APPLICATION_JSON})	
	public List<Map<String, Object>> test() throws Exception {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("stepId", "s01");
		map.put("stepName", "stepName01");
		map.put("stepResultId", "r01");
		rtnList.add(map);
		
		map = new HashMap<String, Object>();		
		map.put("stepId", "s02");
		map.put("stepName", "stepName02");
		map.put("stepResultId", "r02");
		rtnList.add(map);
		
		map = new HashMap<String, Object>();		
		map.put("stepId", "s03");
		map.put("stepName", "stepName03");
		map.put("stepResultId", "r03");
		rtnList.add(map);
		
		map = new HashMap<String, Object>();		
		map.put("stepId", "s04");
		map.put("stepName", "stepName04");
		map.put("stepResultId", "r04");
		rtnList.add(map);
		
		return rtnList;
	}		
}
