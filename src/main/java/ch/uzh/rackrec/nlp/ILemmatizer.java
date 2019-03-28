package ch.uzh.rackrec.nlp;

import java.util.List;

public interface ILemmatizer {
	List<String> lemmatize(String s);
	ILemmatizer enableStopWordRemoval();
	ILemmatizer enableDuplicateRemoval();
	ILemmatizer enableCautiosStopWordRemoval();
}
