package com.kic.jira.sb.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Table;

@Table("sbIntegrationConfig")
@Preload
public interface SbIntegrationConfig extends Entity {
	
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
