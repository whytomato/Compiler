package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

public class PrimaryExp {
    public Exp exp;
    public int dim;
    public LVal lVal;
    public Number number;
    static Lexer lexer = Lexer.getInstance();

    public PrimaryExp() {
        this.exp = null;
        this.lVal = null;
        this.number = null;
        this.dim = 0;
    }

    public static PrimaryExp PrimaryExpParse(LVal lVal) {
        PrimaryExp primaryExp = new PrimaryExp();
        primaryExp.lVal = lVal;
        Parser.stringBuilder.append("<LVal>\n");
        return primaryExp;
    }

    public static PrimaryExp PrimaryExpParse() {
        PrimaryExp primaryExp = new PrimaryExp();
        if (lexer.getType() == LexType.LPARENT) {
            Parser.stringBuilder.append("LPARENT (\n");
            lexer.next();
            primaryExp.exp = Exp.ExpParse();
            primaryExp.dim = primaryExp.exp.dim;
            Parser.stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.RPARENT) Parser.error();
            Parser.stringBuilder.append("RPARENT )\n");
            lexer.next();
        } else if (lexer.getType() == LexType.IDENFR) {
            primaryExp.lVal = LVal.LValParse();
            primaryExp.dim = primaryExp.lVal.Dim;
            Parser.stringBuilder.append("<LVal>\n");
        } else if (lexer.getType() == LexType.INTCON) {
            primaryExp.number = Number.NumberParse();
            primaryExp.dim = 0;
            Parser.stringBuilder.append("<Number>\n");
        } else Parser.error();
        return primaryExp;
    }//PrimaryExp → '(' Exp ')' | LVal | Number
}
