package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class FuncDef {
    public FuncType funcType;
    public String ident;
    public FuncFParams funcFParams;
    public Block block;
    static Lexer lexer = Lexer.getInstance();

    public FuncDef() {
        this.funcType = null;
        this.ident = "";
        this.funcFParams = null;
        this.block = null;
    }

    public static FuncDef FuncDefParse() {
        FuncDef funcDef = new FuncDef();
        funcDef.funcType = FuncType.FuncTypeParse();
        Parser.stringBuilder.append("<FuncType>\n");
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        funcDef.ident = lexer.getToken();
        Parser.stringBuilder.append("IDENFR ").append(funcDef.ident).append("\n");
        lexer.next();
        if (lexer.getType() != LexType.LPARENT) Parser.error();
        Parser.stringBuilder.append("LPARENT (\n");
        lexer.next();
        if (lexer.getType() != LexType.RPARENT) {
            funcDef.funcFParams = FuncFParams.FuncFParamsParse();
            Parser.stringBuilder.append("<FuncFParams>\n");
            if (lexer.getType() != LexType.RPARENT) Parser.error();
        }
        Parser.stringBuilder.append("RPARENT )\n");
        lexer.next();
        funcDef.block = Block.BlockParse();
        Parser.stringBuilder.append("<Block>\n");
        return funcDef;
    }//FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
}
