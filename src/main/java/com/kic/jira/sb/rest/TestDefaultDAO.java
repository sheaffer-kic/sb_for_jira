package com.kic.jira.sb.rest;

import javax.inject.Inject;
import javax.inject.Named;

import net.java.ao.EntityStreamCallback;
import net.java.ao.Query;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.kic.jira.sb.ao.SbInteConfig;
import com.kic.jira.sb.vo.SbIntegrationConfigVo;

@Named
public class TestDefaultDAO implements TestDAO {

	private final ActiveObjects ao;
	
	@Inject
	public TestDefaultDAO(@ComponentImport ActiveObjects ao) {
		this.ao = ao;
	}
	
	@Override
	public SbInteConfig insertSbInteConfig(SbIntegrationConfigVo sbInteConfigVo)
			throws Exception {
		
		SbInteConfig sbAo = ao.create(SbInteConfig.class);
		
		//System.out.println("DefaultSbConfigDAO : insertSbConfig()=====>sbConfigVo.getSbId(): "+sbConfigVo.getSbId());		
		
		sbAo.setProjectKey(sbInteConfigVo.getProjectKey());
		sbAo.setIssueType(sbInteConfigVo.getIssueType());
		sbAo.setBuildTargetId(sbInteConfigVo.getBuildTargetId());
		sbAo.setBuildTargetName(sbInteConfigVo.getBuildTargetName());
		sbAo.setBuildStepId(sbInteConfigVo.getBuildStepId());
		sbAo.setBuildProgressId(sbInteConfigVo.getBuildProgressId());
		sbAo.setBuildProgressName(sbInteConfigVo.getBuildProgressName());
		sbAo.setBuildProgressAction(sbInteConfigVo.getBuildProgressAction());
		
		sbAo.setBuildSuccessId(sbInteConfigVo.getBuildSuccessId());
		sbAo.setBuildSuccessName(sbInteConfigVo.getBuildSuccessName());
		
		sbAo.setBuildFailId(sbInteConfigVo.getBuildFailId());
		sbAo.setBuildFailName(sbInteConfigVo.getBuildFailName());

		sbAo.save();

		return sbAo;	
	}

	@Override
	public SbIntegrationConfigVo selectSbInteConfig(String projectKey, String issueType) throws Exception {
		
		
		//Query streamQ = Query.select("ID, SB_CF_ID, SB_CF_NAME, SB_ID, SB_PASSWORD, URL, REG_DATE, MODIFY_DATE");
		String sql = "ID, PROJECT_KEY, ISSUE_TYPE, "+
					 "BUILD_TARGET_ID, BUILD_TARGET_NAME, BUILD_STEP_ID, " +
					 "BUILD_PROGRESS_ID, BUILD_PROGRESS_NAME, BUILD_PROGRESS_ACTION, " +
					 "BUILD_SUCCESS_ID, BUILD_SUCCESS_NAME, " +
					 "BUILD_FAIL_ID, BUILD_FAIL_NAME";
		Query streamQ = Query.select(sql).where("PROJECT_KEY = ? AND ISSUE_TYPE = ?", projectKey, issueType);
		
		
		//("PROJECT=? and DEPLOY_TIER=? and JENKINS_PROJECT=? " + strWhere, project, tier, jenkinsJob)));
		
		System.out.println("DefaultSbConfigDAO : selectSbConfig()=====> 22222");
		
		final SbIntegrationConfigVo sbVo = new SbIntegrationConfigVo();
		
		ao.stream(SbInteConfig.class, streamQ, new EntityStreamCallback<SbInteConfig, Integer>() {		
            @Override
            public void onRowRead(SbInteConfig t) {
            	sbVo.setProjectKey(t.getProjectKey());            	
            	sbVo.setIssueType(t.getIssueType()); 
            	sbVo.setBuildTargetName (t.getBuildTargetName());
            	sbVo.setBuildStepId (t.getBuildStepId());
            	sbVo.setBuildProgressId (t.getBuildProgressId());
            	sbVo.setBuildProgressName (t.getBuildProgressName());
            	sbVo.setBuildProgressAction (t.getBuildProgressAction());
            	sbVo.setBuildSuccessId (t.getBuildSuccessId());
            	sbVo.setBuildSuccessName (t.getBuildSuccessName());
            	sbVo.setBuildFailId  (t.getBuildFailId());
            	sbVo.setBuildFailName (t.getBuildFailName());
            }
		});
		

		return sbVo;
	}

}
