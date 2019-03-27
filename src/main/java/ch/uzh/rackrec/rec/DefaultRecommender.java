package ch.uzh.rackrec.rec;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import ch.uzh.rackrec.rec.config.AbstractModule;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

public class DefaultRecommender extends AbstractRecommender {


    public DefaultRecommender(AbstractModule module) {
        super(module);
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(IQuery iQuery) {
        return null;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx) {
        return null;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
        return null;
    }

    @Override
    public int getLastModelSize() {
        return 0;
    }
}
