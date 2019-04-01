package ch.uzh.rackrec.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.process.*;

public class IdentifierLemmatizer implements ILemmatizer {
    private boolean removeStopWords;
    private boolean removeDuplicates;

    private IStopWordProvider stopWordProvider;

    private Predicate<String> isNotAStopWord = p-> !stopWordProvider.isStopWord(p);

    @Inject
    public IdentifierLemmatizer (IStopWordProvider stopWordProvider) {
        this.stopWordProvider = stopWordProvider;
        this.removeStopWords = false;
        this.removeDuplicates = false;
    }
    public IdentifierLemmatizer enableStopWordRemoval () {
        this.removeStopWords = true;
        return this;
    }
    public IdentifierLemmatizer enableDuplicateRemoval () {
        this.removeDuplicates = true;
        return this;
    }
    public IdentifierLemmatizer enableCautiosStopWordRemoval () {
        this.stopWordProvider = new CautiosStopWordProvider();
        return this;
    }

    @Override
    public List<String> lemmatize(String identifier) {
        String paddedIdentifier = tokenizeSentence(identifier);
        List <String> words = new ArrayList<>(Arrays.asList(paddedIdentifier.split(" ")));

        words = words.stream()
                .map(word -> word.toLowerCase())
                .collect(Collectors.toList());

        if(this.removeStopWords) {
            words = words.stream()
                    .filter(this.isNotAStopWord)
                    .collect(Collectors.toList());
        }

        Sentence sent = new Sentence(words);
        List<String> lemmas = new ArrayList<String>(sent.lemmas());

        if (this.removeDuplicates) {
            Set<String> prunedLemmas = new LinkedHashSet<>(lemmas);
            lemmas.clear();
            lemmas.addAll(prunedLemmas);
        }
        return lemmas;
    }

    public String tokenizeSentence(String s) {
        return s.replaceAll(
            String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"), " "
        );
    }
}