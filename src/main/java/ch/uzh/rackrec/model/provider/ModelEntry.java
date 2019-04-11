package ch.uzh.rackrec.model.provider;

import java.util.List;

import cc.kave.commons.model.naming.types.ITypeName;

public class ModelEntry {
    // The tokens that were mined from a Context (processed instance variables, properties, Method Variables)
    List<String> tokens;

    // The list of APIs references in the Context (API class or method reference on API class)
    List<String> apiReferences;

    // The enclosing type of the Context (Class name)
    ITypeName enclosingContextType;
    public ModelEntry(List<String> tokens, List<String> apiReferences, ITypeName enclosingType) throws Exception {
       	boolean noTokensOrAPIs = tokens.size() == 0 || apiReferences.size() == 0;
       	boolean contextUndefined = enclosingType == null;
       	boolean invalidMenuEntry = noTokensOrAPIs || contextUndefined;

        if(invalidMenuEntry) {
            throw new Exception();
        }
        this.tokens = tokens;
        this.apiReferences = apiReferences;
        this.enclosingContextType = enclosingType;
    }
}