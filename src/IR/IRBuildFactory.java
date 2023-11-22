package IR;

import IR.Type.*;
import IR.Value.*;
import IR.Value.Instructions.*;
import parser.parse.FuncFParam;

import java.util.ArrayList;
import java.util.Objects;

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
        basicBlock.setBbName();
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

    public LoadInst buildLoadInst(Type type, Value pointer, BasicBlock bb) {
        LoadInst loadInst = new LoadInst(null, type, bb);

        loadInst.setPointer(pointer);
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

    public GetElementPtr buildGetElementPtr(BasicBlock bb, ArrayList<Value> offsets, Value value, int type, Boolean needLoad) {
        ArrayList<Integer> index;
        GetElementPtr getElementPtr = new GetElementPtr(null, null, true);
        getElementPtr.calIndex(offsets, value, type);
        if (((PointerType) value.getType()).getType() instanceof ArrayType) {
            String memName = "%v" + getElementPtr.getValNumber();
            getElementPtr.setMemName(memName);
//            getElementPtr.calIndex(offsets, value, type);
            int indexSize = getElementPtr.getIndex().size();
            int arraySize = ((ArrayType) ((PointerType) value.getType()).getType()).getArraySize().size();
            if (arraySize == indexSize) {
                getElementPtr.setType(new PointerType(new IntegerType()));
                bb.getInstructions().add(getElementPtr);
                LoadInst loadInst;
                if (needLoad)
                    buildLoadInst(((PointerType) getElementPtr.getType()).getType(), getElementPtr, bb);
                return getElementPtr;
            } else {
                Type arrayType = value.getType();
                GetElementPtr getElementPtr1 = null;
                for (int i = 0; i <= indexSize; i++) {
                    GetElementPtr getElementPtr2 = getElementPtr1;
                    getElementPtr1 = new GetElementPtr(null, null, true);
                    memName = "%v" + getElementPtr1.getValNumber();
                    getElementPtr1.setMemName(memName);
                    getElementPtr1.setPtrval(Objects.requireNonNullElse(getElementPtr2, value));
                    if (i == indexSize) {
                        getElementPtr1.getIndex().add(new Constant("0", new IntegerType()));
                    } else getElementPtr1.getIndex().add(getElementPtr.getIndex().get(i));

                    if (arrayType instanceof PointerType) {
                        arrayType = new PointerType(((ArrayType) ((PointerType) arrayType).getType()).getElementType());
                    }
                    getElementPtr1.setType(arrayType);
                    bb.getInstructions().add(getElementPtr1);
                }
                return getElementPtr1;
            }
        } else {
            LoadInst loadInst = buildLoadInst(((PointerType) value.getType()).getType(), value, bb);
//            getElementPtr.calIndex(offsets, loadInst, type);
//            getElementPtr.setType(new PointerType(new IntegerType()));
            Type type1 = ((PointerType) ((PointerType) value.getType()).getType()).getType();
            getElementPtr.calIndex(offsets, value, type);
            getElementPtr.setFlag(false);
            int indexSize = getElementPtr.getIndex().size();

            getElementPtr.setPtrval(loadInst);
            if (type1 instanceof IntegerType) {
                if (indexSize == 1) {
                    String memName = "%v" + getElementPtr.getValNumber();
                    getElementPtr.setMemName(memName);
                    getElementPtr.setType(((PointerType) value.getType()).getType());
                    bb.getInstructions().add(getElementPtr);
                    buildLoadInst(((PointerType) getElementPtr.getType()).getType(), getElementPtr, bb);
                    return getElementPtr;
                } else {
                    return getElementPtr;
                }

            } else {
                int arraySize = ((ArrayType) type1).getArraySize().size();
                if (indexSize == 0) {
                    return getElementPtr;
                }
                if (indexSize - arraySize == 1) {
                    getElementPtr.setPtrval(loadInst);
                    getElementPtr.setFlag(false);
                    getElementPtr.setType(loadInst.getType());
                    String memName = "%v" + getElementPtr.getValNumber();
                    getElementPtr.setMemName(memName);
                    bb.getInstructions().add(getElementPtr);
                    ArrayList<Value> newIndex = new ArrayList<>();
                    for (int i = 1; i < indexSize; i++) {
                        newIndex.add(getElementPtr.getIndex().get(i));
                    }
                    GetElementPtr getElementPtr1 = new GetElementPtr(null, null, true);
                    getElementPtr1.setPtrval(getElementPtr);
                    getElementPtr1.setIndex(newIndex);
                    getElementPtr1.setType(new PointerType(new IntegerType()));
                    memName = "%v" + getElementPtr.getValNumber();
                    getElementPtr1.setMemName(memName);
                    bb.getInstructions().add(getElementPtr1);
                    buildLoadInst(new IntegerType(), getElementPtr1, bb);
                    return getElementPtr1;
                } else {
                    getElementPtr.setPtrval(loadInst);
                    getElementPtr.setFlag(false);
                    getElementPtr.setType(loadInst.getType());
                    String memName = "%v" + getElementPtr.getValNumber();
                    getElementPtr.setMemName(memName);
                    bb.getInstructions().add(getElementPtr);
                    ArrayList<Value> newIndex = new ArrayList<>();
                    newIndex.add(new Constant("0", new IntegerType()));
                    GetElementPtr getElementPtr1 = new GetElementPtr(null, null, true);
                    getElementPtr1.setPtrval(getElementPtr);
                    getElementPtr1.setIndex(newIndex);
                    getElementPtr1.setType(new PointerType(new IntegerType()));
                    memName = "%v" + getElementPtr.getValNumber();
                    getElementPtr1.setMemName(memName);
                    bb.getInstructions().add(getElementPtr1);
                    return getElementPtr1;
                }
            }
        }
    }

    public CallInst buildCall(BasicBlock bb, String name, Type type, ArrayList<Value> args) {
        CallInst callInst = new CallInst(name, type);
        if (args != null) {
            ArrayList<Value> newArg = new ArrayList<>(args);
            callInst.setArgs(newArg);
        }
        if (((FuncType) type).getRetype() == 0) {
            String memName = "%v" + callInst.getValNumber();
            callInst.setMemName(memName);
        }
        bb.getInstructions().add(callInst);
        return callInst;
    }

    public void buildFuncFParam(FuncFParam funcFParam, Function function, BasicBlock bb, ArrayList<Integer> param) {
        int dim = funcFParam.dim;
        Argument argument;
        if (dim == 0) {
            argument = new Argument(funcFParam.ident, new IntegerType());
        } else if (dim == 1) {
            argument = new Argument(funcFParam.ident, new PointerType(new IntegerType()));
        } else {
            ArrayList<Integer> size = new ArrayList<>(param);
            ArrayType arrayType = new ArrayType();
            arrayType.setArraySize(size);
            argument = new Argument(funcFParam.ident, new PointerType(arrayType));
        }
        argument.setDim(dim);
        String memName = "%v" + argument.getValNumber();
        argument.setMemName(memName);
        function.addArgs(argument);
        AllocInst allocInst = buildAllocInst(funcFParam.ident, new PointerType(argument.getType()), bb);
        StoreInst storeInst = buildStoreInst(null, bb, argument, allocInst);
        module.pushSymbol(funcFParam.ident, allocInst);

    }

    public IcmpInst buildIcmpInst(BasicBlock bb, String name, Type type, String op, Value op1, Value op2) {
        IcmpInst icmpInst = new IcmpInst(name, type, op1, op2);
        icmpInst.setMemName(String.valueOf(icmpInst.getValNumber()));
        icmpInst.setCond(op);
        bb.getInstructions().add(icmpInst);
        return icmpInst;
    }

    public BrInst buildBrInst(BasicBlock bb, String name, Type type, Value cond, BasicBlock destBb, BasicBlock l1bb, BasicBlock l2bb) {
        BrInst brInst = new BrInst(name, type, cond);
        if (cond == null) {
            String destName = destBb.getBbName();
            brInst.setDest(destName);
            bb.getInstructions().add(brInst);
        } else {
            String l1Name = l1bb.getBbName();
            String l2Name = l2bb.getBbName();
            brInst.setLabels(l1Name, l2Name);
            bb.getInstructions().add(brInst);
        }
        return brInst;
    }

    public Value buildZextInst(BasicBlock currentBB, Value value, Type type) {
        ZextInst zextInst = new ZextInst(null, type, value);
        zextInst.setMemName(String.valueOf(zextInst.getValNumber()));
        currentBB.getInstructions().add(zextInst);
        return zextInst;
    }
}
