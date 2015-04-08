package edu.carleton.comp4601.project.index;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

public class IndexTimerTask extends TimerTask {
	
	 	@Override
	    public void run() {
	        System.out.println("Timer task started at:"+new Date());
	        completeTask();
	        System.out.println("Timer task finished at:"+new Date());
	    }
	 
	    private void completeTask() {
	        try {
	        	ProductIndexer.getInstance().updateProductSet();
	       
				ProductIndexer.getInstance().resetIndex();
			 
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
