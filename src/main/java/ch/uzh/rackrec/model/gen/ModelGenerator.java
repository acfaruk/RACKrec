package ch.uzh.rackrec.model.gen;

import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.provider.ModelEntry;

import java.util.Properties;
import java.util.logging.Logger;

public abstract class ModelGenerator {

    protected final KaveDataSet kaveDataSet;
    protected final Properties properties;
    protected final Logger logger;

    public ModelGenerator(Properties properties, KaveDataSet kaveDataSet, Logger logger) {
        this.properties = properties;
        this.kaveDataSet = kaveDataSet;
        this.logger = logger;
    }

    public abstract Iterable<ModelEntry> getModelEntries();

}
