package mips;

import IR.IRModule;
import IR.Type.ArrayType;
import IR.Type.IntegerType;
import IR.Type.PointerType;
import IR.Value.*;
import IR.Value.Instructions.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Mips {
    IRModule irModule = IRModule.getInstance();
    private static Mips mips = null;
    private ArrayList<GlobalData> globalDataArrayList;
    private ArrayList<MipsFunction> functions;
    private ArrayList<MipsInst> mipsInsts;
    private HashMap<String, Integer> hashMap;
    private int startNum;
    private int currentNum;
    private MipsFunction currentFunction;
    private MipsBB currentBB;
    private StringBuilder sb;

    private int registerNum;

    public static Mips getInstance() {
        if (mips == null) mips = new Mips();
        return mips;
    }

    private Mips() {
        this.globalDataArrayList = new ArrayList<>();
        this.hashMap = new HashMap<>();
        this.startNum = 0;
        this.currentNum = 0;
        this.sb = new StringBuilder();
        this.registerNum = 0;
        this.mipsInsts = new ArrayList<>();
        this.functions = new ArrayList<>();
    }

    public void start() {
        for (int i = 0; i < irModule.getGlobalValues().size(); i++) {
            GlobalData globalData = new GlobalData();
            globalData.analyse(irModule.getGlobalValues().get(i));
            globalDataArrayList.add(globalData);
        }
        for (Function function : irModule.getFunctions()) {
            MipsFunction mipsFunction = new MipsFunction(function.getName());
            functions.add(mipsFunction);
            currentFunction = mipsFunction;
//            for (Argument arg : function.getArgs()) {
//                updateMap(arg.getMemName());
//
//            }
            for (int i = 0; i < function.getArgs().size(); i++) {
                int num = (function.getArgs().size() - i) * 4;
                hashMap.put(function.getArgs().get(i).getMemName(), num);
            }
            analyseFunction(function);
        }
//        print();
    }

    public void analyseFunction(Function function) {
        for (BasicBlock basicBlock : function.getBasicBlocks()) {
            MipsBB mipsBB = new MipsBB(basicBlock.getBbName());
            currentFunction.getBbs().add(mipsBB);
            currentBB = mipsBB;
            analyseBB(basicBlock);
        }
    }

    public void analyseBB(BasicBlock basicBlock) {
        String str = "";
        for (int i = 0; i < basicBlock.getInstructions().size(); i++) {
            Instruction instruction = basicBlock.getInstructions().get(i);
            if (instruction instanceof CallInst callInst) {
                if (callInst.getName().equals("@putch")) {
                    int num = Integer.parseInt(callInst.getArgs().get(0).getMemName());
                    if (num == '\n') {
                        str += "\\n";
                    } else
                        str += (char) Integer.parseInt(callInst.getArgs().get(0).getMemName());
                } else {
                    if (!str.isEmpty()) {
                        GlobalData globalData = new GlobalData();
                        globalData.stringAnalyse(str);
                        globalDataArrayList.add(globalData);
                        buildLi(4);
                        buildLa(globalData.getName());
                        buildSyscall();
                        str = "";

                    }
                    analyseInst(callInst);
                }
            } else {
                if (!str.isEmpty()) {
                    GlobalData globalData = new GlobalData();
                    globalData.stringAnalyse(str);
                    globalDataArrayList.add(globalData);
                    buildLi(4);
                    buildLa(globalData.getName());
                    buildSyscall();
                    str = "";
                }
                analyseInst(instruction);
            }

        }
    }

    public void analyseInst(Instruction instruction) {
        if (instruction instanceof AllocInst allocInst) {
            if (allocInst.getType() instanceof PointerType pointerType) {
                if (pointerType.getType() instanceof IntegerType) {
                    updateMap(allocInst.getMemName());
                } else if (pointerType.getType() instanceof ArrayType arrayType) {
                    updateMap(allocInst.getMemName(), arrayType.getArraySize());
                } else if (pointerType.getType() instanceof PointerType pointerType1) {
                    updateMap(allocInst.getMemName());
                }
            }

        } else if (instruction instanceof StoreInst storeInst) {
            if (storeInst.getPointer() instanceof GlobalValue globalValue) {
                if (storeInst.getValue() instanceof Constant constant) {
                    MipsInst liInst = buildLi(constant.getMemName());
                    MipsInst laInst = buildLa(globalValue);
                    buildSw(liInst.getOperand1(), laInst.getOperand1(), null);
                } else {
                    MipsInst lwInst = buildLw("", storeInst.getValue());
                    MipsInst laInst = buildLa(globalValue);
                    buildSw(lwInst.getOperand1(), laInst.getOperand1(), null);
                }
            } else if (storeInst.getPointer() instanceof GetElementPtr getElementPtr) {
                if (storeInst.getValue() instanceof Constant constant) {
                    MipsInst liInst = buildLi(constant.getMemName());
                    MipsInst laInst = buildLw("", getElementPtr);
                    buildSw(liInst.getOperand1(), laInst.getOperand1(), null);
                } else {
                    MipsInst lwInst = buildLw("", storeInst.getValue());
                    MipsInst lwInst1 = buildLw("", getElementPtr);
                    buildSw(lwInst.getOperand1(), lwInst1.getOperand1(), null);
                }
            } else {
                if (storeInst.getValue() instanceof Constant constant) {
                    MipsInst liInst = buildLi(constant.getMemName());
                    buildSw(liInst.getOperand1(), "", storeInst.getPointer());
                } else {
                    MipsInst lwInst = buildLw("", storeInst.getValue());
                    buildSw(lwInst.getOperand1(), "", storeInst.getPointer());
                }
            }
        } else if (instruction instanceof LoadInst loadInst) {
            if (loadInst.getPointer() instanceof GlobalValue globalValue) {
                MipsInst laInst = buildLa(globalValue);
                MipsInst lwInst = buildLw(laInst.getOperand1(), null);
                updateMap(loadInst.getMemName());
                buildSw(lwInst.getOperand1(), "", loadInst);
            } else if (loadInst.getPointer() instanceof GetElementPtr getElementPtr) {
                MipsInst lwInst = buildLw("", getElementPtr);
                MipsInst lwInst1 = buildLw(lwInst.getOperand1(), null);
                updateMap(loadInst.getMemName());
                buildSw(lwInst1.getOperand1(), "", loadInst);
            } else {
                addMap(loadInst.getMemName(), loadInst.getPointer());
            }
        } else if (instruction instanceof CalculateInst calculateInst) {
            MipsInst left, right;
            if (calculateInst.getLeft() instanceof Constant constant) {
                left = buildLi(constant.getMemName());
            } else {
                left = buildLw("", calculateInst.getLeft());
            }
            if (calculateInst.getRight() instanceof Constant constant) {
                right = buildLi(constant.getMemName());
            } else {
                right = buildLw("", calculateInst.getRight());
            }
            MipsInst calInst = buildCal(calculateInst.getCalType(), left.getOperand1(), right.getOperand1());
            updateMap(calculateInst.getMemName());
            buildSw(calInst.getOperand1(), "", calculateInst);
        } else if (instruction instanceof ReturnInst returnInst) {
            if (!currentFunction.getName().equals("main")) {
                if (returnInst.getValue() == null) {
                    buildJr();
                    return;
                }
                MipsInst mipsInst;
                if (returnInst.getValue() instanceof Constant constant) {
                    mipsInst = new MipsInst("li", "$v1", constant.getMemName(), "");
                } else {
                    int num = hashMap.get(returnInst.getValue().getMemName());
                    mipsInst = new MipsInst("lw", "$v1", num + "($sp)", "");
                }
                currentBB.getMipsInsts().add(mipsInst);
                buildJr();
            } else {
                buildLi(10);
                buildSyscall();
            }
        } else if (instruction instanceof BrInst brInst) {
            if (brInst.getCond() == null) {
                buildJ(brInst.getDest());
            } else {
                MipsInst lwInst = buildLw("", brInst.getCond());
                buildBeq(lwInst.getOperand1(), brInst.getIfFalse());
                buildJ(brInst.getIfTrue());
            }
        } else if (instruction instanceof IcmpInst icmpInst) {
            MipsInst op1, op2;
            if (icmpInst.getOp1() instanceof Constant) {
                op1 = buildLi(icmpInst.getOp1().getMemName());
            } else op1 = buildLw("", icmpInst.getOp1());
            if (icmpInst.getOp2() instanceof Constant) {
                op2 = buildLi(icmpInst.getOp2().getMemName());
            } else op2 = buildLw("", icmpInst.getOp2());
            MipsInst cmpInst = buildCmp(icmpInst.getCond(), op1.getOperand1(), op2.getOperand1());
            updateMap(icmpInst.getMemName());
            buildSw(cmpInst.getOperand1(), "", icmpInst);
        } else if (instruction instanceof ZextInst zextInst) {
            addMap(zextInst.getMemName(), zextInst.getValue());
        } else if (instruction instanceof GetElementPtr getElementPtr) {
            
        } else if (instruction instanceof CallInst callInst) {
            if (callInst.getName().equals("@putint")) {
                buildLi(1);
                if (callInst.getArgs().get(0) instanceof Constant constant) {
                    MipsInst mipsInst = new MipsInst("li", "$a0", constant.getMemName(), "");
                    currentBB.getMipsInsts().add(mipsInst);
                } else {
                    buildLw(callInst.getArgs().get(0));
                }

                buildSyscall();
            } else if (callInst.getName().equals("@getint")) {
                buildLi(5);
                buildSyscall();
                updateMap(callInst.getMemName());
                buildSw("$v0", "", callInst);
            } else {


                int spNum = currentNum;
                MipsInst raInst = new MipsInst("sw", "$ra", spNum + "($sp)", "");
                currentBB.getMipsInsts().add(raInst);
                for (int i = 0; i < callInst.getArgs().size(); i++) {
                    int num = i * 4;
                    MipsInst mipsInst = null;
                    if (callInst.getArgs().get(i) instanceof Constant constant) {
                        MipsInst newLiInst = buildLi(constant.getMemName());
                        mipsInst = new MipsInst("sw", newLiInst.getOperand1(), spNum - num - 4 + "($sp)", "");
                    } else {
                        MipsInst lwInst = buildLw("", callInst.getArgs().get(i));
                        mipsInst = new MipsInst("sw", lwInst.getOperand1(), spNum - num - 4 + "($sp)", "");
                    }
                    currentBB.getMipsInsts().add(mipsInst);
                }

                MipsInst liInst = buildLi(Integer.toString(spNum - callInst.getArgs().size() * 4 - 4));
                MipsInst addInst = buildCal("addu", "$sp", "$sp", liInst.getOperand1());

                buildJal(callInst.getName());

                liInst = buildLi(Integer.toString(-spNum + callInst.getArgs().size() * 4 + 4));
                buildCal("addu", "$sp", "$sp", liInst.getOperand1());
                updateMap(callInst.getMemName());
                MipsInst lwInst = new MipsInst("lw", "$ra", spNum + "($sp)", "");
                currentBB.getMipsInsts().add(lwInst);
                MipsInst mipsInst = new MipsInst("sw", "$v1", spNum + "($sp)", "");
                currentBB.getMipsInsts().add(mipsInst);
            }
        }

    }

    private void updateMap(String name) {
        hashMap.put(name, currentNum);
        currentNum -= 4;
    }

    private void updateMap(String name, ArrayList<Integer> arraySize) {
        hashMap.put(name, currentNum);
        int j = 1;
        for (Integer i : arraySize) {
            j *= i;
        }
        currentNum -= j * 4;
    }

    private void addMap(String name, Value value) {
        int num = hashMap.get(value.getMemName());
        hashMap.put(name, num);
    }

    private MipsInst buildLa(Value value) {
        String op = "la";
        String op1 = "$t" + getRegisterNum();
        String op2 = value.getName();
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildLa(String string) {
        String op = "la";
        String op1 = "$a0";
        String op2 = string;
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildLi(String string) {
        String op = "li";
        String op1 = "$t" + getRegisterNum();
        String op2 = string;
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private void buildSw(String op1, String op2, Value value) {
        String op = "sw";

        if (op2.isEmpty()) {
            int num = hashMap.get(value.getMemName());
            op2 = num + "($sp)";
        } else {
            op2 = 0 + "(" + op2 + ")";
        }
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);

    }

    private MipsInst buildNewLw(int spNum, Value value) {
        String op = "lw";
        String op1 = "$t" + getRegisterNum();
        int num = hashMap.get(value.getMemName()) - spNum;
        String op2 = num + "($sp)";
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;

    }

    private MipsInst buildLw(String op2, Value value) {
        String op = "lw";
        String op1 = "$t" + getRegisterNum();
        if (op2.isEmpty()) {
            int num = hashMap.get(value.getMemName());
            op2 = num + "($sp)";
        } else {
            op2 = 0 + "(" + op2 + ")";
        }
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildLw(Value value) {
        String op = "lw";
        String op1 = "$a0";
        int num = hashMap.get(value.getMemName());
        String op2 = num + "($sp)";
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildCal(String op, String op2, String op3) {
        if (op.equals("add") || op.equals("sub")) {
            op = op + "u";
            String op1 = "$t" + getRegisterNum();
            MipsInst mipsInst = new MipsInst(op, op1, op2, op3);
            currentBB.getMipsInsts().add(mipsInst);
            return mipsInst;
        } else if (op.equals("mul")) {
            op = "mult";

            MipsInst mipsInst = new MipsInst(op, op2, op3, "");
            currentBB.getMipsInsts().add(mipsInst);
            op = "mflo";
            String op1 = "$t" + getRegisterNum();
            MipsInst mfloInst = new MipsInst(op, op1, "", "");
            currentBB.getMipsInsts().add(mfloInst);
            return mfloInst;
        } else if (op.equals("div")) {
            op = "div";
            MipsInst mipsInst = new MipsInst(op, op2, op3, "");
            currentBB.getMipsInsts().add(mipsInst);
            op = "mflo";
            String op1 = mipsInst.getOperand1();
            MipsInst mfloInst = new MipsInst(op, op1, "", "");
            currentBB.getMipsInsts().add(mfloInst);
            return mfloInst;
        } else {
            op = "div";
            MipsInst mipsInst = new MipsInst(op, op2, op3, "");
            currentBB.getMipsInsts().add(mipsInst);
            op = "mfhi";
            String op1 = "$t" + getRegisterNum();
            MipsInst mfhiInst = new MipsInst(op, op1, "", "");
            currentBB.getMipsInsts().add(mfhiInst);
            return mfhiInst;
        }

    }

    private MipsInst buildCal(String op, String op1, String op2, String op3) {
        MipsInst mipsInst = new MipsInst(op, op1, op2, op3);
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildLi(int num) {
        String op = "li";
        String op1 = "$v0";
        String op2 = Integer.toString(num);
        MipsInst mipsInst = new MipsInst(op, op1, op2, "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildJr() {
        MipsInst mipsInst = new MipsInst("jr", "$ra", "", "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private void buildJ(String op1) {
        String op = "j";
        op1 = op1.substring(1);
        MipsInst mipsInst = new MipsInst(op, op1, "", "");
        currentBB.getMipsInsts().add(mipsInst);
    }

    private void buildJal(String op1) {
        String op = "jal";
        op1 = op1.substring(1);
        MipsInst mipsInst = new MipsInst(op, op1, "", "");
        currentBB.getMipsInsts().add(mipsInst);
    }

    private MipsInst buildCmp(String op, String op2, String op3) {
        if (op.equals("eq") || op.equals("ne")) {
            op = 's' + op;
        }
        String op1 = "$t" + getRegisterNum();
        MipsInst mipsInst = new MipsInst(op, op1, op2, op3);
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildBeq(String op1, String ifFalse) {
        String op = "beq";
        String op2 = "$0";
        String op3 = ifFalse.substring(1);
        MipsInst mipsInst = new MipsInst(op, op1, op2, op3);
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private MipsInst buildSyscall() {
        MipsInst mipsInst = new MipsInst("syscall", "", "", "");
        currentBB.getMipsInsts().add(mipsInst);
        return mipsInst;
    }

    private int getRegisterNum() {
        registerNum += 1;
        if (registerNum >= 8) registerNum = 0;
        return registerNum;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        System.out.println(".data");
        sb.append(".data\n");
        for (GlobalData globalData : globalDataArrayList) {
            sb.append(globalData).append("\n");
            System.out.println(globalData);
        }
        System.out.println(".text");
        System.out.println("j main");
        sb.append(".text\n").append("j main\n");
        for (MipsFunction function : functions) {
            System.out.println(function);
            sb.append(function).append("\n");
        }
        return sb.toString();
    }
}
