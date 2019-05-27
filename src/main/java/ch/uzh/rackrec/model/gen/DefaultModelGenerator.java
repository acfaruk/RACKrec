package ch.uzh.rackrec.model.gen;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.gen.nlp.ILemmatizer;
import ch.uzh.rackrec.model.gen.nlp.IdentifierLemmatizer;
import ch.uzh.rackrec.model.gen.visitor.ApiReferenceVisitor;
import ch.uzh.rackrec.model.gen.visitor.TokenVisitor;
import ch.uzh.rackrec.model.provider.InvalidModelEntryException;
import ch.uzh.rackrec.model.provider.ModelEntry;
import com.google.inject.Inject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultModelGenerator extends ModelGenerator {

    private ArrayList<ModelEntry> modelEntries = new ArrayList<ModelEntry>();
    private final ILemmatizer lemmatizer;
    private final List<String> apisToCheck;

    @Inject
    public DefaultModelGenerator(ILemmatizer lemmatizer, Properties properties, KaveDataSet kaveDataSet, Logger logger) {
        super(properties, kaveDataSet, logger);
        this.lemmatizer = lemmatizer.enableDuplicateRemoval().enableStopWordRemoval();

        if (properties.getProperty("apis") == null){
            if (Boolean.parseBoolean(properties.getProperty("generate-model")))
                logger.log(Level.INFO, "There are no api's specified for modelgeneration! Checking all apis now.");
            this.apisToCheck = null;
        }else{
            this.apisToCheck = Arrays.asList(properties.getProperty("apis").split(","));
        }

    }

    @Override
    public Iterable<ModelEntry> getModelEntries() {
        return new Iterable<ModelEntry>() {
            @Override
            public Iterator<ModelEntry> iterator() {
                return new Iterator<ModelEntry>() {
                    Iterator<Context> contextIterator = kaveDataSet.getContextData().iterator();

                    @Override
                    public boolean hasNext() {
                        return contextIterator.hasNext();
                    }

                    @Override
                    public ModelEntry next() {

                        ModelEntry result = null;

                        while(result == null && contextIterator.hasNext()){
                            Context context = contextIterator.next();
                            List<String> tokens = new ArrayList<>();
                            List<String> apiReferences = new ArrayList<>();
                            ITypeName enclosingContextType = context.getSST().getEnclosingType();

                            context.getSST().accept(new ApiReferenceVisitor(lemmatizer, apisToCheck), apiReferences);
                            context.getSST().accept(new TokenVisitor(lemmatizer), tokens);


                            try{
                                while (apiReferences.remove("???"));
                                while (apiReferences.remove(".ctor"));

                                result = new ModelEntry(tokens, apiReferences, enclosingContextType);
                            }catch(InvalidModelEntryException ex){
                                //swallow
                                logger.log(Level.FINEST, "Invalid Model Entry");
                                result = null;
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }


}