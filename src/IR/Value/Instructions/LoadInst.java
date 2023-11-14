package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class LoadInst extends Instruction {
    private BasicBlock bb;
    private String memName;
    private Value pointer;

    public LoadInst(String name, Type type, BasicBlock bb) {
        super(name, type);
        this.bb = bb;
    }

    @Override
    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public Value getPointer() {
        return pointer;
    }

    public void setPointer(Value pointer) {
        this.pointer = pointer;
    }

    @Override
    public String toString() {
        return "    " + getMemName() + " = load " + getType() + ", " +
                pointer.getType() + " " + pointer.getMemName();
    }
}
