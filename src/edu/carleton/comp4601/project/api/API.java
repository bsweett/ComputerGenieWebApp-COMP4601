package edu.carleton.comp4601.project.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.carleton.comp4601.project.dao.GenricServerResponse;

@Path("/api")
public class API {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private String displayName;

	public API() {
		this.displayName = "Computer Genie Restful Web Service";
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String homeAsXML() {
		return "<?xml version=\"1.0\"?>" + "<api> " + displayName + " </api>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String homeAsHtml() {
		return "<html> " + "<title>" + displayName + "</title>" + "<body><h1>" + displayName
				+ "</body></h1>" + "</html> ";
	}
	
	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_XML) 
	@Produces(MediaType.APPLICATION_XML)
	public GenricServerResponse userRequestAsXML() {
		
		return null;
	}
	
	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_XML) 
	@Produces(MediaType.TEXT_HTML)
	public String userRequestAsHTML() {
		
		return "";
	}
}