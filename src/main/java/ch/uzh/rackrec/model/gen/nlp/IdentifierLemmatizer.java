package ch.uzh.rackrec.model.gen.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import edu.stanford.nlp.simple.Sentence;

public class IdentifierLemmatizer implements ILemmatizer {
    private boolean removeStopWords;
    private boolean removeDuplicates;

    private Logger logger;
    
    private IStopWordProvider stopWordProvider;


    private Predicate<String> isNotAStopWord = p-> !stopWordProvider.isStopWord(p);
    private Predicate<String> isNotNoise = p-> !Pattern.matches("\\$\\d+",p);

    @Inject
    public IdentifierLemmatizer (IStopWordProvider stopWordProvider, Logger logger){
        this.stopWordProvider = stopWordProvider;
        this.removeStopWords = false;
        this.removeDuplicates = false;
        this.logger = logger;
    }
    public IdentifierLemmatizer enableStopWordRemoval(){
        this.removeStopWords = true;
        return this;
    }
    public IdentifierLemmatizer enableDuplicateRemoval(){
        this.removeDuplicates = true;
        return this;
    }

    @Override
    public List<String> lemmatize(String identifier){
    	logger.log(Level.FINEST, "Lemmatizing: {0}", identifier);
        String paddedIdentifier = tokenizeSentence(identifier);
        List <String> words = new ArrayList<>(Arrays.asList(paddedIdentifier.split(" ")));

        words = words.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        if(this.removeStopWords) {
            words = words.stream()
                    .filter(this.isNotAStopWord)
                    .filter(this.isNotNoise)
                    .collect(Collectors.toList());
        }
        if (words.isEmpty()){
            return new ArrayList<String>();
        }

        Sentence sent = new Sentence(words);
        List<String> lemmas = new ArrayList<>(sent.lemmas());

        if (this.removeDuplicates) {
            Set<String> prunedLemmas = new LinkedHashSet<>(lemmas);
            lemmas.clear();
            lemmas.addAll(prunedLemmas);
        }
        return lemmas;
    }

    private String tokenizeSentence(String s) {
        return s.replaceAll(
            String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"), " "
        );
    }
}