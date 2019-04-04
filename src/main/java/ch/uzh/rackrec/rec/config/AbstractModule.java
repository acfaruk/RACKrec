package ch.uzh.rackrec.rec.config;

import java.util.Properties;

public abstract class AbstractModule extends com.google.inject.AbstractModule {

    protected final Properties properties;

    public AbstractModule(Properties properties){
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}
