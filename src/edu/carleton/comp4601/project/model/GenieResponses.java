package edu.carleton.comp4601.project.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GenieResponses")
@XmlAccessorType (XmlAccessType.FIELD)
public class GenieResponses {

	private ArrayList<GenieResponse> responses;
	
	public GenieResponses() {
		this.setResponses(new ArrayList<GenieResponse>());
	}
	
	public void setResponses(ArrayList<GenieResponse> responses) {
		this.responses = responses;
	}

	public ArrayList<GenieResponse> getResponses() {
		return responses;
	}
	
	public boolean addResponse(GenieResponse response) {
		return this.responses.add(response);
	}
	
	public boolean removeResponse(GenieResponse response) {
		return this.responses.remove(response);
	}
}
