package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;
import javafx.util.Pair;

import java.util.List;

public class KKC {
    protected Pair<String,String> keywordPair;
    protected List<IName> apis;
    protected Double score;

    public KKC (Pair<String,String> keywordPair, List<IName> apis, Double score){
        this.keywordPair = keywordPair;
        this.apis = apis;
        this.score = score;
    }

    public Pair<String, String> getKeywordPair() {
        return keywordPair;
    }

    public List<IName> getApis() {
        return apis;
    }

    public Double getScore() {
        return score;
    }
}
