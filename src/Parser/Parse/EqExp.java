package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class EqExp {
    public ArrayList<RelExp> relExpArrayList;
    public ArrayList<String> eqOp;
    static Lexer lexer = Lexer.getInstance();

    public EqExp() {
        this.relExpArrayList = new ArrayList<>();
        this.eqOp = new ArrayList<>();
    }

    public static EqExp EqExpParse() {
        EqExp eqExp = new EqExp();
        eqExp.relExpArrayList.add(RelExp.RelExpParse());
        Parser.stringBuilder.append("<RelExp>\n");
        while (true) {
            if (lexer.getType() != LexType.EQL && lexer.getType() != LexType.NEQ) break;
            Parser.stringBuilder.append("<EqExp>\n");
            eqExp.eqOp.add(lexer.getToken());
            if (lexer.getType() == LexType.EQL) Parser.stringBuilder.append("EQL ==\n");
            else Parser.stringBuilder.append("NEQ !=\n");
            lexer.next();
            eqExp.relExpArrayList.add(RelExp.RelExpParse());
            Parser.stringBuilder.append("<RelExp>\n");
        }
        return eqExp;
    }
}
