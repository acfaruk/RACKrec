package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;

import java.util.Properties;

public abstract class Model {

    protected final ModelGenerator modelGenerator;
    protected Properties properties;

    public Model(ModelGenerator modelGenerator) {
        this.modelGenerator = modelGenerator;
    }

    public void setProperties(Properties properties) {
        modelGenerator.setProperties(properties);
        this.properties = properties;
    }
}
