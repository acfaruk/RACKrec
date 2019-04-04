package ch.uzh.rackrec.model.gen;

import ch.uzh.rackrec.data.KaveDataSet;
import com.google.inject.Inject;

import java.util.Properties;

public class DefaultModelGenerator extends ModelGenerator {

    @Inject
    public DefaultModelGenerator(Properties properties, KaveDataSet kaveDataSet) {
        super(properties, kaveDataSet);
    }
}
