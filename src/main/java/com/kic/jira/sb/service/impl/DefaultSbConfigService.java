package com.kic.jira.sb.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.kic.jira.sb.ao.SbConfig;
import com.kic.jira.sb.dao.SbConfigDAO;
import com.kic.jira.sb.service.SbConfigService;
import com.kic.jira.sb.vo.SbConfigVo;

@Transactional
@Named
public class DefaultSbConfigService implements SbConfigService{

	private static final Logger logger = LoggerFactory.getLogger(DefaultSbConfigService.class);
	
	private final SbConfigDAO sbConfigDAO;
	private final I18nResolver i18nResolver;
	
	@Inject
	public DefaultSbConfigService(SbConfigDAO sbConfigDAO,
							  @ComponentImport I18nResolver i18nResolver) {
		this.sbConfigDAO = sbConfigDAO;
		this.i18nResolver = i18nResolver;
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
