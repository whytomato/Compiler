package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.TableRoot;

import java.util.ArrayList;

//CompUnit -> {Decl} {FuncDef} MainFuncDef
public class CompUnit {
    public ArrayList<Decl> declArrayList;
    public ArrayList<FuncDef> funcDefArrayList;
    public MainFuncDef mainFuncDef;
    Lexer lexer = Lexer.getInstance();
    TableRoot tableRoot = TableRoot.getInstance();


    public CompUnit() {
        this.declArrayList = new ArrayList<>();
        this.funcDefArrayList = new ArrayList<>();
        this.mainFuncDef = null;
    }

    public void CompUnitParse() {
        while (lexer.getPreType() != LexType.MAINTK && lexer.getPrePreType() != LexType.LPARENT) {
            if (tableRoot.getNum() != 1) tableRoot.save();
            Parser.compUnit.declArrayList.add(Decl.DeclParse());
        }
        while (lexer.getPreType() != LexType.MAINTK) {
            if (tableRoot.getNum() != 1) tableRoot.save();
            Parser.compUnit.funcDefArrayList.add(FuncDef.FuncDefParse());
            Parser.stringBuilder.append("<FuncDef>\n");
        }
        if (tableRoot.getNum() != 1) tableRoot.save();
        Parser.compUnit.mainFuncDef = MainFuncDef.MainFuncDefParse();
        Parser.stringBuilder.append("<MainFuncDef>\n");
    }//CompUnit → {Decl} {FuncDef} MainFuncDef
}

