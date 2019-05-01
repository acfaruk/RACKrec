package ch.uzh.rackrec.model.gen;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.provider.ModelEntry;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

public class DefaultModelGenerator extends ModelGenerator {

    private ArrayList<ModelEntry> modelEntries = new ArrayList<ModelEntry>();

    @Inject
    public DefaultModelGenerator(Properties properties, KaveDataSet kaveDataSet, Logger logger) {
        super(properties, kaveDataSet, logger);

        calculateModelEntries();
    }

    private void calculateModelEntries() {
        for (Context context : kaveDataSet.getContextData()){

        }
    }


}