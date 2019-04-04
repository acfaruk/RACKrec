package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;

import java.util.List;
import java.util.Map;

public class KKC {
    protected Map.Entry<String,String> keywordPair;
    protected List<IName> apis;
    protected Double score;

    public KKC (Map.Entry<String,String> keywordPair, List<IName> apis, Double score){
        this.keywordPair = keywordPair;
        this.apis = apis;
        this.score = score;
    }

    public Map.Entry<String, String> getKeywordPair() {
        return keywordPair;
    }

    public List<IName> getApis() {
        return apis;
    }

    public Double getScore() {
        return score;
    }
}
