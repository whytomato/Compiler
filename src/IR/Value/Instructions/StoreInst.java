package IR.Value.Instructions;

import IR.Type.ArrayType;
import IR.Type.IntegerType;
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
        StringBuilder sb = new StringBuilder();
        sb.append("    " + "store" + " ");
//        if (value instanceof Argument) {
//            if (value.getType() instanceof IntegerType) {
//                sb.append(value.getType()).append(" ").append(value.getMemName());
//            } else {
//                if (((PointerType) value.getType()).getType() instanceof ArrayType) {
//                    print(sb, (ArrayType) ((PointerType) value.getType()).getType());
//                    sb.append("*").append(value.getMemName()).append(" ");
//                } else sb.append(value.getType()).append(" ").append(value.getMemName());
//            }
//        } else {
//            sb.append(value.getType()).append(" ").append(value.getMemName());
//        }
//        sb.append(", ");
//        if (((PointerType) pointer.getType()).getType() instanceof PointerType) {
//            if (((PointerType) ((PointerType) pointer.getType()).getType()).getType() instanceof ArrayType) {
//                print(sb, (ArrayType) ((PointerType) ((PointerType) pointer.getType()).getType()).getType());
//                sb.append("* *").append(pointer.getMemName());
//            } else
//                sb.append(pointer.getType()).append(" * ").append(pointer.getMemName());
//        } else sb.append(pointer.getType()).append(" ").append(pointer.getMemName());
        sb.append(value.getType()).append(" ").append(value.getMemName());
        sb.append(", ");
        sb.append(pointer.getType()).append(" ").append(pointer.getMemName());
        return sb.toString();
    }

    public void print(StringBuilder sb, ArrayType arrayType) {
        if (arrayType.getElementType() instanceof IntegerType) {
            sb.append("[").append(arrayType.getArraySize().get(0)).append(" x i32] ");
            return;
        }
        sb.append(arrayType);
        sb.append(" ");
    }
}
