package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;
import com.google.inject.Inject;

import java.util.ArrayList;
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
        List<KAC> kacList = new ArrayList<KAC>();
        return kacList;
    }

    @Override
    public List<KKC> getKKC(List<KAC> kac, Double similarity) {
        List<KKC> kkclist = new ArrayList<KKC>();
        return kkclist;
    }

    @Override
    public List<String> getContext(String keyword) {
        List<String> context = new ArrayList<String>();
        return context;
    }

}
