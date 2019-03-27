package ch.uzh.rackrec.rec;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

public interface IRecommender<TQuery> {

    /**
     * use the recommender-specific query format to query proposals
     *
     * @param query
     *            the query in a format specfic to the recommender
     * @return a sorted set of the proposed methods plus probability
     */
    Set<Pair<IMemberName, Double>> query(TQuery query);

    /**
     * query proposals by providing a context
     *
     * @param ctx
     *            the query as a Context
     * @return a sorted set of the proposed methods plus probability
     */
    Set<Pair<IMemberName, Double>> query(Context ctx);

    /**
     * query proposals by providing a context and the proposals given by the IDE
     *
     * @param ctx
     *            the query as a Context
     * @param ideProposals
     *            the proposal given by the IDE
     * @return a sorted set of the proposed methods plus probability
     */
    Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals);

    /**
     * Request the size of the model that was last used by the recommender.
     *
     * @return model size in bytes
     */
    int getLastModelSize();
}
