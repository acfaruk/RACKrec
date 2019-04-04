package ch.uzh.rackrec.data;

import java.util.Properties;

public abstract class KaveDataSet {
    protected final Properties properties;

    public KaveDataSet(Properties properties){
        this.properties = properties;
    }
}
