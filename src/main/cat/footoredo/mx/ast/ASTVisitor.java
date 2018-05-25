package cat.footoredo.mx.ast;

public interface ASTVisitor <S, E> {
    // Statements
    S visit(BlockNode node);
    S visit(ExpressionStatementNode node);
    S visit(IfNode node);
    S visit(WhileNode node);
    S visit(ForNode node);
    S visit(BreakNode node);
    S visit(ContinueNode node);
    S visit(ReturnNode node);
    S visit(NullStatementNode node);
    S visit(LocalVariableDeclarationNode node);

    // Expressions
    E visit(AssignNode node);
    E visit(LogicalOpNode node);
    E visit(ArithmeticOpNode node);
    E visit(BinaryOpNode node);
    E visit(UnaryOpNode node);
    E visit(PrefixNode node);
    E visit(SuffixNode node);
    E visit(ComparationNode node);
    E visit(ArefNode node);
    E visit(MemberNode node);
    E visit(FuncallNode node);
    E visit(NewNode node);
    E visit(VariableNode node);
    E visit(IntegerLiteralNode node);
    E visit(StringLiteralNode node);
    E visit(BooleanLiteralNode node);
    E visit(NullLiteralNode node);
}
