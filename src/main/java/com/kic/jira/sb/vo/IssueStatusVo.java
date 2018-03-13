package com.kic.jira.sb.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IssueStatusVo implements Serializable {
	private static final long serialVersionUID = -4422682327465896057L;

	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name = "stepId")
	private int stepId;
	
	@XmlElement(name = "actionId")
	private int actionId;
	
	@XmlElement(name = "name")
	private String name; 
	
	@XmlElement(name = "iconUrl")
	private String iconUrl; 
	
	@XmlElement(name = "actionList")
	List<IssueStatusVo> actionList;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public List<IssueStatusVo> getActionList() {
		return actionList;
	}
	public void setActionList(List<IssueStatusVo> actionList) {
		this.actionList = actionList;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
