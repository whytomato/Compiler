package Parser;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parse.Number;
import Parser.Parse.*;

import java.io.IOException;

public class Parser {
    Lexer lexer = Lexer.getInstance();
    CompUnit compUnit = new CompUnit();
    public StringBuilder stringBuilder = new StringBuilder();

    public void parse() throws IOException {
        CompUnit();
        stringBuilder.append("<CompUnit>\n");
        printf();
    }

    private void CompUnit() {
        while (lexer.getPreType() != LexType.MAINTK && lexer.getPrePreType() != LexType.LPARENT) {
            compUnit.declArrayList.add(Decl());
        }
        while (lexer.getPreType() != LexType.MAINTK) {
            compUnit.funcDefArrayList.add(FuncDef());
            stringBuilder.append("<FuncDef>\n");
        }
        compUnit.mainFuncDef = MainFuncDef();
        stringBuilder.append("<MainFuncDef>\n");
    }//CompUnit → {Decl} {FuncDef} MainFuncDef


    private Decl Decl() {
        Decl decl = new Decl();
        if (lexer.getType() == LexType.CONSTTK) {
            decl.constDecl = ConstDecl();
            stringBuilder.append("<ConstDecl>\n");
        } else {
            decl.varDecl = VarDecl();
            stringBuilder.append("<VarDecl>\n");
        }
        return decl;
    }//Decl → ConstDecl | VarDecl

    private ConstDecl ConstDecl() {
        ConstDecl constDecl = new ConstDecl();
        if (lexer.getType() != LexType.CONSTTK) error();
        stringBuilder.append("CONSTTK const\n");
        lexer.next();
        if (lexer.getType() != LexType.INTTK) error();
        stringBuilder.append("INTTK int\n");
        lexer.next();
        constDecl.constDefArrayList.add(ConstDef());
        stringBuilder.append("<ConstDef>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            stringBuilder.append("COMMA ,\n");
            lexer.next();
            constDecl.constDefArrayList.add(ConstDef());
            stringBuilder.append("<ConstDef>\n");
        }
        if (lexer.getType() != LexType.SEMICN) error();
        stringBuilder.append("SEMICN ;\n");
        lexer.next();
        return constDecl;
    }//ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'

    private ConstDef ConstDef() {
        ConstDef constDef = new ConstDef();
        if (lexer.getType() != LexType.IDENFR) error();
        constDef.Ident = lexer.getToken();
        stringBuilder.append("IDENFR ").append(constDef.Ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            stringBuilder.append("LBRACK [\n");
            lexer.next();
            constDef.constExpArrayList.add(ConstExp());
            stringBuilder.append("<ConstExp>\n");
            if (lexer.getType() != LexType.RBRACK) error();
            stringBuilder.append("RBRACK ]\n");
            lexer.next();
        }
        if (lexer.getType() != LexType.ASSIGN) error();
        stringBuilder.append("ASSIGN =\n");
        lexer.next();
        constDef.constInitval = ConstInitval();
        stringBuilder.append("<ConstInitVal>\n");
        return constDef;
    }//ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal

    private ConstInitval ConstInitval() {
        ConstInitval constInitval = new ConstInitval();
        if (lexer.getType() != LexType.LBRACE) {
            constInitval.constExp = ConstExp();
            stringBuilder.append("<ConstExp>\n");
        } else {
            stringBuilder.append("LBRACE {\n");
            lexer.next();
            if (lexer.getType() != LexType.RBRACE) {
                constInitval.constInitvalArrayList.add(ConstInitval());
                stringBuilder.append("<ConstInitVal>\n");
                while (true) {
                    if (lexer.getType() != LexType.COMMA) break;
                    stringBuilder.append("COMMA ,\n");
                    lexer.next();
                    constInitval.constInitvalArrayList.add(ConstInitval());
                    stringBuilder.append("<ConstInitVal>\n");
                }
                if (lexer.getType() != LexType.RBRACE) error();
            }
            stringBuilder.append("RBRACE }\n");
            lexer.next();
        }
        return constInitval;
    }//ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'

