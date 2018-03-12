package com.kic.jira.sb.service;

import com.kic.jira.sb.vo.SbConfigVo;
import com.kic.jira.sb.ao.SbConfig;

public interface SbConfigService {

	public SbConfigVo getSelectSbConfig() throws Exception;
	public SbConfig setInsertSbConfig(SbConfigVo sbConfigVo) throws Exception;
	public SbConfig setUpdateSbConfig(SbConfigVo sbConfigVo, int id) throws Exception;
	public int setDeleteSbConfig(int id) throws Exception;	
}
