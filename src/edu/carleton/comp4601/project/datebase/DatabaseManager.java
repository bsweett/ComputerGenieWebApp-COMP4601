package edu.carleton.comp4601.project.datebase;

import java.net.UnknownHostException;
import java.util.HashSet;

import org.mongodb.morphia.Morphia;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import edu.carleton.comp4601.project.dao.Product;
import edu.carleton.comp4601.project.dao.User;

public class DatabaseManager {

	public DB getDatabase() {
		return db;
	}

	public void setDatabase(DB db) {
		this.db = db;
	}

	public static void setInstance(DatabaseManager instance) {
		DatabaseManager.instance = instance;
	}

	private DB db;
	private MongoClient mongoClient;
	private Morphia morphia;
	private static DatabaseManager instance;

	public DatabaseManager() {

		try {
			this.morphia = new Morphia();
			this.morphia.map(Product.class);
			
			this.mongoClient = new MongoClient( "localhost" );
			setDatabase(this.mongoClient.getDB( "computergenie" ));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}
	
	// Products
	
	public DBCollection getProductCollection() {

		DBCollection col;

		if (db.collectionExists("products")) {
			col = db.getCollection("products");
			return col;
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", false).add("size", 2000000000l).get();
			col = db.createCollection("products", options);
			return col;
		}
	}
	
	public HashSet<Product> getAllProducts() {
		
		HashSet<Product> hashSet = new HashSet<Product>();
		
		try {
			DBCollection col = getProductCollection();
			DBCursor cursor = col.find();
			
			while(cursor.hasNext()) {
				DBObject obj = cursor.next();
				Product product = this.morphia.fromDBObject(Product.class, obj);
				if(product != null) {
					hashSet.add(product);
				}
			}
			return hashSet;
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
		}
		
		return null;
	}

	public boolean addNewProduct(Product product) {

		try {
			DBCollection col = getProductCollection();
			col.insert(this.morphia.toDBObject(product));
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return false;
		}

		return true; 	
	}

	public boolean updateProduct(Product newProduct, Product oldProduct) {

		try {
			DBCollection col = getProductCollection();
			col.update(this.morphia.toDBObject(oldProduct), this.morphia.toDBObject(newProduct));

		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return false;
		}

		return true; 
	}

	public Product removeProduct(String id) {	

		try {
			BasicDBObject query = new BasicDBObject("id", id);
			DBCollection col = getProductCollection();
			DBObject result = col.findAndRemove(query);
			return morphia.fromDBObject(Product.class, result);
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return null;
		}
	}
	
	public int getProductCollectionSize() {
		DBCollection col = this.getProductCollection();
		return (int) col.getCount();
	}
	
	// User

	public DBCollection getUserCollection() {

		DBCollection col;

		if (db.collectionExists("users")) {
			col = db.getCollection("users");
			return col;
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", false).add("size", 1000000000l).get();
			col = db.createCollection("users", options);
			return col;
		}
	}

	public boolean addNewUser(User user) {

		try {
			DBCollection col = getUserCollection();
			col.insert(buildDBObject(user));
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return false;
		}

		return true; 	
	}

	public boolean updateUser(User newUser, User oldUser) {

		try {
			DBCollection col = getUserCollection();
			col.update(buildDBObject(oldUser), buildDBObject(newUser));

		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return false;
		}

		return true; 
	}

	public User removeUser(String id) {	

		try {
			BasicDBObject query = new BasicDBObject("id", id);
			DBCollection col = getUserCollection();
			DBObject result = col.findAndRemove(query);
			return new User(result.toMap());
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return null;
		}
	}

	public User findUser(String id) {

		try {

			BasicDBObject query = new BasicDBObject("id", id);
			DBCollection col = getUserCollection();
			DBObject result = col.findOne(query);

			if(result != null) {
				return new User(result.toMap());
			}

			return null;
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return null;
		}

	}
	
	public User findUserByPasswordEmail(String email, String password) {
		try {

			BasicDBObject query = new BasicDBObject("email", email);
			query.append("passwordhash", password);
			DBCollection col = getUserCollection();
			DBObject result = col.findOne(query);

			if(result != null) {
				return new User(result.toMap());
			}

			return null;
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return null;
		}
	}
	
	public User findUserByToken(String authToken) {

		try {

			BasicDBObject query = new BasicDBObject("authtoken", authToken);
			DBCollection col = getUserCollection();
			DBObject result = col.findOne(query);

			if(result != null) {
				return new User(result.toMap());
			}

			return null;
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return null;
		}

	}
	
	public BasicDBObject buildDBObject(User user) {
		
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("authtoken", user.getAuthToken());
		newObj.put("id", user.getId());
		newObj.put("firstname", user.getFirstname());
		newObj.put("lastname", user.getLastname());
		newObj.put("email", user.getEmail());
		newObj.put("passwordhash", user.getPasswordHash());
		newObj.put("gender", user.getGender());
		newObj.put("birthday", user.getBirthday());
		newObj.put("lastlogintime", user.getLastLoginTime());
		newObj.put("productids", user.getProductIds());
		return newObj;
		
	}

	public int getUserCollectionSize() {
		DBCollection col = this.getUserCollection();
		return (int) col.getCount();
	}

	public static DatabaseManager getInstance() {

		if (instance == null)
			instance = new DatabaseManager();
		return instance;

	}

	public void stopMongoClient() {

		if(this.mongoClient != null) {
			this.mongoClient.close();
		}

	}
}