    private VarDecl VarDecl() {
        VarDecl varDecl = new VarDecl();
        if (lexer.getType() != LexType.INTTK) error();
        stringBuilder.append("INTTK int\n");
        lexer.next();
        varDecl.varDefArrayList.add(VarDef());
        stringBuilder.append("<VarDef>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            stringBuilder.append("COMMA ,\n");
            lexer.next();
            varDecl.varDefArrayList.add(VarDef());
            stringBuilder.append("<VarDef>\n");
        }
        if (lexer.getType() != LexType.SEMICN) error();
        stringBuilder.append("SEMICN ;\n");
        lexer.next();
        return varDecl;
    }//VarDecl → BType VarDef { ',' VarDef } ';'

    private VarDef VarDef() {
        VarDef varDef = new VarDef();
        if (lexer.getType() != LexType.IDENFR) error();
        varDef.ident = lexer.getToken();
        stringBuilder.append("IDENFR ").append(varDef.ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            stringBuilder.append("LBRACK [\n");
            lexer.next();
            varDef.constExpArrayList.add(ConstExp());
            stringBuilder.append("<ConstExp>\n");
            if (lexer.getType() != LexType.RBRACK) error();
            stringBuilder.append("RBRACK ]\n");
            lexer.next();
        }
        if (lexer.getType() == LexType.ASSIGN) {
            stringBuilder.append("ASSIGN =\n");
            lexer.next();
            varDef.initVal = InitVal();
            stringBuilder.append("<InitVal>\n");
        }
        return varDef;
    }//VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal

    private InitVal InitVal() {
        InitVal initVal = new InitVal();
        if (lexer.getType() != LexType.LBRACE) {
            initVal.exp = Exp();
            stringBuilder.append("<Exp>\n");
        } else {
            stringBuilder.append("LBRACE {\n");
            lexer.next();
            if (lexer.getType() != LexType.RBRACE) {
                initVal.initValArrayList.add(InitVal());
                stringBuilder.append("<InitVal>\n");
                while (true) {
                    if (lexer.getType() != LexType.COMMA) break;
                    stringBuilder.append("COMMA ,\n");
                    lexer.next();
                    initVal.initValArrayList.add(InitVal());
                    stringBuilder.append("<InitVal>\n");
                }
                if (lexer.getType() != LexType.RBRACE) error();
            }
            stringBuilder.append("RBRACE }\n");
            lexer.next();
        }
        return initVal;
    }//InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'

    private FuncDef FuncDef() {
        FuncDef funcDef = new FuncDef();
        funcDef.funcType = FuncType();
        stringBuilder.append("<FuncType>\n");
        if (lexer.getType() != LexType.IDENFR) error();
        funcDef.ident = lexer.getToken();
        stringBuilder.append("IDENFR ").append(funcDef.ident).append("\n");
        lexer.next();
        if (lexer.getType() != LexType.LPARENT) error();
        stringBuilder.append("LPARENT (\n");
        lexer.next();
        if (lexer.getType() != LexType.RPARENT) {
            funcDef.funcFParams = FuncFParams();
            stringBuilder.append("<FuncFParams>\n");
            if (lexer.getType() != LexType.RPARENT) error();
        }
        stringBuilder.append("RPARENT )\n");
        lexer.next();
        funcDef.block = Block();
        stringBuilder.append("<Block>\n");
        return funcDef;
    }//FuncDef → FuncType Ident '(' [FuncFParams] ')' Block

    private MainFuncDef MainFuncDef() {
        MainFuncDef mainFuncDef = new MainFuncDef();
        stringBuilder.append("INTTK int\n");
        lexer.next();
        stringBuilder.append("MAINTK main\n");
        lexer.next();
        stringBuilder.append("LPARENT (\n");
        lexer.next();
        if (lexer.getType() != LexType.RPARENT) error();
        stringBuilder.append("RPARENT )\n");
        lexer.next();
        mainFuncDef.block = Block();
        stringBuilder.append("<Block>\n");
        return mainFuncDef;
    }//MainFuncDef → 'int' 'main' '(' ')' Block

    public FuncType FuncType() {
        FuncType funcType = new FuncType();
        if (lexer.getType() == LexType.VOIDTK) {
            funcType.funcType = lexer.getToken();
            stringBuilder.append("VOIDTK ").append(funcType.funcType).append("\n");
        } else if (lexer.getType() == LexType.INTTK) {
            funcType.funcType = lexer.getToken();
            stringBuilder.append("INTTK ").append(funcType.funcType).append("\n");
        } else error();
        lexer.next();
        return funcType;
    }//FuncType → 'void' | 'int'

