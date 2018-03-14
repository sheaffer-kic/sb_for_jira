package com.kic.jira.sb.rest;

import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

public interface TestDAO {
	public SbIntegrationConfigVo selectSbInteConfig(String projectKey, String issueType) throws Exception;
	public SbInteConfig insertSbInteConfig(SbIntegrationConfigVo sbInteConfigVo) throws Exception;
}
