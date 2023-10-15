package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

import java.util.ArrayList;

public class InitVal {
    public Exp exp;
    public ArrayList<InitVal> initValArrayList;
    static Lexer lexer = Lexer.getInstance();

    public InitVal() {
        this.exp = null;
        this.initValArrayList = new ArrayList<>();
    }

    public static InitVal InitValParse() {
        InitVal initVal = new InitVal();
        if (lexer.getType() != LexType.LBRACE) {
            initVal.exp = Exp.ExpParse();
            Parser.stringBuilder.append("<Exp>\n");
        } else {
            Parser.stringBuilder.append("LBRACE {\n");
            lexer.next();
            if (lexer.getType() != LexType.RBRACE) {
                initVal.initValArrayList.add(InitValParse());
                Parser.stringBuilder.append("<InitVal>\n");
                while (true) {
                    if (lexer.getType() != LexType.COMMA) break;
                    Parser.stringBuilder.append("COMMA ,\n");
                    lexer.next();
                    initVal.initValArrayList.add(InitValParse());
                    Parser.stringBuilder.append("<InitVal>\n");
                }
                if (lexer.getType() != LexType.RBRACE) Parser.error();
            }
            Parser.stringBuilder.append("RBRACE }\n");
            lexer.next();
        }
        return initVal;
    }//InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
}
