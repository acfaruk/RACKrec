package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.provider.IDatabaseProvider;
import ch.uzh.rackrec.model.provider.ModelEntry;
import ch.uzh.rackrec.model.provider.SQLiteProvider;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;
import com.google.inject.Inject;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultModel extends Model {

    @Inject
    public DefaultModel(Properties properties, ModelGenerator modelGenerator, Logger logger, IDatabaseProvider databaseProvider) {
        super(properties, modelGenerator, logger, databaseProvider);


        if (!databaseProvider.isFileReady()) {
            try {
                databaseProvider.prepareSchemas();
                for (ModelEntry m : modelGenerator.getModelEntries()) {
                    try {
                        databaseProvider.saveMinedContext(m);
                    } catch (SQLException e) {
                        logger.log(Level.FINE, "Context not saved" + e.getMessage());
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
                System.exit(-1);
            }
        }

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
    public List<KKC> getKKC(List<KAC> kacs, Double similarity) {
        List<KKC> KKCs = new ArrayList<>();
        for (KAC kac1 : kacs){
            for (KAC kac2 : kacs){
                if (kac1 != kac2){
                    try {
                        KKC kkc = sqLiteProvider.getKKCForKeywords(new AbstractMap.SimpleEntry<>(kac1.getKeyword(),kac2.getKeyword()));
                        if (kkc.getKkcScore() >= similarity) {
                            KKCs.add(kkc);
                        }
                    }
                    catch (SQLException e){
                        this.logger.log(Level.FINE,e.getMessage());
                    }
                }
            }
        }
        return KKCs;
    }


}
