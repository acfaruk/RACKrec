package ch.uzh.rackrec.model.view;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;

import static junit.framework.TestCase.assertTrue;

public class KKCTest {
    KKC sut;
    String keyword1;
    String keyword2;
    Map.Entry<String, String> keywordpair;
    List<MethodName> apis;
    Double score;


    @Before
    public void initialize(){
        keyword1 = "Test";
        keyword2 = "Passed";
        keywordpair = new AbstractMap.SimpleEntry<>(keyword1,keyword2);
        apis = new ArrayList<>();
        apis.add(new MethodName("[class.a].m()"));
        score = 0.86547;
    }
    @Test
    public void test(){
        sut = new KKC(keywordpair,apis,score);
        boolean keywordsAreTrue = sut.getKeywordPair() == keywordpair;
        boolean apiListIsTrue = sut.getApis() == apis;
        boolean scoreIsTrue = sut.getKkcScore() == 0.86547;
        assertTrue(keywordsAreTrue);
        assertTrue(apiListIsTrue);
        assertTrue(scoreIsTrue);
    }
}
