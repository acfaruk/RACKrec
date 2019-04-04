package ch.uzh.rackrec.model.gen.nlp;
import java.util.List;

public interface IStopWordProvider {
    public boolean isStopWord(String word);
    public List<String> getStopWords();
}
