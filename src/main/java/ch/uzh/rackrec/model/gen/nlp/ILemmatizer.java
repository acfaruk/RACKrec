package ch.uzh.rackrec.model.gen.nlp;

import java.util.List;

public interface ILemmatizer {
    List<String> lemmatize(String s);
    ILemmatizer enableStopWordRemoval();
    ILemmatizer enableDuplicateRemoval();
}
