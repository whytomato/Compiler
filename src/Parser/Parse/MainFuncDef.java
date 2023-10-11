package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class MainFuncDef {
    public Block block;
    static Lexer lexer = Lexer.getInstance();

    public MainFuncDef() {
        this.block = null;
    }

    public static MainFuncDef MainFuncDefParse() {
        MainFuncDef mainFuncDef = new MainFuncDef();
        Parser.stringBuilder.append("INTTK int\n");
        lexer.next();
        Parser.stringBuilder.append("MAINTK main\n");
        lexer.next();
        Parser.stringBuilder.append("LPARENT (\n");
        lexer.next();
        if (lexer.getType() != LexType.RPARENT) Parser.error();
        Parser.stringBuilder.append("RPARENT )\n");
        lexer.next();
        mainFuncDef.block = Block.BlockParse();
        Parser.stringBuilder.append("<Block>\n");
        return mainFuncDef;
    }//MainFuncDef → 'int' 'main' '(' ')' Block
}
