package IR.Value.Instructions;

import IR.Type.PointerType;
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
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(getMemName()).append(" = load ");
//        if (((PointerType) getType()).getType() instanceof PointerType pointerType) {
//            if (pointerType.getType() instanceof ArrayType) {
//
//            } else sb.append(((PointerType) getType()).getType());
//        } else {
//            sb.append(((PointerType) getType()).getType());
//        }
//        sb.append(pointer.getType()).append(pointer.getName());
        sb.append(((PointerType) pointer.getType()).getType());
        sb.append(", ");
        sb.append(pointer.getType());
        sb.append(" ");
        sb.append(pointer.getMemName());
        return sb.toString();
    }
}
