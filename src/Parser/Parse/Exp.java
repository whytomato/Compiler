package Parser.Parse;

import Parser.Parser;

public class Exp {
    public AddExp addExp;

    public Exp() {
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
        exp.addExp = AddExp.AddExpParse();
        Parser.stringBuilder.append("<AddExp>\n");
        return exp;
    }//Exp → AddExp
}
