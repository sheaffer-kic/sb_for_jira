package com.kic.jira.sb.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.tx.Transactional;
import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.dao.SbIntegrationConfigDAO;
import com.kic.jira.sb.service.SbIntegrationConfigService;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

@Transactional
@Named
public class SbIntegrationConfigServiceImpl implements SbIntegrationConfigService {

	private static final Logger logger = LoggerFactory.getLogger(SbIntegrationConfigServiceImpl.class);

	private final SbIntegrationConfigDAO sbIntegrationConfigDAO;
	
	@Inject	
	public SbIntegrationConfigServiceImpl(SbIntegrationConfigDAO sbIntegrationConfigDAO) {
		this.sbIntegrationConfigDAO = sbIntegrationConfigDAO;	
	}

	@Override
	public List<SbIntegrationConfigVo> getListSbIntegrationConfig(String projectKey) throws Exception{
		return sbIntegrationConfigDAO.listSbIntegrationConfig(projectKey);
	}	

	//smart builder에서 호출하여사용함
	@Override
	public SbIntegrationConfigVo getSelectSbIntegrationConfig(String projectKey, String issueType) throws Exception{
		return sbIntegrationConfigDAO.selectSbIntegrationConfig(projectKey, issueType);
	}
	
	@Override
	public SbIntegrationConfigVo getSelectSbIntegrationConfig(int id) throws Exception{
		return sbIntegrationConfigDAO.selectSbIntegrationConfig(id);
	}
		
	@Override
	public SbInteConfig setInsertSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo) throws Exception{
		return sbIntegrationConfigDAO.insertSbIntegrationConfig(sbIntegrationConfigVo);
		
	}
		
	@Override
	public SbInteConfig setUpdateSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo, int id) throws Exception{
		return sbIntegrationConfigDAO.updateSbIntegrationConfig(sbIntegrationConfigVo, id);
	}
	
	@Override
	public int setDeleteSbIntegrationConfig(int id) throws Exception{
		return sbIntegrationConfigDAO.deleteSbIntegrationConfig(id);
	}
		
}
