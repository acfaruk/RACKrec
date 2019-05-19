package ch.uzh.rackrec.data;

import cc.kave.commons.model.events.completionevents.Context;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class KaveDataSet {
    protected final Properties properties;
    protected final Logger logger;
    public KaveDataSet(Properties properties, Logger logger){
    	
    	this.logger = logger;
        this.properties = properties;
    }

    public abstract Iterable<Context> getContextData();
}
