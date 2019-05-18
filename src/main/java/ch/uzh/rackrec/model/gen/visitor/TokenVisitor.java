package ch.uzh.rackrec.model.gen.visitor;

/**
 * Copyright 2016 Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import cc.kave.commons.model.ssts.declarations.*;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.*;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import ch.uzh.rackrec.model.gen.nlp.ILemmatizer;
import ch.uzh.rackrec.model.gen.nlp.IdentifierLemmatizer;
import com.google.inject.Inject;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * this visitor is a simple example of how to traverse an SST with a visitor. It
 * simply collects all types that it finds in the SST.
 *
 * This extends one of the two abstract implementations of the
 * {@link ISSTNodeVisitor} interface. The {@link AbstractTraversingNodeVisitor}
 * automatically traverses the whole syntax tree and you can selectively
 * override required methods.
 *
 * The other implementation is {@link AbstractThrowingNodeVisitor}, which forces
 * you overwrite all methods to ensure you did not forget anything.
 */
public class TokenVisitor extends AbstractTraversingNodeVisitor<List<String>, Void> {

    private final ILemmatizer lemmatizer;

    public TokenVisitor(ILemmatizer lemmatizer){
        this.lemmatizer = lemmatizer;
    }

    @Override
    public Void visit(IFieldDeclaration stmt, List<String> strings) {
        String name = stmt.getName().getName();
        List<String> names = lemmatizer.lemmatize(name);

        strings.addAll(names);
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IMethodDeclaration decl, List<String> strings) {
        String name = decl.getName().getName();
        List<String> names = lemmatizer.lemmatize(name);

        strings.addAll(names);
        return super.visit(decl, strings);
    }

    // TODO
//    @Override
//    public Void visit(IPropertyDeclaration decl, List<String> strings) {
//        String name = decl.getName().getIdentifier();
//        List<String> names = lemmatizer.lemmatize(name);
//
//        strings.addAll(names);
//        return super.visit(decl, strings);
//    }

    @Override
    public Void visit(IVariableDeclaration stmt, List<String> strings) {
        String name = stmt.getReference().getIdentifier();
        List<String> names = lemmatizer.lemmatize(name);

        strings.addAll(names);
        return super.visit(stmt, strings);
    }

}
