package com.kic.jira.sb.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SbResultVo implements Serializable {
	private static final long serialVersionUID = 5489148359546280643L;

	@XmlElement(name = "ID")
	private int ID;	
	
	@XmlElement(name = "sbProjResultId")
	private int sbProjResultId;
	
	@XmlElement(name = "sbProjResult")
	private String sbProjResult;
	
	@XmlElement(name = "issueKey")
	private String issueKey;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getSbProjResultId() {
		return sbProjResultId;
	}

	public void setSbProjResultId(int sbProjResultId) {
		this.sbProjResultId = sbProjResultId;
	}

	public String getSbProjResult() {
		return sbProjResult;
	}

	public void setSbProjResult(String sbProjResult) {
		this.sbProjResult = sbProjResult;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
