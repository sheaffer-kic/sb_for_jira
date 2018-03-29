package com.kic.jira.sb.dao;

import java.util.List;

import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

public interface SbIntegrationConfigDAO {
	public List<SbIntegrationConfigVo> listSbIntegrationConfig(String projectKey) throws Exception;
	public SbIntegrationConfigVo selectSbIntegrationConfig(String projectKey, String issueType) throws Exception;//smart builder에서 호출하여사용함
	public SbIntegrationConfigVo selectSbIntegrationConfig(int id) throws Exception;
	public SbInteConfig insertSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo) throws Exception;
	public SbInteConfig updateSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo, int id) throws Exception;
	public int deleteSbIntegrationConfig(int id) throws Exception;
}
