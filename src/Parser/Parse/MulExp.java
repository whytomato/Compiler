package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class MulExp {
    public ArrayList<UnaryExp> unaryExpArrayList;
    public ArrayList<String> mulOp;
    static Lexer lexer = Lexer.getInstance();

    public MulExp() {
        this.unaryExpArrayList = new ArrayList<>();
        this.mulOp = new ArrayList<>();
    }

    public static MulExp MulExpParse(LVal lVal) {
        MulExp mulExp = new MulExp();
        mulExp.unaryExpArrayList.add(UnaryExp.UnaryExpParse(lVal));
        Parser.stringBuilder.append("<UnaryExp>\n");
        while (true) {
            if (lexer.getType() != LexType.MULT && lexer.getType() != LexType.DIV && lexer.getType() != LexType.MOD)
                break;
            Parser.stringBuilder.append("<MulExp>\n");
            mulExp.mulOp.add(lexer.getToken());
            if (lexer.getType() == LexType.MULT) Parser.stringBuilder.append("MULT *\n");
            else if (lexer.getType() == LexType.DIV) Parser.stringBuilder.append("DIV /\n");
            else if (lexer.getType() == LexType.MOD) Parser.stringBuilder.append("MOD %\n");
            lexer.next();
            mulExp.unaryExpArrayList.add(UnaryExp.UnaryExpParse());
            Parser.stringBuilder.append("<UnaryExp>\n");
        }
        return mulExp;
    }//MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp

    public static MulExp MulExpParse() {
        MulExp mulExp = new MulExp();
        mulExp.unaryExpArrayList.add(UnaryExp.UnaryExpParse());
        Parser.stringBuilder.append("<UnaryExp>\n");
        while (true) {
            if (lexer.getType() != LexType.MULT && lexer.getType() != LexType.DIV && lexer.getType() != LexType.MOD)
                break;
            Parser.stringBuilder.append("<MulExp>\n");
            mulExp.mulOp.add(lexer.getToken());
            if (lexer.getType() == LexType.MULT) Parser.stringBuilder.append("MULT *\n");
            else if (lexer.getType() == LexType.DIV) Parser.stringBuilder.append("DIV /\n");
            else if (lexer.getType() == LexType.MOD) Parser.stringBuilder.append("MOD %\n");
            lexer.next();
            mulExp.unaryExpArrayList.add(UnaryExp.UnaryExpParse());
            Parser.stringBuilder.append("<UnaryExp>\n");
        }
        return mulExp;
    }
}
