package edu.carleton.comp4601.project.index;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import edu.carleton.comp4601.project.dao.Use;
import edu.carleton.comp4601.project.model.GenieRequest;

public class GenieQuerier {

	private GenieRequest request;
	
	
	public GenieQuerier(GenieRequest request) {
		this.request = request;
	}
	
	public ArrayList<String> askGenie() {
		
		if(request == null || request.getForm() == null || request.getUse() == null 
				|| request.getOs() == null || request.getPrice() == 0) {
			return null;
		}
		ArrayList<String> productIds = new ArrayList<String>();
		ArrayList<String> queries = new ArrayList<String>();
		ArrayList<String> titleQueries = new ArrayList<String>();
		
		Use useEnum = null;
		
		//Form query for desktop or laptop
		queries.add("type:" + request.getForm().toLowerCase());
		titleQueries.add("type:" + request.getForm().toLowerCase());
		
		//Fuzzy search (~) specifies similarity value from 0 - 1 where 1 is most similar
		//OS query for windows, mac or chrome
		queries.add("os:*" + request.getOs().toLowerCase() + "*");
		titleQueries.add("title:*" + request.getOs().toLowerCase() + "*");
		
		//Use
		if(request.getUse().equals("Business")) {
			useEnum = Use.BUSINESS;
		} else if(request.getUse().equals("Gaming")) {
			useEnum = Use.GAMING;
		} else if(request.getUse().equals("School")) {
			useEnum = Use.SCHOOL;
		} else if(request.getUse().equals("Leisure")) {
			useEnum = Use.LEISURE;
		}
		
		if(useEnum.processorSpeedQuery() != null) {
			queries.add(useEnum.processorSpeedQuery());
		}
		
		if(useEnum.quadCoreQuery() != null) {
			queries.add(useEnum.quadCoreQuery());
		}
		
		if(useEnum.screenResolutionQuery() != null && !request.getForm().toLowerCase().equals("desktop")) {
			queries.add(useEnum.screenResolutionQuery());
		}
		
		// TODO: Need a better way to parse video memory, killing results
		if(useEnum.videoMemQuery() != null) {
			//queries.add(useEnum.videoMemQuery());
		}
		
		//Price range query
		String lowerPrice = "";
		String upperPrice = "";
		
		if(request.getPrice() == 500) {
			lowerPrice = "0.00";
			upperPrice = "500.00";
		} else if(request.getPrice() == 1000) {
			lowerPrice = "500.01";
			upperPrice = "1000.00";
		} else if(request.getPrice() == 2000) {
			lowerPrice = "1000.01";
			upperPrice = "2000.00";
		} else if(request.getPrice() == 5000) {
			lowerPrice = "2000.01";
			upperPrice = "5000.00";
		}
		
		queries.add("price:[" + lowerPrice + " TO " + upperPrice + "]"); 
		titleQueries.add("price:[" + lowerPrice + " TO " + upperPrice + "]");
		
		//Screen size query
		String screenQuery = "";
		
		if(request.getScreen() == 13) {
			screenQuery = "screenSize:*11* OR screenSize:*12* OR screenSize:*13* OR screenSize:*14* OR screenSize:*15*";
		} else if(request.getScreen() == 15) {
			screenQuery = "screenSize:*13* OR screenSize:*14* OR screenSize:*15* OR screenSize:*16* OR screenSize:*17*";
		} else if(request.getScreen() == 18) {
			screenQuery = "screenSize:*16* OR screenSize:*17* OR screenSize:*18* OR screenSize:*19* OR screenSize:*20*";
		} else if(request.getScreen() == 28) {
			screenQuery = "screenSize:*21* OR screenSize:*22* OR screenSize:*23* OR screenSize:*24* OR screenSize:*25* OR screenSize:*26* OR screenSize:*27* OR screenSize:*28*";
		}
		
		if(request.getScreen() != 0) {
			queries.add(screenQuery);
		}
		
		//Minimum Memory Query
		String memoryQuery = "";
		
		if(request.getMemory() == 4) {
			memoryQuery = "ramSize:*4gb* OR ramSize:*5gb* OR ramSize:*6gb* OR ramSize:*7gb* OR ramSize:*\"4 gb\"* OR ramSize:*\"5 gb\"* OR ramSize:*\"6 gb\"* OR ramSize:*\"7 gb\"*";
		} else if(request.getMemory() == 8) {
			memoryQuery = "ramSize:*8gb* OR ramSize:*9gb* OR ramSize:*10gb* OR ramSize:*11gb* OR ramSize:*\"8 gb\"* OR ramSize:*\"9 gb\"* OR ramSize:*\"10 gb\"* OR ramSize:*\"11 gb\"*";
		} else if(request.getMemory() == 12) {
			memoryQuery = "ramSize:*12gb* OR ramSize:*13gb* OR ramSize:*14gb* OR ramSize:*15gb* OR ramSize:*\"12 gb\"* OR ramSize:*\"13 gb\"* OR ramSize:*\"14 gb\"* OR ramSize:*\"15 gb\"*";
		} else if(request.getMemory() == 16) {
			memoryQuery = "ramSize:*16gb* OR ramSize:*17gb* OR ramSize:*18gb* OR ramSize:*19gb* OR ramSize:*\"16 gb\"* OR ramSize:*\"17 gb\"* OR ramSize:*\"18 gb\"* OR ramSize:*\"19 gb\"*";
		} else if(request.getMemory() == 20) {
			memoryQuery = "ramSize:*20gb* OR ramSize:*21gb* OR ramSize:*22gb* OR ramSize:*23gb* OR ramSize:*\"20 gb\"* OR ramSize:*\"21 gb\"* OR ramSize:*\"22 gb\"* OR ramSize:*\"23 gb\"*";
		} else if(request.getMemory() == 24) {
			memoryQuery = "ramSize:*24gb* OR ramSize:*25gb* OR ramSize:*26gb* OR ramSize:*27gb* OR ramSize:*\"24 gb\"* OR ramSize:*\"25 gb\"* OR ramSize:*\"26 gb\"* OR ramSize:*\"27 gb\"*";
		} else if(request.getMemory() == 28) {
			memoryQuery = "ramSize:*28gb* OR ramSize:*29gb* OR ramSize:*30gb* OR ramSize:*31gb* OR ramSize:*\"28 gb\"* OR ramSize:*\"29 gb\"* OR ramSize:*\"30 gb\"* OR ramSize:*\"31 gb\"*";
		} else if(request.getMemory() == 32) {
			memoryQuery = "ramSize:*32gb* OR ramSize:*33gb* OR ramSize:*34gb* OR ramSize:*35gb* OR ramSize:*\"33 gb\"* OR ramSize:*\"34 gb\"* OR ramSize:*\"35 gb\"* OR ramSize:*\"36 gb\"*";
		}
		
		if(useEnum.memory() > request.getMemory()) {
			titleQueries.add(useEnum.memoryQuery().replaceAll("ramSize", "title"));
			queries.add(useEnum.memoryQuery());
		} else if(request.getMemory() != 0) {
			queries.add(memoryQuery);
		}
		
		//Minimum HDD Query
		String hddQuery = "";

		if(request.getHdd() == 500) {
			hddQuery = "space:*5??* OR space:*6??* OR space:*7??* OR space:*8??* OR space:*9??*";
		} else if(request.getHdd() == 1000) {
			hddQuery = "space:*1*";
		} else if(request.getHdd() == 1500) {
			hddQuery = "space:*1.5*";
		} else if(request.getHdd() == 2000) {
			hddQuery = "space:*2*";
		} else if(request.getHdd() == 2500) {
			hddQuery = "space:*2.5*";
		} else if(request.getHdd() == 3000) {
			hddQuery = "space:*3*";
		} else if(request.getHdd() == 3500) {
			hddQuery = "space:*3.5*";
		} else if(request.getHdd() == 4000) {
			hddQuery = "space:*4* OR space:*5* OR space:*6*";
		}
		
		if(useEnum.hdd() > request.getHdd()) {
			titleQueries.add(useEnum.hddQuery().replaceAll("space", "title"));
			queries.add(useEnum.hddQuery());
		} else if(request.getHdd() != 0) {
			queries.add(hddQuery);
		}
			
		// TODO: Need a better way to parse SSD, killing results
		if(request.isSsd() || useEnum.ssd()) {
			//titleQueries.add("title:*ssd*");
			//queries.add("title:*ssd*");
		}
	
		SearchEngine searchEngine;
		try {
			searchEngine = new SearchEngine();
			TopDocs topDocs = searchEngine.performSearch(queries, 6);
			productIds.addAll(getProductIdsFromHits(topDocs.scoreDocs, searchEngine));
			System.out.println("With full query we found: " + productIds.size());
			if(productIds.size() < 3) {
				System.out.println("Could not find more than 3 objects with full query, resorting to title");
				searchEngine = new SearchEngine();
				TopDocs topDocs2 = searchEngine.performSearch(titleQueries, 6);
				productIds.addAll(getProductIdsFromHits(topDocs2.scoreDocs, searchEngine));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productIds;
	}
	
	//Gets all product id's from a lucene document query
	public ArrayList<String> getProductIdsFromHits(ScoreDoc[] hits, SearchEngine searchEngine) throws IOException {
		ArrayList<String> productIds = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			Document doc = searchEngine.getDocument(hits[i].doc);
			productIds.add(doc.get("id"));
		}
		return productIds;
	}
}
