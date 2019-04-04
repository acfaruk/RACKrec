package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;

import java.util.List;
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
    public abstract List<KAC> getKAC (List<String> keyword, Integer sigma);
    public abstract List<KKC> getKKC (List<KAC> KAC, Double similarity);
    public abstract List<String> getContext (String keyword);
}