    private FuncFParams FuncFParams() {
        FuncFParams funcFParams = new FuncFParams();
        funcFParams.funcFParamArrayList.add(FuncFParam());
        stringBuilder.append("<FuncFParam>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            stringBuilder.append("COMMA ,\n");
            lexer.next();
            funcFParams.funcFParamArrayList.add(FuncFParam());
            stringBuilder.append("<FuncFParam>\n");
        }
        return funcFParams;
    }

    public FuncFParam FuncFParam() {
        FuncFParam funcFParam = new FuncFParam();
        if (lexer.getType() != LexType.INTTK) error();
        stringBuilder.append("INTTK int\n");
        lexer.next();
        if (lexer.getType() != LexType.IDENFR) error();
        funcFParam.ident = lexer.getToken();
        stringBuilder.append("IDENFR ").append(funcFParam.ident).append("\n");
        lexer.next();
        if (lexer.getType() == LexType.LBRACK) {
            stringBuilder.append("LBRACK [\n");
            lexer.next();
            if (lexer.getType() != LexType.RBRACK) error();
            stringBuilder.append("RBRACK ]\n");
            lexer.next();
            while (true) {
                if (lexer.getType() != LexType.LBRACK) break;
                stringBuilder.append("LBRACK [\n");
                lexer.next();
                funcFParam.constExpArrayList.add(ConstExp());
                stringBuilder.append("<ConstExp>\n");
                if (lexer.getType() != LexType.RBRACK) error();
                stringBuilder.append("RBRACK ]\n");
                lexer.next();
            }
        }
        return funcFParam;
    }//FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]

    private Block Block() {
        Block block = new Block();
        if (lexer.getType() != LexType.LBRACE) error();
        stringBuilder.append("LBRACE {\n");
        lexer.next();
        while (true) {
            if (lexer.getType() == LexType.RBRACE) break;
            block.blockItemArrayList.add(BlockItem());
        }
        stringBuilder.append("RBRACE }\n");
        lexer.next();
        return block;
    }

    private BlockItem BlockItem() {
        BlockItem blockItem = new BlockItem();
        if (lexer.getType() == LexType.CONSTTK || lexer.getType() == LexType.INTTK) {
            blockItem.decl = Decl();
        } else {
            blockItem.stmt = Stmt();
            stringBuilder.append("<Stmt>\n");
        }
        return blockItem;
    }//BlockItem → Decl | Stmt

