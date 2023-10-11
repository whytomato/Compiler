package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class PrimaryExp {
    public Exp exp;
    public LVal lVal;
    public Number number;
    static Lexer lexer = Lexer.getInstance();

    public PrimaryExp() {
        this.exp = null;
        this.lVal = null;
        this.number = null;
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
            Parser.stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.RPARENT) Parser.error();
            Parser.stringBuilder.append("RPARENT )\n");
            lexer.next();
        } else if (lexer.getType() == LexType.IDENFR) {
            primaryExp.lVal = LVal.LValParse();
            Parser.stringBuilder.append("<LVal>\n");
        } else if (lexer.getType() == LexType.INTCON) {
            primaryExp.number = Number.NumberParse();
            Parser.stringBuilder.append("<Number>\n");
        } else Parser.error();
        return primaryExp;
    }//PrimaryExp → '(' Exp ')' | LVal | Number
}
