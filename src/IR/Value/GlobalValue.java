package IR.Value;

import IR.Type.ArrayType;
import IR.Type.IntegerType;
import IR.Type.PointerType;
import IR.Type.Type;

import java.util.ArrayList;

public class GlobalValue extends Constant {
    private boolean isConst;
    private int num;
    private String memName;

    private ArrayList<Integer> arraySize;
    private ArrayList<Integer> arrayNum;
    private int staticI = 0;

    public GlobalValue(String name, Type type, Boolean isConst) {
        super(name, type);
        this.isConst = isConst;
        this.memName = "@" + name;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String getMemName() {
        return memName;
    }

    public void addSize(int num) {
        if (arraySize == null) arraySize = new ArrayList<>();
        this.arraySize.add(num);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(memName);
        sb.append(" = dso_local ");
        if (isConst) sb.append("constant ");
        else sb.append("global ");
        if (((PointerType) getType()).getType() instanceof ArrayType) {
            print(sb, (ArrayType) ((PointerType) getType()).getType());
        } else {
            sb.append(((PointerType) getType()).getType());
            sb.append(" ");
            sb.append(num);
        }
        staticI = 0;
        return sb.toString();
    }

    public void addArrayNum(ArrayList<Integer> num) {
        if (arrayNum == null) arrayNum = new ArrayList<>();
        arrayNum.addAll(num);
    }

    public void print(StringBuilder sb, ArrayType arrayType) {
        if (arrayType.getElementType() instanceof IntegerType) {
            sb.append("[").append(arrayType.getArraySize().get(0)).append(" x i32] [");
            for (int i = staticI; i < staticI + arrayType.getArraySize().get(0); i++) {
                sb.append("i32 ").append(arrayNum.get(i));
                if (i != staticI + arrayType.getArraySize().get(0) - 1) sb.append(", ");
            }
            staticI += arrayType.getArraySize().get(0);
            sb.append("]");
            return;
        }
        sb.append(arrayType);
        sb.append(" ");
        sb.append("[");
        for (int i = 0; i < arrayType.getArraySize().get(0); i++) {
            print(sb, (ArrayType) arrayType.getElementType());
            if (i != arrayType.getArraySize().get(0) - 1) sb.append(", ");
        }
        sb.append("]");
    }

    public void setZero() {
        int i = 1;
        for (int i1 = 0; i1 < ((ArrayType) (((PointerType) getType()).getType())).getArraySize().size(); i1++) {
            i *= ((ArrayType) (((PointerType) getType()).getType())).getArraySize().get(i1);
        }
        if (arrayNum == null) arrayNum = new ArrayList<>();
        for (int i1 = 0; i1 < i; i1++) {
            arrayNum.add(0);
        }
    }

    public int cal(ArrayList<Integer> cal) {
        int index = 0;
        ArrayList<Integer> P = ((ArrayType) (((PointerType) getType()).getType())).getP();
        for (int i = 0; i < P.size(); i++) {
            index += cal.get(i) * P.get(i);
        }
        return arrayNum.get(index);
    }
}
