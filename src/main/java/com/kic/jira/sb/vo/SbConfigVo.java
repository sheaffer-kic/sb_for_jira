package com.kic.jira.sb.vo;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SbConfigVo implements Serializable {
	/**
	 * Smart Builder vo
	 */
	//private static final long serialVersionUID = 2603228253672123512L;
	private static final long serialVersionUID = -4936432013397974208L;
	
	@XmlElement(name = "ID")
	private int ID;	
	
	@XmlElement(name = "sbId")
	private String sbId;	
	
	@XmlElement(name = "sbPassword")
	private String sbPassword;
	
	@XmlElement(name = "url")
	private String url;
	
	@XmlElement(name = "sbCfId")
	private String sbCfId; //SmartBuildr의 customfield Id
	
	@XmlElement(name = "sbCfName")
	private String sbCfName; //SmartBuildr의 customfield Id
	
	
	@XmlElement(name = "jiraId")
	private String jiraId; //jira계정
	
	@XmlElement(name = "jiraPassword")
	private String jiraPassword;
	
	@XmlElement(name = "regDate")
	private String regDate;
	
	@XmlElement(name = "modifyDate")
	private String modifyDate;

	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public String getSbId() {
		return sbId;
	}
	public void setSbId(String sbId) {
		this.sbId = sbId;
	}
	
	public String getSbPassword() {
		return sbPassword;
	}
	public void setSbPassword(String sbPassword) {
		this.sbPassword = sbPassword;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getSbCfName() {
		return sbCfName;
	}
	public void setSbCfName(String sbCfName) {
		this.sbCfName = sbCfName;
	}	
	
	public String getSbCfId() {
		return sbCfId;
	}
	public void setSbCfId(String sbCfId) {
		this.sbCfId = sbCfId;
	}	

	
	public String getJiraId() {
		return jiraId;
	}
	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}
	
	public String getJiraPassword() {
		return jiraPassword;
	}
	public void setJiraPassword(String jiraPassword) {
		this.jiraPassword = jiraPassword;
	}
	
	public String getRegDate(){
		return regDate;
	}
	public void setRegDate(Date regDate){
		this.regDate = regDate.toString();
	}
	
	public String getModifyDate(){
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate){
		this.modifyDate = modifyDate.toString();
	}	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
