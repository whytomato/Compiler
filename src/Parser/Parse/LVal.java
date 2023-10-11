package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class LVal {
    public String ident;
    public ArrayList<Exp> expArrayList;
    static Lexer lexer = Lexer.getInstance();

    public LVal() {
        this.ident = "";
        this.expArrayList = new ArrayList<>();
    }

    public static LVal LValParse() {
        LVal lVal = new LVal();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        lVal.ident = lexer.getToken();
        Parser.stringBuilder.append("IDENFR ").append(lVal.ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            lVal.expArrayList.add(Exp.ExpParse());
            Parser.stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.RBRACK) Parser.error();
            Parser.stringBuilder.append("RBRACK ]\n");
            lexer.next();
        }
        return lVal;
    }//LVal → Ident {'[' Exp ']'}
}
