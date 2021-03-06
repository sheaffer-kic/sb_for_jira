package com.kic.jira.sb.dao.impl;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.ao.SbConfig;
import com.kic.jira.sb.dao.SbConfigDAO;
import com.kic.jira.sb.vo.SbConfigVo;

import net.java.ao.EntityStreamCallback;
import net.java.ao.Query;

@Named
public class DefaultSbConfigDAO implements SbConfigDAO{

	private static final Logger logger = LoggerFactory.getLogger(DefaultSbConfigDAO.class);

	private final ActiveObjects ao;
	
	@Inject
	public DefaultSbConfigDAO(@ComponentImport ActiveObjects ao) {
		this.ao = ao;
	}
	
	@Override
	public SbConfigVo selectSbConfig() throws Exception{
		//System.out.println("DefaultSbConfigDAO : selectSbConfig()=====> 1");
		
		Query streamQ = Query.select("ID, JIRA_ID, JIRA_PASSWORD, SB_CF_ID, SB_CF_NAME, SB_ID, SB_PASSWORD, URL, REG_DATE, MODIFY_DATE");
		
		//System.out.println("DefaultSbConfigDAO : selectSbConfig()=====> 22222");
		
		final SbConfigVo sbConfigVo = new SbConfigVo();
		
		ao.stream(SbConfig.class, streamQ, new EntityStreamCallback<SbConfig, Integer>() {		
            @Override
            public void onRowRead(SbConfig t) {
            	sbConfigVo.setID(t.getID());
        		sbConfigVo.setSbId(t.getSbId());
        		sbConfigVo.setSbPassword(t.getSbPassword());
        		sbConfigVo.setUrl(t.getUrl());
        		sbConfigVo.setSbCfId(t.getSbCfId());
        		sbConfigVo.setSbCfName(t.getSbCfName());
        		sbConfigVo.setJiraId(t.getJiraId());
        		sbConfigVo.setJiraPassword(t.getJiraPassword());        		
        		sbConfigVo.setRegDate(t.getRegDate());
        		//sbConfigVo.setModifyDate(t.getModifyDate());
            }
		});
		return sbConfigVo;		
	};
	
	@Override
	public SbConfig insertSbConfig(SbConfigVo sbConfigVo) throws Exception{
		SbConfig sbConfigAo = ao.create(SbConfig.class);
		sbConfigAo.setSbId(sbConfigVo.getSbId());
		sbConfigAo.setSbPassword(sbConfigVo.getSbPassword());
		sbConfigAo.setUrl(sbConfigVo.getUrl());		
		sbConfigAo.setSbCfId(sbConfigVo.getSbCfId());
		sbConfigAo.setSbCfName(sbConfigVo.getSbCfName());
		sbConfigAo.setJiraId(sbConfigVo.getJiraId());
		sbConfigAo.setJiraPassword(sbConfigVo.getJiraPassword());		
		sbConfigAo.setRegDate(new Date());	
		
		sbConfigAo.save();
		return sbConfigAo;		
		
	};
	
	
	@Override
	public SbConfig updateSbConfig(SbConfigVo sbConfigVo, int id) throws Exception{
		SbConfig sbConfigAo = ao.get(SbConfig.class, id);
		
		sbConfigAo.setSbId(sbConfigVo.getSbId());
		sbConfigAo.setSbPassword(sbConfigVo.getSbPassword());
		sbConfigAo.setUrl(sbConfigVo.getUrl());
		sbConfigAo.setSbCfId(sbConfigVo.getSbCfId());	
		sbConfigAo.setSbCfName(sbConfigVo.getSbCfName());
		sbConfigAo.setJiraId(sbConfigVo.getJiraId());
		sbConfigAo.setJiraPassword(sbConfigVo.getJiraPassword());		
		sbConfigAo.setModifyDate(new Date());
		
		sbConfigAo.save();		

		return sbConfigAo;			
	};
	
	
	@Override
	public int deleteSbConfig(int id) throws Exception{
		//System.out.println("DefaultSbConfigDAO : deleteSbConfig()=====> 1");		
		SbConfig sbConfigAo = ao.get(SbConfig.class, id);
		
		ao.delete(sbConfigAo);

		//System.out.println("DefaultSbConfigDAO : deleteSbConfig()=====> ao.count() : "+ao.count(SbConfig.class));
		return ao.count(SbConfig.class);
	};	
	
}
