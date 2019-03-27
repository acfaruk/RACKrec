package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import com.google.inject.Inject;

import java.util.Properties;

public abstract class Model {
    protected Properties properties;
    protected ModelGenerator modelGenerator;

    @Inject
    public Model(ModelGenerator modelGenerator){
        this.modelGenerator = modelGenerator;
    }

    public void setProperties(Properties properties) {
        modelGenerator.setProperties(properties);
        this.properties = properties;
    }
}
