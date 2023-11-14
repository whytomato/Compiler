package IR;

import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Function;
import IR.Value.Instructions.*;
import IR.Value.Value;

public class IRBuildFactory {
    private static IRBuildFactory factory = null;
    IRModule module = IRModule.getInstance();

    private IRBuildFactory() {

    }

    public static IRBuildFactory getInstance() {
        if (factory == null) factory = new IRBuildFactory();
        return factory;
    }

    //    public BasicBlock buildBasicBlock(Function parentFunc) {
//    }
//
//    public Function buildFunction(String name, String type, IRModule module) {
//    }
//
//    public Const buildCalculateNumber(Const _left, Const _right, String op) {
//    }
//
//    public AllocInst buildAllocInst(Type type, BasicBlock bb) {
//    }
//
//    public StoreInst buildStoreInst(Value value, Value pointer, BasicBlock bb) {
//    }
    public Integer buildCalculateNumber(int left, int right, String op) {
        module.getVisitor().getOpList().remove(module.getVisitor().getOpList().size() - 1);
        switch (op) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                return left / right;
            case "%":
                return left % right;
            default:
                return null;
        }
    }

    public CalculateInst buildCalculateValue(Value left, Value right, String op, Type type, BasicBlock bb) {
        module.getVisitor().getOpList().remove(module.getVisitor().getOpList().size() - 1);
        CalculateInst calculateInst = new CalculateInst(left, right, null, type, bb);
        String memName = "%v" + calculateInst.getValNumber();
        calculateInst.setMemName(memName);
        left.addUse(new Use(left, calculateInst));
        right.addUse(new Use(right, calculateInst));
        bb.getInstructions().add(calculateInst);
        switch (op) {
            case "+":
                calculateInst.setCalType("add");
                return calculateInst;
            case "-":
                calculateInst.setCalType("sub");
                return calculateInst;
            case "*":
                calculateInst.setCalType("mul");
                return calculateInst;

            case "/":
                calculateInst.setCalType("sdiv");
                return calculateInst;
            case "%":
                calculateInst.setCalType("srem");
                return calculateInst;
            default:
                return null;
        }
    }

    public BasicBlock buildBasicBlock(Function parentFunc) {
        BasicBlock basicBlock = new BasicBlock();
        parentFunc.getBasicBlocks().add(basicBlock);
        return basicBlock;
    }

    public Function buildFunction(String name, Type type) {
        Function function = new Function(name, type);
        module.getFunctions().add(function);
        return function;
    }

    public AllocInst buildAllocInst(String name, Type type, BasicBlock bb) {
        AllocInst allocInst = new AllocInst(name, type, bb);
        String memName = "%v" + allocInst.getValNumber();
        allocInst.setMemName(memName);
        bb.getInstructions().add(allocInst);
        return allocInst;
    }

    public LoadInst buildLoadInst(Type type, String name, BasicBlock bb) {
        LoadInst loadInst = new LoadInst(name, type, bb);
        loadInst.setPointer(module.find(name));
        String memName = "%v" + loadInst.getValNumber();
        loadInst.setMemName(memName);
        bb.getInstructions().add(loadInst);
        return loadInst;
    }

    public StoreInst buildStoreInst(Type type, BasicBlock bb, Value value, Value pointer) {
        StoreInst storeInst = new StoreInst(null, type, bb, value, pointer);

        bb.getInstructions().add(storeInst);
        return storeInst;
    }

    public ReturnInst buildReturn(String name, Type type, BasicBlock bb, Value value) {
        ReturnInst returnInst = new ReturnInst(name, type, value);
        bb.getInstructions().add(returnInst);
        return returnInst;
    }

//    public GetElementPtr buildGetElementPtr() {
//
//    }
//    public StoreInst buildStoreInst(Value value, Value pointer, BasicBlock bb) {
//
//    }
}
