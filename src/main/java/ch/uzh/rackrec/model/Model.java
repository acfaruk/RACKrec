package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;

import java.util.Properties;
import java.util.logging.Logger;

public abstract class Model {

    protected final ModelGenerator modelGenerator;
    protected final Properties properties;
    protected final Logger logger;

    public Model(Properties properties, ModelGenerator modelGenerator, Logger logger) {
    	this.logger = logger;
        this.properties = properties;
        this.modelGenerator = modelGenerator;
    }

}
