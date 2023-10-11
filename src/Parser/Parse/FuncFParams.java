package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class FuncFParams {
    public ArrayList<FuncFParam> funcFParamArrayList;
    static Lexer lexer = Lexer.getInstance();

    public FuncFParams() {
        this.funcFParamArrayList = new ArrayList<>();
    }

    public static FuncFParams FuncFParamsParse() {
        FuncFParams funcFParams = new FuncFParams();
        funcFParams.funcFParamArrayList.add(FuncFParam.FuncFParamParse());
        Parser.stringBuilder.append("<FuncFParam>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            Parser.stringBuilder.append("COMMA ,\n");
            lexer.next();
            funcFParams.funcFParamArrayList.add(FuncFParam.FuncFParamParse());
            Parser.stringBuilder.append("<FuncFParam>\n");
        }
        return funcFParams;
    }
}
