package ch.uzh.rackrec.rec;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import ch.uzh.rackrec.model.Model;
import ch.uzh.rackrec.rec.config.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

public abstract class AbstractRecommender implements IRecommender<IQuery> {

    protected AbstractModule module;
    protected Injector injector;
    protected Model model;

    public AbstractRecommender(AbstractModule module){
        this.module = module;
        this.injector = Guice.createInjector(module);
        this.model = injector.getInstance(Model.class);

    }

    @Override
    public abstract Set<Pair<IMemberName, Double>> query(IQuery iQuery);

    @Override
    public abstract Set<Pair<IMemberName, Double>> query(Context ctx);

    @Override
    public abstract Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals);

    @Override
    public abstract int getLastModelSize();
}
