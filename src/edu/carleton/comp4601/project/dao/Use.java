package edu.carleton.comp4601.project.dao;

public enum Use {

	BUSINESS (8, 1000, 2.0, true, 1080, false, 0),
    SCHOOL   (4, 1000, 2.0, true, 0, false, 0),
    LEISURE   (4, 500, 0, false, 0, false, 0),
    GAMING    (8, 1000, 2.3, false, 1080, true, 2);

    private final int memory;   // in gb
    private final int hdd; // in gb
    private final double processorSpeed; // in Ghz
    private final int screenResolution; // in Pixels
    private final boolean ssd;
    private final boolean quadCore;
    private final double videoMem; // in gb
    
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
    		return "ramSize:*4gb* OR *5gb* OR *6gb* OR *7gb* OR *8gb* OR *\"4 gb\"* OR *\"5 gb\"* OR *\"6 gb\"* OR *\"7 gb\"* OR *\"8 gb\"*";
    	} else {
    		return "ramSize:*8gb* OR *9gb* OR *10gb* OR *11gb* OR *12gb* OR *13gb* OR *14gb* OR *15gb* OR *16gb* OR *17gb* OR *18gb* OR *19gb* OR *2?gb* OR *3?gb* OR " 
    				+ "*\"8 gb\"* OR *\"9 gb\"* OR *\"10 gb\"* OR *\"11 gb\"* OR *\"12 gb\"* OR *\"13 gb\"* OR *\"14 gb\"* OR *\"15 gb\"* OR " 
    				+ "*\"16 gb\"* OR *\"17 gb\"* OR *\"18 gb\"* OR *\"19 gb\"* OR *\"2? gb\"* OR *\"3? gb\"*";
    	}
    	
    }
    public String hddQuery() { 
    	if(hdd == 500) { 
    		return "space:*5??* OR *6??* OR *7??* OR *8??* OR *9??* OR *1* OR *2*";
    	} else {
    		return "space:*1* OR *2* OR *3* OR *4* OR *5* OR *6*";
    	} 
    }
    public String processorSpeedQuery() { 
    	if(processorSpeed == 0) { 
    		return null;
    	} else if(processorSpeed == 2.0) {
    		return "proSpeed:*2.*";
    	} else {
    		return "proSpeed:*2.* OR *3.*";
    	}
    }
    public String quadCoreQuery() { 
    	if(quadCore) {
    		return "cores:*quad-core*";
    	} else {
    		return null;
    	} 
    }
    public String screenResolutionQuery() {
    	if(screenResolution == 0) {
    		return null;
    	} else {
    		return "ScreenRes:*" + Integer.toString(screenResolution) + "*";
    	}
    }
    public String videoMemQuery() {
    	if(videoMem == 2) {
    		return "memoryCap:*2*";
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
