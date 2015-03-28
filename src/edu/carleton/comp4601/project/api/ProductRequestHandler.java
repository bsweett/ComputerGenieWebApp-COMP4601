package edu.carleton.comp4601.project.api;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class ProductRequestHandler extends Action {

	
	public ProductRequestHandler(UriInfo uri, Request request, String id) {
		super(uri, request, id);
	}
}
