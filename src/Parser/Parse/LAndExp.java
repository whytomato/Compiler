package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class LAndExp {
    public ArrayList<EqExp> eqExpArrayList;
    static Lexer lexer = Lexer.getInstance();

    public LAndExp() {
        this.eqExpArrayList = new ArrayList<>();
    }

    public static LAndExp LAndExpParse() {
        LAndExp lAndExp = new LAndExp();
        lAndExp.eqExpArrayList.add(EqExp.EqExpParse());
        Parser.stringBuilder.append("<EqExp>\n");
        while (true) {
            if (lexer.getType() != LexType.AND) break;
            Parser.stringBuilder.append("<LAndExp>\n");
            Parser.stringBuilder.append("AND &&\n");
            lexer.next();
            lAndExp.eqExpArrayList.add(EqExp.EqExpParse());
            Parser.stringBuilder.append("<EqExp>\n");
        }
        return lAndExp;
    }
}
