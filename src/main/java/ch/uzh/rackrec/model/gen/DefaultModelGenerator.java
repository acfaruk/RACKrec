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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultModelGenerator extends ModelGenerator {

    private ArrayList<ModelEntry> modelEntries = new ArrayList<ModelEntry>();
    private final ILemmatizer lemmatizer;
    private final List<String> apisToCheck;

    @Inject
    public DefaultModelGenerator(ILemmatizer lemmatizer, Properties properties, KaveDataSet kaveDataSet, Logger logger) {
        super(properties, kaveDataSet, logger);
        this.lemmatizer = lemmatizer.enableDuplicateRemoval();

        if (properties.getProperty("apis") == null){
            logger.log(Level.INFO, "There are no api's specified for modelgeneration! Checking all apis now.");
            this.apisToCheck = null;
        }else{
            this.apisToCheck = Arrays.asList(properties.getProperty("apis").split(","));
        }

        calculateModelEntries();
    }

    @Override
    public Iterable<ModelEntry> getModelEntries() {
        return modelEntries;
    }

    private void calculateModelEntries() {
        for (Context context : kaveDataSet.getContextData()){
            List<String> tokens = new ArrayList<>();
            List<String> apiReferences = new ArrayList<>();
            ITypeName enclosingContextType = context.getSST().getEnclosingType();

            context.getSST().accept(new ApiReferenceVisitor(lemmatizer, apisToCheck), apiReferences);
            context.getSST().accept(new TokenVisitor(lemmatizer), tokens);

            try{
                modelEntries.add(new ModelEntry(tokens, apiReferences, enclosingContextType));
            }catch(InvalidModelEntryException ex){
                //swallow
                logger.log(Level.FINEST, "Invalid Model Entry");
            }
        }
    }


}