package IR;

import IR.Type.FuncType;
import IR.Type.IntegerType;
import IR.Type.PointerType;
import IR.Type.Type;
import IR.Value.*;
import IR.Value.Instructions.AllocInst;
import parser.Parser;
import parser.parse.*;

import java.util.ArrayList;

public class Visitor {
    IRModule module = IRModule.getInstance();
    IRBuildFactory factory = IRBuildFactory.getInstance();
    private Integer saveNum = null;
    private Value saveValue = null;
    private ArrayList<String> opList = new ArrayList<>();
    private ArrayList<Integer> calList = new ArrayList<>();
    private ArrayList<Integer> arrayNum = new ArrayList<>();
    private Boolean isGlobal = true;
    private BasicBlock currentBB = null;

    public void visitCompUnit() {
        module.pushSymTable();
        for (Decl decl : Parser.compUnit.declArrayList) {
            visitDecl(decl);
        }
        isGlobal = false;
        for (FuncDef funcDef : Parser.compUnit.funcDefArrayList) {
            visitFuncDef(funcDef);
        }
//        module.pushSymTable();
        visitMainFuncDef(Parser.compUnit.mainFuncDef);
    }


    private void visitDecl(Decl decl) {
        if (decl.constDecl != null) {
            for (ConstDef constDef : decl.constDecl.constDefArrayList) {
                visitConstDef(constDef);
            }
        } else {
            for (VarDef varDef : decl.varDecl.varDefArrayList) {
                visitVarDef(varDef);
            }
        }
    }

    private void visitConstDef(ConstDef constDef) {
        String name = constDef.Ident;
        if (!constDef.constExpArrayList.isEmpty()) {
            GlobalValue globalValue = new GlobalValue(name, new PointerType(new IntegerType()), true);
            module.pushSymbol(name, globalValue);
            for (ConstExp constExp : constDef.constExpArrayList) {
                visitAddExp(constExp.addExp);
                globalValue.addSize(saveNum);
                saveNum = null;
            }
            globalValue.calP();
            module.getGlobalValues().add(globalValue);
            visitConstInitVal(constDef.constInitval);
            globalValue.addArrayNum(arrayNum);
            arrayNum.clear();
        } else {
            GlobalValue globalValue = new GlobalValue(name, new PointerType(new IntegerType()), true);
            module.pushSymbol(name, globalValue);
            visitConstInitVal(constDef.constInitval);
            globalValue = (GlobalValue) module.find(name);
            globalValue.setNum(saveNum);
            module.getGlobalValues().add(globalValue);
//            System.out.println(((GlobalValue) module.find(name)).getNum());
            saveNum = null;
        }
    }

    private void visitConstInitVal(ConstInitVal constInitVal) {
        if (constInitVal.constExp != null) {
            visitConstExp(constInitVal.constExp);
            arrayNum.add(saveNum);
        } else {
            for (ConstInitVal initVal : constInitVal.constInitValArrayList) {
                visitConstInitVal(initVal);
            }
        }
    }

    private void visitVarDef(VarDef varDef) {
        String name = varDef.ident;
        if (isGlobal) {
            if (!varDef.constExpArrayList.isEmpty()) {
                GlobalValue globalValue = new GlobalValue(name, new PointerType(new IntegerType()), false);
                module.pushSymbol(name, globalValue);
                for (ConstExp constExp : varDef.constExpArrayList) {
                    visitAddExp(constExp.addExp);
                    globalValue.addSize(saveNum);
                    saveNum = null;
                }
                globalValue.calP();
                module.getGlobalValues().add(globalValue);
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal, true);
                    globalValue.addArrayNum(arrayNum);
                    arrayNum.clear();
                } else {
                    globalValue.setZero();
                }
            } else {
                GlobalValue globalValue = new GlobalValue(name, new PointerType(new IntegerType()), false);
                module.pushSymbol(name, globalValue);
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal, false);
                    globalValue = (GlobalValue) module.find(name);
                    globalValue.setNum(saveNum);
                    module.getGlobalValues().add(globalValue);
//                    System.out.println(((GlobalValue) module.find(name)).getNum());
                    saveNum = null;
                } else {
                    globalValue = (GlobalValue) module.find(name);
                    globalValue.setNum(0);
                    module.getGlobalValues().add(globalValue);
//                    System.out.println(((GlobalValue) module.find(name)).getNum());
                    saveNum = null;
                }
            }
        } else {
            if (!varDef.constExpArrayList.isEmpty()) {
                AllocInst allocInst = factory.buildAllocInst(name, new PointerType(new IntegerType()), currentBB);
                module.pushSymbol(name, allocInst);
                for (ConstExp constExp : varDef.constExpArrayList) {
                    visitAddExp(constExp.addExp);
                    allocInst.addSize(Integer.parseInt(saveValue.getMemName()));
                    saveValue = null;
                }
                allocInst.calP();
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal, true);
                }

            } else {
                AllocInst allocInst = factory.buildAllocInst(name, new PointerType(new IntegerType()), currentBB);
                module.pushSymbol(name, allocInst);
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal, false);
                    allocInst = (AllocInst) module.find(name);
                    allocInst.setValue(saveValue);
