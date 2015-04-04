package edu.carleton.comp4601.project.dao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@XmlRootElement(name = "review")
@XmlAccessorType (XmlAccessType.FIELD)
@Entity("reviews")
public class Review {

	//Identifiers 
	@Id ObjectId id;
	
	private String productId;
	private String userId;
	private String userName;
	private String content;
	private Opinion opinion;
	private Integer upScore;
	private Integer downScore;
	private long date;
	
	public Review() {
		
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getUpScore() {
		return upScore;
	}

	public void setUpScore(Integer upScore) {
		this.upScore = upScore;
	}

	public Integer getDownScore() {
		return downScore;
	}

	public void setDownScore(Integer downScore) {
		this.downScore = downScore;
	}

	public Opinion getOpinion() {
		return opinion;
	}

	public void setOpinion(Opinion opinion) {
		this.opinion = opinion;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
}
