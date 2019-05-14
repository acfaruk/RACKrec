package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.provider.SQLiteProvider;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Model {

    protected final ModelGenerator modelGenerator;
    protected final Properties properties;
    protected final Logger logger;
    protected final SQLiteProvider sqLiteProvider;

    public Model(Properties properties, ModelGenerator modelGenerator, Logger logger, SQLiteProvider sqLiteProvider) {
    	this.logger = logger;
        this.properties = properties;
        this.modelGenerator = modelGenerator;
        this.sqLiteProvider = sqLiteProvider;

    }
    public abstract List<KAC> getKAC (List<String> keyword, Integer sigma);
    public abstract List<KKC> getKKC (List<KAC> kac, Double similarity);
    public abstract List<String> getContext (String keyword);
}
