package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;

import java.util.Properties;

public abstract class Model {

    protected final ModelGenerator modelGenerator;
    protected final Properties properties;

    public Model(Properties properties, ModelGenerator modelGenerator) {
        this.properties = properties;
        this.modelGenerator = modelGenerator;
    }

}
