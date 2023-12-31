package IR;

import IR.Type.FuncType;
import IR.Type.*;
import IR.Value.*;
import IR.Value.Instructions.AllocInst;
import IR.Value.Instructions.CallInst;
import IR.Value.Instructions.GetElementPtr;
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
    private Integer offset = 0;
    private Boolean isGlobal = true;
    private BasicBlock currentBB = null;
    private Function currentFunction = null;
    private BasicBlock breakBlock = null;
    private BasicBlock forStmt2Block = null;

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
        if (isGlobal) {
            if (!constDef.constExpArrayList.isEmpty()) {
                ArrayType arrayType = new ArrayType();
                for (ConstExp constExp : constDef.constExpArrayList) {
                    visitAddExp(constExp.addExp);
                    arrayType.addSize(saveNum);
                    saveNum = null;
                }
                GlobalValue globalValue = new GlobalValue(name, new PointerType(arrayType), true);
                ((ArrayType) ((PointerType) globalValue.getType()).getType()).calP();
                module.pushSymbol(name, globalValue);
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
                saveNum = null;
                arrayNum.clear();
            }
        } else {
            if (!constDef.constExpArrayList.isEmpty()) {
                ArrayType arrayType = new ArrayType();
                for (ConstExp constExp : constDef.constExpArrayList) {
                    visitAddExp(constExp.addExp);
                    arrayType.addSize(Integer.parseInt(saveValue.getMemName()));
                    saveValue = null;
                }
                AllocInst allocInst = factory.buildAllocInst(name, new PointerType(arrayType), currentBB);
                module.pushSymbol(name, allocInst);
                ((ArrayType) ((PointerType) allocInst.getType()).getType()).calP();
                visitConstInitVal(constDef.constInitval, allocInst);
                offset = 0;
            } else {
                AllocInst allocInst = factory.buildAllocInst(name, new PointerType(new IntegerType()), currentBB);
                module.pushSymbol(name, allocInst);
                visitConstInitVal(constDef.constInitval);
                allocInst = (AllocInst) module.find(name);
                allocInst.setValue(saveValue);
                factory.buildStoreInst(null, currentBB, saveValue, allocInst);
//                module.pushSymbol(name, saveValue);
                saveValue = null;
            }

        }
    }

    private void visitConstInitVal(ConstInitVal constInitVal) {
        if (constInitVal.constExp != null) {
            visitConstExp(constInitVal.constExp);
            if (isGlobal)
                arrayNum.add(saveNum);
        } else {
            for (ConstInitVal constInitVal1 : constInitVal.constInitValArrayList) {
                visitConstInitVal(constInitVal1);
            }
        }
    }

    private void visitConstInitVal(ConstInitVal constInitVal, Value value) {
        if (constInitVal.constExp != null) {
            visitAddExp(constInitVal.constExp.addExp);
            ArrayList<Value> values = new ArrayList<>();
            values.add(new Constant(offset.toString(), new IntegerType()));
            GetElementPtr getElementPtr = factory.buildGetElementPtr(currentBB, values, value, 0, false);
            factory.buildStoreInst(null, currentBB, saveValue, getElementPtr);
            offset += 1;
        } else {
            for (ConstInitVal val : constInitVal.constInitValArrayList) {
                visitConstInitVal(val, value);
            }
        }
    }

    private void visitVarDef(VarDef varDef) {
        String name = varDef.ident;
        if (isGlobal) {
            if (!varDef.constExpArrayList.isEmpty()) {
                ArrayType arrayType = new ArrayType();
                for (ConstExp constExp : varDef.constExpArrayList) {
                    visitAddExp(constExp.addExp);
                    arrayType.addSize(saveNum);
                    saveNum = null;
                }
                GlobalValue globalValue = new GlobalValue(name, new PointerType(arrayType), false);
                ((ArrayType) ((PointerType) globalValue.getType()).getType()).calP();
                module.pushSymbol(name, globalValue);
                module.getGlobalValues().add(globalValue);
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal);
                    globalValue.addArrayNum(arrayNum);
                    arrayNum.clear();
                } else {
                    globalValue.setZero();
                }
            } else {
                GlobalValue globalValue = new GlobalValue(name, new PointerType(new IntegerType()), false);
                module.pushSymbol(name, globalValue);
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal);
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
                arrayNum.clear();
            }
        } else {
            if (!varDef.constExpArrayList.isEmpty()) {
                ArrayType arrayType = new ArrayType();
                for (ConstExp constExp : varDef.constExpArrayList) {
                    visitAddExp(constExp.addExp);
                    arrayType.addSize(Integer.parseInt(saveValue.getMemName()));
                    saveValue = null;
                }
                AllocInst allocInst = factory.buildAllocInst(name, new PointerType(arrayType), currentBB);
                module.pushSymbol(name, allocInst);
                ((ArrayType) ((PointerType) allocInst.getType()).getType()).calP();
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal, allocInst);
                    offset = 0;
                }

            } else {
                AllocInst allocInst = factory.buildAllocInst(name, new PointerType(new IntegerType()), currentBB);
                module.pushSymbol(name, allocInst);
                if (varDef.initVal != null) {
                    visitInitVal(varDef.initVal);
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

    private void visitInitVal(InitVal initVal) {
        if (initVal.exp != null) {
            visitExp(initVal.exp);
            if (isGlobal)
                arrayNum.add(saveNum);
        } else {
            for (InitVal val : initVal.initValArrayList) {
                visitInitVal(val);
            }
        }
    }

    private void visitInitVal(InitVal initVal, Value value) {
        if (initVal.exp != null) {
            visitExp(initVal.exp);
            ArrayList<Value> values = new ArrayList<>();
            values.add(new Constant(offset.toString(), new IntegerType()));
            GetElementPtr getElementPtr = factory.buildGetElementPtr(currentBB, values, value, 0, false);
            factory.buildStoreInst(null, currentBB, saveValue, getElementPtr);
            offset += 1;
        } else {
            for (InitVal val : initVal.initValArrayList) {
                visitInitVal(val, value);
            }
        }
    }

    private void visitFuncDef(FuncDef funcDef) {
        String name = funcDef.ident;
        Function function;
        if (funcDef.funcType.funcType.equals("int"))
            function = factory.buildFunction(name, new FuncType(0));
        else function = factory.buildFunction(name, new FuncType(1));
        module.pushSymbol(name, function);
        module.pushSymTable();
        currentFunction = function;
        currentBB = factory.buildBasicBlock(function);
        int i = 0;
        ArrayList<Integer> param = new ArrayList<>();
        for (FuncFParam funcFParam : funcDef.funcFParams.funcFParamArrayList) {
//            Constant constant = new Constant("%" + i, new IntegerType());
//            function.addArgs(constant);
//            AllocInst allocInst = factory.buildAllocInst(funcFParam.ident, new PointerType(new IntegerType()), currentBB);
//            StoreInst storeInst = factory.buildStoreInst(null, currentBB, constant, allocInst);
//            module.pushSymbol(funcFParam.ident, allocInst);
//            i++;
            for (ConstExp constExp : funcFParam.constExpArrayList) {
                visitConstExp(constExp);
                param.add(Integer.parseInt(saveValue.getMemName()));
//                System.out.println(saveValue.getMemName());
            }
            factory.buildFuncFParam(funcFParam, function, currentBB, param);
            param.clear();
        }
        visitBlock(funcDef.block);
        String retName = null;
        if (!currentBB.getInstructions().isEmpty()) {
            retName = currentBB.getInstructions().get(currentBB.getInstructions().size() - 1).getName();
        }
        if (retName == null || !retName.equals("ret")) {
            factory.buildReturn("ret", null, currentBB, null);
        }
        module.popSymTable();
    }

    private void visitMainFuncDef(MainFuncDef mainFuncDef) {
        String name = "main";
        Function function = factory.buildFunction(name, new FuncType(0));
        module.pushSymbol(name, function);
        module.pushSymTable();
        currentFunction = function;
        currentBB = factory.buildBasicBlock(function);
        visitBlock(mainFuncDef.block);
        module.popSymTable();
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
        String name;
        Value value;
        BasicBlock condBlock;
        switch (stmt.flag) {
            case 1:
                name = stmt.lVal.ident;
                value = module.find(name);
                ArrayList<Value> values = new ArrayList<>();
                if (!stmt.lVal.expArrayList.isEmpty()) {
                    for (Exp exp : stmt.lVal.expArrayList) {
                        visitExp(exp);
                        values.add(saveValue);
                        saveValue = null;
                    }
                    value = factory.buildGetElementPtr(currentBB, values, value, 1, false);
                } else value = module.find(name);
//                System.out.println(value.getMemName());
                visitExp(stmt.expArrayList.get(0));
                factory.buildStoreInst(null, currentBB, saveValue, value);
                saveValue = null;
//                StoreInst storeInst = factory.buildStoreInst(null, currentBB);
                break;
            case 2:
                if (!stmt.expArrayList.isEmpty()) {
                    visitExp(stmt.expArrayList.get(0));
                }
                break;
            case 3:
                module.pushSymTable();
                visitBlock(stmt.block);
                module.popSymTable();
                break;
            case 4:
                condBlock = factory.buildBasicBlock(currentFunction);
                BasicBlock stmt1Block = factory.buildBasicBlock(currentFunction);
                BasicBlock stmt2Block = factory.buildBasicBlock(currentFunction);
                BasicBlock stmt3Block = factory.buildBasicBlock(currentFunction);
                factory.buildBrInst(currentBB, null, null, null, condBlock, null, null);
                currentBB = condBlock;
                visitCond(stmt.cond, stmt1Block, stmt2Block);
                currentBB = stmt1Block;
                visitStmt(stmt.stmtArrayList.get(0));
                if (stmt.stmtArrayList.size() == 1) {
                    factory.buildBrInst(currentBB, null, null, null, stmt2Block, null, null);
                } else {
                    factory.buildBrInst(currentBB, null, null, null, stmt3Block, null, null);
                }

                currentBB = stmt2Block;
                if (stmt.stmtArrayList.size() != 1) visitStmt(stmt.stmtArrayList.get(1));
                factory.buildBrInst(currentBB, null, null, null, stmt3Block, null, null);
                currentBB = stmt3Block;
                break;
            case 5:
//                visitForStmt(stmt.forStmtArrayList.)
//                System.out.println(stmt.getHasFor1());
//                System.out.println(stmt.getHasFor2());
                BasicBlock forStmt1Block = factory.buildBasicBlock(currentFunction);
                condBlock = factory.buildBasicBlock(currentFunction);
                BasicBlock stmtBlock = factory.buildBasicBlock(currentFunction);
                forStmt2Block = factory.buildBasicBlock(currentFunction);
                breakBlock = factory.buildBasicBlock(currentFunction);
                BasicBlock temp1 = forStmt2Block;
                BasicBlock temp2 = breakBlock;
                factory.buildBrInst(currentBB, null, null, null, forStmt1Block, null, null);
                currentBB = forStmt1Block;
                if (stmt.getHasFor1()) visitForStmt(stmt.forStmtArrayList.get(0));
                factory.buildBrInst(currentBB, null, null, null, condBlock, null, null);
                currentBB = condBlock;
                if (stmt.cond != null) visitCond(stmt.cond, stmtBlock, breakBlock);
                else
                    factory.buildBrInst(currentBB, null, null, null, stmtBlock, null, null);
                currentBB = stmtBlock;
                visitStmt(stmt.stmtArrayList.get(0));
                forStmt2Block = temp1;
                breakBlock = temp2;
                factory.buildBrInst(currentBB, null, null, null, forStmt2Block, null, null);
                currentBB = forStmt2Block;
                if (stmt.getHasFor2()) {
                    if (stmt.getHasFor1()) visitForStmt(stmt.forStmtArrayList.get(1));
                    else visitForStmt(stmt.forStmtArrayList.get(0));
                }
                factory.buildBrInst(currentBB, null, null, null, condBlock, null, null);
                currentBB = breakBlock;
                break;
            case 6:
                factory.buildBrInst(currentBB, null, null, null, breakBlock, null, null);
                break;
            case 7:
                factory.buildBrInst(currentBB, null, null, null, forStmt2Block, null, null);
                break;
            case 8:
                if (!stmt.expArrayList.isEmpty()) visitExp(stmt.expArrayList.get(0));
                else saveValue = null;
                factory.buildReturn("ret", null, currentBB, saveValue);
//                System.out.println(saveNum);
                saveValue = null;
                saveNum = null;
                break;
            case 9:
                name = stmt.lVal.ident;
                value = module.find(name);
                values = new ArrayList<>();
                if (!stmt.lVal.expArrayList.isEmpty()) {
                    for (Exp exp : stmt.lVal.expArrayList) {
                        visitExp(exp);
                        values.add(saveValue);
                        saveValue = null;
                    }
                    value = factory.buildGetElementPtr(currentBB, values, value, 1, false);
                } else value = module.find(name);
                CallInst callInst = factory.buildCall(currentBB, "@getint", new FuncType(0), null);
                factory.buildStoreInst(null, currentBB, callInst, value);
                break;
            case 10:
                int t = 0;
                int num;
                Constant constant;
                for (int i = 0; i < stmt.formatString.length(); i++) {
                    char c = stmt.formatString.charAt(i);
                    ArrayList<Value> args = new ArrayList<>();
                    switch (c) {
                        case '%':
                            visitExp(stmt.expArrayList.get(t));
                            args.add(saveValue);
                            factory.buildCall(currentBB, "@putint", new FuncType(1), args);
                            t++;
                            i++;
                            break;
                        case '"':
                            break;
                        case '\\':
                            num = '\n';
                            args = new ArrayList<>();
                            constant = new Constant(Integer.toString(num), new IntegerType());
                            args.add(constant);
                            factory.buildCall(currentBB, "@putch", new FuncType(1), args);
                            i++;
                            break;
                        default:
                            num = c;
                            args = new ArrayList<>();
                            constant = new Constant(Integer.toString(num), new IntegerType());
                            args.add(constant);
                            factory.buildCall(currentBB, "@putch", new FuncType(1), args);
//                            System.out.println((int) c);
                    }

                }
            default:
                break;
        }
    }

    private void visitForStmt(ForStmt forStmt) {
        String name;
        Value value;
        name = forStmt.lVal.ident;
        value = module.find(name);
        ArrayList<Value> values = new ArrayList<>();
        if (!forStmt.lVal.expArrayList.isEmpty()) {
            for (Exp exp : forStmt.lVal.expArrayList) {
                visitExp(exp);
                values.add(saveValue);
                saveValue = null;
            }
            value = factory.buildGetElementPtr(currentBB, values, value, 1, false);
        } else value = module.find(name);
        visitExp(forStmt.exp);
        factory.buildStoreInst(null, currentBB, saveValue, value);
        saveValue = null;
    }

    private void visitExp(Exp exp) {
        visitAddExp(exp.addExp);
    }

    private void visitCond(Cond cond, BasicBlock stmt1Block, BasicBlock stmt2Block) {
        visitLOrExp(cond.lOrExp, stmt1Block, stmt2Block);
    }

    private void visitLVal(LVal lVal) {
        if (isGlobal) {
            String name = lVal.ident;
            Value value = module.find(name);
            if (lVal.expArrayList.isEmpty()) {
                saveNum = ((GlobalValue) value).getNum();
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
//                        break;
//                    } else {
//                        saveNum = factory.buildCalculateNumber(0, saveNum, opList.get(i));
//                    }
//                }
            } else {
                ArrayList<Integer> cal = new ArrayList<>();
                for (Exp exp : lVal.expArrayList) {
                    visitExp(exp);
                    cal.add(saveNum);
                }
                saveNum = ((GlobalValue) value).cal(cal);
            }
        } else {
            String name = lVal.ident;
            Value value = module.find(name);
//            if (value instanceof GlobalValue globalValue) {
//                if (!(((PointerType) value.getType()).getType() instanceof ArrayType)) {
//                    saveValue = new Constant(Integer.toString(globalValue.getNum()), new IntegerType());
//                    return;
//                }
//            }
//            if (value instanceof Constant constant) {
//                saveValue = constant;
//                return;
//            }
            if (((PointerType) value.getType()).getType() instanceof IntegerType) {
                Type type = ((PointerType) value.getType()).getType();
                saveValue = factory.buildLoadInst(type, value, currentBB);
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
//                        break;
//                    } else {
//                        saveValue = factory.buildCalculateValue(new Constant(null, new IntegerType()), saveValue, opList.get(i), new IntegerType(), currentBB);
//                    }
//                }
            } else {
                ArrayList<Value> values = new ArrayList<>();
                for (Exp exp : lVal.expArrayList) {
                    visitExp(exp);
                    values.add(saveValue);
                    saveValue = null;
                }
                saveValue = factory.buildGetElementPtr(currentBB, values, value, 1, true);
                saveValue = currentBB.getInstructions().get(currentBB.getInstructions().size() - 1);
//                saveValue = factory.buildLoadInst(((PointerType) saveValue.getType()).getType(), saveValue, currentBB);
            }
        }
    }

    private void visitPrimaryExp(PrimaryExp primaryExp) {
        if (isGlobal) {
            if (primaryExp.number != null) {
                saveNum = primaryExp.number.intConst;
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
//                        break;
//                    } else {
//                        saveNum = factory.buildCalculateNumber(0, saveNum, opList.get(i));
//                    }
//                }

            } else if (primaryExp.exp != null) {
                visitExp(primaryExp.exp);
            } else if (primaryExp.lVal != null) {
                visitLVal(primaryExp.lVal);
            }
        } else {
            if (primaryExp.number != null) {
                String name = Integer.toString(primaryExp.number.intConst);
                saveValue = new Constant(name, new IntegerType());
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
//                        break;
//                    } else if (opList.get(i).equals("-")) {
//                        if (saveValue instanceof Constant) {
//                            int num = -Integer.parseInt(saveValue.getMemName());
//                            ((Constant) saveValue).setMemName(Integer.toString(num));
//                            opList.remove(opList.get(opList.size() - 1));
//                        } else
//                            saveValue = factory.buildCalculateValue(new Constant("0", new IntegerType()), saveValue, opList.get(i), new IntegerType(), currentBB);
//                    } else {
//                        opList.remove(opList.get(opList.size() - 1));
//                    }
//                }
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
                for (int i = opList.size() - 1; i >= 0; i--) {
                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
                        break;
                    } else {
                        saveNum = factory.buildCalculateNumber(0, saveNum, opList.get(i));
                    }
                }
            } else {
                opList.add(unaryExp.unaryOp.unaryOp);
                visitUnaryExp(unaryExp.unaryExp);
                for (int i = opList.size() - 1; i >= 0; i--) {
                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-") || opList.get(i).equals("!"))) break;
                    else if (opList.get(i).equals("-")) {
                        if (saveValue instanceof Constant) {
                            int num = -Integer.parseInt(saveValue.getMemName());
                            ((Constant) saveValue).setMemName(Integer.toString(num));
                            opList.remove(opList.get(opList.size() - 1));
                        } else {
                            saveValue = factory.buildCalculateValue(new Constant("0", new IntegerType()), saveValue, opList.get(i), new IntegerType(), currentBB);
                        }
                    } else if (opList.get(i).equals("+")) {
                        opList.remove(opList.get(opList.size() - 1));
                    } else {
                        if (saveValue instanceof Constant) {
                            int num = Integer.parseInt(saveValue.getMemName());
                            if (num != 0) num = 0;
                            else num = 1;
                            ((Constant) saveValue).setMemName(Integer.toString(num));
                            opList.remove(opList.get(opList.size() - 1));
                        } else {
                            saveValue = factory.buildIcmpInst(currentBB, null, new IntegerType(), "==", new Constant("0", new IntegerType()), saveValue);
                            saveValue = factory.buildZextInst(currentBB, saveValue, new IntegerType());
                            opList.remove(opList.get(opList.size() - 1));
                        }
                    }
                }
            }


        } else if (unaryExp.primaryExp != null) {
            visitPrimaryExp(unaryExp.primaryExp);
//            if (isGlobal) {
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
//                        break;
//                    } else {
//                        saveNum = factory.buildCalculateNumber(0, saveNum, opList.get(i));
//                    }
//                }
//            } else {
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) break;
//                    else if (opList.get(i).equals("-")) {
//                        if (saveValue instanceof Constant) {
//                            int num = -Integer.parseInt(saveValue.getMemName());
//                            ((Constant) saveValue).setMemName(Integer.toString(num));
//                            opList.remove(opList.get(opList.size() - 1));
//                        } else {
//                            saveValue = factory.buildCalculateValue(new Constant("0", new IntegerType()), saveValue, opList.get(i), new IntegerType(), currentBB);
//                        }
//                    } else opList.remove(opList.get(opList.size() - 1));
//                }
//            }
        } else {
            Value function = module.find(unaryExp.ident);
            if (unaryExp.funcRParams == null) {
                saveValue = factory.buildCall(currentBB, function.getMemName(), function.getType(), null);
            } else {
                ArrayList<Value> args = new ArrayList<>();
                for (Exp exp : unaryExp.funcRParams.expArrayList) {
                    visitExp(exp);
                    args.add(saveValue);
                    saveValue = null;
                }
                saveValue = factory.buildCall(currentBB, function.getMemName(), function.getType(), args);
            }
//            if (isGlobal) {
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) {
//                        break;
//                    } else {
//                        saveNum = factory.buildCalculateNumber(0, saveNum, opList.get(i));
//                    }
//                }
//            } else {
//                for (int i = opList.size() - 1; i >= 0; i--) {
//                    if (!(opList.get(i).equals("+") || opList.get(i).equals("-"))) break;
//                    else if (opList.get(i).equals("-")) {
//                        if (saveValue instanceof Constant) {
//                            int num = -Integer.parseInt(saveValue.getMemName());
//                            ((Constant) saveValue).setMemName(Integer.toString(num));
//                            opList.remove(opList.get(opList.size() - 1));
//                        } else {
//                            saveValue = factory.buildCalculateValue(new Constant("0", new IntegerType()), saveValue, opList.get(i), new IntegerType(), currentBB);
//                        }
//                    } else opList.remove(opList.get(opList.size() - 1));
//                }
//            }
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

    private void visitRelExp(RelExp relExp) {
        visitAddExp(relExp.addExpArrayList.get(0));
        Value value = saveValue;
        String relOp;
        for (int i = 1; i < relExp.addExpArrayList.size(); i++) {
            value = saveValue;
            relOp = relExp.RelOp.get(i - 1);
            visitAddExp(relExp.addExpArrayList.get(i));
            saveValue = factory.buildIcmpInst(currentBB, null, new IntegerType(), relOp, value, saveValue);
            saveValue = factory.buildZextInst(currentBB, saveValue, new IntegerType());
        }
    }

    private void visitEqExp(EqExp eqExp) {
        visitRelExp(eqExp.relExpArrayList.get(0));
        Value value = saveValue;
        String eqOp;
        for (int i = 1; i < eqExp.relExpArrayList.size(); i++) {
            value = saveValue;
            eqOp = eqExp.eqOp.get(i - 1);
            visitRelExp(eqExp.relExpArrayList.get(i));
            saveValue = factory.buildIcmpInst(currentBB, null, new IntegerType(), eqOp, value, saveValue);
            saveValue = factory.buildZextInst(currentBB, saveValue, new IntegerType());
        }
    }

    private void visitLAndExp(LAndExp lAndExp, BasicBlock trueBlock, BasicBlock falseBlock) {
        BasicBlock basicBlock;
        for (int i = 0; i < lAndExp.eqExpArrayList.size(); i++) {
            if (i != lAndExp.eqExpArrayList.size() - 1) {
                basicBlock = factory.buildBasicBlock(currentFunction);

            } else basicBlock = trueBlock;
            visitEqExp(lAndExp.eqExpArrayList.get(i));
            saveValue = factory.buildIcmpInst(currentBB, null, new IntegerType(),
                    "!=", new Constant("0", new IntegerType()), saveValue);
            if (i != lAndExp.eqExpArrayList.size() - 1) {
                factory.buildBrInst(currentBB, null, null, saveValue, null, basicBlock, falseBlock);
                currentBB = basicBlock;
            }

        }
    }

    private void visitLOrExp(LOrExp lOrExp, BasicBlock stmt1, BasicBlock stmt2) {
        BasicBlock basicBlock;
        for (int i = 0; i < lOrExp.lAndExpArrayList.size(); i++) {
            if (i != lOrExp.lAndExpArrayList.size() - 1) {
                basicBlock = factory.buildBasicBlock(currentFunction);
            } else {
                basicBlock = stmt2;
            }
            visitLAndExp(lOrExp.lAndExpArrayList.get(i), stmt1, basicBlock);
            factory.buildBrInst(currentBB, null, null, saveValue, null, stmt1, basicBlock);
            currentBB = i == lOrExp.lAndExpArrayList.size() - 1 ? stmt1 : basicBlock;
        }
    }

    private void visitConstExp(ConstExp constExp) {
        visitAddExp(constExp.addExp);

    }

    public ArrayList<String> getOpList() {
        return opList;
    }

}
