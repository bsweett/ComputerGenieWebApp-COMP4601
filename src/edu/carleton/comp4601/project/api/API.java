package edu.carleton.comp4601.project.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.carleton.comp4601.project.index.ProductIndexer;

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
		return displayBasicXMLWithTitle(displayName);
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String homeAsHtml() {
		return displayBasicHTMLWithTitle(displayName);
	}

	@GET
	@Path("/reset") 
	@Produces(MediaType.TEXT_HTML)
	public String resetWithHTML() {

		try {
			ProductIndexer.getInstance().updateProductSet();

			if(ProductIndexer.getInstance().resetIndex()) {
				return displayBasicHTMLWithTitle("Reset Complete");
			}
		} catch(IOException i) {
			return displayBasicHTMLWithTitle("Reset Failed - See Server Logs: " + i.toString());
		}

		return displayBasicHTMLWithTitle("Reset Failed - See Server Logs");
	}

	@GET
	@Path("/reset") 
	@Produces(MediaType.APPLICATION_XML)
	public String resetWithXML() {
		try {

			ProductIndexer.getInstance().updateProductSet();

			if(ProductIndexer.getInstance().resetIndex()) {
				return displayBasicXMLWithTitle("Reset Complete");
			}
		} catch(IOException i) {
			return displayBasicXMLWithTitle("Reset Failed - See Server Logs: " + i.toString());
		}
		return displayBasicXMLWithTitle("Reset Failed - See Server Logs");
	}

	@Path("/user/{authToken}")
	public Action userRequestAsXML(@PathParam("authToken") String authToken) {	

		return new UserRequestHandler(uriInfo, request, authToken);
	}

	@Path("/product/{authToken}")
	public Action productRequestAsXML(@PathParam("authToken") String authToken) {

		return new ProductRequestHandler(uriInfo, request, authToken);
	}

	@Path("/genie/{authToken}")
	public Action genieRequestAsXML(@PathParam("authToken") String authToken) {

		return new GenieRequestHandler(uriInfo, request, authToken);
	}

	private String displayBasicHTMLWithTitle(String title) {

		return "<html> " + "<title>" + title + "</title>" + "<body><h3>" + title
				+ "</body></h3>" + "</html> ";
	}

	private String displayBasicXMLWithTitle(String title) {

		return "<?xml version=\"1.0\"?>" + "<api> " + title + " </api>";
	}
}