package edu.carleton.comp4601.project.datebase;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bson.types.ObjectId;
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
import edu.carleton.comp4601.project.dao.Review;
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
			this.morphia.map(Review.class);
			
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
		
		return hashSet;
	}
	
	public Product getProductById(String id) {
		
		try {
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
			DBCollection col = getProductCollection();
			DBObject result = col.findOne(query);
			
			if(result != null) {
				return this.morphia.fromDBObject(Product.class, result);
			}
			
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
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
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

	public boolean updateUser(User newUser, String userId) {

		try {
			BasicDBObject query = new BasicDBObject("id", userId);
			DBCollection col = getUserCollection();
			col.update(query, buildDBObject(newUser));

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
	
	// Reviews
	
	public DBCollection getReviewCollection() {

		DBCollection col;

		if (db.collectionExists("reviews")) {
			col = db.getCollection("reviews");
			return col;
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", false).add("size", 2000000000l).get();
			col = db.createCollection("reviews", options);
			return col;
		}
	}
	
	/**
	 * Add a new review object to the DB
	 * 
	 * @param review
	 * @return
	 */
	public boolean addNewReview(Review review) {

		try {
			DBCollection col = getReviewCollection();
			col.insert(this.morphia.toDBObject(review));
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
			return false;
		}

		return true; 	
	}
	
	public Review getReviewByUserIdForProductId(String userId, String productId) {
		
		try {
			BasicDBObject query = new BasicDBObject("userId", userId);
			query.append("productId", productId);
			DBCollection col = getReviewCollection();
			DBObject result = col.findOne(query);
			
			if(result != null) {
				return this.morphia.fromDBObject(Review.class, result);
			}
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
		}

		return null;
	}
	
	/**
	 * Gets all the reviews with a given userId
	 * 
	 * @param userId
	 * @return
	 */
	public ArrayList<Review> getReviewsByUserId(String userId) {
		
		ArrayList<Review> arrayList = new ArrayList<Review>();
		
		try {
			BasicDBObject query = new BasicDBObject("userId", userId);
			DBCollection col = getReviewCollection();
			DBCursor cursor = col.find(query);
			
			while(cursor.hasNext()) {
				DBObject obj = cursor.next();
				Review review = this.morphia.fromDBObject(Review.class, obj);
				if(review != null) {
					arrayList.add(review);
				}
			}
			return arrayList;
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
		}
		
		return arrayList;
	}
	
	/**
	 * Returns a list of all reviews by a given user with a given opinion (like or dislike)
	 * 
	 * @param userId
	 * @param opinion
	 * @return arrayList
	 */
	public ArrayList<Review> getReviewsByUserIdWithOpinion(String userId, String opinion) {
		
		ArrayList<Review> arrayList = new ArrayList<Review>();
		
		try {
			BasicDBObject query = new BasicDBObject("userId", userId);
			query.put("opinion", opinion);
			DBCollection col = getReviewCollection();
			DBCursor cursor = col.find(query);
			
			while(cursor.hasNext()) {
				DBObject obj = cursor.next();
				Review review = this.morphia.fromDBObject(Review.class, obj);
				if(review != null) {
					arrayList.add(review);
				}
			}
			return arrayList;
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
		}
		
		return arrayList;
	}
	
	public ArrayList<Review> getReviewsByProductId(String productId) {
		
		ArrayList<Review> arrayList = new ArrayList<Review>();
		
		try {
			BasicDBObject query = new BasicDBObject("productId", productId);
			DBCollection col = getReviewCollection();
			DBCursor cursor = col.find(query);
			
			while(cursor.hasNext()) {
				DBObject obj = cursor.next();
				Review review = this.morphia.fromDBObject(Review.class, obj);
				if(review != null) {
					arrayList.add(review);
				}
			}
			return arrayList;
			
		} catch (MongoException e) {
			System.out.println("MongoException: " + e.getLocalizedMessage());
		}
		
		return arrayList;
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
