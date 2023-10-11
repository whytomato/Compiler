package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class FuncRParams {
    public ArrayList<Exp> expArrayList;
    static Lexer lexer = Lexer.getInstance();

    public FuncRParams() {
        this.expArrayList = new ArrayList<>();
    }

    public static FuncRParams FuncRParamsParse() {
        FuncRParams funcRParams = new FuncRParams();
        funcRParams.expArrayList.add(Exp.ExpParse());
        Parser.stringBuilder.append("<Exp>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            Parser.stringBuilder.append("COMMA ,\n");
            lexer.next();
            funcRParams.expArrayList.add(Exp.ExpParse());
            Parser.stringBuilder.append("<Exp>\n");
        }
        return funcRParams;
    }//FuncRParams → Exp { ',' Exp }
}
