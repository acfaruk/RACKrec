package ch.uzh.rackrec.rec.config;

import ch.uzh.rackrec.data.ContextKaveDataSet;
import ch.uzh.rackrec.data.EventsKaveDataSet;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.DefaultModel;
import ch.uzh.rackrec.model.Model;
import ch.uzh.rackrec.model.gen.DefaultModelGenerator;
import ch.uzh.rackrec.model.gen.ModelGenerator;

public class DefaultModule extends AbstractModule {

    private KaveDataConfig kaveDataConfig;

    public DefaultModule(){
        super();
    }

    public static DefaultModule create(){
        return new DefaultModule();
    }

    public DefaultModule setKaveData(KaveDataConfig kaveDataConfig){
        this.kaveDataConfig = kaveDataConfig;
        return this;
    }

    @Override
    protected void configure() {
        bind(Model.class).to(DefaultModel.class);
        bind(ModelGenerator.class).to(DefaultModelGenerator.class);

        switch (kaveDataConfig){
            case CONTEXT:
                bind(KaveDataSet.class).to(ContextKaveDataSet.class);
                break;
            case EVENTS:
                bind(KaveDataSet.class).to(EventsKaveDataSet.class);
                break;
        }
    }
}
