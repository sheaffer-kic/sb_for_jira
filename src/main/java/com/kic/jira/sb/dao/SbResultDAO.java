package com.kic.jira.sb.dao;

import java.util.List;

import com.kic.jira.sb.vo.SbResultVo;

public interface SbResultDAO {
	public void insertSbResult(SbResultVo sbResultVo) throws Exception;
	public void updateSbResult(String sbProjResult, int id) throws Exception;

	public List<SbResultVo> selectSbResultByIssueKey(String issueKey) throws Exception;
	
	public SbResultVo selectSbResultById(int id) throws Exception;
	public SbResultVo selectSbResultBySbResult(String issueKey, String result) throws Exception;
}
