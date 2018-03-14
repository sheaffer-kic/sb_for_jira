package com.kic.jira.sb.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class SbProjectVo implements Serializable {
	private static final long serialVersionUID = -6212517597584054950L;
	
	@XmlElement(name = "id")
	private String id; //프로젝트명 (auiSelect2 에서는 id 값을 저장함. -> id 를 넣었을 경우  이슈보기에서 id 가 보여짐.)
	
	@XmlElement(name = "text")
	private String text;  //프로젝트 명
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
