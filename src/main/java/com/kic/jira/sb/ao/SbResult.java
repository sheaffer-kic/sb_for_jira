package com.kic.jira.sb.ao;
import java.util.Date;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.Table;

@Table("sbResult")
@Preload
public interface SbResult extends Entity {

	
	//issueKey
	String getIssueKey();
	void setIssueKey(String issueKey);
	
	@Indexed
	int getSbProjResultId();
	void setSbProjResultId(int sbProjResultId);
	
	//I:진행중, P:성공, F:실패
	String getSbProjResult();
	void setSbProjResult(String sbProjResult);
	
	Date getRegDate();
	void setRegDate(Date regDate);
	
	Date getModifyDate();
	void setModifyDate(Date modifyDate);
}
