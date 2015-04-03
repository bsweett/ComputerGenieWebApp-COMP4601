package edu.carleton.comp4601.project.index;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

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
