package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;

import java.util.*;


public class KAC {
    private String keyword;
    private Map<Integer, IName> rankToApi;
    KAC(String keyword, Map<Integer, IName> freqToApi){
        this.keyword = keyword;
        this.rankToApi = freqToApi;
    }

    public String getKeyword() {
        return keyword;
    }

    public Map<Integer, IName> getRankToApi() {
        return rankToApi;
    }

    public Map<IName,Double> getKacScore(){
        Map<IName, Double> kacScore = new HashMap<>();
        Map<Integer,IName> rankToApi = this.getRankToApi();
        int size = rankToApi.size();
        Set<Integer> keyset = rankToApi.keySet();
        ArrayList<Integer> keylist = new ArrayList(keyset);
        Collections.sort(keylist);
        for(int i = 0; i < size;i++){
            IName api = rankToApi.get(keylist.get(i));
            double score =1.0- keylist.get(i).doubleValue() / size;
            kacScore.put(api,score);
        }
        return kacScore;
    }
}