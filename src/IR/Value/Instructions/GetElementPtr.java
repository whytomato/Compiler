package IR.Value.Instructions;

import IR.Type.ArrayType;
import IR.Type.IntegerType;
import IR.Type.PointerType;
import IR.Type.Type;
import IR.Value.Constant;
import IR.Value.Value;

import java.util.ArrayList;

public class GetElementPtr extends Instruction {
    private Value ptrval;
    private ArrayList<Value> index;
    private String memName;
    private Boolean flag;

    public GetElementPtr(String name, Type type, Boolean flag) {
        super(name, type);
        this.index = new ArrayList<>();
        this.flag = flag;
    }

    public void calIndex(ArrayList<Value> indexs, Value value, int type) {
        ptrval = value;
        if (type == 0) {

            if (value instanceof AllocInst allocInst) {
                int offset = Integer.parseInt(indexs.get(0).getMemName());
                ArrayList<Integer> pSize = ((ArrayType) ((PointerType) allocInst.getType()).getType()).getP();
                for (Integer i : pSize) {
                    Constant constant = new Constant("" + offset / i, new IntegerType());
                    index.add(constant);
                    offset = offset % i;
                }
                return;
            } else {

            }
        } else if (type == 1) {
            index = indexs;
        }

    }

    public Value getPtrval() {
        return ptrval;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    @Override
    public String getMemName() {
        return memName;
    }

    public ArrayList<Value> getIndex() {
        return index;
    }

    public void setPtrval(Value ptrval) {
        this.ptrval = ptrval;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setIndex(ArrayList<Value> index) {
        this.index = index;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(memName).append(" = ").append("getelementptr ");
//        ptrval.getType();
        sb.append(((PointerType) ptrval.getType()).getType()).append(", ");
        sb.append(((PointerType) ptrval.getType()).getType()).append("*").append(ptrval.getMemName());
        if (flag) {
            sb.append(", ").append("i32").append(" 0");
            for (Value value : index) {
                sb.append(", ").append(value.getType()).append(" ").append(value.getMemName());
            }

        } else {
            sb.append(", ").append(index.get(0).getType()).append(" ").append(index.get(0).getMemName());
        }

        return sb.toString();
    }
}
