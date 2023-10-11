package Parser.Parse;

import Parser.Parser;

public class Cond {
    public LOrExp lOrExp;

    public Cond() {
        this.lOrExp = null;
    }

    public static Cond CondParse() {
        Cond cond = new Cond();
        cond.lOrExp = LOrExp.LOrExpParse();
        Parser.stringBuilder.append("<LOrExp>\n");
        return cond;
    }//Cond → LOrExp
}
