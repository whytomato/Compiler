package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

import java.util.ArrayList;

public class FuncFParams {
    public ArrayList<FuncFParam> funcFParamArrayList;
    public ArrayList<Integer> dimList;
    static Lexer lexer = Lexer.getInstance();

    public FuncFParams() {
        this.funcFParamArrayList = new ArrayList<>();
        this.dimList = new ArrayList<>();
    }

    public static FuncFParams FuncFParamsParse() {
        FuncFParams funcFParams = new FuncFParams();
        FuncFParam funcFParam = FuncFParam.FuncFParamParse();
        funcFParams.funcFParamArrayList.add(funcFParam);
        funcFParams.dimList.add(funcFParam.dim);
        Parser.stringBuilder.append("<FuncFParam>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            Parser.stringBuilder.append("COMMA ,\n");
            lexer.next();
            funcFParam = FuncFParam.FuncFParamParse();
            funcFParams.funcFParamArrayList.add(funcFParam);
            funcFParams.dimList.add(funcFParam.dim);
            Parser.stringBuilder.append("<FuncFParam>\n");
        }
        return funcFParams;
    }
}
