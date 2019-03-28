package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import com.google.inject.Inject;

public class DefaultModel extends Model {

    @Inject
    public DefaultModel(ModelGenerator modelGenerator) {
        super(modelGenerator);
    }
}
