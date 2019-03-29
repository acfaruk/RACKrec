package ch.uzh.rackrec.model.gen;

import ch.uzh.rackrec.data.KaveDataSet;

import java.util.Properties;

public abstract class ModelGenerator {

    protected final KaveDataSet kaveDataSet;
    protected Properties properties;

    public ModelGenerator(KaveDataSet kaveDataSet) {
        this.kaveDataSet = kaveDataSet;
    }

    public void setProperties(Properties properties) {
        kaveDataSet.setProperties(properties);
        this.properties = properties;
    }
}
