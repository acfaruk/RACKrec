package ch.uzh.rackrec.nlp;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ModifiedStopWordProviderTest {
    IStopWordProvider sut;
    String validStopWord;
    String invalidStopWord;
    String emptyString;

    @Before
    public void initialize(){
        validStopWord = "the";
        invalidStopWord = "from";
        emptyString = "";

        sut = new ModifiedStopwordProvider();
    }

    @Test
    public void getAllStopWords(){
        List<String> allStopWords = sut.getStopWords();
        boolean providesWords = allStopWords.size() > 0;
        assertTrue(providesWords);
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
