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
        freqToApi = new HashMap<Integer, MethodName>();
        freqToApi.put(300,new MethodName("mscorlib.method1"));
        freqToApi.put(334,new MethodName("mscorlib.method2"));


    }
    @Test
    public void test(){
        sut = new KAC(keyword, freqToApi);
        boolean keywordIsTrue = sut.getKeyword().equals("Test");
        boolean mapIsTrue = sut.getFreqToApi() == freqToApi;
        assertTrue(keywordIsTrue&&mapIsTrue);
    }
}
