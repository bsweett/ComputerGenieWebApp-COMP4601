package edu.carleton.comp4601.project.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import edu.carleton.comp4601.project.dao.Product;
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
		
		GenieRequest genie = genieRequest.getValue();
		System.out.println(genie.toString());
		
		GenieQuerier querier = new GenieQuerier(genie);
		
		ArrayList<String> productIds = querier.askGenie();
		String[] productIdsArray = productIds.toArray(new String[productIds.size()]);
		
		System.out.println(productIdsArray[0]);
		
		ArrayList<Product> products = DatabaseManager.getInstance().findArrayOfProductsByIds(productIdsArray);
		
		GenieResponses responses = new GenieResponses();
		
		for(Product p : products) {
			System.out.println("Found a product: " + p.getId().toString());
			GenieResponse gr = new GenieResponse(p.getId().toString(), p.getTitle(), p.getUrl(), p.getImageSrc(), Float.parseFloat(p.getPrice()), p.getRetailer().name());
			responses.addResponse(gr);
		}
		
		
		
		//GenieResponse r1 = new GenieResponse("1","Ben","http://google.com","https://s-media-cache-ak0.pinimg.com/236x/9a/26/84/9a2684c4213171476e13732af3b26537.jpg",0f,"NCIX");
		//GenieResponse r2 = new GenieResponse("2","Brayden","http://google.com","https://s-media-cache-ak0.pinimg.com/236x/9a/26/84/9a2684c4213171476e13732af3b26537.jpg",0f,"NCIX");
		//GenieResponse r3 = new GenieResponse("3","Colin","http://google.com","https://s-media-cache-ak0.pinimg.com/236x/9a/26/84/9a2684c4213171476e13732af3b26537.jpg",0f,"NCIX");
		
		//responses.addResponse(r1);
		//responses.addResponse(r2);
		//responses.addResponse(r3);
		
		return responses;
	}
}
