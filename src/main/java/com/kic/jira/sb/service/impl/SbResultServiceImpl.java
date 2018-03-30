package com.kic.jira.sb.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.tx.Transactional;
import com.kic.jira.sb.dao.SbResultDAO;
import com.kic.jira.sb.service.SbResultService;
import com.kic.jira.sb.vo.SbResultVo;

@Transactional
@Named
public class SbResultServiceImpl implements SbResultService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(SbResultServiceImpl.class);

	private final SbResultDAO sbResultDAO;
	
	@Inject	
	public SbResultServiceImpl(SbResultDAO sbResultDAO) {
		this.sbResultDAO = sbResultDAO;	
	}
	

	@Override
	public SbResultVo getSelectSbResultById(int id) throws Exception {
		return sbResultDAO.selectSbResultById(id);
	}
	
	@Override
	public SbResultVo getSelectSbResultBySbResult(String issueKey, String result) throws Exception {
		return sbResultDAO.selectSbResultBySbResult(issueKey, result);
	}

	@Override
	public void setInsertSbResult(SbResultVo sbResultVo) throws Exception {
		sbResultDAO.insertSbResult(sbResultVo);
	}

	@Override
	public void setUpdateSbResult(String sbProjResult, int id) throws Exception {
		// TODO Auto-generated method stub
		sbResultDAO.updateSbResult(sbProjResult, id);
	}


	@Override
	public List<SbResultVo> getSelectSbResultByIssueKey(String issueKey) throws Exception {
		// TODO Auto-generated method stub
		return sbResultDAO.selectSbResultByIssueKey(issueKey);
	}

}
