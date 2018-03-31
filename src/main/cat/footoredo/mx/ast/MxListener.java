package cat.footoredo.mx.ast;
// Generated from Mx.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxParser}.
 */
public interface MxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(MxParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(MxParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#globalDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDeclaration(MxParser.GlobalDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#globalDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDeclaration(MxParser.GlobalDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(MxParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(MxParser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#mainDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMainDeclaration(MxParser.MainDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#mainDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMainDeclaration(MxParser.MainDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MxParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MxParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MxParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MxParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(MxParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(MxParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#createdName}.
	 * @param ctx the parse tree
	 */
	void enterCreatedName(MxParser.CreatedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#createdName}.
	 * @param ctx the parse tree
	 */
	void exitCreatedName(MxParser.CreatedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorRest(MxParser.ArrayCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorRest(MxParser.ArrayCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterClassCreatorRest(MxParser.ClassCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitClassCreatorRest(MxParser.ClassCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#arguements}.
	 * @param ctx the parse tree
	 */
	void enterArguements(MxParser.ArguementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#arguements}.
	 * @param ctx the parse tree
	 */
	void exitArguements(MxParser.ArguementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(MxParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(MxParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(MxParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(MxParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#boolLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(MxParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#boolLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(MxParser.BoolLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(MxParser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(MxParser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(MxParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(MxParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(MxParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(MxParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(MxParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(MxParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(MxParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(MxParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#typeSpec}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpec(MxParser.TypeSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#typeSpec}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpec(MxParser.TypeSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(MxParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(MxParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(MxParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(MxParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(MxParser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(MxParser.MemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(MxParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(MxParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(MxParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(MxParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#parametersList}.
	 * @param ctx the parse tree
	 */
	void enterParametersList(MxParser.ParametersListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#parametersList}.
	 * @param ctx the parse tree
	 */
	void exitParametersList(MxParser.ParametersListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(MxParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(MxParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MxParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MxParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(MxParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(MxParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(MxParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(MxParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(MxParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(MxParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclarationStatement(MxParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclarationStatement(MxParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(MxParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(MxParser.LocalVariableDeclarationContext ctx);
}