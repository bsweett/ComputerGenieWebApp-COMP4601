package edu.carleton.comp4601.project.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@XmlRootElement(name = "user")
@XmlAccessorType (XmlAccessType.FIELD)
public class User {
	
	private String authToken;
	private String id;
	private String firstname;
	private String lastname;
	private String email;
	private String passwordHash;
	private String gender;
	private String birthday;
	private long lastLoginTime;
	
	@XmlElement(name = "productIds")
	private ArrayList<String> productIds;
	
	public User() {
		this.productIds = new ArrayList<String>();
	}

	public User(String id) {
		this();
		this.setId(id);
	}

	@SuppressWarnings("unchecked")
	public User(Map<?, ?> map) {
		this();
		this.setAuthToken((String) map.get("authtoken"));
		this.setId((String) map.get("id"));
		this.setFirstname((String) map.get("firstname"));
		this.setLastname((String) map.get("lastname"));
		this.setEmail((String) map.get("email"));
		this.setPasswordHash((String) map.get("passwordhash"));
		this.setGender((String) map.get("gender"));
		this.setBirthday((String) map.get("birthday"));
		this.setLastLoginTime((long) map.get("lastlogintime"));
		this.productIds = (ArrayList<String>) map.get("productIds");
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	public void setLastLoginTimeFromDate(DateTime date) {
		this.lastLoginTime = date.getMillis();
	}
	
	public DateTime getLoginTimeAsDate() {
		return new DateTime(this.lastLoginTime);
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public void setBirthdayFromDate(DateTime date) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		this.birthday = formatter.print(date);
	}
	
	public DateTime getBirthdayAsDate() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		DateTime dt = formatter.parseDateTime(this.birthday);
		return dt;
	}
	
	public ArrayList<String> getProductIds() {
		return this.productIds;
	}
}