//                    System.out.println(((AllocInst) module.find(name)).getNum());
                    factory.buildStoreInst(null, currentBB, saveValue, allocInst);
                    saveValue = null;
                } else {

                }
            }
        }
    }

    private void visitInitVal(InitVal initVal, Boolean isArray) {
        if (initVal.exp != null) {
            visitExp(initVal.exp);
            if (isGlobal)
                arrayNum.add(saveNum);
            else {
                if (isArray) {
                    factory.buildGetElementPtr();
                }
            }
        } else {
            for (InitVal val : initVal.initValArrayList) {
                visitInitVal(val, true);
            }
        }
    }

    private void visitFuncDef(FuncDef funcDef) {

    }

    private void visitMainFuncDef(MainFuncDef mainFuncDef) {
        String name = "main";
        Function function = factory.buildFunction(name, new FuncType(0));
        module.pushSymbol(name, function);
        module.pushSymTable();
        currentBB = factory.buildBasicBlock(function);
        visitBlock(mainFuncDef.block);
    }

    private void visitBlock(Block block) {
//        if (isGlobal) {
//            BasicBlock basicBlock = new BasicBlock();
//            Function function = module.getFunctions().get(module.getFunctions().size() - 1);
//            function.getBasicBlocks().add(basicBlock);
//        }

        for (BlockItem blockItem : block.blockItemArrayList) {
            if (blockItem.decl != null) visitDecl(blockItem.decl);
            else visitStmt(blockItem.stmt);
        }
    }

    private void visitStmt(Stmt stmt) {
        switch (stmt.flag) {
            case 1:
//                visitLVal(stmt.lVal);
                String name = stmt.lVal.ident;
                Value value = module.find(name);

//                System.out.println(value.getMemName());
                visitExp(stmt.expArrayList.get(0));
                factory.buildStoreInst(null, currentBB, saveValue, value);
                saveValue = null;
//                StoreInst storeInst = factory.buildStoreInst(null, currentBB);
                break;
            case 3:
                module.pushSymTable();
                visitBlock(stmt.block);
                module.popSymTable();
                break;
            case 8:
                if (!stmt.expArrayList.isEmpty()) visitExp(stmt.expArrayList.get(0));
                factory.buildReturn(null, null, currentBB, saveValue);
//                System.out.println(saveNum);
                saveValue = null;
                saveNum = null;
                break;
            default:
                break;
        }
    }


    private void visitExp(Exp exp) {
        visitAddExp(exp.addExp);
    }

    private void visitLVal(LVal lVal) {
        if (isGlobal) {
            String name = lVal.ident;
            Value value = module.find(name);
            if (lVal.expArrayList == null) {
                saveNum = ((GlobalValue) value).getNum();
                for (int i = opList.size() - 1; i >= 0; i--) {
                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
                        break;
                    } else {
                        saveNum = factory.buildCalculateNumber(0, saveNum, opList.get(i));
                    }
                }
            } else {
                ArrayList<Integer> cal = new ArrayList<>();
                for (Exp exp : lVal.expArrayList) {
                    visitExp(exp);
                    cal.add(saveNum);
                    System.out.println(saveNum);
                }
                saveNum = ((GlobalValue) value).cal(cal);
                System.out.println(saveNum);
            }

        } else {
            String name = lVal.ident;
            Value value = module.find(name);
            Type type = ((PointerType) value.getType()).getType();
            saveValue = factory.buildLoadInst(type, name, currentBB);
            for (int i = opList.size() - 1; i >= 0; i--) {
                if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
                    break;
                } else {
                    saveValue = factory.buildCalculateValue(new Constant(null, new IntegerType()), saveValue, opList.get(i), new IntegerType(), currentBB);
                }
            }
        }
    }

    private void visitPrimaryExp(PrimaryExp primaryExp) {
        if (isGlobal) {
            if (primaryExp.number != null) {
                saveNum = primaryExp.number.intConst;
                for (int i = opList.size() - 1; i >= 0; i--) {
                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
                        break;
                    } else {
                        saveNum = factory.buildCalculateNumber(0, saveNum, opList.get(i));
                    }
                }

            } else if (primaryExp.exp != null) {
                visitExp(primaryExp.exp);
            } else if (primaryExp.lVal != null) {
                visitLVal(primaryExp.lVal);
            }
        } else {
            if (primaryExp.number != null) {
                String name = Integer.toString(primaryExp.number.intConst);
                saveValue = new Constant(name, new IntegerType());
                for (int i = opList.size() - 1; i >= 0; i--) {
                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
                        break;
                    } else if (opList.get(i).equals("-")) {
                        if (saveValue instanceof Constant) {
                            int num = -Integer.parseInt(saveValue.getMemName());
                            ((Constant) saveValue).setMemName(Integer.toString(num));
                            opList.remove(opList.get(opList.size() - 1));
                        } else
                            saveValue = factory.buildCalculateValue(new Constant("0", new IntegerType()), saveValue, opList.get(i), new IntegerType(), currentBB);
                    } else {
                        opList.remove(opList.get(opList.size() - 1));
                    }
                }
            } else if (primaryExp.exp != null) {
                visitExp(primaryExp.exp);
            } else if (primaryExp.lVal != null) {
                visitLVal(primaryExp.lVal);
            }
        }
    }

    private void visitUnaryExp(UnaryExp unaryExp) {
        if (unaryExp.unaryOp != null) {
            if (isGlobal) {
                if (saveNum == null) saveNum = 0;
                opList.add(unaryExp.unaryOp.unaryOp);
                visitUnaryExp(unaryExp.unaryExp);
            } else {
                opList.add(unaryExp.unaryOp.unaryOp);
                visitUnaryExp(unaryExp.unaryExp);
            }


        } else {
            visitPrimaryExp(unaryExp.primaryExp);
        }
    }


    private void visitAddExp(AddExp addExp) {
        visitMulExp(addExp.mulExpArrayList.get(0));
        if (!addExp.addOp.isEmpty()) {
            for (int i = 1; i < addExp.mulExpArrayList.size(); i++) {
                if (isGlobal) {
                    int num = saveNum;
                    visitMulExp(addExp.mulExpArrayList.get(i));
                    opList.add(addExp.addOp.get(i - 1));
                    saveNum = factory.buildCalculateNumber(num, saveNum, opList.get(opList.size() - 1));
                } else {
                    Value value = saveValue;
                    visitMulExp(addExp.mulExpArrayList.get(i));
                    String op = addExp.addOp.get(i - 1);
                    if (value instanceof Constant && saveValue instanceof Constant) {
                        int num = 0;
                        if (op.equals("+"))
                            num = Integer.parseInt(value.getMemName()) + Integer.parseInt(saveValue.getMemName());
                        else if (op.equals("-"))
                            num = Integer.parseInt(value.getMemName()) - Integer.parseInt(saveValue.getMemName());
                        ((Constant) saveValue).setMemName(Integer.toString(num));
                    } else {
                        opList.add(op);
                        saveValue = factory.buildCalculateValue(value, saveValue, op, new IntegerType(), currentBB);
                    }
                }

            }
        }
    }

    private void visitMulExp(MulExp mulExp) {
        visitUnaryExp(mulExp.unaryExpArrayList.get(0));
        if (!mulExp.mulOp.isEmpty()) {
            for (int i = 1; i < mulExp.unaryExpArrayList.size(); i++) {
                if (isGlobal) {
                    int value = saveNum;
                    visitUnaryExp(mulExp.unaryExpArrayList.get(i));
                    opList.add(mulExp.mulOp.get(i - 1));
                    saveNum = factory.buildCalculateNumber(value, saveNum, opList.get(opList.size() - 1));
                } else {
                    Value value = saveValue;
                    visitUnaryExp(mulExp.unaryExpArrayList.get(i));
                    String op = mulExp.mulOp.get(i - 1);
                    if (value instanceof Constant && saveValue instanceof Constant) {
                        int num = 0;
                        if (op.equals("*"))
                            num = Integer.parseInt(value.getMemName()) * Integer.parseInt(saveValue.getMemName());
                        else if (op.equals("/"))
                            num = Integer.parseInt(value.getMemName()) / Integer.parseInt(saveValue.getMemName());
                        else if (op.equals("%"))
                            num = Integer.parseInt(value.getMemName()) % Integer.parseInt(saveValue.getMemName());
                        ((Constant) saveValue).setMemName(Integer.toString(num));
                    } else {
                        opList.add(op);
                        saveValue = factory.buildCalculateValue(value, saveValue, op, new IntegerType(), currentBB);
                    }
                }
            }
        }
    }

    private void visitConstExp(ConstExp constExp) {
        visitAddExp(constExp.addExp);

    }

    public ArrayList<String> getOpList() {
        return opList;
    }

}
