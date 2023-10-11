package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class LOrExp {
    public ArrayList<LAndExp> lAndExpArrayList;
    static Lexer lexer = Lexer.getInstance();

    public LOrExp() {
        this.lAndExpArrayList = new ArrayList<>();
    }

    public static LOrExp LOrExpParse() {
        LOrExp lOrExp = new LOrExp();
        lOrExp.lAndExpArrayList.add(LAndExp.LAndExpParse());
        Parser.stringBuilder.append("<LAndExp>\n");
        while (true) {
            if (lexer.getType() != LexType.OR) break;
            Parser.stringBuilder.append("<LOrExp>\n");
            Parser.stringBuilder.append("OR ||\n");
            lexer.next();
            lOrExp.lAndExpArrayList.add(LAndExp.LAndExpParse());
            Parser.stringBuilder.append("<LAndExp>\n");
        }
        return lOrExp;
    }
}
