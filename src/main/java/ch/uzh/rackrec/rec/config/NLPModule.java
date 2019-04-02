package ch.uzh.rackrec.rec.config;

import ch.uzh.rackrec.nlp.IStopWordProvider;
import ch.uzh.rackrec.nlp.NLTKStopWordProvider;

public class NLPModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IStopWordProvider.class).to(NLTKStopWordProvider.class);
    }

    public static NLPModule create(){
        return new NLPModule();
    }
}
