package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;
import com.google.inject.Inject;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DefaultModel extends Model {

    @Inject
    public DefaultModel(Properties properties, ModelGenerator modelGenerator, Logger logger) {
        super(properties, modelGenerator, logger);
    }

    @Override
    public List<KAC> getKAC(List<String> keyword, Integer sigma) {
        return null;
    }

    @Override
    public List<KKC> getKKC(List<KAC> KAC, Double similarity) {
        return null;
    }

    @Override
    public List<String> getContext(String keyword) {
        return null;
    }

}
