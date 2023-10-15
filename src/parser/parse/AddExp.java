package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

import java.util.ArrayList;

public class AddExp {
    public ArrayList<MulExp> mulExpArrayList;
    public ArrayList<String> addOp;
    public int dim;
    static Lexer lexer = Lexer.getInstance();

    public AddExp() {
        this.dim = 0;
        this.mulExpArrayList = new ArrayList<>();
        this.addOp = new ArrayList<>();
    }

    public static AddExp AddExpParse(LVal lVal) {
        AddExp addExp = new AddExp();
        addExp.mulExpArrayList.add(MulExp.MulExpParse(lVal));
        Parser.stringBuilder.append("<MulExp>\n");
        while (true) {
            if (lexer.getType() != LexType.PLUS && lexer.getType() != LexType.MINU)
                break;
            Parser.stringBuilder.append("<AddExp>\n");
            addExp.addOp.add(lexer.getToken());
            if (lexer.getType() == LexType.PLUS) Parser.stringBuilder.append("PLUS +\n");
            else Parser.stringBuilder.append("MINU -\n");
            lexer.next();
            addExp.mulExpArrayList.add(MulExp.MulExpParse());
            Parser.stringBuilder.append("<MulExp>\n");
        }
        return addExp;
    }//AddExp → MulExp | AddExp ('+' | '−') MulExp

    public static AddExp AddExpParse() {
        AddExp addExp = new AddExp();
        addExp.mulExpArrayList.add(MulExp.MulExpParse());
        Parser.stringBuilder.append("<MulExp>\n");
        while (true) {
            if (lexer.getType() != LexType.PLUS && lexer.getType() != LexType.MINU)
                break;
            Parser.stringBuilder.append("<AddExp>\n");
            addExp.addOp.add(lexer.getToken());
            if (lexer.getType() == LexType.PLUS) Parser.stringBuilder.append("PLUS +\n");
            else Parser.stringBuilder.append("MINU -\n");
            lexer.next();
            addExp.mulExpArrayList.add(MulExp.MulExpParse());
            Parser.stringBuilder.append("<MulExp>\n");
        }
        for (int i = 0; i < addExp.mulExpArrayList.size(); i++) {
            if (addExp.dim < addExp.mulExpArrayList.get(i).dim) {
                addExp.dim = addExp.mulExpArrayList.get(i).dim;
            }
        }
        return addExp;
    }//AddExp → MulExp | AddExp ('+' | '−') MulExp
}
