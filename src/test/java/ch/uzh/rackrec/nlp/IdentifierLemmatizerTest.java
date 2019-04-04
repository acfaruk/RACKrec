package ch.uzh.rackrec.nlp;

import org.junit.Before;
import org.junit.Test;

import ch.uzh.rackrec.model.gen.nlp.ILemmatizer;
import ch.uzh.rackrec.model.gen.nlp.IStopWordProvider;
import ch.uzh.rackrec.model.gen.nlp.IdentifierLemmatizer;
import ch.uzh.rackrec.model.gen.nlp.NLTKStopWordProvider;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

public class IdentifierLemmatizerTest {
    ILemmatizer sut;
    ILemmatizer sutWithoutStopWord;
    ILemmatizer sutWithoutDuplicates;
    ILemmatizer sutWithoutDuplicatesAndStopWord;
    IStopWordProvider mockedProvider;
    String emptyString;
    String camelCaseIdentifier;

    @Before
    public void initialize(){
        emptyString = "";
        camelCaseIdentifier = "computeHashMockedstopwordStringString";
        mockedProvider = mock(NLTKStopWordProvider.class);
        sut = new IdentifierLemmatizer(mockedProvider).enableStopWordRemoval();
        sutWithoutStopWord = new IdentifierLemmatizer(mockedProvider);
        sutWithoutDuplicates = new IdentifierLemmatizer(mockedProvider)
                                   .enableDuplicateRemoval();
        sutWithoutDuplicatesAndStopWord = new IdentifierLemmatizer(mockedProvider)
                                              .enableDuplicateRemoval()
                                              .enableStopWordRemoval();

        when(mockedProvider.isStopWord("mockedstopword")).thenReturn(true);
        when(mockedProvider.isStopWord("compute")).thenReturn(false);
        when(mockedProvider.isStopWord("hash")).thenReturn(false);
        when(mockedProvider.isStopWord("string")).thenReturn(false);
    }

    @Test
    public void test(){
        List<String> lemmas = sutWithoutStopWord.lemmatize(camelCaseIdentifier);
        boolean hasFiveLemmas = lemmas.size() == 5;
        assertTrue(hasFiveLemmas);
    }

    @Test
    public void testWithStopWordRemoval(){
        List<String> lemmas = sut.lemmatize(camelCaseIdentifier);
        boolean hasFourLemmas = lemmas.size() == 4;
        assertTrue(hasFourLemmas);
    }

    @Test
    public void testWithDuplicateRemoval(){
        List<String> lemmas = sutWithoutDuplicates.lemmatize(camelCaseIdentifier);
        boolean hasFourLemmas = lemmas.size() == 4;
        assertTrue(hasFourLemmas);
    }

    @Test
    public void testWithDuplicateAndStopWordRemoval(){
        List<String> lemmas = sutWithoutDuplicatesAndStopWord.lemmatize(camelCaseIdentifier);
        boolean hasThreeLemmas = lemmas.size() == 3;
        assertTrue(hasThreeLemmas);
    }
}
