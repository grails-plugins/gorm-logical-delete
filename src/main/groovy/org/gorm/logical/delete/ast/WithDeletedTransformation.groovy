/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gorm.logical.delete.ast

import gorm.logical.delete.PreQueryListener
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.stmt.TryCatchStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.syntax.Types
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class WithDeletedTransformation extends AbstractASTTransformation {

    static final String IGNORE_DELETED__FILTER_PROPERTY_NAME = 'IGNORE_DELETED_FILTER'
    static final String FILTER_PROPERTY_VALUE_VARIABLE_NAME = '$originalValue'

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {

        /*
         * This wraps the original method code like this...
         *

         def originalMethod() {
             Boolean $originalValue = PreQueryListener.IGNORE_DELETED_FILTER.get()
             try {
                 PreQueryListener.IGNORE_DELETED_FILTER.set(true)

                 // original method code goes here
             } finally {
                 PreQueryListener.IGNORE_DELETED_FILTER.set($originalValue)
             }
         }

         *
         */
        MethodNode methodNode = (MethodNode) nodes[1]

        Statement originalCode = methodNode.code

        Expression preQueryListenerClassExpression = new ClassExpression(ClassHelper.make(PreQueryListener))

        Expression ignoreDeletedFilterPropertyExpression = new PropertyExpression(preQueryListenerClassExpression,
                IGNORE_DELETED__FILTER_PROPERTY_NAME)

        Expression getInitialFilterValueMethodCall = new MethodCallExpression(ignoreDeletedFilterPropertyExpression,
                'get',
                new ArgumentListExpression())

        Expression originalFilterPropertyValue = new VariableExpression(FILTER_PROPERTY_VALUE_VARIABLE_NAME,
                ClassHelper.make(Boolean))

        Expression declareAndAssignOriginalFilterValueExpression =
                new DeclarationExpression(originalFilterPropertyValue,
                        Token.newSymbol(Types.EQUALS, 0, 0),
                        getInitialFilterValueMethodCall)

        BlockStatement tryStatement = new BlockStatement()
        Expression setValueArgumentListExpression = new ArgumentListExpression(new ConstantExpression(true))
        Expression setValueMethodCallExpression = new MethodCallExpression(ignoreDeletedFilterPropertyExpression,
                'set',
                setValueArgumentListExpression)
        tryStatement.addStatement(new ExpressionStatement(setValueMethodCallExpression))
        tryStatement.addStatement(originalCode)

        Expression setFilterValueMethodCall = new MethodCallExpression(ignoreDeletedFilterPropertyExpression,
                'set',
                new ArgumentListExpression(originalFilterPropertyValue))

        Statement restoreOriginalFilterValueExpression = new ExpressionStatement(setFilterValueMethodCall)

        TryCatchStatement tryCatchStatement = new TryCatchStatement(tryStatement, restoreOriginalFilterValueExpression)

        BlockStatement newMethodBody = new BlockStatement()
        newMethodBody.addStatement(new ExpressionStatement(declareAndAssignOriginalFilterValueExpression))
        newMethodBody.addStatement(tryCatchStatement)
        methodNode.code = newMethodBody
    }
}
