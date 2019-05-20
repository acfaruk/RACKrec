package ch.uzh.rackrec.rec;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import ch.uzh.rackrec.model.gen.visitor.TokenVisitor;
import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;
import ch.uzh.rackrec.rec.config.AbstractModule;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DefaultRecommender extends AbstractRecommender {

    private TokenVisitor tokenParser;
    public DefaultRecommender(AbstractModule module) {
        super(module);
        this.tokenParser = new TokenVisitor(this.lemmatizer.enableDuplicateRemoval().enableStopWordRemoval());
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(IQuery iQuery) {
        return null;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx) {
        List<String> tokensInContext = this.getTokensFromContext(ctx);
        Map<MethodName, Double> result = this.getRackRecomendations(tokensInContext);
        Set<Pair<IMemberName, Double>> collector = this.collectPairSet(result);
        return collector;
    }

    @Override
    public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
        return null;
    }

    @Override
    public int getLastModelSize() {
        return 0;
    }
    
    public Set<Pair<IMemberName, Double>> collectPairSet(Map<MethodName, Double> inputMap) {
        Set<Pair<IMemberName, Double>> collector = new HashSet<>();
        Iterator<Entry<MethodName, Double>> it = inputMap.entrySet().iterator();
        while(it.hasNext()) {
            Entry<MethodName, Double> entry = it.next();
            IMemberName name = entry.getKey();
            Double score = entry.getValue();
            ImmutablePair <IMemberName, Double> pair = new ImmutablePair<IMemberName, Double>(name, score);
            collector.add(pair);
        }
        return collector;
    }

    public List<String> getTokensFromContext(Context ctx) {
        List<String> tokenCollector = new ArrayList<>();
        ctx.getSST().accept(this.tokenParser, tokenCollector);

        return tokenCollector;
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
        List<KKC> kkcList = model.getKKC(kacList,Double.parseDouble(module.getProperties().getProperty("lambda")));
        for (KKC kkc :kkcList){
            for (MethodName name :kkc.getApis()){
                if (apiWithScore.containsKey(name)){
                    apiWithScore.replace(name,kkc.getKkcScore()+apiWithScore.get(name));
                }
            }
        }
        
        Map<MethodName, Double> sortedMap = apiWithScore.entrySet().stream()
        .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
        (e1, e2) -> e1, LinkedHashMap::new));
        
        return sortedMap;

    }

}
