package ch.uzh.rackrec.rec.config;

import java.util.Properties;

public abstract class AbstractModule extends com.google.inject.AbstractModule {

    protected Properties properties;

    public AbstractModule(){
        this.properties = new Properties();
    }

    public Properties getProperties() {
        return properties;
    }
}
