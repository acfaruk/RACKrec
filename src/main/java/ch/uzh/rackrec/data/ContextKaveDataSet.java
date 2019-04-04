package ch.uzh.rackrec.data;

import com.google.inject.Inject;

import java.util.Properties;

public class ContextKaveDataSet extends KaveDataSet {

    @Inject
    public ContextKaveDataSet(Properties properties){
        super(properties);
    }
}
