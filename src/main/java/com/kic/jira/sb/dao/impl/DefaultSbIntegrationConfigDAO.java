package com.kic.jira.sb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.dao.SbIntegrationConfigDAO;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

import net.java.ao.EntityStreamCallback;
import net.java.ao.Query;

@Named
public class DefaultSbIntegrationConfigDAO implements SbIntegrationConfigDAO {
	private static final Logger logger = LoggerFactory.getLogger(DefaultSbIntegrationConfigDAO.class);

	private final ActiveObjects ao;
	
	@Inject
	public DefaultSbIntegrationConfigDAO(@ComponentImport ActiveObjects ao) {
		this.ao = ao;
	}
	
	@Override
	public List<SbInteConfig> listSbIntegrationConfig() throws Exception{
		
		Query streamQ = Query.select("ID, PROJECT_KEY, ISSUE_TYPE, ISSUE_TYPE_NAME, BUILD_TARGET_ID, BUILD_TARGET_NAME, "
				+ "BUILD_STEP_ID, BUILD_PROGRESS_ID, BUILD_PROGRESS_NAME, BUILD_PROGRESS_ACTION, "
				+ "BUILD_SUCCESS_ID, BUILD_SUCCESS_NAME, BUILD_FAIL_ID, BUILD_FAIL_NAME");

		final List<SbInteConfig> rtnList = new ArrayList<SbInteConfig>();
		
		ao.stream(SbInteConfig.class, streamQ, new EntityStreamCallback<SbInteConfig, Integer>() {		
            @Override
            public void onRowRead(SbInteConfig t) {
            	rtnList.add(t);          	
            }
		});		
		
		return rtnList;
	}
	
	//smart builder에서 호출하여사용함
	@Override
	public SbIntegrationConfigVo selectSbIntegrationConfig(String projectKey, String issueType) throws Exception{
		Query streamQ = Query.select("ID, PROJECT_KEY, ISSUE_TYPE, ISSUE_TYPE_NAME, BUILD_TARGET_ID, BUILD_TARGET_NAME, "
				+ "BUILD_STEP_ID, BUILD_PROGRESS_ID, BUILD_PROGRESS_NAME, BUILD_PROGRESS_ACTION, "
				+ "BUILD_SUCCESS_ID, BUILD_SUCCESS_NAME, BUILD_FAIL_ID, BUILD_FAIL_NAME").where("PROJECT_KEY = ? AND ISSUE_TYPE = ?", projectKey, issueType);	
		
		final SbIntegrationConfigVo sbIntegrationConfigVo = new SbIntegrationConfigVo();
		
		ao.stream(SbInteConfig.class, streamQ, new EntityStreamCallback<SbInteConfig, Integer>() {		
            @Override
            public void onRowRead(SbInteConfig t) {
            	sbIntegrationConfigVo.setID(t.getID());
        		sbIntegrationConfigVo.setProjectKey(t.getProjectKey());
        		sbIntegrationConfigVo.setIssueType(t.getIssueType());
        		sbIntegrationConfigVo.setIssueType(t.getIssueTypeName());
        		sbIntegrationConfigVo.setIssueTypeName(t.getIssueTypeName());
        		sbIntegrationConfigVo.setBuildTargetId(t.getBuildTargetId());
        		sbIntegrationConfigVo.setBuildTargetName(t.getBuildTargetName());
        		sbIntegrationConfigVo.setBuildStepId(t.getBuildStepId());
        		sbIntegrationConfigVo.setBuildProgressId(t.getBuildProgressId());
        		sbIntegrationConfigVo.setBuildProgressName(t.getBuildProgressName());
        		sbIntegrationConfigVo.setBuildProgressAction(t.getBuildProgressAction());
        		sbIntegrationConfigVo.setBuildSuccessId(t.getBuildSuccessId());
        		sbIntegrationConfigVo.setBuildSuccessName(t.getBuildSuccessName());
        		sbIntegrationConfigVo.setBuildFailId(t.getBuildFailId());
        		sbIntegrationConfigVo.setBuildFailName(t.getBuildFailName());           		
	
            }
		});		
		
		return sbIntegrationConfigVo;
	}
	
