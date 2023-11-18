package IR;

import IR.Value.Function;
import IR.Value.GlobalValue;
import IR.Value.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class IRModule {
    private static IRModule module = null;

    private ArrayList<GlobalValue> globalValues;
    private ArrayList<Function> functions;
    private ArrayList<HashMap<String, Value>> symTables;
    private Visitor visitor = null;

    private IRModule() {
        this.symTables = new ArrayList<>();
        this.globalValues = new ArrayList<>();
        this.functions = new ArrayList<>();
    }

    public static IRModule getInstance() {
        if (module == null) module = new IRModule();
        return module;
    }

    public Value find(String ident) {
        int len = symTables.size();
        for (int i = len - 1; i >= 0; i--) {
            HashMap<String, Value> symTable = symTables.get(i);
            Value res = symTable.get(ident);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    public void pushSymbol(String ident, Value value) {
        int len = symTables.size();
        symTables.get(len - 1).put(ident, value);
    }

    public void pushSymTable() {
        symTables.add(new HashMap<>());
    }

    public String visit() {
        if (visitor == null)
            visitor = new Visitor();
        visitor.visitCompUnit();
        return print();
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public ArrayList<HashMap<String, Value>> getSymTables() {
        return symTables;
    }

    public void popSymTable() {
        symTables.remove(symTables.size() - 1);
    }

    private String print() {
//        for (int i = 0; i < symTables.size(); i++) {
////            System.out.println(i);
//            HashMap<String, Value> symTable = symTables.get(i);
//            for (String key : symTable.keySet()) {
//                Value value = symTable.get(key);
//                if (value instanceof AllocInst) {
////                    System.out.println(key + ":" + ((AllocInst) symTable.get(key)).getMemName());
//                } else if (value instanceof GlobalValue) {
//                    System.out.println(key + ":" + ((GlobalValue) symTable.get(key)).getNum());
//                } else if (value instanceof Function) {
//                    System.out.println(key + ":");
//                }
//
//            }
//        }
        StringBuilder sb = new StringBuilder();
        sb.append("""
                declare i32 @getint()
                declare void @putint(i32)
                declare void @putch(i32)
                declare void @putstr(i8*)
                                
                """);
//        System.out.println(sb);
        for (GlobalValue globalValue : module.globalValues) {

            sb.append(globalValue);
            sb.append("\n");
//            System.out.println(globalValue);
        }
        sb.append("\n");
        for (Function function : module.functions) {
            sb.append(function);
//            System.out.println(function);
        }
        System.out.println(sb);
        return sb.toString();
//        for (int i = 0; i < module.functions.get(0).getBasicBlocks().get(0).getInstructions().size(); i++) {
//            Instruction instruction = module.functions.get(0).getBasicBlocks().get(0).getInstructions().get(i);
//            System.out.println(instruction);
//
//        }
    }

    public ArrayList<GlobalValue> getGlobalValues() {
        return globalValues;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }
}
