package ch.uzh.rackrec.nlp;

import static org.junit.Assert.assertTrue;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StopWordProviderTest {
    IStopWordProvider sut;
    String validStopWord;
    String invalidStopWord;
    String emptyString;

    @Before
    public void initialize(){
        validStopWord = "the";
        invalidStopWord = "word";
        emptyString = "";

        sut = new NLTKStopWordProvider();
    }

    @Test
    public void getAllStopWords(){
        List<String> allStopWords = sut.getStopWords();
        assertTrue(allStopWords.size() > 0);
    }
    
    @Test
    public void isAStopWord(){
        boolean isStopWord = sut.isStopWord(validStopWord);
        assertTrue(isStopWord);
    }

    @Test
    public void isNotAStopWord(){
        boolean isNotStopWord = !sut.isStopWord(invalidStopWord);
        assertTrue(isNotStopWord);
    }

    @Test
    public void testEmptyString(){
        boolean isNotStopWord = !sut.isStopWord(emptyString);
        assertTrue(isNotStopWord);
    }
}
