package edu.carleton.comp4601.project.model;


public class StatResponse {

	private String upvotes;
	private String downvotes;
	private String total;
	
	public StatResponse() {
		
	}
	
	public StatResponse(int up, int down, int total) {
		this.upvotes = Integer.toString(up);
		this.downvotes = Integer.toString(down);
		this.total = Integer.toString(total);
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
