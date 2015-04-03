package edu.carleton.comp4601.project.index;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
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
		
		if(useEnum.screenResolutionQuery() != null && request.getForm().toLowerCase() != "desktop") {
			queries.add(useEnum.screenResolutionQuery());
		}
		
		if(useEnum.videoMemQuery() != null) {
			queries.add(useEnum.videoMemQuery());
		}
		
		//Price range query
		String lowerPrice = "";
		String upperPrice = "";
		
		if(request.getPrice() == 500) {
			lowerPrice = "0";
			upperPrice = "500";
		} else if(request.getPrice() == 1000) {
			lowerPrice = "501";
			upperPrice = "1000";
		} else if(request.getPrice() == 2000) {
			lowerPrice = "1001";
			upperPrice = "2000";
		} else if(request.getPrice() == 5000) {
			lowerPrice = "2001";
			upperPrice = "5000";
		}
		
		
		queries.add("price:[" + lowerPrice + " TO " + upperPrice + "]"); 
		//titleQueries.add("price:[" + lowerPrice + " TO " + upperPrice + "]");
		
		//Screen size query
		String screenQuery = "";
		
		if(request.getScreen() == 13) {
			screenQuery = "screenSize:*11* OR *12* OR *13* OR *14* OR *15*";
		} else if(request.getScreen() == 15) {
			screenQuery = "screenSize:*13* OR *14* OR *15* OR *16* OR *17*";
		} else if(request.getScreen() == 18) {
			screenQuery = "screenSize:*16* OR *17* OR *18* OR *19* OR *20*";
		} else if(request.getScreen() == 28) {
			screenQuery = "screenSize:*21* OR *22* OR *23* OR *24* OR *25* OR *26* OR *27* OR *28*";
		}
		
		if(request.getScreen() != 0) {
			queries.add(screenQuery);
		}
		
		//Minimum Memory Query
		String memoryQuery = "";
		
		if(request.getMemory() == 4) {
			memoryQuery = "ramSize:*4gb* OR *5gb* OR *6gb* OR *7gb* OR *\"4 gb\"* OR *\"5 gb\"* OR *\"6 gb\"* OR *\"7 gb\"*";
		} else if(request.getMemory() == 8) {
			memoryQuery = "ramSize:*8gb* OR *9gb* OR *10gb* OR *11gb* OR *\"8 gb\"* OR *\"9 gb\"* OR *\"10 gb\"* OR *\"11 gb\"*";
		} else if(request.getMemory() == 12) {
			memoryQuery = "ramSize:*12gb* OR *13gb* OR *14gb* OR *15gb* OR *\"12 gb\"* OR *\"13 gb\"* OR *\"14 gb\"* OR *\"15 gb\"*";
		} else if(request.getMemory() == 16) {
			memoryQuery = "ramSize:*16gb* OR *17gb* OR *18gb* OR *19gb* OR *\"16 gb\"* OR *\"17 gb\"* OR *\"18 gb\"* OR *\"19 gb\"*";
		} else if(request.getMemory() == 20) {
			memoryQuery = "ramSize:*20gb* OR *21gb* OR *22gb* OR *23gb* OR *\"20 gb\"* OR *\"21 gb\"* OR *\"22 gb\"* OR *\"23 gb\"*";
		} else if(request.getMemory() == 24) {
			memoryQuery = "ramSize:*24gb* OR *25gb* OR *26gb* OR *27gb* OR *\"24 gb\"* OR *\"25 gb\"* OR *\"26 gb\"* OR *\"27 gb\"*";
		} else if(request.getMemory() == 28) {
			memoryQuery = "ramSize:*28gb* OR *29gb* OR *30gb* OR *31gb* OR *\"28 gb\"* OR *\"29 gb\"* OR *\"30 gb\"* OR *\"31 gb\"*";
		} else if(request.getMemory() == 32) {
			memoryQuery = "ramSize:*32gb* OR *33gb* OR *34gb* OR *35gb* OR *\"33 gb\"* OR *\"34 gb\"* OR *\"35 gb\"* OR *\"36 gb\"*";
		}
		
		if(useEnum.memory() > request.getMemory()) {
			//titleQueries.add(useEnum.memoryQuery().replaceAll("ramSize", "title"));
			queries.add(useEnum.memoryQuery());
		} else if(request.getMemory() != 0) {
			queries.add(memoryQuery);
		}
		
		//Minimum HDD Query
		String hddQuery = "";

		if(request.getHdd() == 500) {
			hddQuery = "space:*5??* OR *6??* OR *7??* OR *8??* OR *9??*";
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
			hddQuery = "space:*4* OR *5* OR *6*";
		}
		
		if(useEnum.hdd() > request.getHdd()) {
			//titleQueries.add(useEnum.hddQuery().replaceAll("space", "title"));
			queries.add(useEnum.hddQuery());
		} else if(request.getHdd() != 0) {
			queries.add(hddQuery);
		}
			
		//For SSD check us dType:
		if(request.isSsd() || useEnum.ssd()) {
			//titleQueries.add("title:SSD~0.8");
			queries.add("title:*ssd*");
		}
	
		SearchEngine searchEngine;
		try {
			searchEngine = new SearchEngine();
			TopDocs topDocs = searchEngine.performSearch(queries, 6);
			productIds.addAll(getProductIdsFromHits(topDocs.scoreDocs, searchEngine));
			
			if(productIds.size() < 3) {
				System.out.println("Testing");
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
