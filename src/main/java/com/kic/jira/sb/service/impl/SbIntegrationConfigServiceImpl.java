package com.kic.jira.sb.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.dao.SbIntegrationConfigDAO;
import com.kic.jira.sb.service.SbIntegrationConfigService;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

@Transactional
@Named
public class SbIntegrationConfigServiceImpl implements SbIntegrationConfigService {

	private static final Logger logger = LoggerFactory.getLogger(SbIntegrationConfigServiceImpl.class);

	private final SbIntegrationConfigDAO sbIntegrationConfigDAO;
	private final I18nResolver i18nResolver;
	
	@Inject	
	public SbIntegrationConfigServiceImpl(SbIntegrationConfigDAO sbIntegrationConfigDAO,
			  @ComponentImport I18nResolver i18nResolver) {
		this.sbIntegrationConfigDAO = sbIntegrationConfigDAO;
		this.i18nResolver = i18nResolver;
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
