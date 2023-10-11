package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class RelExp {
    public ArrayList<AddExp> addExpArrayList;
    public ArrayList<String> RelOp;
    static Lexer lexer = Lexer.getInstance();

    public RelExp() {
        this.addExpArrayList = new ArrayList<>();
        this.RelOp = new ArrayList<>();
    }

    public static RelExp RelExpParse() {
        RelExp relExp = new RelExp();
        relExp.addExpArrayList.add(AddExp.AddExpParse());
        Parser.stringBuilder.append("<AddExp>\n");
        while (true) {
            if (lexer.getType() != LexType.LSS && lexer.getType() != LexType.LEQ &&
                    lexer.getType() != LexType.GRE && lexer.getType() != LexType.GEQ) break;
            Parser.stringBuilder.append("<RelExp>\n");
            relExp.RelOp.add(lexer.getToken());
            if (lexer.getType() == LexType.LSS) Parser.stringBuilder.append("LSS <\n");
            else if (lexer.getType() == LexType.LEQ) Parser.stringBuilder.append("LEQ <=\n");
            else if (lexer.getType() == LexType.GRE) Parser.stringBuilder.append("GRE >\n");
            else Parser.stringBuilder.append("GEQ >=\n");
            lexer.next();
            relExp.addExpArrayList.add(AddExp.AddExpParse());
            Parser.stringBuilder.append("<AddExp>\n");
        }
        return relExp;
    }
}
