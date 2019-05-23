package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class KACTest {
    KAC sut;
    String keyword;
    Map<Integer, MethodName> freqToApi;


    @Before
    public void initialize(){
        keyword = "Test";
        freqToApi = new HashMap<>();
        freqToApi.put(300,new MethodName("test"));
        freqToApi.put(255,new MethodName("homo"));
        freqToApi.put(60,new MethodName("wayne"));
        freqToApi.put(80,new MethodName("okey"));
        freqToApi.put(3465,new MethodName("mscorlib"));


    }
    @Test
    public void test(){
        sut = new KAC(keyword, freqToApi);
        boolean keywordIsTrue = sut.getKeyword().equals("Test");
        boolean mapIsTrue = sut.getRankToApi() == freqToApi;
        assertTrue(keywordIsTrue&&mapIsTrue);
    }
    @Test
    public void getScoreTest(){
        sut = new KAC(keyword,freqToApi);
        Map<MethodName,Double> kacScore = sut.getKacScore();
        assertTrue(kacScore.size() == 5);
        double msScore = kacScore.get(new MethodName("mscorlib"));
        assertTrue(msScore == 1);
        double wayneScore = kacScore.get(new MethodName("wayne"));
        assertTrue(wayneScore == 1.0- ( 4 / 5.0));
    }
}
