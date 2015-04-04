package edu.carleton.comp4601.project.index;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
import edu.carleton.comp4601.project.dao.Screen;

public class ProductIndexer {

	private String dirPath;
	private IndexWriter indexWriter = null;
	private HashSet<Product> productSet;
	
	public ProductIndexer(String dirPath, HashSet<Product> products) {
		this.dirPath = dirPath;
		this.productSet = products;
	}
	
	public void updateProductSet(HashSet<Product> products) {
		this.productSet = products;
	}
	
	public boolean resetIndex() {
		try {
			
			if(productSet != null) {
				getIndexWriter(true);
				
				for(Product p : this.productSet) {
				    indexProduct(p);
				}

				closeIndexWriter();				
				return true;
			}
			
		} catch (IOException e) {
			System.err.println("IOException while indexing products");
		}
	
		return false;
	}
	
	private void indexProduct(Product product) throws IOException {
		
		IndexWriter writer = getIndexWriter(false);
		
		Dimensions dim = product.getDimensions();
		Screen screen = product.getScreen();
		Processor processor = product.getProcessor();
		RAM ram = product.getRam();
		Harddrive drive = product.getHarddrive();
		InputOutput io = product.getIo();
		GraphicsCard gCard = product.getGraphics();
		
		StringField[] fields = {
				// General
				new StringField("id", product.getId().toString(), Field.Store.YES),
				new StringField("title", product.getTitle(), Field.Store.YES),
				new StringField("retailer", product.getRetailer().toString(), Field.Store.YES),
				new StringField("model", product.getModel(), Field.Store.YES),
				new StringField("type", product.getType().toString(), Field.Store.YES),
				//Dimensions
				new StringField("depth", dim.getDepth(), Field.Store.YES),
				new StringField("width", dim.getWidth(), Field.Store.YES),
				new StringField("height", dim.getHeight(), Field.Store.YES),
				new StringField("weight", dim.getWeight(), Field.Store.YES),
				//Screen
				new StringField("ScreenRes", screen.getScreenRes(), Field.Store.YES),
				new StringField("screenSize", screen.getScreenSize(), Field.Store.YES),
				new StringField("touch", screen.isTouchScreen(), Field.Store.YES),
				//Processor
				new StringField("proBrand", processor.getBrand(), Field.Store.YES),
				new StringField("cores", processor.getNumberOfCores(), Field.Store.YES),
				new StringField("proType", processor.getProcessorType(), Field.Store.YES),
				new StringField("proSpeed", processor.getSpeed(), Field.Store.YES),
				//RAM
				new StringField("ramSize", ram.getMemorySize(), Field.Store.YES),
				new StringField("ramType", ram.getType(), Field.Store.YES),
				//Harddrive
				new StringField("space", drive.getCapacity(), Field.Store.YES),
				new StringField("driveType", drive.getDriveType(), Field.Store.YES),
				new StringField("speed", drive.getSpeed(), Field.Store.YES),
				new StringField("dType", drive.getType(), Field.Store.YES),
				//IO
				new StringField("keyboard", io.getKeyboard(), Field.Store.YES),
				new StringField("mouse", io.getMouse(), Field.Store.YES),
				new StringField("speakers", io.getSpeakers(), Field.Store.YES),
				new StringField("usb", io.getUSBPorts(), Field.Store.YES),
				new StringField("dvi", io.isHasDVI(), Field.Store.YES),
				new StringField("vga", io.isHasVGA(), Field.Store.YES),
				new StringField("hdmi", io.isHasHDMI(), Field.Store.YES),
				new StringField("webcam", io.isHasWebCam(), Field.Store.YES),
				new StringField("firewire", io.getFirewire(),Field.Store.YES),
				new StringField("remote", io.getRemote(),Field.Store.YES),
				new StringField("other", io.getOther(),Field.Store.YES),
				//Graphics
				new StringField("manufacturer", gCard.getManufacturer(),Field.Store.YES),
				new StringField("gMemType", gCard.getMemoryType(),Field.Store.YES),
				new StringField("gModel", gCard.getModel(),Field.Store.YES),
				new StringField("memoryCap", gCard.getModel(),Field.Store.YES),
				//Other
				new StringField("os", product.getOs(),Field.Store.YES),
				new StringField("wifi", product.getWifi(),Field.Store.YES),
				new StringField("audio", product.getAudioDescription(),Field.Store.YES),
				new StringField("battery", product.getBatteryLife(), Field.Store.YES)
		};
		
		Document doc = addAllStringsToDocument(fields);
		
		writer.addDocument(doc);
	}
	
	private Document addAllStringsToDocument(StringField[] fields) {
		Document doc = new Document();
		
		for(StringField sf : fields) {
			doc.add(sf);
		}
		
		return doc;
	}
				
	//Gets an instance of the indexWriter if it does not exist
	private IndexWriter getIndexWriter(boolean create) throws IOException {
		if (indexWriter == null) {
			Directory indexDir = FSDirectory.open(new File( this.dirPath + "product-index" ));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, new StandardAnalyzer());
			indexWriter = new IndexWriter(indexDir, config);
		}
		return indexWriter;
	}    

	//Closes the index writer
	private void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}
}
