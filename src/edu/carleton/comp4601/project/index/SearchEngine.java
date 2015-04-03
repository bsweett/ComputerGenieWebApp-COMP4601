package edu.carleton.comp4601.project.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class SearchEngine {

	private IndexSearcher searcher = null;
	private QueryParser parser = null;
	private final String lucenePath = System.getProperty("user.home") + "/data/product-index";
	
	/** Creates a new instance of SearchEngine */
	public SearchEngine() throws IOException {
		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(lucenePath))));
		parser = new QueryParser("content", new StandardAnalyzer());
	}

	public TopDocs performSearch(ArrayList<String> queryStrings, int n)
			throws IOException, ParseException {
		
		BooleanQuery booleanQuery = new BooleanQuery();
		
		for(String queryString : queryStrings) {
			Query query = parser.parse(queryString);
			booleanQuery.add(query, BooleanClause.Occur.MUST);
		}
		
		return searcher.search(booleanQuery, n);
	}

	public Document getDocument(int docId)
			throws IOException {
		return searcher.doc(docId);

	}

}
