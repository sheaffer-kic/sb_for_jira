package com.kic.jira.sb.service;

import java.util.List;

import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

public interface SbIntegrationConfigService {

	public List<SbInteConfig> getListSbIntegrationConfig() throws Exception;
	public SbIntegrationConfigVo getSelectSbIntegrationConfig(int id) throws Exception;
	public SbInteConfig setInsertSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo) throws Exception;
	public SbInteConfig setUpdateSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo, int id) throws Exception;
	public int setDeleteSbIntegrationConfig(int id) throws Exception;	

}
