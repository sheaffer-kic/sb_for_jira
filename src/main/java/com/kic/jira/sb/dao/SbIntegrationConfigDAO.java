package com.kic.jira.sb.dao;

import java.util.List;

import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

public interface SbIntegrationConfigDAO {
	public List<SbInteConfig> listSbIntegrationConfig() throws Exception;
	public SbIntegrationConfigVo selectSbIntegrationConfig(String projectKey, String issueType) throws Exception;
	public SbInteConfig insertSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo) throws Exception;
	public SbInteConfig updateSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo, int id) throws Exception;
	public int deleteSbIntegrationConfig(int id) throws Exception;
}
