package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;

import java.util.*;


public class KAC {
    private String keyword;
    private Map<Integer, MethodName> rankToApi;
    public KAC(String keyword, Map<Integer, MethodName> freqToApi){
        this.keyword = keyword;
        this.rankToApi = freqToApi;
    }

    public String getKeyword() {
        return keyword;
    }

    public Map<Integer, MethodName> getRankToApi() {
        return rankToApi;
    }

    public Map<MethodName,Double> getKacScore(){
        Map<MethodName, Double> kacScore = new HashMap<>();
        int size = rankToApi.size();
        Set<Integer> keyset = rankToApi.keySet();
        ArrayList<Integer> keylist = new ArrayList(keyset);
        Collections.sort(keylist);
        for(int i = 0; i < size;i++){
            MethodName api = rankToApi.get(keylist.get(i));
            double score =1.0- keylist.get(i).doubleValue() / size;
            kacScore.put(api,score);
        }
        return kacScore;
    }
}