package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

import java.util.ArrayList;

public class MulExp {
    public ArrayList<UnaryExp> unaryExpArrayList;
    public ArrayList<String> mulOp;
    static Lexer lexer = Lexer.getInstance();
    public int dim;

    public MulExp() {
        this.unaryExpArrayList = new ArrayList<>();
        this.mulOp = new ArrayList<>();
        this.dim = 0;
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
        boolean flag = false;
        MulExp mulExp = new MulExp();
        mulExp.unaryExpArrayList.add(UnaryExp.UnaryExpParse());
        Parser.stringBuilder.append("<UnaryExp>\n");
        while (true) {
            if (lexer.getType() != LexType.MULT && lexer.getType() != LexType.DIV && lexer.getType() != LexType.MOD)
                break;
            Parser.stringBuilder.append("<MulExp>\n");
            flag = true;

            mulExp.mulOp.add(lexer.getToken());
            if (lexer.getType() == LexType.MULT) Parser.stringBuilder.append("MULT *\n");
            else if (lexer.getType() == LexType.DIV) Parser.stringBuilder.append("DIV /\n");
            else if (lexer.getType() == LexType.MOD) Parser.stringBuilder.append("MOD %\n");
            lexer.next();
            mulExp.unaryExpArrayList.add(UnaryExp.UnaryExpParse());
            Parser.stringBuilder.append("<UnaryExp>\n");
        }
        if (flag) mulExp.dim = 0;
        else {
            mulExp.dim = mulExp.unaryExpArrayList.get(0).dim;
        }
        return mulExp;
    }
}
