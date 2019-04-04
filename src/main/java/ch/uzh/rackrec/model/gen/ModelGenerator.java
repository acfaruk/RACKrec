package ch.uzh.rackrec.model.gen;

import ch.uzh.rackrec.data.KaveDataSet;

import java.util.Properties;

public abstract class ModelGenerator {

    protected final KaveDataSet kaveDataSet;
    protected final Properties properties;

    public ModelGenerator(Properties properties, KaveDataSet kaveDataSet) {
        this.properties = properties;
        this.kaveDataSet = kaveDataSet;
    }

}
