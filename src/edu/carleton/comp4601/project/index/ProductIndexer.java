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

import edu.carleton.comp4601.project.dao.Product;

public class ProductIndexer {

	private String dirPath;
	private IndexWriter indexWriter = null;
	private HashSet<Product> productSet;
	
	public ProductIndexer(String dirPath, HashSet<Product> products) {
		this.dirPath = dirPath;
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
		
		StringField[] fields = {
				
				// General
				new StringField("id", product.getId().toString(), Field.Store.YES),
				new StringField("title", product.getTitle(), Field.Store.YES),
				new StringField("title", product.getTitle(), Field.Store.YES),
				new StringField("retailer", product.getRetailer().toString(), Field.Store.YES),
				new StringField("model", product.getModel(), Field.Store.YES),
				new StringField("type", product.getType().toString(), Field.Store.YES),
				
				//Dimensions
				//new StringField("", product.)
				
				//Screen
				
				//Processor
				
				//RAM
				
				//Harddrive
				
				//IO
				
				//Graphics
				
				//Other
				
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
