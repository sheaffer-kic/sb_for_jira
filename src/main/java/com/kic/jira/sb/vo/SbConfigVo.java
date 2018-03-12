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
	
	public String toString(){
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
