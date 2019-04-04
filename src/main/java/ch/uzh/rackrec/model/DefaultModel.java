package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import com.google.inject.Inject;

import java.util.Properties;

public class DefaultModel extends Model {

    @Inject
    public DefaultModel(Properties properties, ModelGenerator modelGenerator) {
        super(properties, modelGenerator);
    }
}
