package edu.carleton.comp4601.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GenieRequest")
@XmlAccessorType (XmlAccessType.FIELD)
public class GenieRequest {

	private String form;
	private String os;
	private String use;
	private int price;
	private int screen;
	private int memory;
	private int hdd;
	private boolean ssd;
	
	public GenieRequest() {
		
	}
	
	public GenieRequest(String form, String os, String use, int price, int screen, int memory, int hdd, boolean ssd) {
		this();
		setForm(form);
		setOs(os);
		setUse(use);
		setPrice(price);
		setScreen(screen);
		setMemory(memory);
		setHdd(hdd);
		setSsd(ssd);
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getScreen() {
		return screen;
	}

	public void setScreen(int screen) {
		this.screen = screen;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getHdd() {
		return hdd;
	}

	public void setHdd(int hdd) {
		this.hdd = hdd;
	}

	public boolean isSsd() {
		return ssd;
	}

	public void setSsd(boolean ssd) {
		this.ssd = ssd;
	}
	
	public String toString() {
		return this.form + " " + this.os + " " + this.use + " " + this.price + " " 
				+ this.screen + " " + this.memory + " " + this.hdd + " " + this.ssd;
	}
}
