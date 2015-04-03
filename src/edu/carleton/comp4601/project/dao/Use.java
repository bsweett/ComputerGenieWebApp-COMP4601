package edu.carleton.comp4601.project.dao;

public enum Use {

	BUSINESS (8, 1000, 2.0, true, 1080, false, 0),
    SCHOOL   (4, 1000, 2.0, true, 0, false, 0),
    LEISURE   (4, 500, 0, false, 0, false, 0),
    GAMING    (8, 1000, 2.3, false, 1080, true, 2);

    private final int memory;   // in GB
    private final int hdd; // in GB
    private final double processorSpeed; // in Ghz
    private final int screenResolution; // in Pixels
    private final boolean ssd;
    private final boolean quadCore;
    private final double videoMem; // in GB
    
    Use(int memory, int hdd, double processorSpeed, boolean quadCore, int screenResolution, boolean ssd, double videoMem) {
        this.memory = memory;
        this.hdd = hdd;
        this.processorSpeed = processorSpeed;
        this.quadCore = quadCore;
        this.screenResolution = screenResolution;
        this.ssd = ssd;
        this.videoMem = videoMem;
    }
    
    public String memoryQuery() { 
    	if(memory == 4) {
    		return "ramSize:4* OR 5* OR 6* OR 7* OR 8* OR \"4 GB\" OR \"5 GB\" OR \"6 GB\" OR \"7 GB\" OR \"8 GB\"";
    	} else {
    		return "ramSize:8* OR 9* OR 10* OR 11* OR 12* OR 13* OR 14* OR 15* OR 16* OR 17* OR 18* OR 19* OR 2?* OR 3?* OR " 
    				+ "\"8 GB\" OR \"9 GB\" OR \"10 GB\" OR \"11 GB\" OR \"12 GB\" OR \"13 GB\" OR \"14 GB\" OR \"15 GB\" OR " 
    				+ "\"16 GB\" OR \"17 GB\" OR \"18 GB\" OR \"19 GB\" OR \"2? GB\" OR \"3? GB\"";
    	}
    	
    }
    public String hddQuery() { 
    	if(hdd == 500) { 
    		return "space:500* OR 1* OR 2*";
    	} else {
    		return "space:1* OR 2* OR 3* OR 4* OR 5* OR 6*";
    	} 
    }
    public String processorSpeedQuery() { 
    	if(processorSpeed == 0) { 
    		return null;
    	} else if(processorSpeed == 2.0) {
    		return "proSpeed:2.*";
    	} else {
    		return "proSpeed:2.* OR 3.*";
    	}
    }
    public String quadCoreQuery() { 
    	if(quadCore) {
    		return "cores:Quad-Core~0.7";
    	} else {
    		return null;
    	} 
    }
    public String screenResolutionQuery() {
    	if(screenResolution == 0) {
    		return null;
    	} else {
    		return "ScreenRes:" + Integer.toString(screenResolution) + "~0.8";
    	}
    }
    public String videoMemQuery() {
    	if(videoMem == 2) {
    		return "memoryCap:2*";
    	} else {
    		return null;
    	}	
    }
    
    public int memory() {
    	return memory;
    }
    
    public int hdd() {
    	return hdd;
    }
    
    public boolean ssd() {
    	return ssd;
    }
}
