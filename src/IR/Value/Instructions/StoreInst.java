package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.BasicBlock;
import IR.Value.Value;

public class StoreInst extends Instruction {
    private BasicBlock bb;
    private Value value;
    private Value pointer;

    public StoreInst(String name, Type type, BasicBlock bb, Value value, Value pointer) {
        super(name, type);
        this.bb = bb;
        this.value = value;
        this.pointer = pointer;
    }

    public BasicBlock getBb() {
        return bb;
    }

    public Value getValue() {
        return value;
    }

    public Value getPointer() {
        return pointer;
    }

    @Override
    public String toString() {
        return "    " + "store" + " " + value.getType() + " " +
                value.getMemName() + ", " + pointer.getType() + " " +
                pointer.getMemName();
    }
}
