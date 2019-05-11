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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.declarations.*;
import cc.kave.commons.model.ssts.expressions.assignable.*;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.*;
import cc.kave.commons.model.ssts.statements.*;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import ch.uzh.rackrec.model.gen.nlp.IdentifierLemmatizer;

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
public class ApiReferenceVisitor extends AbstractTraversingNodeVisitor<List<String>, Void> {

    private final IdentifierLemmatizer lemmatizer;
    private final List<String> apis;

    public ApiReferenceVisitor(IdentifierLemmatizer lemmatizer, String[] apis) {
        this.lemmatizer = lemmatizer;
        this.apis = Arrays.asList(apis);
    }

    @Override
    public Void visit(IFieldDeclaration stmt, List<String> strings) {
        String assembly = stmt.getName().getValueType().getAssembly().getName();
        if (apis.contains(assembly) == false)
            return super.visit(stmt, strings);

        strings.add(stmt.getName().getValueType().getName());
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IVariableDeclaration stmt, List<String> strings) {
        String assembly = stmt.getType().getAssembly().getName();
        if (apis.contains(assembly) == false)
            return super.visit(stmt, strings);

        strings.add(stmt.getType().getName());
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IInvocationExpression expr, List<String> strings) {
        String assembly = expr.getMethodName().getDeclaringType().getAssembly().getName();
        if (apis.contains(assembly) == false)
            return super.visit(expr, strings);

        strings.add(expr.getMethodName().getName());
        return super.visit(expr, strings);
    }
}
