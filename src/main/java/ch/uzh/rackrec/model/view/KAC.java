package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;
import java.util.HashMap;
import java.util.Map;


public class KAC {
    protected String keyword;
    protected Map<Integer, IName> freqToApi;
    public KAC(String keyword, Map<Integer,IName> freqToApi){
        this.keyword = keyword;
        this.freqToApi = freqToApi;
    }

    public String getKeyword() {
        return keyword;
    }

    public  Map<Integer, IName> getFreqToApi() {
        return freqToApi;
    }
}
