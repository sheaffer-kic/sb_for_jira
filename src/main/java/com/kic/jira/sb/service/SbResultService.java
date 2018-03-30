package com.kic.jira.sb.service;

import java.util.List;

import com.kic.jira.sb.vo.SbResultVo;

public interface SbResultService {
	//public List<SbResultVo> getListSbIntegrationConfig(String projectKey) throws Exception;
	public SbResultVo getSelectSbResultById(int id) throws Exception;
	public SbResultVo getSelectSbResultBySbResult(String issueKey, String result) throws Exception;
	
	public List<SbResultVo> getSelectSbResultByIssueKey(String issueKey) throws Exception;
	
	public void setInsertSbResult(SbResultVo sbResultVo) throws Exception;
	public void setUpdateSbResult(String sbProjResult, int id) throws Exception;
}
