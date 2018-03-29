package com.kic.jira.sb.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SbIntegrationConfigVo implements Serializable {
	private static final long serialVersionUID = -7830516092464822807L;

	@XmlElement(name = "ID")
	private int ID;
	
	@XmlElement(name = "projectKey")
	private String projectKey; //프로젝트 키
	
	@XmlElement(name = "issueType")
	private String issueType; //이슈타입
	
	@XmlElement(name = "issueTypeName")
	private String issueTypeName; //이슈타입 이름	
	
	@XmlElement(name = "buildTargetId")
	private int buildTargetId; //빌드대상 상태
	
	@XmlElement(name = "buildTargetName")
	private String buildTargetName; //빌드대상 상태 이름
	
	@XmlElement(name = "buildStepId")
	private int buildStepId; //buildTargetId 상태에 해당하는 stepId
	
	
	@XmlElement(name = "buildProgressId")
	private int buildProgressId; //빌드중 이슈 상태
	
	@XmlElement(name = "buildProgressName")
	private String buildProgressName; //빌드중 이슈 상태 이름
	
	@XmlElement(name = "buildProgressAction")
	private int buildProgressAction; //이 액션(트랜지션)을 수행하면 buildProgressId 상태가 됨.
	
	 
	
	@XmlElement(name = "buildSuccessId")
	private int buildSuccessId; //빌드 성공시 수행할 트랜지션 
	
	@XmlElement(name = "buildSuccessName")
	private String buildSuccessName;//빌드 성공시 수행할 트랜지션 이름

	
	@XmlElement(name = "buildFailId")
	private int buildFailId; //빌드 실패시 수행할 트랜지션
	
	@XmlElement(name = "buildFailName")
	private String buildFailName;//빌드 실패시 수행할 트랜지션 이름
	

	
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getIssueTypeName() {
		return issueTypeName;
	}
	public void setIssueTypeName(String issueTypeName) {
		this.issueTypeName = issueTypeName;
	}	
	public int getBuildTargetId() {
		return buildTargetId;
	}
	public void setBuildTargetId(int buildTargetId) {
		this.buildTargetId = buildTargetId;
	}
	public String getBuildTargetName() {
		return buildTargetName;
	}
	public void setBuildTargetName(String buildTargetName) {
		this.buildTargetName = buildTargetName;
	}
	public int getBuildStepId() {
		return buildStepId;
	}
	public void setBuildStepId(int buildStepId) {
		this.buildStepId = buildStepId;
	}
	public int getBuildProgressId() {
		return buildProgressId;
	}
	public void setBuildProgressId(int buildProgressId) {
		this.buildProgressId = buildProgressId;
	}
	public String getBuildProgressName() {
		return buildProgressName;
	}
	public void setBuildProgressName(String buildProgressName) {
		this.buildProgressName = buildProgressName;
	}
	public int getBuildProgressAction() {
		return buildProgressAction;
	}
	public void setBuildProgressAction(int buildProgressAction) {
		this.buildProgressAction = buildProgressAction;
	}
	public int getBuildSuccessId() {
		return buildSuccessId;
	}
	public void setBuildSuccessId(int buildSuccessId) {
		this.buildSuccessId = buildSuccessId;
	}
	public String getBuildSuccessName() {
		return buildSuccessName;
	}
	public void setBuildSuccessName(String buildSuccessName) {
		this.buildSuccessName = buildSuccessName;
	}
	public int getBuildFailId() {
		return buildFailId;
	}
	public void setBuildFailId(int buildFailId) {
		this.buildFailId = buildFailId;
	}
	public String getBuildFailName() {
		return buildFailName;
	}
	public void setBuildFailName(String buildFailName) {
		this.buildFailName = buildFailName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
