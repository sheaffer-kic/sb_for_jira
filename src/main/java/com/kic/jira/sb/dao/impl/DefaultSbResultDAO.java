package com.kic.jira.sb.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import net.java.ao.EntityStreamCallback;
import net.java.ao.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.ao.SbResult;
import com.kic.jira.sb.dao.SbResultDAO;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;
import com.kic.jira.sb.vo.SbResultVo;

@Named
public class DefaultSbResultDAO implements  SbResultDAO{
	private static final Logger logger = LoggerFactory.getLogger(DefaultSbResultDAO.class);

	private final ActiveObjects ao;
	
	@Inject
	public DefaultSbResultDAO(@ComponentImport ActiveObjects ao) {
		this.ao = ao;
	}

	@Override
	public void insertSbResult(SbResultVo sbResultVo) throws Exception {
		// TODO Auto-generated method stub
		SbResult sbResultAo = ao.create(SbResult.class);
		sbResultAo.setIssueKey(sbResultVo.getIssueKey());
		sbResultAo.setSbProjResultId(sbResultVo.getSbProjResultId());
		sbResultAo.setSbProjResult(sbResultVo.getSbProjResult());
		sbResultAo.setRegDate(new Date());
		
		
		sbResultAo.save();

	}

	@Override
	public void updateSbResult(String sbProjResult, int id) throws Exception {
		// TODO Auto-generated method stub
		SbResult sbResultAo = ao.get(SbResult.class, id);
		sbResultAo.setSbProjResult(sbProjResult);
		sbResultAo.setModifyDate(new Date());
		sbResultAo.save();
	}

	@Override
	public SbResultVo selectSbResultBySbResult(String issueKey, String result) throws Exception {
		Query streamQ = Query.select("ID, ISSUE_KEY, SB_PROJ_RESULT, SB_PROJ_RESULT_ID")
											.where("ISSUE_KEY = ? AND SB_PROJ_RESULT = ? ", issueKey, result);
		final SbResultVo sbResultVo = new SbResultVo();
		
		ao.stream(SbResult.class, streamQ, new EntityStreamCallback<SbResult, Integer>() {		
            @Override
            public void onRowRead(SbResult t) {
            	sbResultVo.setID(t.getID());
            	sbResultVo.setIssueKey(t.getIssueKey());
            	sbResultVo.setSbProjResult(t.getSbProjResult());
            	sbResultVo.setSbProjResultId(t.getSbProjResultId());
            	       	
            }
		});		
		
		return sbResultVo;
	}
	
	
	
	@Override
	public SbResultVo selectSbResultById(int id) throws Exception {

		Query streamQ = Query.select("ID, ISSUE_KEY, SB_PROJ_RESULT, SB_PROJ_RESULT_ID").where("ID = ? ", id);			
		final SbResultVo sbResultVo = new SbResultVo();
		
		ao.stream(SbResult.class, streamQ, new EntityStreamCallback<SbResult, Integer>() {		
            @Override
            public void onRowRead(SbResult t) {
            	sbResultVo.setID(t.getID());
            	sbResultVo.setIssueKey(t.getIssueKey());
            	sbResultVo.setSbProjResult(t.getSbProjResult());
            	sbResultVo.setSbProjResultId(t.getSbProjResultId());
            	       	
            }
		});		
		
		return sbResultVo;
	}

	@Override
	public List<SbResultVo> selectSbResultByIssueKey(String issueKey) throws Exception {

		Query streamQ = Query.select("ID, ISSUE_KEY, SB_PROJ_RESULT, SB_PROJ_RESULT_ID").where("ISSUE_KEY = ? ", issueKey);	
		
		final List<SbResultVo> rtnList = new ArrayList<SbResultVo>();
		
		ao.stream(SbResult.class, streamQ, new EntityStreamCallback<SbResult, Integer>() {		
            @Override
            public void onRowRead(SbResult t) {
            	SbResultVo vo = new SbResultVo();
            	vo.setID(t.getID());
            	vo.setIssueKey(t.getIssueKey());
            	vo.setSbProjResult(t.getSbProjResult());
            	vo.setSbProjResultId(t.getSbProjResultId());
            	rtnList.add(vo);
            }
		});		
		
		return rtnList;
	}	
}
