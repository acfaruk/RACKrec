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

    public ApiReferenceVisitor(IdentifierLemmatizer lemmatizer, String[] apis) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public Void visit(ISST sst, List<String> strings) {
        return super.visit(sst, strings);
    }

    @Override
    public Void visit(IDelegateDeclaration stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IEventDeclaration stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IFieldDeclaration stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IMethodDeclaration decl, List<String> strings) {
        return super.visit(decl, strings);
    }

    @Override
    public Void visit(IPropertyDeclaration decl, List<String> strings) {
        return super.visit(decl, strings);
    }

    @Override
    public Void visit(IVariableDeclaration stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IAssignment stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IBreakStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IContinueStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IEventSubscriptionStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IExpressionStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IGotoStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(ILabelledStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IReturnStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IThrowStatement stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(IDoLoop block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    protected List<Void> visit(List<IStatement> body, List<String> strings) {
        return super.visit(body, strings);
    }

    @Override
    public Void visit(IForEachLoop block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(IForLoop block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(IIfElseBlock block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(ILockBlock stmt, List<String> strings) {
        return super.visit(stmt, strings);
    }

    @Override
    public Void visit(ISwitchBlock block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(ITryBlock block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(IUncheckedBlock block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(IUnsafeBlock block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(IUsingBlock block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(IWhileLoop block, List<String> strings) {
        return super.visit(block, strings);
    }

    @Override
    public Void visit(ICompletionExpression entity, List<String> strings) {
        return super.visit(entity, strings);
    }

    @Override
    public Void visit(IComposedExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IIfElseExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IInvocationExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(ILambdaExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(ILoopHeaderBlockExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IConstantValueExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(INullExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IReferenceExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(ICastExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IIndexAccessExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(ITypeCheckExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IBinaryExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IUnaryExpression expr, List<String> strings) {
        return super.visit(expr, strings);
    }

    @Override
    public Void visit(IEventReference ref, List<String> strings) {
        return super.visit(ref, strings);
    }

    @Override
    public Void visit(IFieldReference ref, List<String> strings) {
        return super.visit(ref, strings);
    }

    @Override
    public Void visit(IMethodReference ref, List<String> strings) {
        return super.visit(ref, strings);
    }

    @Override
    public Void visit(IPropertyReference ref, List<String> strings) {
        return super.visit(ref, strings);
    }

    @Override
    public Void visit(IVariableReference ref, List<String> strings) {
        return super.visit(ref, strings);
    }

    @Override
    public Void visit(IIndexAccessReference ref, List<String> strings) {
        return super.visit(ref, strings);
    }

    @Override
    public Void visit(IUnknownReference ref, List<String> strings) {
        return super.visit(ref, strings);
    }

    @Override
    public Void visit(IUnknownExpression unknownExpr, List<String> strings) {
        return super.visit(unknownExpr, strings);
    }

    @Override
    public Void visit(IUnknownStatement unknownStmt, List<String> strings) {
        return super.visit(unknownStmt, strings);
    }
}
