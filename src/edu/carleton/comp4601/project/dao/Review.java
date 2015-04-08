package edu.carleton.comp4601.project.dao;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@XmlRootElement(name = "review")
@XmlAccessorType (XmlAccessType.FIELD)
@Entity("reviews")
public class Review {

	//Identifiers 
	@XmlTransient
	@Id ObjectId id;
	
	private String productId;
	private String userId;
	private String userName;
	private String content;
	private String opinion;
	private Integer upScore;
	private Integer downScore;
	private long date;
	private String productName;
	private String url;
	
	@XmlElement(name = "voter")
	private ArrayList<String> voters;
	
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

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
	public void setObjectId(ObjectId id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void upVote() {
		this.upScore++;
	}
	
	public void downVote() {
		this.downScore++;
	}
	
	public void setVoters(ArrayList<String> voters) {
		this.voters = voters;
	}
	
	public ArrayList<String> getVoters() {
		return this.voters;
	}
	
	public void addVoter(String id) {
		this.voters.add(id);
	}
}
