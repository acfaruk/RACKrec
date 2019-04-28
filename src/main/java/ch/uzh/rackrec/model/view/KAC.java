package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;
import java.util.Map;


public class KAC {
    private String keyword;
    private Map<Integer, String> freqToApi;
    public KAC(String keyword, Map<Integer, String> freqToApi){
        this.keyword = keyword;
        this.freqToApi = freqToApi;
    }

    public String getKeyword() {
        return keyword;
    }

    public Map<Integer, String> getFreqToApi() {
        return freqToApi;
    }
}
