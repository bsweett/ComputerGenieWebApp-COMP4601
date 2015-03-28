package edu.carleton.comp4601.project.api;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class Action {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	String authToken;

	public Action(UriInfo uriInfo, Request request, String authToken) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.authToken = authToken;
	}

}
