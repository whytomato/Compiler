package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.Value;

public class ReturnInst extends Instruction {

    private Value value;

    public ReturnInst(String name, Type type, Value value) {
        super(name, type);
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("ret ");
        if (value != null) {
            sb.append("i32 ");
            sb.append(value.getMemName());
        } else sb.append("void");
        return sb.toString();
    }
}
