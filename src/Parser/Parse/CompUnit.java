package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

//CompUnit -> {Decl} {FuncDef} MainFuncDef
public class CompUnit {
    public ArrayList<Decl> declArrayList;
    public ArrayList<FuncDef> funcDefArrayList;
    public MainFuncDef mainFuncDef;
    Lexer lexer = Lexer.getInstance();


    public CompUnit() {
        this.declArrayList = new ArrayList<>();
        this.funcDefArrayList = new ArrayList<>();
        this.mainFuncDef = null;
    }

    public void CompUnitParse() {
        while (lexer.getPreType() != LexType.MAINTK && lexer.getPrePreType() != LexType.LPARENT) {
            Parser.compUnit.declArrayList.add(Decl.DeclParse());
        }
        while (lexer.getPreType() != LexType.MAINTK) {
            Parser.compUnit.funcDefArrayList.add(FuncDef.FuncDefParse());
            Parser.stringBuilder.append("<FuncDef>\n");
        }
        Parser.compUnit.mainFuncDef = MainFuncDef.MainFuncDefParse();
        Parser.stringBuilder.append("<MainFuncDef>\n");
    }//CompUnit → {Decl} {FuncDef} MainFuncDef
}

