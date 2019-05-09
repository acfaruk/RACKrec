package ch.uzh.rackrec.model.gen;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.gen.nlp.IdentifierLemmatizer;
import ch.uzh.rackrec.model.gen.visitor.ApiReferenceVisitor;
import ch.uzh.rackrec.model.gen.visitor.TokenVisitor;
import ch.uzh.rackrec.model.provider.ModelEntry;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DefaultModelGenerator extends ModelGenerator {

    private ArrayList<ModelEntry> modelEntries = new ArrayList<ModelEntry>();
    private final IdentifierLemmatizer lemmatizer;

    @Inject
    public DefaultModelGenerator(IdentifierLemmatizer lemmatizer, Properties properties, KaveDataSet kaveDataSet, Logger logger) {
        super(properties, kaveDataSet, logger);
        this.lemmatizer = lemmatizer.enableDuplicateRemoval();
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

            context.getSST().accept(new ApiReferenceVisitor(lemmatizer, properties.getProperty("apis").split(",")), apiReferences);
            context.getSST().accept(new TokenVisitor(lemmatizer), tokens);

            modelEntries.add(new ModelEntry(tokens, apiReferences, enclosingContextType));
        }
    }


}