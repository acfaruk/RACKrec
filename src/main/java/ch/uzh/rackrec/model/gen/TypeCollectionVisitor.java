package ch.uzh.rackrec.model.gen;

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

import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
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
public class TypeCollectionVisitor extends AbstractThrowingNodeVisitor<Set<ITypeName>, Void> {

    @Override
    public Void visit(IVariableDeclaration stmt, Set<ITypeName> seenTypes) {
        // for a variable declaration you can access the referenced type
        ITypeName type = stmt.getType();
        // let's put it into the index
        seenTypes.add(type);
        return null;
    }

    @Override
    public Void visit(IMethodDeclaration decl, Set<ITypeName> seenTypes) {
        // access the fully-qualified name of this method
        IMethodName name = decl.getName();

        // store the return type
        seenTypes.add(name.getReturnType());

        // iterate over all parameters of this method
        for (IParameterName param : name.getParameters()) {
            // and store the simple names
            seenTypes.add(param.getValueType());
        }

        // now continue with the traversal, implemented in the base class
        return super.visit(decl, seenTypes);
    }

    //TODO: IMPLEMENT THESE
    @Override
    public Void visit(IBreakStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IContinueStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IExpressionStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IGotoStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(ILabelledStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IReturnStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IThrowStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IDoLoop block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(IForEachLoop block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(IForLoop block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(IIfElseBlock block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(ILockBlock stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(ISwitchBlock block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(ITryBlock block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(IUncheckedBlock block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(IUnsafeBlock block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(IUsingBlock block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(IWhileLoop block, Set<ITypeName> iTypeNames) {
        return super.visit(block, iTypeNames);
    }

    @Override
    public Void visit(ICompletionExpression entity, Set<ITypeName> iTypeNames) {
        return super.visit(entity, iTypeNames);
    }

    @Override
    public Void visit(IComposedExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IIfElseExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IInvocationExpression entity, Set<ITypeName> iTypeNames) {
        return super.visit(entity, iTypeNames);
    }

    @Override
    public Void visit(ILambdaExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(ILoopHeaderBlockExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IConstantValueExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(INullExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IReferenceExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IEventReference eventRef, Set<ITypeName> iTypeNames) {
        return super.visit(eventRef, iTypeNames);
    }

    @Override
    public Void visit(IFieldReference fieldRef, Set<ITypeName> iTypeNames) {
        return super.visit(fieldRef, iTypeNames);
    }

    @Override
    public Void visit(IMethodReference methodRef, Set<ITypeName> iTypeNames) {
        return super.visit(methodRef, iTypeNames);
    }

    @Override
    public Void visit(IPropertyReference methodRef, Set<ITypeName> iTypeNames) {
        return super.visit(methodRef, iTypeNames);
    }

    @Override
    public Void visit(IVariableReference varRef, Set<ITypeName> iTypeNames) {
        return super.visit(varRef, iTypeNames);
    }

    @Override
    public Void visit(IUnknownReference unknownRef, Set<ITypeName> iTypeNames) {
        return super.visit(unknownRef, iTypeNames);
    }

    @Override
    public Void visit(IUnknownExpression unknownExpr, Set<ITypeName> iTypeNames) {
        return super.visit(unknownExpr, iTypeNames);
    }

    @Override
    public Void visit(IUnknownStatement unknownStmt, Set<ITypeName> iTypeNames) {
        return super.visit(unknownStmt, iTypeNames);
    }

    @Override
    public Void visit(IEventSubscriptionStatement stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(ICastExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IIndexAccessExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(ITypeCheckExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IIndexAccessReference indexAccessRef, Set<ITypeName> iTypeNames) {
        return super.visit(indexAccessRef, iTypeNames);
    }

    @Override
    public Void visit(IBinaryExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IUnaryExpression expr, Set<ITypeName> iTypeNames) {
        return super.visit(expr, iTypeNames);
    }

    @Override
    public Void visit(IAssignment stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(ISST sst, Set<ITypeName> iTypeNames) {
        return super.visit(sst, iTypeNames);
    }

    @Override
    public Void visit(IDelegateDeclaration stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IEventDeclaration stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IFieldDeclaration stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

    @Override
    public Void visit(IPropertyDeclaration stmt, Set<ITypeName> iTypeNames) {
        return super.visit(stmt, iTypeNames);
    }

}
