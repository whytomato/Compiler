package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.Func;
import symbol.TableRoot;

import java.util.Objects;

public class FuncDef {
    public FuncType funcType;
    public String ident;
    public FuncFParams funcFParams;
    public Block block;
    static Lexer lexer = Lexer.getInstance();
    static Error error = Error.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();

    public FuncDef() {
        this.funcType = null;
        this.ident = "";
        this.funcFParams = null;
        this.block = null;
    }

    public static FuncDef FuncDefParse() {
        boolean flag = true, flag1 = true;
        FuncDef funcDef = new FuncDef();
        funcDef.funcType = FuncType.FuncTypeParse();
        Parser.stringBuilder.append("<FuncType>\n");
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        funcDef.ident = lexer.getToken();
        if (tableRoot.search(false, funcDef.ident)) {
            flag = false;
            error.getError('b', lexer.getLine());
        }
        Parser.stringBuilder.append("IDENFR ").append(funcDef.ident).append("\n");
        lexer.next();
        if (lexer.getType() != LexType.LPARENT) Parser.error();
        Parser.stringBuilder.append("LPARENT (\n");
        tableRoot.save(funcDef.ident);
        lexer.next();
        funcDef.funcFParams = new FuncFParams();
        if (lexer.getType() != LexType.RPARENT) {
            if (lexer.getType() != LexType.LBRACE) {
                funcDef.funcFParams = FuncFParams.FuncFParamsParse();
                Parser.stringBuilder.append("<FuncFParams>\n");
            }
            if (lexer.getType() != LexType.RPARENT) {
                flag1 = false;
                error.getError('j', lexer.getLastLine());
            }
        }
        if (flag) {
            int retype = 0;
            if (Objects.equals(funcDef.funcType.funcType, "void")) {
                retype = 1;
            }
            Func func = new Func(tableRoot.getNum() - 1,
                    funcDef.ident, -1, retype, funcDef.funcFParams.dimList);
            tableRoot.getSymbolTable(tableRoot.getNum() - 1).save(func);
        }
        if (flag1) {
            Parser.stringBuilder.append("RPARENT )\n");
            lexer.next();
        }
        funcDef.block = Block.BlockParse(true);
        Parser.stringBuilder.append("<Block>\n");
        return funcDef;
    }//FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
}
