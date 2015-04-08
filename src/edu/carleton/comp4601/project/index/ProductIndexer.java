package edu.carleton.comp4601.project.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.carleton.comp4601.project.dao.Dimensions;
import edu.carleton.comp4601.project.dao.GraphicsCard;
import edu.carleton.comp4601.project.dao.Harddrive;
import edu.carleton.comp4601.project.dao.InputOutput;
import edu.carleton.comp4601.project.dao.Processor;
import edu.carleton.comp4601.project.dao.Product;
import edu.carleton.comp4601.project.dao.RAM;
import edu.carleton.comp4601.project.dao.Review;
import edu.carleton.comp4601.project.dao.Screen;
import edu.carleton.comp4601.project.datebase.DatabaseManager;

public class ProductIndexer {

	private IndexWriter indexWriter = null;
	private HashSet<Product> productSet = new HashSet<Product>();
	private static ProductIndexer instance;
	private final String dirPath = System.getProperty("user.home") + "/data/";
	TimerTask timerTask;
	Timer timer;
	
	public static ProductIndexer getInstance() {

		if (instance == null)
			instance = new ProductIndexer();
		return instance;

	}
	
	public static void setInstance(ProductIndexer instance) {
		ProductIndexer.instance = instance;
	}
	
	public ProductIndexer() {
		timerTask = new IndexTimerTask();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 3600*1000);
	}
	
	public void updateProductSet() {
		this.productSet = DatabaseManager.getInstance().getAllProducts();
	}
	
	public boolean resetIndex() throws IOException {
			
			if(productSet != null) {
				
				this.indexWriter = null;
				
				for(Product p : this.productSet) {
				    indexProduct(p);
				}

				closeIndexWriter();				
				return true;
			}
			
		
	
		return false;
	}
	
	private void indexProduct(Product product) throws IOException {
		
		IndexWriter writer = getIndexWriter();
		
		Dimensions dim = product.getDimensions();
		Screen screen = product.getScreen();
		Processor processor = product.getProcessor();
		RAM ram = product.getRam();
		Harddrive drive = product.getHarddrive();
		InputOutput io = product.getIo();
		GraphicsCard gCard = product.getGraphics();
		
	
		FieldType myStringType = new FieldType(StringField.TYPE_STORED);
		myStringType.setOmitNorms(false);
		
		Field[] fields = {
				// General
				new Field("id", product.getId().toString(), myStringType),
				new Field("title", product.getTitle(), myStringType),
				new Field("retailer", product.getRetailer().toString(), myStringType),
				new Field("model", product.getModel(), myStringType),
				new Field("type", product.getType().toString(), myStringType),
				new Field("price", this.priceToString(product.getPrice()), myStringType),
				//Dimensions
				new Field("depth", dim.getDepth(), myStringType),
				new Field("width", dim.getWidth(), myStringType),
				new Field("height", dim.getHeight(), myStringType),
				new Field("weight", dim.getWeight(), myStringType),
				//Screen
				new Field("ScreenRes", screen.getScreenRes(), myStringType),
				new Field("screenSize", screen.getScreenSize(), myStringType),
				new Field("touch", screen.isTouchScreen(), myStringType),
				//Processor
				new Field("proBrand", processor.getBrand(), myStringType),
				new Field("cores", processor.getNumberOfCores(), myStringType),
				new Field("proType", processor.getProcessorType(), myStringType),
				new Field("proSpeed", processor.getSpeed(), myStringType),
				//RAM
				new Field("ramSize", ram.getMemorySize(), myStringType),
				new Field("ramType", ram.getType(), myStringType),
				//Harddrive
				new Field("space", drive.getCapacity(), myStringType),
				new Field("driveType", drive.getDriveType(), myStringType),
				new Field("speed", drive.getSpeed(), myStringType),
				new Field("dType", drive.getType(), myStringType),
				//IO
				new Field("keyboard", io.getKeyboard(), myStringType),
				new Field("mouse", io.getMouse(), myStringType),
				new Field("speakers", io.getSpeakers(), myStringType),
				new Field("usb", io.getUSBPorts(), myStringType),
				new Field("dvi", io.isHasDVI(), myStringType),
				new Field("vga", io.isHasVGA(), myStringType),
				new Field("hdmi", io.isHasHDMI(), myStringType),
				new Field("webcam", io.isHasWebCam(), myStringType),
				new Field("firewire", io.getFirewire(),myStringType),
				new Field("remote", io.getRemote(),myStringType),
				new Field("other", io.getOther(),myStringType),
				//Graphics
				new Field("manufacturer", gCard.getManufacturer(),myStringType),
				new Field("gMemType", gCard.getMemoryType(),myStringType),
				new Field("gModel", gCard.getModel(),myStringType),
				new Field("memoryCap", gCard.getModel(),myStringType),
				//Other
				new Field("os", product.getOs(),myStringType),
				new Field("wifi", product.getWifi(),myStringType),
				new Field("audio", product.getAudioDescription(),myStringType),
				new Field("battery", product.getBatteryLife(), myStringType)
		};
		
		fields = this.boostProducts(fields, product);
		
		Document doc = addAllStringsToDocument(fields);
		
		writer.addDocument(doc);
	}
	
	private Document addAllStringsToDocument(Field[] fields) {
		Document doc = new Document();
		
		for(Field sf : fields) {
			doc.add(sf);
		}
		
		return doc;
	}
				
	//Gets an instance of the indexWriter if it does not exist
	private IndexWriter getIndexWriter() throws IOException {
		if (indexWriter == null) {
			
			File path = new File(this.dirPath + "product-index");
	        
			if(path.exists()) { 
	           File[] existingFiles = null;
	           existingFiles = path.listFiles();
	           
	           if(existingFiles != null) {
	        	   for(int i=0; i<existingFiles.length; i++) {
	        		   existingFiles[i].delete();
	        	   }
	           }
	           
	        } else {
	        	path.mkdirs();
	        }
	        
			Directory indexDir = FSDirectory.open(path);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, new StandardAnalyzer());
			indexWriter = new IndexWriter(indexDir, config);
		}

		return indexWriter;
	}    

	//Closes the index writer
	public void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}
	
	private String priceToString(String price) {
	
		String cleanPrice = price.replaceAll("[^0-9]", "");
	
		while(cleanPrice.length() < 7) {
			cleanPrice = "0" + cleanPrice;
		}
	
		return cleanPrice;
	}
	
	private Field[] boostProducts(Field[] fields, Product product) {
		ArrayList<Review> reviews = new ArrayList<Review>();
		reviews = DatabaseManager.getInstance().getReviewsByProductId(product.getId().toString());
		int overallScore = 0;
		
		for (Review review : reviews) {
			overallScore += review.getUpScore() - review.getDownScore();
		}
		
		if(reviews.size() > 0) {
			overallScore = overallScore/reviews.size();
		}
		
		if(overallScore > 4) {
			overallScore = 4;
		}
		
		if(overallScore < -4) {
			overallScore = -4;
		}
		
		float boost = (overallScore * 0.25f) + 1;
		
		for(int i=0; i<fields.length; i++) {
			fields[i].setBoost(boost);
		}
		
		return fields;
	}
}
