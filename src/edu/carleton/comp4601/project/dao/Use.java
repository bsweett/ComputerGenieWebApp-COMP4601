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
    		return "ramSize:*4gb* OR ramSize:*5gb* OR ramSize:*6gb* OR ramSize:*7gb* OR ramSize:*8gb* OR ramSize:*\"4 gb\"* OR ramSize:*\"5 gb\"* OR ramSize:*\"6 gb\"* OR ramSize:*\"7 gb\"* OR ramSize:*\"8 gb\"*";
    	} else {
    		return "ramSize:*8gb* OR ramSize:*9gb* OR ramSize:*10gb* OR ramSize:*11gb* OR ramSize:*12gb* OR ramSize:*13gb* OR ramSize:*14gb* OR ramSize:*15gb* OR ramSize:*16gb* OR ramSize:*17gb* OR ramSize:*18gb* OR ramSize:*19gb* OR ramSize:*2?gb* OR ramSize:*3?gb* OR " 
    				+ "ramSize:*\"8 gb\"* OR ramSize:*\"9 gb\"* OR ramSize:*\"10 gb\"* OR ramSize:*\"11 gb\"* OR ramSize:*\"12 gb\"* OR ramSize:*\"13 gb\"* OR ramSize:*\"14 gb\"* OR ramSize:*\"15 gb\"* OR " 
    				+ "ramSize:*\"16 gb\"* OR ramSize:*\"17 gb\"* OR ramSize:*\"18 gb\"* OR ramSize:*\"19 gb\"* OR ramSize:*\"2? gb\"* OR ramSize:*\"3? gb\"*";
    	}
    	
    }
    public String hddQuery() { 
    	if(hdd == 500) { 
    		return "space:*5??* OR space:*6??* OR space:*7??* OR space:*8??* OR space:*9??* OR space:*1* OR space:*2*";
    	} else {
    		return "space:*1* OR space:*2* OR space:*3* OR space:*4* OR space:*5* OR space:*6*";
    	} 
    }
    public String processorSpeedQuery() { 
    	if(processorSpeed == 0) { 
    		return null;
    	} else if(processorSpeed == 2.0) {
    		return "proSpeed:*2.*";
    	} else {
    		return "proSpeed:*2.* OR proSpeed:*3.*";
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
    		return "ScreenRes:*\"1920 x 1080\"*";
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
