package cat.footoredo.mx.compiler;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.ir.*;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.String;
import cat.footoredo.mx.ir.Variable;
import cat.footoredo.mx.type.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IRGenerator implements ASTVisitor<Void, Expression> {
    private final TypeTable typeTable;
    private AST ast;

    public IRGenerator(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    private Variable thisPointer;
    private ClassType currentClass;
    public IR generate(AST ast) throws SemanticException {
        scopeStack = new LinkedList<>();
        scopeStack.add(ast.getScope());
        this.ast = ast;
        ast.getScope().declareEntity(new cat.footoredo.mx.entity.Variable(new TypeNode(new PointerType()), "_fvck__thisPointer"));
        ast.getScope().declareEntity(new cat.footoredo.mx.entity.Variable(new TypeNode(new PointerType()), "_fvck__n"));
        thisPointer = ref(ast.getScope().get("_fvck__thisPointer"));
        ((cat.footoredo.mx.entity.Variable)(ast.getScope().get("_fvck__thisPointer"))).setGlobal (true);
        ((cat.footoredo.mx.entity.Variable)(ast.getScope().get("_fvck__n"))).setGlobal (true);
        currentClass = null;
        for (TypeDefinition t: ast.getTypeDefinitions()) {
            if (t instanceof ClassNode) {
                // System.out.println (((ClassNode) t).getType());
                ((ClassNode) t).getClassType().computeOffsets();
                currentClass = ((ClassNode) t).getClassType();
            }
            for (Function f: t.getMemberMethods()) {
                if (f instanceof DefinedFunction) {
                    ((DefinedFunction) f).setIR(compileFunctionBody((DefinedFunction) f));
                }
            }
            currentClass = null;
        }
        statements = ast.getStatements();
        for (cat.footoredo.mx.entity.Variable variable: ast.getVariables()) {
            if (variable.isStatic())
                variable.setIR(transformExpression(variable.getInitializer()));
            else if (variable.hasInitializer()) {
                // System.out.println (variable.getName());
                statements.add (new Assign(variable.getLocation(), addressOf(ref(variable)), transformExpression(variable.getInitializer())));
                // System.out.println(statements.get(1).getClass());
            }
        }
        for (DefinedFunction f: ast.getFunctions()) {
            f.setIR(compileFunctionBody(f));
        }

        scopeStack.removeLast();
        return ast.getIR();
    }

    private void transformStatement(StatementNode statementNode) {
        statementNode.accept(this);
    }

    private void transformStatement(ExpressionNode expressionNode) {
        expressionNode.accept(this);
    }

    private int expressionLevel = 0;

    private Expression transformExpression(ExpressionNode expressionNode) {
        expressionLevel ++;
        Expression e = expressionNode.accept(this);
        expressionLevel --;
        return e;
    }

    private boolean isStatement() {
        return expressionLevel == 0;
    }

    private List<Statement> statements;
    private LinkedList<Scope> scopeStack;
    private LinkedList<Label> breakStack;
    private LinkedList<Label> continueStack;
    private Label functionEndLabel;

    private List<Statement> compileFunctionBody (DefinedFunction function) {
        statements = new ArrayList<>();
        breakStack = new LinkedList<>();
        continueStack = new LinkedList<>();
        functionEndLabel = new Label();
        function.setFunctionEndLabel(functionEndLabel);

        scopeStack.add(function.getScope());

        transformStatement(function.getBlock());
        label(functionEndLabel);
        scopeStack.removeLast();
        return statements;
    }

    private void label (Label label) {
        statements.add (new LabelStatement(null, label));
    }

    private void jump (Location location, Label target) {
        statements.add(new Jump(location, target));
    }

    private void jump (Label target) {
        jump (null, target);
    }

    private void cjump (Location location, Expression cond, Label thenLabel, Label elseLabel) {
        statements.add (new CJump(location, cond, thenLabel, elseLabel));
    }

    private void pushBreak (Label label) {
        breakStack.add (label);
    }

    private void popBreak () {
        breakStack.removeLast();
    }

    private Label currentBreakTarget () {
        return breakStack.getLast();
    }

    private void pushContinue (Label label) {
        continueStack.add (label);
    }

    private void popContinue () {
        continueStack.removeLast();
    }

    private Label currentContinueTarget () {
        return continueStack.getLast();
    }

    private void assign (Location location, Expression lhs, Expression rhs) {
        statements.add (new Assign(location, addressOf(lhs), rhs));
    }

    private cat.footoredo.mx.entity.Variable tmpVariable (cat.footoredo.mx.type.Type type) {
        return scopeStack.getLast().allocateTmpVariable(type);
    }

    private Expression transformOpAssign (Location location, Op op, Expression lhs, Expression rhs) {
        assign (location, lhs, binary(op, lhs, rhs));
        return isStatement() ? null : lhs;
    }

    private Binary binary (Type type, Op op, Expression lhs, Expression rhs) {
        return new Binary(type, op, lhs, rhs);
    }

    private Binary binary (Op op, Expression lhs, Expression rhs) {
        return new Binary(lhs.getType(), op, lhs, rhs);
    }

    private Expression transformIndex (ArefNode node) {
        return transformExpression(node.getIndex());
    }

    private Variable setThisPointer (Location location, Expression newThisPointer) {
        Variable originalThisPointer = ref(tmpVariable(new PointerType()));
        assign(location, originalThisPointer, thisPointer);
        assign(location, thisPointer, newThisPointer);
        return originalThisPointer;
    }

    private void recoverThisPointer (Location location, Variable originalThisPointer) {
        assign(location, thisPointer, originalThisPointer);
    }

    @Override
    public Void visit(BlockNode node) {
        scopeStack.add(node.getScope());
        //System.out.println ("BLOCK SCOPE: " + node.getScope());
        for (StatementNode statementNode: node.getStatements()) {
            transformStatement(statementNode);
        }
        scopeStack.removeLast();
        return null;
    }

    @Override
    public Void visit(ExpressionStatementNode node) {
        Expression e = node.getExpression().accept(this);
        if (e != null) {
            //System.out.println("useless statement @" + node.getLocation());
        }
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        Label thenLabel = new Label();
        Label elseLabel = new Label();
        Label endLabel = new Label();

        Expression cond = transformExpression(node.getJudge());

        if (node.hasElseStatement()) {
            cjump (node.getLocation(), cond, thenLabel, elseLabel);
            label (thenLabel);
            transformStatement(node.getThenStatement());
            jump (endLabel);
            label (elseLabel);
            transformStatement (node.getElseStatement());
            label (endLabel);
        }
        else {
            cjump (node.getLocation(), cond, thenLabel, endLabel);
            label (thenLabel);
            transformStatement (node.getThenStatement());
            label (endLabel);
        }

        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        Label beginLabel = new Label();
        Label bodyLabel = new Label();
        Label endLabel = new Label();

        beginLabel.setPairedEndLabel(endLabel);

        label (beginLabel);
        cjump (node.getLocation(), transformExpression(node.getJudge()), bodyLabel, endLabel);

        label (bodyLabel);
        pushContinue(beginLabel);
        pushBreak(endLabel);

        transformStatement(node.getBody());

        popBreak();
        popContinue();

        jump (beginLabel);
        label (endLabel);

        return null;
    }

    @Override
    public Void visit(ForNode node) {
        Label beginLabel = new Label();
        Label bodyLabel = new Label();
        Label endLabel = new Label();

        beginLabel.setPairedEndLabel(endLabel);

        if (node.hasInit())
            transformStatement(node.getInit());

        label (beginLabel);
        if (node.hasJudge())
            cjump (node.getLocation(), transformExpression(node.getJudge()), bodyLabel, endLabel);

        label (bodyLabel);
        pushContinue(beginLabel);
        pushBreak(endLabel);

        transformStatement(node.getBody());
        if (node.hasUpdate())
            transformExpression(node.getUpdate());

        popBreak();
        popContinue();

        jump (beginLabel);
        label (endLabel);

        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        jump (node.getLocation(), currentBreakTarget());
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        jump (node.getLocation(), currentContinueTarget());
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        statements.add(new Return(node.getLocation(), node.hasExpression() ? transformExpression(node.getExpression()) : null, functionEndLabel));
        return null;
    }

    @Override
    public Void visit(NullStatementNode node) {
        return null;
    }

    @Override
    public Void visit(LocalVariableDeclarationNode node) {
        if (node.hasInitialExpression()) {
            cat.footoredo.mx.entity.Variable variable = node.getVariable();
            assign (variable.getLocation(), ref (variable), transformExpression(node.getInitialExpression()));
        }
        return null;
    }

    @Override
    public Expression visit(AssignNode node) {
        // System.out.println (node.getLocation());
        Location lhsLocation = node.getLhs().getLocation();
        Location rhsLocation = node.getRhs().getLocation();
        if (isStatement()) {
            Expression rhs = transformExpression(node.getRhs());
            // System.out.println ("here");
            // System.out.println (node.getLhs().getClass());
            assign (lhsLocation, transformExpression(node.getLhs()), rhs);
            return null;
        }
        else {
            cat.footoredo.mx.entity.Variable tmp = tmpVariable(node.getRhs().getType());
            cat.footoredo.mx.ir.Variable tmpVar = ref (tmp);
            assign (rhsLocation, tmpVar, transformExpression(node.getRhs()));
            assign (lhsLocation, transformExpression(node.getLhs()), tmpVar);
            return tmpVar;
        }
    }

    private void process (ExpressionNode node, Label trueLabel, Label falseLabel) {
        if (node instanceof LogicalOpNode) {
            LogicalOpNode logicalOpNode = (LogicalOpNode) node;
            ExpressionNode lhs = logicalOpNode.getLhs();
            ExpressionNode rhs = logicalOpNode.getRhs();

            Label rightLabel = new Label();
            if (logicalOpNode.getOperator().equals("&&")) {
                process(lhs, rightLabel, falseLabel);
            }
            else {
                process(lhs, trueLabel, rightLabel);
            }

            label (rightLabel);
            process(rhs, trueLabel, falseLabel);
        }
        else {
            cat.footoredo.mx.entity.Variable tmp = tmpVariable(node.getType());
            assign (node.getLocation(), ref(tmp), transformExpression(node));
            cjump (node.getLocation(), ref(tmp), trueLabel, falseLabel);
        }
    }

    @Override
    public Expression visit(LogicalOpNode node) {
        /*Expression rhs = transformExpression(node.getRhs());
        Expression lhs = transformExpression(node.getLhs());

        Op op = Op.internBinary(node.getOperator(),false);
        cat.footoredo.mx.type.Type type = node.getType();
        return isStatement() ? null : new Binary(asmType(type), op, lhs, rhs);*/

        Label trueLabel = new Label ();
        Label falseLabel = new Label ();
        Label endLabel = new Label ();

        process(node, trueLabel, falseLabel);

        cat.footoredo.mx.entity.Variable variable = tmpVariable(node.getType());

        label (trueLabel);
        assign (node.getLocation(), ref(variable), new Integer(Type.INT8, 1));
        jump (endLabel);

        label (falseLabel);
        assign (node.getLocation(), ref(variable), new Integer(Type.INT8, 0));
        jump (endLabel);

        label (endLabel);

        return isStatement() ? null : ref (variable);
    }

    @Override
    public Expression visit(ArithmeticOpNode node) {
        Expression rhs = transformExpression(node.getRhs());
        Expression lhs = transformExpression(node.getLhs());
        if (node.getLhs().getType().isString()) {
            Entity compareFunction = ast.getEntity("_strcat");
            Expression caller = ref(compareFunction);
            List<Expression> args = new ArrayList<>();
            args.add (lhs); args.add (rhs);
            return new Call(asmType(new PointerType()), caller, args);
        }
        else {
            Op op = Op.internBinary(node.getOperator(), node.getLhs().getType().isSigned());
            cat.footoredo.mx.type.Type type = node.getType();
            return isStatement() ? null : new Binary(asmType(type), op, lhs, rhs);
        }
    }

    @Override
    public Expression visit(UnaryOpNode node) {
        if (node.getOperator().equals("+")) {
            return transformExpression(node.getExpression());
        }
        else {
            return new Unary(asmType(node.getType()), Op.internUnary(node.getOperator()), transformExpression(node.getExpression()));
        }
    }

    @Override
    public Expression visit(PrefixNode node) {
        cat.footoredo.mx.type.Type type = node.getType();
        return transformOpAssign(node.getLocation(), binaryOp(node.getOperator()),
                transformExpression(node.getExpression()), immediate(type, 1));
    }

    @Override
    public Expression visit(SuffixNode node) {
        Expression expression = transformExpression(node.getExpression());
        cat.footoredo.mx.type.Type type = node.getType();
        Op op = binaryOp(node.getOperator());
        Location location = node.getLocation();

        if (isStatement()) {
            transformOpAssign(location, op, expression, immediate(type, 1));
            return null;
        }
        else {
            cat.footoredo.mx.entity.Variable variable = tmpVariable(type);
            assign (location, ref (variable), expression);
            assign (location, expression, binary(ref (variable).getType(), op, ref (variable), immediate(type, 1)));
            return ref (variable);
        }
    }

    @Override
    public Expression visit(ComparationNode node) {
        Expression rhs = transformExpression(node.getRhs());
        Expression lhs = transformExpression(node.getLhs());
        cat.footoredo.mx.type.Type type = node.getType();
        if (node.getLhs().getType().isString()) {
            Entity compareFunction = ast.getEntity("_strcmp");
            Expression caller = ref(compareFunction);
            List<Expression> args = new ArrayList<>();
            args.add (lhs); args.add (rhs);
            Call cmp = new Call(asmType(new IntegerType(true)), caller, args);
            Op op = Op.internBinary(node.getOperator(), true);
            return isStatement() ? null : new Binary(asmType(type), op, cmp, new Integer(Type.INT64,0));
        }
        else {
            // System.err.println("asdas" + node.getLocation());
            // System.err.println(node.getLhs().getType());
            Op op = Op.internBinary(node.getOperator(), node.getLhs().getType().getCmpType().isSigned());
            return isStatement() ? null : new Binary(asmType(type), op, lhs, rhs);
        }
    }

    @Override
    public Expression visit(ArefNode node) {
        Expression expression = transformExpression(node.getExpression());
        Expression offset = new Binary(ptrdiff_t(), Op.MUL, size (node.elementSize()), transformIndex(node));
        Binary address = new Binary(ptr_t(), Op.ADD, expression, offset);
        return memory(address, node.getType());
    }

    @Override
    public Expression visit(MemberNode node) {
        // System.out.println (node.getExpression() instanceof VariableNode);
        Expression expression = transformExpression(node.getExpression());
        node.setInstance (expression);
        if (!node.getType().isFunction()) {
            Expression offset = ptrdiff(node.getOffset());
            Expression address = new Binary(ptr_t(), Op.ADD, expression, offset);
            return memory(address, node.getType());
        }
        else {
            java.lang.String typeName = node.getTypeName();
            Entity entity = ast.getTypeDefinition(typeName).getScope().get(node.getName());
            return ref (entity);
        }
    }

    @Override
    public Expression visit(FuncallNode node) {
        List<Expression> args = new ArrayList<>();
        for (ExpressionNode arg : node.getParams()) {
            args.add(transformExpression(arg));
        }
        Expression caller = transformExpression(node.getCaller());
        Expression call;
        Variable originalThisPointer = null;
        if (node.getCaller() instanceof MemberNode) {
            // System.out.println(((MemberNode) node.getCaller()).getInstance().getClass());
            originalThisPointer = setThisPointer( node.getLocation(),
                    ((MemberNode) node.getCaller()).getInstance() );
            // assign (node.getLocation(), ref(scopeStack.getLast().get("this")), thisPointer);
            call = new Call(asmType(node.getReturnType()), caller, args);
        }
        else {
            call = new Call (asmType(node.getReturnType()), caller, args);
        }
        if (isStatement()) {
            statements.add (new ExpressionStatement(node.getLocation(), call));
            if (originalThisPointer != null) {
                recoverThisPointer (node.getLocation(), originalThisPointer);
            }
            return null;
        }
        else {
            cat.footoredo.mx.entity.Variable tmp = tmpVariable(node.getReturnType());
            assign(node.getLocation(), ref (tmp), call);
            if (originalThisPointer != null) {
                recoverThisPointer (node.getLocation(), originalThisPointer);
            }
            return ref (tmp);
        }
    }

    private Expression allocateArray(Location location, ArrayType arrayType, List<ExpressionNode> _lengths) {
        List<ExpressionNode> lengths = new ArrayList<>(_lengths);
        // System.out.println (lengths.size());
        if (lengths.size() == 0) {
            throw new Error("array size not specified.");
        }
        Expression length = transformExpression(lengths.get(0));
        /*if (length instanceof Integer) {
            System.out.println (((Integer) length).getValue());
        }*/
        // System.out.println (arrayType.getBaseType().size());
        Expression entrySize = size(arrayType.getBaseType().size());
        Expression sizeSize = size(8);
        Expression totalSize = binary(Op.ADD, sizeSize, binary(Op.MUL, length, entrySize));
        // System.out.println ("here");
        Malloc malloc = new Malloc(ptr_t(), totalSize);
        Expression address = ref(tmpVariable(new PointerType()));
        assign (location, address, malloc);
        assign (location, memory(address, new IntegerType(false)), length);
        Expression startAddress = ref(tmpVariable(new PointerType()));
        assign(location, startAddress, binary(Op.ADD, address, size(8)));
        lengths.remove(0);
        if (lengths.size() > 0) {
            Expression currentAddress = ref(tmpVariable(new PointerType()));
            assign(location, currentAddress, startAddress);
            Expression countDown = ref (tmpVariable(new IntegerType(false)));
            assign (location, countDown, length);
            Expression cond = binary(Op.NEQ, countDown, size(0));

            Label begLabel = new Label ();
            Label bodyLabel = new Label ();
            Label endLabel = new Label ();

            label (begLabel);
            cjump (location, cond, bodyLabel, endLabel);
            label (bodyLabel);
            assign (location, memory(currentAddress, new PointerType()), allocateArray(location, (ArrayType) arrayType.getBaseType(), lengths));
            transformOpAssign(location, Op.ADD, currentAddress, size(8));
            transformOpAssign(location, Op.SUB, countDown, size(1));
            jump (begLabel);
            label (endLabel);
        }
        return startAddress;
    }

    @Override
    public Expression visit(NewNode node) {
        // System.out.println(node.getLocation());
        CreatorNode creator = node.getCreator();
        if (creator.hasArgs()) {
            // System.out.println(node.getLocation());
            List<Expression> args = new ArrayList<>();
            for (ExpressionNode arg : creator.getArgs()) {
                args.add (transformExpression(arg));
            }
            // System.out.println(creator.getType().allocateSize());
            Malloc malloc = new Malloc(ptr_t(), size(creator.getType().allocateSize()));
            Expression address = ref(tmpVariable(new PointerType()));
            assign (node.getLocation(), address, malloc);
            java.lang.String typeName = creator.getTypeName ();
            Entity constructor = ast.getTypeDefinition(typeName).getScope().get(typeName);
            Variable originalThisPointer = setThisPointer(node.getLocation(), address);
            Expression call = new Call(nullType(), ref(constructor), args);
            // System.out.println ("here");
            statements.add (new ExpressionStatement(node.getLocation(), call));
            recoverThisPointer(node.getLocation(), originalThisPointer);
            return address;
        }
        else if (creator.hasLengths()) {
            Expression address = allocateArray (node.getLocation(), (ArrayType) node.getType(), creator.getLengths());
            return address;
        }
        else {
            throw new SemanticException(node.getLocation(), "\"new type\" is not valid");
        }
    }

    @Override
    public Expression visit(VariableNode node) {
        if (node.isMember()) {
            // System.out.println (node.getName() + " " + node.getLocation());
            Expression offset = ptrdiff(currentClass.getOffset(node.getName()));
            Expression address = new Binary(ptr_t(), Op.ADD, thisPointer, offset);
            Variable tmp = ref(tmpVariable(new PointerType()));
            assign (node.getLocation(), tmp, address);
            return memory(tmp, new PointerType());
        }
        else if (node.getName().equals("this")) {
            return thisPointer;
        }
        else {
            // System.out.println("here");
            // System.out.println (node.getLocation());
            cat.footoredo.mx.ir.Variable variable = ref(node.getEntity());
            return node.isLoadable() ? variable : addressOf(variable);
            // return variable;
        }
    }

    @Override
    public Expression visit(BinaryOpNode node) {
        Expression right = transformExpression(node.getRhs());
        Expression left = transformExpression(node.getLhs());
        Op op = Op.internBinary(node.getOperator(), node.isSigned());
        cat.footoredo.mx.type.Type type = node.getType();
        return new Binary(asmType(type), op, left, right);
    }

    @Override
    public Expression visit(IntegerLiteralNode node) {
        return new Integer(asmType(node.getType()), node.getValue());
    }

    @Override
    public Expression visit(StringLiteralNode node) {
        return new String(asmType(node.getType()), node.getEntry());
    }

    @Override
    public Expression visit(BooleanLiteralNode node) {
        return new Integer(asmType(node.getType()), node.getValue() ? 1 : 0);
    }

    @Override
    public Expression visit(NullLiteralNode node) {
        return new Null(asmType(node.getType()));
    }

    private Expression addressOf (Expression expression) {/*
        if (expression instanceof Address) return expression;
        else return expression.getAddressNode(ptr_t());*/
        return expression;
    }

    private Type asmType (cat.footoredo.mx.type.Type type) {
        return Type.get(type.size());
    }

    private Type varType (cat.footoredo.mx.type.Type type) {
        if (!type.isScaler ()) {
            return null;
        }
        return Type.get(type.size());
    }

    private Type nullType () {
        return Type.get(0);
    }

    private cat.footoredo.mx.ir.Variable ref (Entity entity) {
        /*if (varType(entity.getType()) == null)
            System.out.println (entity.getLocation());*/
        return new cat.footoredo.mx.ir.Variable(varType(entity.getType()), entity);
    }

    private Op binaryOp (java.lang.String unaryOp) {
        return unaryOp.equals("++") ? Op.ADD : Op.SUB;
    }

    private Memory memory(Entity entity) {
        return new Memory(asmType(entity.getType()), ref(entity));
    }

    private Memory memory(Expression expression, cat.footoredo.mx.type.Type type) {
        // System.out.println ("sss" + type + asmType(type));
        return new Memory(asmType(type), expression);
    }

    private Integer immediate (cat.footoredo.mx.type.Type type, long value) {
        return new Integer(int_t(), value);
    }

    private Integer size (long value) {
        return new Integer(size_t(), value);
    }

    private Integer ptrdiff (long value) {
        return new Integer(ptrdiff_t(), value);
    }

    private Type int_t() {
        return Type.get(TypeTable.integerSize);
    }

    private Type ptr_t() {
        return Type.get(TypeTable.pointerSize);
    }

    private Type ptrdiff_t() {
        return Type.get(TypeTable.longSize);
    }

    private Type size_t() {
        return Type.get(TypeTable.longSize);
    }

    private Type bool_t() {
        return Type.get(TypeTable.booleanSize);
    }
}
