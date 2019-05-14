package ch.uzh.rackrec.rec.config;

import ch.uzh.rackrec.data.ContextKaveDataSet;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.DefaultModel;
import ch.uzh.rackrec.model.Model;
import ch.uzh.rackrec.model.gen.DefaultModelGenerator;
import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.gen.nlp.ILemmatizer;
import ch.uzh.rackrec.model.gen.nlp.IStopWordProvider;
import ch.uzh.rackrec.model.gen.nlp.IdentifierLemmatizer;
import ch.uzh.rackrec.model.gen.nlp.NLTKStopWordProvider;
import ch.uzh.rackrec.model.provider.SQLiteProvider;

import java.util.Properties;

public class KaveContextModule extends AbstractModule {

    public KaveContextModule(Properties properties) {
        super(properties);
        checkProperties();
    }

    private void checkProperties() {
        if (properties.getProperty("base-path") == null)
            throw new RuntimeException("The base path was not set. Please set a base path in the properties!");

        if (properties.getProperty("model-path") == null)
            throw new RuntimeException("The model path was not set. Please set a model path in the properties!");
    }

    @Override
    protected void configure() {
        bind(Model.class).to(DefaultModel.class);
        bind(ModelGenerator.class).to(DefaultModelGenerator.class);
        bind(KaveDataSet.class).to(ContextKaveDataSet.class);
        bind(IStopWordProvider.class).to(NLTKStopWordProvider.class);
        bind(ILemmatizer.class).to(IdentifierLemmatizer.class);
        bind(Properties.class).toInstance(properties);
        bind(SQLiteProvider.class).to(SQLiteProvider.class);
    }
}
