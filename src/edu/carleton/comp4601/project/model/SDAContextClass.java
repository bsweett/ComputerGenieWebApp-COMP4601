package edu.carleton.comp4601.project.model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.carleton.comp4601.project.datebase.DatabaseManager;
import edu.carleton.comp4601.project.index.ProductIndexer;

public class SDAContextClass implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			DatabaseManager.getInstance().stopMongoClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Initializes all singletons and computes page rank on server start
	public void contextInitialized(ServletContextEvent arg0) {
		DatabaseManager.getInstance();
		ProductIndexer.getInstance();   
	}
}
