package cat.footoredo.mx.cst;
// Generated from Mx.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(MxParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(MxParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(MxParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(MxParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#arrayCreator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreator(MxParser.ArrayCreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#arguements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguements(MxParser.ArguementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(MxParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(MxParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaratorId(MxParser.VariableDeclaratorIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(MxParser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(MxParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#typeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpec(MxParser.TypeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#baseTypeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseTypeSpec(MxParser.BaseTypeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassType(MxParser.ClassTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(MxParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#memberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclaration(MxParser.MemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(MxParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(MxParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#parametersList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametersList(MxParser.ParametersListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(MxParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(MxParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MxParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(MxParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(MxParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(MxParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclarationStatement(MxParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclaration(MxParser.LocalVariableDeclarationContext ctx);
}