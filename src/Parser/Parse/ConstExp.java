package Parser.Parse;

import Parser.Parser;

public class ConstExp {
    public AddExp addExp;

    public ConstExp() {
        this.addExp = null;
    }

    public static ConstExp ConstExpParse() {
        ConstExp constExp = new ConstExp();
        constExp.addExp = AddExp.AddExpParse();
        Parser.stringBuilder.append("<AddExp>\n");
        return constExp;
    }
}