	@Override
	public SbIntegrationConfigVo selectSbIntegrationConfig(int id) throws Exception{

		Query streamQ = Query.select("ID, PROJECT_KEY, ISSUE_TYPE, ISSUE_TYPE_NAME, BUILD_TARGET_ID, BUILD_TARGET_NAME, "
				+ "BUILD_STEP_ID, BUILD_PROGRESS_ID, BUILD_PROGRESS_NAME, BUILD_PROGRESS_ACTION, "
				+ "BUILD_SUCCESS_ID, BUILD_SUCCESS_NAME, BUILD_FAIL_ID, BUILD_FAIL_NAME").where("ID = ? ", id);	
		
		final SbIntegrationConfigVo sbIntegrationConfigVo = new SbIntegrationConfigVo();
		
		ao.stream(SbInteConfig.class, streamQ, new EntityStreamCallback<SbInteConfig, Integer>() {		
            @Override
            public void onRowRead(SbInteConfig t) {
            	sbIntegrationConfigVo.setID(t.getID());
        		sbIntegrationConfigVo.setProjectKey(t.getProjectKey());
        		sbIntegrationConfigVo.setIssueType(t.getIssueType());
        		sbIntegrationConfigVo.setIssueTypeName(t.getIssueTypeName());
        		sbIntegrationConfigVo.setBuildTargetId(t.getBuildTargetId());
        		sbIntegrationConfigVo.setBuildTargetName(t.getBuildTargetName());
        		sbIntegrationConfigVo.setBuildStepId(t.getBuildStepId());
        		sbIntegrationConfigVo.setBuildProgressId(t.getBuildProgressId());
        		sbIntegrationConfigVo.setBuildProgressName(t.getBuildProgressName());
        		sbIntegrationConfigVo.setBuildProgressAction(t.getBuildProgressAction());
        		sbIntegrationConfigVo.setBuildSuccessId(t.getBuildSuccessId());
        		sbIntegrationConfigVo.setBuildSuccessName(t.getBuildSuccessName());
        		sbIntegrationConfigVo.setBuildFailId(t.getBuildFailId());
        		sbIntegrationConfigVo.setBuildFailName(t.getBuildFailName());           	
            }
		});		
		
		return sbIntegrationConfigVo;
	}
	
	@Override
	public SbInteConfig insertSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo) throws Exception{
		
		SbInteConfig sbInteConfigAo = ao.create(SbInteConfig.class);
		
		sbInteConfigAo.setProjectKey(sbIntegrationConfigVo.getProjectKey());
		sbInteConfigAo.setIssueType(sbIntegrationConfigVo.getIssueType());
		sbInteConfigAo.setIssueTypeName(sbIntegrationConfigVo.getIssueTypeName());
		sbInteConfigAo.setBuildTargetId(sbIntegrationConfigVo.getBuildTargetId());
		sbInteConfigAo.setBuildTargetName(sbIntegrationConfigVo.getBuildTargetName());
		sbInteConfigAo.setBuildStepId(sbIntegrationConfigVo.getBuildStepId());
		sbInteConfigAo.setBuildProgressId(sbIntegrationConfigVo.getBuildProgressId());
		sbInteConfigAo.setBuildProgressName(sbIntegrationConfigVo.getBuildProgressName());
		sbInteConfigAo.setBuildProgressAction(sbIntegrationConfigVo.getBuildProgressAction());
		sbInteConfigAo.setBuildSuccessId(sbIntegrationConfigVo.getBuildSuccessId());
		sbInteConfigAo.setBuildSuccessName(sbIntegrationConfigVo.getBuildSuccessName());
		sbInteConfigAo.setBuildFailId(sbIntegrationConfigVo.getBuildFailId());
		sbInteConfigAo.setBuildFailName(sbIntegrationConfigVo.getBuildFailName());	
		
		sbInteConfigAo.save();
		return sbInteConfigAo;
	}
	
	@Override
	public SbInteConfig updateSbIntegrationConfig(SbIntegrationConfigVo sbIntegrationConfigVo, int id) throws Exception{
		
		SbInteConfig sbInteConfigAo = ao.get(SbInteConfig.class, id);
		
		sbInteConfigAo.setProjectKey(sbIntegrationConfigVo.getProjectKey());
		sbInteConfigAo.setIssueType(sbIntegrationConfigVo.getIssueType());
		sbInteConfigAo.setIssueTypeName(sbIntegrationConfigVo.getIssueTypeName());
		sbInteConfigAo.setBuildTargetId(sbIntegrationConfigVo.getBuildTargetId());
		sbInteConfigAo.setBuildTargetName(sbIntegrationConfigVo.getBuildTargetName());
		sbInteConfigAo.setBuildStepId(sbIntegrationConfigVo.getBuildStepId());
		sbInteConfigAo.setBuildProgressId(sbIntegrationConfigVo.getBuildProgressId());
		sbInteConfigAo.setBuildProgressName(sbIntegrationConfigVo.getBuildProgressName());
		sbInteConfigAo.setBuildProgressAction(sbIntegrationConfigVo.getBuildProgressAction());
		sbInteConfigAo.setBuildSuccessId(sbIntegrationConfigVo.getBuildSuccessId());
		sbInteConfigAo.setBuildSuccessName(sbIntegrationConfigVo.getBuildSuccessName());
		sbInteConfigAo.setBuildFailId(sbIntegrationConfigVo.getBuildFailId());
		sbInteConfigAo.setBuildFailName(sbIntegrationConfigVo.getBuildFailName());
		
		sbInteConfigAo.save();
		return sbInteConfigAo;
	}
	
	@Override
	public int deleteSbIntegrationConfig(int id) throws Exception{
		
		SbInteConfig sbInteConfigAo = ao.get(SbInteConfig.class, id);
		
		ao.delete(sbInteConfigAo);
		
		return ao.count(SbInteConfig.class);
	}

}
