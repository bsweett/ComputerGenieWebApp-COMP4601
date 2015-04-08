package edu.carleton.comp4601.project.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import edu.carleton.comp4601.project.dao.Product;
import edu.carleton.comp4601.project.dao.User;
import edu.carleton.comp4601.project.datebase.DatabaseManager;
import edu.carleton.comp4601.project.index.GenieQuerier;
import edu.carleton.comp4601.project.model.GenieRequest;
import edu.carleton.comp4601.project.model.GenieResponse;
import edu.carleton.comp4601.project.model.GenieResponses;

public class GenieRequestHandler extends Action {

	public GenieRequestHandler(UriInfo uriInfo, Request request, String id) {
		super(uriInfo, request, id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public GenieResponses processGenieRequestAsXML(JAXBElement<GenieRequest> genieRequest) {
		
		User userSearch = DatabaseManager.getInstance().findUserByToken(super.authToken);

		if(userSearch == null) {
			return null;
		}
		
		GenieRequest genie = genieRequest.getValue();		
		GenieQuerier querier = new GenieQuerier(genie);
		
		ArrayList<String> productIds = querier.askGenie();
		ArrayList<Product> products = new ArrayList<Product>();
		
		for(String pid : productIds) {
			Product p = DatabaseManager.getInstance().getProductById(pid);
			products.add(p);
		}
		
		GenieResponses responses = new GenieResponses();		
		for(Product p : products) {
			System.out.println("Found a product: " + p.getId().toString());
			GenieResponse gr = new GenieResponse(p.getId().toString(), p.getTitle(), p.getUrl(), p.getImageSrc(), p.getPrice(), p.getRetailer().name());
			responses.addResponse(gr);
		}
		
		return responses;
	}
	
	@POST
	@Path("/history")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public GenieResponses processGenieHistoryAsXML(JAXBElement<User> u) {
		
		User user = u.getValue();
		User userSearch = DatabaseManager.getInstance().findUserByToken(super.authToken);

		if(userSearch == null) {
			return null;
		}
		
		user.setAuthToken(userSearch.getAuthToken());
		
		// TODO: Update doesn't need to be the full object.
		// Use MongoDB queries to push each productId from the array to the user array in the DB
		if(!DatabaseManager.getInstance().updateUser(user, userSearch.getId())) {
			return null;
		}
		
		ArrayList<String> productIds = new ArrayList<String>();
		productIds = user.getProductIds();
		
		ArrayList<Product> products = new ArrayList<Product>();
		

			for(String pid : productIds) {
				System.out.println("The product id is: " + pid);
				
				if(!pid.isEmpty()) {
					Product p = DatabaseManager.getInstance().getProductById(pid);
					products.add(p);
				}
			}
		
		
		GenieResponses responses = new GenieResponses();
		
		for(Product p : products) {
			GenieResponse gr = new GenieResponse(p.getId().toString(), p.getTitle(), p.getUrl(), p.getImageSrc(), p.getPrice(), p.getRetailer().name());
			responses.addResponse(gr);
		}
		
		return responses;
	}
}
