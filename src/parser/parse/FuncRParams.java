package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.Func;
import symbol.Symbol;
import symbol.TableRoot;

import java.util.ArrayList;

public class FuncRParams {
    public ArrayList<Exp> expArrayList;
    public int num;
    static Lexer lexer = Lexer.getInstance();
    static Error error = Error.getInstance();

    public FuncRParams() {
        this.num = 0;
        this.expArrayList = new ArrayList<>();
    }

    public static FuncRParams FuncRParamsParse(String name, int line) {
        FuncRParams funcRParams = new FuncRParams();
        funcRParams.expArrayList.add(Exp.ExpParse());
        Parser.stringBuilder.append("<Exp>\n");
        funcRParams.num++;
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            Parser.stringBuilder.append("COMMA ,\n");
            lexer.next();
            funcRParams.expArrayList.add(Exp.ExpParse());
            Parser.stringBuilder.append("<Exp>\n");
            funcRParams.num++;
        }
        Symbol symbol = TableRoot.getInstance().getSymbol(name);
        if (symbol != null && symbol.getType() == -1) {
            Func func = (Func) symbol;
            for (int i = 0; i < func.getParamTypeList().size(); i++) {
                if (i < funcRParams.expArrayList.size()) {
                    int dim = funcRParams.expArrayList.get(i).dim;
                    if (dim == -1) error.getError('e', line);
                    else {
                        if (dim != func.getParamTypeList().get(i)) error.getError('e', line);
                    }
                }
            }
        }
        return funcRParams;
    }//FuncRParams → Exp { ',' Exp }
}
