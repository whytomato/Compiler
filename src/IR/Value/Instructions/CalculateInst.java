package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class CalculateInst extends Instruction {

    private String calType;
    private Value left;
    private Value right;
    private String memName;
    private BasicBlock bb;

    public CalculateInst(Value left, Value right, String name, Type type, BasicBlock bb) {
        super(name, type);
        this.left = left;
        this.right = right;
        this.bb = bb;
    }

    public void setCalType(String calType) {
        this.calType = calType;
    }

    public String getCalType() {
        return calType;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    @Override
    public String getMemName() {
        return memName;
    }

    @Override
    public String toString() {
        return "    " + memName + " = " + calType + " " + getType() + " "
                + getOperands().get(0).getMemName() + ", " + getOperands().get(1).getMemName();
    }
}
