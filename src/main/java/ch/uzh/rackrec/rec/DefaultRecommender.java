package ch.uzh.rackrec.rec;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.rec.config.AbstractModule;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

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

    public Map<MethodName,Double> getRackRecomendations(List<String> keywords){
        Map<MethodName,Double> apiWithScore = new HashMap<>();
        List<KAC> kacList = model.getKAC(keywords, Integer.getInteger(module.getProperties().getProperty("delta")));
        for (KAC kac :kacList){
            for (Map.Entry<MethodName, Double> entry : kac.getKacScore().entrySet()){
                if (apiWithScore.containsKey(entry.getKey())){
                    apiWithScore.replace(entry.getKey(),entry.getValue()+apiWithScore.get(entry.getKey()));
                }
                else {
                    apiWithScore.put(entry.getKey(),entry.getValue());
                }
            }
        }
        //TO-DOO Implement KKC
        return apiWithScore;

    }

}
