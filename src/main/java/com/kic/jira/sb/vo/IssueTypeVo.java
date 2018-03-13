package com.kic.jira.sb.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IssueTypeVo implements Serializable {
	private static final long serialVersionUID = -2998267987300977421L;

	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name = "name")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
