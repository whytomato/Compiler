package IR.Value.Instructions;

import IR.Type.PointerType;
import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Value;

import java.util.ArrayList;

public class AllocInst extends Instruction {


    private String memName;
    private BasicBlock bb;
    private Value value;
    private ArrayList<Integer> arraySize;
    private ArrayList<Integer> P;

    public AllocInst(String name, Type type, BasicBlock bb) {
        super(name, type);
        this.bb = bb;

    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    @Override
    public String getMemName() {
        return memName;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(getMemName()).append(" = alloca ");
        if (arraySize != null) {
            print(0, sb);
        } else sb.append(((PointerType) getType()).getType());
        return sb.toString();
    }

    public void addSize(int num) {
        if (arraySize == null) arraySize = new ArrayList<>();
        arraySize.add(num);
    }

    public void calP() {
        if (P == null) P = new ArrayList<>();
        Integer mul = 1;
        for (Integer i : arraySize) {
            mul *= i;
        }
        for (Integer i : arraySize) {
            mul /= i;
            P.add(mul);
        }
    }

    public void print(int num, StringBuilder sb) {
        for (int i = num; i < arraySize.size(); i++) {
            sb.append("[").append(arraySize.get(i)).append(" x ");
        }
        sb.append("i32");
        for (Integer i : arraySize) {
            sb.append("]");
        }
    }
}
