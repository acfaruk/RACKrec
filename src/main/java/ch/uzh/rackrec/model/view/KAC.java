package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;
import java.util.HashMap;


public class KAC {
    protected String keyword;
    protected HashMap<Integer, IName> freqToApi;
    public KAC(String keyword, HashMap<Integer,IName> freqToApi){
        this.keyword = keyword;
        this.freqToApi = freqToApi;
    }

    public String getKeyword() {
        return keyword;
    }

    public  HashMap<Integer, IName> getFreqToApi() {
        return freqToApi;
    }
}
