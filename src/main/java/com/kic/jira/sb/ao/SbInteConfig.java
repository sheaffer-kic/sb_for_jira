package com.kic.jira.sb.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Table;

@Table("sbInteConfig")
@Preload
public interface SbInteConfig extends Entity {
	
	//projectKey
	String getProjectKey();
	void setProjectKey(String projectKey);
	
	//issueType
	String getIssueType();
	void setIssueType(String issueType);
	
	//빌드대상 상태  id
	int getBuildTargetId();
	void setBuildTargetId(int buildTargetId);
	
	//빌드대상 상태  이름
	String getBuildTargetName();
	void setBuildTargetName(String dbuildTargetName);
	
	//buildTargetId 상태에 해당하는 stepId
	int getBuildStepId();
	void setBuildStepId(int deployStepId);
	
	
	//빌드중 이슈 상태
	int getBuildProgressId();
	void setBuildProgressId(int deployProgressId);
	
	String getDeployProgressName();
	void setDeployProgressName(String deployProgressName);
	
	//이 액션(트랜지션)을 수행하면 buildProgressId 상태가 됨.
	int getBuildProgressAction();
	void setBuildProgressAction(int getDeployProgressAction);
	
	
	
	//빌드성공시 수행할 트랜지션 id
	int getBuildSuccessId();
	void setBuildSuccessId(int buildSuccessId );

	//빌드성공시 수행할 트랜지션 이름
	String getBuildSuccessName();
	void setBuildSuccessName(int buildSuccessName );
	

	//빌드실패시 수행할 트랜지션 id
	int getBuildFailId();
	void setBuildFailId(int buildFailId );

	//빌드실패시 수행할 트랜지션 이름
	String getBuildFailName();
	void setBuildFailId(String buildFailName);
}
