package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;

import java.util.Map;


public class KAC {
    private String keyword;
    private Map<Integer, MethodName> freqToApi;
    public KAC(String keyword, Map<Integer, MethodName> kacMap){
        this.keyword = keyword;
        this.freqToApi = kacMap;
    }

    public String getKeyword() {
        return keyword;
    }

    public Map<Integer, MethodName> getFreqToApi() {
        return freqToApi;
    }
}
