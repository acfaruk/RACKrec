package ch.uzh.rackrec.data;

import java.util.Properties;
import java.util.logging.Logger;

public abstract class KaveDataSet {
    protected final Properties properties;
    protected final Logger logger;
    public KaveDataSet(Properties properties, Logger logger){
    	
    	this.logger = logger;
        this.properties = properties;
    }
}
