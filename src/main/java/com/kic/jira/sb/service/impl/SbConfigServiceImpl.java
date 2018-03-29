package com.kic.jira.sb.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.tx.Transactional;
import com.kic.jira.sb.ao.SbConfig;
import com.kic.jira.sb.dao.SbConfigDAO;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.vo.SbConfigVo;

@Transactional
@Named
public class SbConfigServiceImpl implements SbConfigService{

	private static final Logger logger = LoggerFactory.getLogger(SbConfigServiceImpl.class);
	
	private final SbConfigDAO sbConfigDAO;
	
	@Inject
	public SbConfigServiceImpl(SbConfigDAO sbConfigDAO) {
		this.sbConfigDAO = sbConfigDAO;
	}
	
	@Override
	public SbConfigVo getSelectSbConfig() throws Exception{
		return sbConfigDAO.selectSbConfig();		
	}
	
	@Override
	public SbConfig setInsertSbConfig(SbConfigVo sbConfigVo) throws Exception{
		return sbConfigDAO.insertSbConfig(sbConfigVo);
	}
	
	@Override
	public SbConfig setUpdateSbConfig(SbConfigVo sbConfigVo, int id) throws Exception{
		return sbConfigDAO.updateSbConfig(sbConfigVo, id);
	}
	
	@Override
	public int setDeleteSbConfig(int id) throws Exception{
		return sbConfigDAO.deleteSbConfig(id);
	}
			
}
