package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;

import java.util.List;
import java.util.Map;

public class KKC {
    protected Map.Entry<String,String> keywordPair;
    protected List<MethodName> apis;
    protected Double score;

    public KKC (Map.Entry<String,String> keywordPair, List<MethodName> apis, Double score){
        this.keywordPair = keywordPair;
        this.apis = apis;
        this.score = score;
    }

    public Map.Entry<String, String> getKeywordPair() {
        return keywordPair;
    }

    public List<MethodName> getApis() {
        return apis;
    }

    public Double getKkcScore() {
        return score;
    }
}
