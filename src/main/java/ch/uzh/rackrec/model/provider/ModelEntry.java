package ch.uzh.rackrec.model.provider;

import java.util.List;

import cc.kave.commons.model.naming.types.ITypeName;

public class ModelEntry {
    // The tokens that were mined from a Context (processed instance variables, properties, Method Variables)
    List<?> tokens;

    // The list of APIs references in the Context (API class or method reference on API class)
    List<?> APIReferences;

    // The enclosing type of the Context (Class name)
    ITypeName enclosingContextType;
}
