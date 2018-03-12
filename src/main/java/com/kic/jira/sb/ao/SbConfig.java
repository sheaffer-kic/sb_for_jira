package com.kic.jira.sb.ao;

import java.util.Date;
import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Table;

@Table("sbConfig")
@Preload

public interface SbConfig extends Entity {

	//smart builder info
	
	//id
	String getSbId();
	void setSbId(String sbId);
	
	//pw
	String getSbPassword();
	void setSbPassword(String sbPassword);
	
	//url
	String getUrl();
	void setUrl(String url);
	
	
	
	Date getRegDate();
	void setRegDate(Date regDate);
	
	Date getModifyDate();
	void setModifyDate(Date modifyDate);			


}
