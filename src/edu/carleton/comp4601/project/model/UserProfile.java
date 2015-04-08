package edu.carleton.comp4601.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "profile")
@XmlAccessorType (XmlAccessType.FIELD)
public class UserProfile {

	private String userId;
	private String upvotes;
	private String downvotes;
	private String total;
	
	public UserProfile() {
		
	}
	
	public UserProfile(int up, int down, int total) {
		this.upvotes = Integer.toString(up);
		this.downvotes = Integer.toString(down);
		this.total = Integer.toString(total);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(String upvotes) {
		this.upvotes = upvotes;
	}

	public String getDownvotes() {
		return downvotes;
	}

	public void setDownvotes(String downvotes) {
		this.downvotes = downvotes;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
