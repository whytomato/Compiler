package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.Value;

public class ZextInst extends Instruction {
    private String memName;
    private Value value;

    public ZextInst(String name, Type type, Value value) {
        super(name, type);
        this.value = value;
    }

    public void setMemName(String memName) {
        this.memName = "%v" + memName;
    }

    @Override
    public String getMemName() {
        return memName;
    }

    @Override
    public String toString() {
        return "    " + memName + " = zext i1 " + value.getMemName() + " to " + getType();
    }
}
