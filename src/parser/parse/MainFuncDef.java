package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.Func;
import symbol.TableRoot;

import java.util.ArrayList;

public class MainFuncDef {
    public Block block;
    static Lexer lexer = Lexer.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();

    public MainFuncDef() {
        this.block = null;
    }

    public static MainFuncDef MainFuncDefParse() {
        MainFuncDef mainFuncDef = new MainFuncDef();
        Parser.stringBuilder.append("INTTK int\n");
        lexer.next();
        Parser.stringBuilder.append("MAINTK main\n");
        lexer.next();
        tableRoot.createTable("main");
        Func func = new Func(tableRoot.getNum() - 1, "main", -1, 0, new ArrayList<>());
        tableRoot.getSymbolTable(tableRoot.getNum() - 1).save(func);
        Parser.stringBuilder.append("LPARENT (\n");
        lexer.next();
        if (lexer.getType() != LexType.RPARENT) Parser.error();
        Parser.stringBuilder.append("RPARENT )\n");
        lexer.next();
        mainFuncDef.block = Block.BlockParse(true);
        Parser.stringBuilder.append("<Block>\n");
        return mainFuncDef;
    }//MainFuncDef → 'int' 'main' '(' ')' Block
}
