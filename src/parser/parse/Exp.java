package parser.parse;

import parser.Parser;

public class Exp {
    public AddExp addExp;
    public int dim;

    public Exp() {
        this.dim = 0;
        this.addExp = null;
    }

    public static Exp ExpParse(LVal lVal) {
        Exp exp = new Exp();
        exp.addExp = AddExp.AddExpParse(lVal);
        Parser.stringBuilder.append("<AddExp>\n");
        return exp;
    }

    public static Exp ExpParse() {
        Exp exp = new Exp();
        AddExp addExp = AddExp.AddExpParse();
        exp.addExp = addExp;
        exp.dim = addExp.dim;
        Parser.stringBuilder.append("<AddExp>\n");
        return exp;
    }//Exp → AddExp
}
