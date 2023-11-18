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


    public void addSize(int num) {
        if (arraySize == null) arraySize = new ArrayList<>();
        arraySize.add(num);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(getMemName()).append(" = alloca ");
//        if (((PointerType) getType()).getType() instanceof ArrayType) {
//            print(sb, (ArrayType) ((PointerType) getType()).getType());
//        } else if (((PointerType) getType()).getType() instanceof PointerType pointerType) {
//            if (pointerType.getType() instanceof ArrayType) {
//                print(sb, (ArrayType) ((PointerType) ((PointerType) getType()).getType()).getType());
//                sb.append("*");
//            } else sb.append(((PointerType) getType()).getType());
//        } else {
//            sb.append(((PointerType) getType()).getType());
//        }
        sb.append(((PointerType) getType()).getType());
        return sb.toString();
    }


//    public void print(StringBuilder sb, ArrayType arrayType) {
//        if (arrayType.getElementType() instanceof IntegerType) {
//            sb.append("[").append(arrayType.getArraySize().get(0)).append(" x i32] ");
//            return;
//        }
//        sb.append(arrayType);
//        sb.append(" ");
//    }
}
