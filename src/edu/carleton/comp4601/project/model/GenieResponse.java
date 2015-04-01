package edu.carleton.comp4601.project.model;

public class GenieResponse {

	private String id;
	private String name;
	private String url;
	private String image;
	private float price;
	private String retailer;
	
	public GenieResponse() {
		
	}
	
	public GenieResponse(String id, String name, String url, String image, float price, String retailer) {
		this();
		setId(id);
		setName(name);
		setUrl(url);
		setImage(image);
		setPrice(price);
		setRetailer(retailer);
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getRetailer() {
		return retailer;
	}

	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}
}
