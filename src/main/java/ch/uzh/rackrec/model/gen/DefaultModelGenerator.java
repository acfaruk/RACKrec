package ch.uzh.rackrec.model.gen;

import ch.uzh.rackrec.data.KaveDataSet;
import com.google.inject.Inject;

public class DefaultModelGenerator extends ModelGenerator {

    @Inject
    public DefaultModelGenerator(KaveDataSet kaveDataSet) {
        super(kaveDataSet);
    }
}
