package edu.carleton.comp4601.project.api;

import java.util.HashSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.carleton.comp4601.project.dao.Product;
import edu.carleton.comp4601.project.datebase.DatabaseManager;
import edu.carleton.comp4601.project.index.ProductIndexer;

@Path("/api")
public class API {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private String displayName;
	private final String lucenePath = System.getProperty("user.home") + "/data/";
	
	private ProductIndexer indexer;
	private HashSet<Product> products;

	public API() {
		this.displayName = "Computer Genie Restful Web Service";
		this.products = new HashSet<Product>();
		this.indexer = new ProductIndexer(lucenePath, products);
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
		HashSet<Product> newPro = DatabaseManager.getInstance().getAllProducts();
		this.indexer.updateProductSet(newPro);
		
		if(this.indexer.resetIndex()) {
			return displayBasicHTMLWithTitle("Reset Complete");
		}
				
		return displayBasicHTMLWithTitle("Reset Failed - See Server Logs");
	}
	
	@GET
	@Path("/reset") 
	@Produces(MediaType.APPLICATION_XML)
	public String resetWithXML() {
		HashSet<Product> newPro = DatabaseManager.getInstance().getAllProducts();
		this.indexer.updateProductSet(newPro);
		
		if(this.indexer.resetIndex()) {
			return displayBasicXMLWithTitle("Reset Complete");
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