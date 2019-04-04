package ch.uzh.rackrec.rec.config;

import ch.uzh.rackrec.data.ContextKaveDataSet;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.DefaultModel;
import ch.uzh.rackrec.model.Model;
import ch.uzh.rackrec.model.gen.DefaultModelGenerator;
import ch.uzh.rackrec.model.gen.ModelGenerator;

import java.util.Properties;

public class KaveContextModule extends AbstractModule {

    public KaveContextModule(Properties properties) {
        super(properties);
    }

    @Override
    protected void configure() {
        bind(Model.class).to(DefaultModel.class);
        bind(ModelGenerator.class).to(DefaultModelGenerator.class);
        bind(KaveDataSet.class).to(ContextKaveDataSet.class);

        bind(Properties.class).toInstance(properties);
    }
}
