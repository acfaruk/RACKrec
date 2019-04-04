package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;
import java.util.Map;


public class KAC {
    private String keyword;
    private Map<Integer, IName> freqToApi;
    KAC(String keyword, Map<Integer, IName> freqToApi){
        this.keyword = keyword;
        this.freqToApi = freqToApi;
    }

    String getKeyword() {
        return keyword;
    }

    Map<Integer, IName> getFreqToApi() {
        return freqToApi;
    }
}