    private Stmt Stmt() {
        Stmt stmt = new Stmt();
        if (lexer.getType() == LexType.LBRACE) {
            stmt.flag = 3;
            stmt.block = Block();
            stringBuilder.append("<Block>\n");
        } else if (lexer.getType() == LexType.IFTK) {
            stringBuilder.append("IFTK if\n");
            lexer.next();
            if (lexer.getType() != LexType.LPARENT) error();
            stringBuilder.append("LPARENT (\n");
            lexer.next();
            stmt.cond = Cond();
            stringBuilder.append("<Cond>\n");
            if (lexer.getType() != LexType.RPARENT) error();
            stringBuilder.append("RPARENT )\n");
            lexer.next();
            stmt.stmtArrayList.add(Stmt());
            stringBuilder.append("<Stmt>\n");
            if (lexer.getType() == LexType.ELSETK) {
                stringBuilder.append("ELSETK else\n");
                lexer.next();
                stmt.flag = 4;
                stmt.stmtArrayList.add(Stmt());
                stringBuilder.append("<Stmt>\n");
            }
        } else if (lexer.getType() == LexType.FORTK) {
            stringBuilder.append("FORTK for\n");
            lexer.next();
            if (lexer.getType() != LexType.LPARENT) error();
            stringBuilder.append("LPARENT (\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) {
                stmt.forStmtArrayList.add(ForStmt());
                stringBuilder.append("<ForStmt>\n");
                if (lexer.getType() != LexType.SEMICN) error();
            }
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) {
                stmt.cond = Cond();
                stringBuilder.append("<Cond>\n");
                if (lexer.getType() != LexType.SEMICN) error();
            }
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            if (lexer.getType() != LexType.RPARENT) {
                stmt.forStmtArrayList.add(ForStmt());
                stringBuilder.append("<ForStmt>\n");
                if (lexer.getType() != LexType.RPARENT) error();
            }
            stringBuilder.append("RPARENT )\n");
            lexer.next();
            stmt.flag = 5;
            stmt.stmtArrayList.add(Stmt());
            stringBuilder.append("<Stmt>\n");

        } else if (lexer.getType() == LexType.BREAKTK) {
            stringBuilder.append("BREAKTK break\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) error();
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 6;
        } else if (lexer.getType() == LexType.CONTINUETK) {
            stringBuilder.append("CONTINUETK continue\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) error();
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 7;
        } else if (lexer.getType() == LexType.RETURNTK) {
            stringBuilder.append("RETURNTK return\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) {
                stmt.expArrayList.add(Exp());
                stringBuilder.append("<Exp>\n");
                if (lexer.getType() != LexType.SEMICN) error();
            }
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 8;
        } else if (lexer.getType() == LexType.PRINTFTK) {
            stringBuilder.append("PRINTFTK printf\n");
            lexer.next();
            if (lexer.getType() != LexType.LPARENT) error();
            stringBuilder.append("LPARENT (\n");
            lexer.next();
            if (lexer.getType() != LexType.STRCON) error();
            stmt.formatString = lexer.getToken();
            stringBuilder.append("STRCON ").append(stmt.formatString).append("\n");
            lexer.next();
            while (true) {
                if (lexer.getType() != LexType.COMMA) break;
                stringBuilder.append("COMMA ,\n");
                lexer.next();
                stmt.expArrayList.add(Exp());
                stringBuilder.append("<Exp>\n");
            }
            if (lexer.getType() != LexType.RPARENT) error();
            stringBuilder.append("RPARENT )\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) error();
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 10;
        } else if (lexer.getType() == LexType.IDENFR) {
            if (lexer.judge()) {
                stmt.lVal = LVal();
                stringBuilder.append("<LVal>\n");
                if (lexer.getType() != LexType.ASSIGN) error();
                stringBuilder.append("ASSIGN =\n");
                lexer.next();
                if (lexer.getType() == LexType.GETINTTK) {
                    stringBuilder.append("GETINTTK getint\n");
                    lexer.next();
                    if (lexer.getType() != LexType.LPARENT) error();
                    stringBuilder.append("LPARENT (\n");
                    lexer.next();
                    if (lexer.getType() != LexType.RPARENT) error();
                    stringBuilder.append("RPARENT )\n");
                    lexer.next();
                    if (lexer.getType() != LexType.SEMICN) error();
                    stringBuilder.append("SEMICN ;\n");
                    lexer.next();
                    stmt.flag = 9;
                } else {
                    stmt.expArrayList.add(Exp());
                    stringBuilder.append("<Exp>\n");
                    if (lexer.getType() != LexType.SEMICN) error();
                    stringBuilder.append("SEMICN ;\n");
                    lexer.next();
                    stmt.flag = 1;
                }
            } else {
                stmt.expArrayList.add(Exp());
                stringBuilder.append("<Exp>\n");
                if (lexer.getType() != LexType.SEMICN) error();
                stringBuilder.append("SEMICN ;\n");
                lexer.next();
                stmt.flag = 2;
            }
        } else if (lexer.getType() == LexType.SEMICN) {
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 2;
        } else {
            stmt.expArrayList.add(Exp());
            stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.SEMICN) error();
            stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 2;
        }
        return stmt;
    }

    private ForStmt ForStmt() {
        ForStmt forStmt = new ForStmt();
        forStmt.lVal = LVal();
        stringBuilder.append("<LVal>\n");
        if (lexer.getType() != LexType.ASSIGN) error();
        stringBuilder.append("ASSIGN =\n");
        lexer.next();
        forStmt.exp = Exp();
        stringBuilder.append("<Exp>\n");
        return forStmt;
    }//ForStmt → LVal '=' Exp

    private Exp Exp() {
        Exp exp = new Exp();
        exp.addExp = AddExp();
        stringBuilder.append("<AddExp>\n");
        return exp;
    }//Exp → AddExp

    public Cond Cond() {
        Cond cond = new Cond();
        cond.lOrExp = LOrExp();
        stringBuilder.append("<LOrExp>\n");
        return cond;
    }//Cond → LOrExp

    public LVal LVal() {
        LVal lVal = new LVal();
        if (lexer.getType() != LexType.IDENFR) error();
        lVal.ident = lexer.getToken();
        stringBuilder.append("IDENFR ").append(lVal.ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            stringBuilder.append("LBRACK [\n");
            lexer.next();
            lVal.expArrayList.add(Exp());
            stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.RBRACK) error();
            stringBuilder.append("RBRACK ]\n");
            lexer.next();
        }
        return lVal;
    }//LVal → Ident {'[' Exp ']'}

