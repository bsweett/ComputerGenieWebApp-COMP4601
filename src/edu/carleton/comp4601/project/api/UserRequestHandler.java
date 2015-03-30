package edu.carleton.comp4601.project.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import edu.carleton.comp4601.project.dao.GenricServerResponse;
import edu.carleton.comp4601.project.dao.User;
import edu.carleton.comp4601.project.datebase.DatabaseManager;

public class UserRequestHandler extends Action {

	public UserRequestHandler(UriInfo uriInfo, Request request, String id) {
		super(uriInfo, request, id);
	}

	private String buildAuthToken(String email, String password, String time) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes("UTF-8"));
		String hash = new String(md.digest(), "UTF-8");
		return email + "$" + hash + "$" + time;
	}

	
	/**
	 * Creates a new user account from the user XML posted. If a user is found with the same email/password pair
	 * it will return that users auth token and false
	 * 
	 * @param user
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public GenricServerResponse createNewUserFromXML(JAXBElement<User> user) {
		User u = user.getValue();
		User search = DatabaseManager.getInstance().findUserByPasswordEmail(u.getEmail(), u.getPasswordHash());
		Response res = null;

		System.out.println("New user");
		
		if(search != null) {
			res = Response.notAcceptable(null).build();
			
			// User already Exists send back there auth token and make a login request
			return new GenricServerResponse(res.getStatus(), "", search.getAuthToken(), false);
		}

		String email = u.getEmail();
		String password = u.getPasswordHash();
		String time = Long.toString(u.getLastLoginTime());

		try {
			String auth = buildAuthToken(email, password, time);
			u.setAuthToken(auth);

			if(DatabaseManager.getInstance().addNewUser(u)) {
				res = Response.ok().build();
				return new GenricServerResponse(res.getStatus(), "", u.getAuthToken(), true);
			}
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			System.err.println("Exception hashing password");
		}

		res = Response.serverError().build();
		return new GenricServerResponse(res.getStatus(), "", "Server Exception", false);
	}

	/**
	 * Logins a user. The auth token in this cause doesn't matter (just make it 0).Takes and email
	 * and a hashed password after in the path. Sends back an updated user object or null if they couldn't
	 * be logged in.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	@GET
	@Path("/{email}/{password}")
	@Produces(MediaType.APPLICATION_XML)
	public User loginUserFromXML(@PathParam("email") String email, @PathParam("password") String password) {
		User search = DatabaseManager.getInstance().findUserByPasswordEmail(email, password);
		User updateUser = search;

		System.out.println("Login user");
		
		if(search == null) {
			return null;
		}
				
		try {
			updateUser.setLastLoginTime((new DateTime().getMillis()));
			String time = Long.toString(updateUser.getLastLoginTime());
			
			String auth = buildAuthToken(email, password, time);
			updateUser.setAuthToken(auth);

			if(DatabaseManager.getInstance().updateUser(updateUser, search)) {
				return updateUser;
			}
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			System.err.println("Exception hashing password");
		}

		return null;
	}
	
	/**
	 * Gets another users profile. Needs the auth token of the user searching and
	 * the id of the user they are looking for.
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public User getUserAsXML(@PathParam("id") String id) {
		
		User search = DatabaseManager.getInstance().findUserByToken(super.authToken);
		
		System.out.println("Get user");
		
		if(search == null) {
			// They are not authorized to use the application
			return null;
		}
		
		User result = DatabaseManager.getInstance().findUser(id);
		return result;
	}
	
	/**
	 * Gets another users profile. Needs the auth token of the user searching and
	 * the id of the user they are looking for.
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_XML)
	public GenricServerResponse deleteUserAsXML() {
		User search = DatabaseManager.getInstance().findUserByToken(super.authToken);
		Response res = null;
		
		System.out.println("Delete user");
		
		if(search == null) {
			// They are not authorized to use the application
			res = Response.serverError().build();
			return new GenricServerResponse(res.getStatus(), "", "Not Authorized", false);
		}
		
		if(DatabaseManager.getInstance().removeUser(search.getId()) != null) {
			res = Response.ok().build();
			return new GenricServerResponse(res.getStatus(), "", "Ok", true);
		}
		
		res = Response.serverError().build();
		return new GenricServerResponse(res.getStatus(), "", "User not found", false);
	}
 }
