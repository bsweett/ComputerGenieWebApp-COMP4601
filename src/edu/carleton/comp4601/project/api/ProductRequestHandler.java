package edu.carleton.comp4601.project.api;

import java.util.ArrayList;
import java.util.HashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import edu.carleton.comp4601.project.dao.Product;
import edu.carleton.comp4601.project.dao.Review;
import edu.carleton.comp4601.project.dao.User;
import edu.carleton.comp4601.project.datebase.DatabaseManager;
import edu.carleton.comp4601.project.model.GenieResponse;
import edu.carleton.comp4601.project.model.GenieResponses;
import edu.carleton.comp4601.project.model.GenricServerResponse;

public class ProductRequestHandler extends Action {

	
	public ProductRequestHandler(UriInfo uri, Request request, String id) {
		super(uri, request, id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public GenieResponses getAllProductsForUserAsXML() {
		
		GenieResponses responses = new GenieResponses();
		User userSearch = DatabaseManager.getInstance().findUserByToken(super.authToken);

		if(userSearch == null) {
			return null;
		}
		
		HashSet<String> pIds = userSearch.getProductIds();
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		for(String pid : pIds) {
			Product p = DatabaseManager.getInstance().getProductById(pid);
			products.add(p);
		}
		
		for(Product p : products) {
			GenieResponse response = new GenieResponse(p.getId().toString(),
					p.getTitle(),p.getUrl(),p.getImageSrc(), Float.parseFloat(p.getPrice()) ,p.getRetailer().toString());
			responses.addResponse(response);
		}
		
		return responses;
	}
	
	@POST
	@Path("/review")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public GenricServerResponse addReviewForProductAsXML(JAXBElement<Review> rev) {
		
		Review review = rev.getValue();
		Response res = null;

		if(review.getContent() != "" && review.getOpinion() != null && review.getScore() != null && review.getUserId() != "" 
				&& review.getProductId() != "") {
			User userSearch = DatabaseManager.getInstance().findUserByToken(super.authToken);
			
			if(userSearch == null) {
				// User not authorized
				res = Response.status(401).build();
				return new GenricServerResponse(res.getStatus(), "Unauthorized", "Invalid Token", false);
			}
			
			Product productSearch = DatabaseManager.getInstance().getProductById(review.getProductId());
			
			if(productSearch == null) {
				// No Product found to review
				res = Response.noContent().build();
				return new GenricServerResponse(res.getStatus(), "Not Acceptable", "Product not found", false);
			}
			
			Review reviewSearch = DatabaseManager.getInstance().getReviewByUserIdForProductId(review.getUserId(), review.getProductId());
			
			if(reviewSearch != null) {
				// User has already reviewed this product
				res = Response.notModified().build();
				return new GenricServerResponse(res.getStatus(), "Not Modified", "This user already reviewed this product", false);
			}
			
			if(DatabaseManager.getInstance().addNewReview(review)) {
				// Review saved correctly
				res = Response.ok().build();
				return new GenricServerResponse(res.getStatus(), "Ok", "Ok", true);
			} else {
				// DB Failed to save review
				res = Response.serverError().build();
				return new GenricServerResponse(res.getStatus(), "Server Error", "Couldnt Save Review", false);
			}
		}
		
		// XML was not valid review
		res = Response.notAcceptable(null).build();
		return new GenricServerResponse(res.getStatus(), "Not Acceptable", "Missing parameters", false);
	}
	
	// NOTE: In production would page them using pagaination 
	@GET
	@Path("/reviews/{productId}")
	@Produces(MediaType.APPLICATION_XML)
	public ArrayList<Review> getAllReviewsAsXML(@PathParam("productId") String productId) {
		
		ArrayList<Review> arrayList = new ArrayList<Review>();
		
		User userSearch = DatabaseManager.getInstance().findUserByToken(super.authToken);
		
		if(userSearch == null) {
			// User not authorized
			return null;
		}
		
		Product productSearch = DatabaseManager.getInstance().getProductById(productId);
		
		if(productSearch == null) {
			// Product doesn't exist
			return arrayList;
		}
		
		return DatabaseManager.getInstance().getReviewsByProductId(productId);
	}
}
