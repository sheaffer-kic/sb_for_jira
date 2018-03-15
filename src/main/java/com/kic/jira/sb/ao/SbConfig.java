package com.kic.jira.sb.ao;

import java.util.Date;
import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Table;

@Table("sbConfig")
@Preload

public interface SbConfig extends Entity {

	//smart builder info
	
	//SmartBuilder id
	String getSbId();
	void setSbId(String sbId);
	
	//SmartBuilder pw
	String getSbPassword();
	void setSbPassword(String sbPassword);
	
	//url
	String getUrl();
	void setUrl(String url);
	
	//sbCfId (customfield for smartbuilder project)
	String getSbCfId();
	void setSbCfId(String sbCfId);
	
	
	String getSbCfName();
	void setSbCfName(String sbCfName);
	
	//Jira id
	String getJiraId();
	void setJiraId(String jiraId);
	
	//Jira pw
	String getJiraPassword();
	void setJiraPassword(String jiraPassword);
	
	Date getRegDate();
	void setRegDate(Date regDate);
	
	Date getModifyDate();
	void setModifyDate(Date modifyDate);			


}
