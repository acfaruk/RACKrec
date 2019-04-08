package ch.uzh.rackrec.data;

import com.google.inject.Inject;

import java.util.Properties;
import java.util.logging.Logger;

public class ContextKaveDataSet extends KaveDataSet {

    @Inject
    public ContextKaveDataSet(Properties properties, Logger logger){
        super(properties, logger);
    }
}
