package com.kic.jira.sb.dao;

import com.kic.jira.sb.ao.SbConfig;
import com.kic.jira.sb.vo.SbConfigVo;

public interface SbConfigDAO {
	public SbConfigVo selectSbConfig() throws Exception;
	public SbConfig insertSbConfig(SbConfigVo sbConfigVo) throws Exception;
	public SbConfig updateSbConfig(SbConfigVo sbConfigVo, int id) throws Exception;
	public int deleteSbConfig(int id) throws Exception;
	
}
