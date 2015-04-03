package edu.carleton.comp4601.project.index;

import java.util.ArrayList;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

import edu.carleton.comp4601.project.dao.ProductType;
import edu.carleton.comp4601.project.model.GenieRequest;
import edu.carleton.comp4601.project.model.GenieResponses;

public class GenieQuerier {

	private GenieRequest request;
	
	
	public GenieQuerier(GenieRequest request) {
		this.request = request;
	}
	
	public GenieResponses askGenie() {
		
		if(request == null || request.getForm() == null || request.getUse() == null 
				|| request.getOs() == null || request.getPrice() == 0) {
			return null;
		}
		
		ArrayList<String> queries = new ArrayList<String>();
		
		//Form query for desktop or laptop
		queries.add("type:" + request.getForm());
		
		//Fuzzy search (~) specifies similarity value from 0 - 1 where 1 is most similar
		//OS query for windows, mac or chrome
		queries.add("os:" + request.getOs() + "~0.5");
		
		//Crazy use rules go here
		//=======================
		
		//Price range query
		String lowerPrice;
		String upperPrice;
		
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
		
		
		queries.add("price:[" + lowerPrice + "TO" + upperPrice + "]"); 
		
		
		//Screen size query
		String screenQuery;
		
		if(request.getScreen() == 13) {
			screenQuery = "screenSize:11* OR " + "12* OR " + "13* OR " + "14* OR " + "15*";
		} else if(request.getScreen() == 15) {
			screenQuery = "screenSize:13* OR " + "14* OR " + "15* OR " + "16* OR " + "17*";
		} else if(request.getScreen() == 18) {
			screenQuery = "screenSize:16* OR " + "17* OR " + "18* OR " + "19* OR " + "20*";
		} else if(request.getScreen() == 28) {
			screenQuery = "screenSize:21* OR " + "22* OR " + "23* OR " + "24* OR " + "25* OR " + "26* OR " + "27* OR " + "28*";
		}
		
		if(request.getScreen() != 0) {
			queries.add(screenQuery);
		}
		
		//Minimum Memory Query
		String memoryQuery;
		
		if(request.getMemory() == 4) {
			memoryQuery = "ramSize:4* OR " + "5* OR " + "6* OR " + "7*";
		} else if(request.getMemory() == 8) {
			memoryQuery = "ramSize:8* OR " + "9* OR " + "10* OR " + "11*";
		} else if(request.getMemory() == 12) {
			memoryQuery = "ramSize:12* OR " + "13* OR " + "14* OR " + "15*";
		} else if(request.getMemory() == 16) {
			memoryQuery = "ramSize:16* OR " + "17* OR " + "18* OR " + "19*";
		} else if(request.getMemory() == 20) {
			memoryQuery = "ramSize:20* OR " + "21* OR " + "22* OR " + "23*";
		} else if(request.getMemory() == 24) {
			memoryQuery = "ramSize:24* OR " + "25* OR " + "26* OR " + "27*";
		} else if(request.getMemory() == 28) {
			memoryQuery = "ramSize:28* OR " + "29* OR " + "30* OR " + "31*";
		} else if(request.getMemory() == 32) {
			memoryQuery = "ramSize:32* OR " + "33* OR " + "34* OR " + "35*";
		}
		
		if(request.getMemory() != 0) {
			queries.add(memoryQuery);
		}
		
		//Minimum HDD Query
		String hddQuery;

		if(request.getHdd() == 500) {
			hddQuery = "space:5??* OR " + "6??* OR " + "7??* OR " + "8??* OR " + "9??*";
		} else if(request.getHdd() == 1000) {
			hddQuery = "space:1*";
		} else if(request.getHdd() == 1500) {
			hddQuery = "space:1.5*";
		} else if(request.getHdd() == 2000) {
			hddQuery = "space:2*";
		} else if(request.getHdd() == 2500) {
			hddQuery = "space:2.5*";
		} else if(request.getHdd() == 3000) {
			hddQuery = "space:3*";
		} else if(request.getHdd() == 3500) {
			hddQuery = "space:3.5*";
		} else if(request.getHdd() == 4000) {
			hddQuery = "space:4*";
		}

		if(request.getHdd() != 0) {
			queries.add(hddQuery);
		}
			
		//For SSD check us dType:
		if(request.isSsd()) {
			queries.add("dType:SSD~0.8");
		}
		
		
		SearchEngine searchEngine = new SearchEngine();
		TopDocs topDocs = searchEngine.performSearch(queries, 6);
		
		
//		String str = "foo bar";
//		String id = "123456";
//		BooleanQuery bq = new BooleanQuery();
//		Query query = qp.parse(str);
//		bq.add(query, BooleanClause.Occur.MUST);
//		bq.add(new TermQuery(new Term("id", id), BooleanClause.Occur.MUST_NOT);
//		
		
		return null;
	}
	
	private void queryForForm() {
//		Query q = new QueryParser(Version.LUCENE_4_10_3, "title", )
	}
}
