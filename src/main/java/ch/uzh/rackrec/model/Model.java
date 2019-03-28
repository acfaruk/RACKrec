package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;

import java.util.Properties;

public abstract class Model {
    protected Properties properties;
    protected ModelGenerator modelGenerator;

    public Model(ModelGenerator modelGenerator) {
        this.modelGenerator = modelGenerator;
    }

    public void setProperties(Properties properties) {
        modelGenerator.setProperties(properties);
        this.properties = properties;
    }
}