    public PrimaryExp PrimaryExp() {
        PrimaryExp primaryExp = new PrimaryExp();
        if (lexer.getType() == LexType.LPARENT) {
            stringBuilder.append("LPARENT (\n");
            lexer.next();
            primaryExp.exp = Exp();
            stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.RPARENT) error();
            stringBuilder.append("RPARENT )\n");
            lexer.next();
        } else if (lexer.getType() == LexType.IDENFR) {
            primaryExp.lVal = LVal();
            stringBuilder.append("<LVal>\n");
        } else if (lexer.getType() == LexType.INTCON) {
            primaryExp.number = Number();
            stringBuilder.append("<Number>\n");
        } else error();
        return primaryExp;
    }//PrimaryExp → '(' Exp ')' | LVal | Number

    public Number Number() {
        Number number = new Number();
        if (lexer.getType() != LexType.INTCON) error();
        number.intConst = Integer.parseInt(lexer.getToken());
        stringBuilder.append("INTCON ").append(number.intConst).append("\n");
        lexer.next();
        return number;
    }//Number → IntConst

    public UnaryExp UnaryExp() {
        UnaryExp unaryExp = new UnaryExp();
        if (lexer.getType() == LexType.LPARENT) {
            unaryExp.primaryExp = PrimaryExp();
            stringBuilder.append("<PrimaryExp>\n");
        } else if (lexer.getType() == LexType.IDENFR) {
            if (lexer.getPreType() == LexType.LPARENT) {
                unaryExp.ident = lexer.getToken();
                stringBuilder.append("IDENFR ").append(unaryExp.ident).append("\n");
                lexer.next();
                if (lexer.getType() != LexType.LPARENT) error();
                stringBuilder.append("LPARENT (\n");
                lexer.next();
                if (lexer.getType() != LexType.RPARENT) {
                    unaryExp.funcRParams = FuncRParams();
                    stringBuilder.append("<FuncRParams>\n");
                    if (lexer.getType() != LexType.RPARENT) error();
                }
                stringBuilder.append("RPARENT )\n");
                lexer.next();
            } else {
                unaryExp.primaryExp = PrimaryExp();
                stringBuilder.append("<PrimaryExp>\n");
            }
        } else if (lexer.getType() == LexType.INTCON) {
            unaryExp.primaryExp = PrimaryExp();
            stringBuilder.append("<PrimaryExp>\n");
        } else {
            unaryExp.unaryOp = UnaryOp();
            stringBuilder.append("<UnaryOp>\n");
            unaryExp.unaryExp = UnaryExp();
            stringBuilder.append("<UnaryExp>\n");
        }
        return unaryExp;
    }//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp

    public UnaryOp UnaryOp() {
        UnaryOp unaryOp = new UnaryOp();
        if (lexer.getType() != LexType.PLUS && lexer.getType() != LexType.MINU && lexer.getType() != LexType.NOT)
            error();
        unaryOp.unaryOp = lexer.getToken();
        if (lexer.getType() == LexType.PLUS) {
            stringBuilder.append("PLUS ").append(unaryOp.unaryOp).append("\n");
        } else if (lexer.getType() == LexType.MINU) {
            stringBuilder.append("MINU ").append(unaryOp.unaryOp).append("\n");
        } else if (lexer.getType() == LexType.NOT) {
            stringBuilder.append("NOT ").append(unaryOp.unaryOp).append("\n");
        }
        lexer.next();
        return unaryOp;
    }//UnaryOp → '+' | '−' | '!'

    public FuncRParams FuncRParams() {
        FuncRParams funcRParams = new FuncRParams();
        funcRParams.expArrayList.add(Exp());
        stringBuilder.append("<Exp>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            stringBuilder.append("COMMA ,\n");
            lexer.next();
            funcRParams.expArrayList.add(Exp());
            stringBuilder.append("<Exp>\n");
        }
        return funcRParams;
    }//FuncRParams → Exp { ',' Exp }

    private MulExp MulExp() {
        MulExp mulExp = new MulExp();
        mulExp.unaryExpArrayList.add(UnaryExp());
        stringBuilder.append("<UnaryExp>\n");
        while (true) {
            if (lexer.getType() != LexType.MULT && lexer.getType() != LexType.DIV && lexer.getType() != LexType.MOD)
                break;
            stringBuilder.append("<MulExp>\n");
            mulExp.mulOp.add(lexer.getToken());
            if (lexer.getType() == LexType.MULT) stringBuilder.append("MULT *\n");
            else if (lexer.getType() == LexType.DIV) stringBuilder.append("DIV /\n");
            else if (lexer.getType() == LexType.MOD) stringBuilder.append("MOD %\n");
            lexer.next();
            mulExp.unaryExpArrayList.add(UnaryExp());
            stringBuilder.append("<UnaryExp>\n");
        }
        return mulExp;
    }

    private AddExp AddExp() {
        AddExp addExp = new AddExp();
        addExp.mulExpArrayList.add(MulExp());
        stringBuilder.append("<MulExp>\n");
        while (true) {
            if (lexer.getType() != LexType.PLUS && lexer.getType() != LexType.MINU)
                break;
            stringBuilder.append("<AddExp>\n");
            addExp.addOp.add(lexer.getToken());
            if (lexer.getType() == LexType.PLUS) stringBuilder.append("PLUS +\n");
            else stringBuilder.append("MINU -\n");
            lexer.next();
            addExp.mulExpArrayList.add(MulExp());
            stringBuilder.append("<MulExp>\n");
        }
        return addExp;
    }

    private RelExp RelExp() {
        RelExp relExp = new RelExp();
        relExp.addExpArrayList.add(AddExp());
        stringBuilder.append("<AddExp>\n");
        while (true) {
            if (lexer.getType() != LexType.LSS && lexer.getType() != LexType.LEQ &&
                    lexer.getType() != LexType.GRE && lexer.getType() != LexType.GEQ) break;
            stringBuilder.append("<RelExp>\n");
            relExp.RelOp.add(lexer.getToken());
            if (lexer.getType() == LexType.LSS) stringBuilder.append("LSS <\n");
            else if (lexer.getType() == LexType.LEQ) stringBuilder.append("LEQ <=\n");
            else if (lexer.getType() == LexType.GRE) stringBuilder.append("GRE >\n");
            else stringBuilder.append("GEQ >=\n");
            lexer.next();
            relExp.addExpArrayList.add(AddExp());
            stringBuilder.append("<AddExp>\n");
        }
        return relExp;
    }

    private EqExp EqExp() {
        EqExp eqExp = new EqExp();
        eqExp.relExpArrayList.add(RelExp());
        stringBuilder.append("<RelExp>\n");
        while (true) {
            if (lexer.getType() != LexType.EQL && lexer.getType() != LexType.NEQ) break;
            stringBuilder.append("<EqExp>\n");
            eqExp.eqOp.add(lexer.getToken());
            if (lexer.getType() == LexType.EQL) stringBuilder.append("EQL ==\n");
            else stringBuilder.append("NEQ !=\n");
            lexer.next();
            eqExp.relExpArrayList.add(RelExp());
            stringBuilder.append("<RelExp>\n");
        }
        return eqExp;
    }

    private LAndExp LAndExp() {
        LAndExp lAndExp = new LAndExp();
        lAndExp.eqExpArrayList.add(EqExp());
        stringBuilder.append("<EqExp>\n");
        while (true) {
            if (lexer.getType() != LexType.AND) break;
            stringBuilder.append("<LAndExp>\n");
            stringBuilder.append("AND &&\n");
            lexer.next();
            lAndExp.eqExpArrayList.add(EqExp());
            stringBuilder.append("<EqExp>\n");
        }
        return lAndExp;
    }

    private LOrExp LOrExp() {
        LOrExp lOrExp = new LOrExp();
        lOrExp.lAndExpArrayList.add(LAndExp());
        stringBuilder.append("<LAndExp>\n");
        while (true) {
            if (lexer.getType() != LexType.OR) break;
            stringBuilder.append("<LOrExp>\n");
            stringBuilder.append("OR ||\n");
            lexer.next();
            lOrExp.lAndExpArrayList.add(LAndExp());
            stringBuilder.append("<LAndExp>\n");
        }
        return lOrExp;
    }

    private ConstExp ConstExp() {
        ConstExp constExp = new ConstExp();
        constExp.addExp = AddExp();
        stringBuilder.append("<AddExp>\n");
        return constExp;
    }

    private void error() {
        System.out.println(lexer.getToken());
        System.out.println("error");
    }


    public void printf() {
//        System.out.println(stringBuilder);
    }
}
