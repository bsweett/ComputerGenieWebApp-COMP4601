package edu.carleton.comp4601.project.dao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType (XmlAccessType.FIELD)
public class GenricServerResponse {

	private Integer httpCode;
	private String httpMessage;
	private String serverMessage;
	private boolean success;
	
	public GenricServerResponse(Integer httpCode, String httpMessage, String serverMessage, boolean success) {
		this.httpCode = httpCode;
		this.httpMessage = httpMessage;
		this.serverMessage = serverMessage;
		this.success = success;
	}

	public Integer getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
	}

	public String getHttpMessage() {
		return httpMessage;
	}

	public void setHttpMessage(String httpMessage) {
		this.httpMessage = httpMessage;
	}

	public String getServerMessage() {
		return serverMessage;
	}

	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
