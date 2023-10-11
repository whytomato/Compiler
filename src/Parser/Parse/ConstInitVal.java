package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class ConstInitVal {
    public ConstExp constExp;
    public ArrayList<ConstInitVal> constInitValArrayList;
    static Lexer lexer = Lexer.getInstance();

    public ConstInitVal() {
        this.constExp = null;
        this.constInitValArrayList = new ArrayList<>();
    }

    public static ConstInitVal ConstInitValParse() {
        ConstInitVal constInitval = new ConstInitVal();
        if (lexer.getType() != LexType.LBRACE) {
            constInitval.constExp = ConstExp.ConstExpParse();
            Parser.stringBuilder.append("<ConstExp>\n");
        } else {
            Parser.stringBuilder.append("LBRACE {\n");
            lexer.next();
            if (lexer.getType() != LexType.RBRACE) {
                constInitval.constInitValArrayList.add(ConstInitValParse());
                Parser.stringBuilder.append("<ConstInitVal>\n");
                while (true) {
                    if (lexer.getType() != LexType.COMMA) break;
                    Parser.stringBuilder.append("COMMA ,\n");
                    lexer.next();
                    constInitval.constInitValArrayList.add(ConstInitValParse());
                    Parser.stringBuilder.append("<ConstInitVal>\n");
                }
                if (lexer.getType() != LexType.RBRACE) Parser.error();
            }
            Parser.stringBuilder.append("RBRACE }\n");
            lexer.next();
        }
        return constInitval;
    }//ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
}
