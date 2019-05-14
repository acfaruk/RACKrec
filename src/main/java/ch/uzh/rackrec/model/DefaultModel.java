package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.provider.SQLiteProvider;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;
import com.google.inject.Inject;
import sun.util.logging.PlatformLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultModel extends Model {

    @Inject
    public DefaultModel(Properties properties, ModelGenerator modelGenerator, Logger logger, SQLiteProvider sqLiteProvider) {
        super(properties, modelGenerator, logger, sqLiteProvider);
    }

    @Override
    public List<KAC> getKAC(List<String> keywords, Integer delta) {
        List<KAC> KACs = new ArrayList<>();
        for (String keyword :keywords) {
            try {
                KACs.add(sqLiteProvider.getTopKAPIForToken(delta, keyword));
            }
            catch (SQLException e){
                this.logger.log(Level.FINE,e.getMessage());
            }

        }
        return KACs;
    }

    @Override
    public List<KKC> getKKC(List<KAC> kac, Double similarity) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getContext(String keyword) {
        return new ArrayList<>();
    }

}
