package IR.Value;

import IR.Type.PointerType;
import IR.Type.Type;

import java.util.ArrayList;

public class GlobalValue extends Constant {
    private boolean isConst;
    private int num;
    private String memName;

    private ArrayList<Integer> arraySize;
    private ArrayList<Integer> P;
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
        if (arraySize != null) {
            print(0, sb);
        } else {
            sb.append(((PointerType) getType()).getType());
            sb.append(" ");
            sb.append(num);
        }
        staticI = 0;
        return sb.toString();
    }

    public void calP() {
        if (P == null) P = new ArrayList<>();
        Integer mul = 1;
        for (Integer i : arraySize) {
            mul *= i;
        }
        for (Integer i : arraySize) {
            mul /= i;
            P.add(mul);
        }
    }

    public void addArrayNum(ArrayList<Integer> num) {
        if (arrayNum == null) arrayNum = new ArrayList<>();
        arrayNum.addAll(num);
    }

    public void print(int num, StringBuilder sb) {
        if (num == arraySize.size() - 1) {
            sb.append("[").append(arraySize.get(num)).append(" x i32] [");
            for (int i = staticI; i < staticI + arraySize.get(num); i++) {
                sb.append("i32 ").append(arrayNum.get(i));
                if (i != staticI + arraySize.get(num) - 1) sb.append(", ");
            }
            staticI += arraySize.get(num);
            sb.append("]");
            return;
        }
        for (int i = num; i < arraySize.size(); i++) {
            sb.append("[").append(arraySize.get(i)).append(" x ");
        }
        sb.append("i32");
        for (Integer i : arraySize) {
            sb.append("]");
        }
        sb.append(" ");
        sb.append("[");
        for (int i = 0; i < arraySize.get(num); i++) {
            print(num + 1, sb);
            if (i != arraySize.get(num) - 1) sb.append(", ");
        }
        sb.append("]");
    }

    public void setZero() {
        int i = 1;
        for (int i1 = 0; i1 < arraySize.size(); i1++) {
            i *= arraySize.get(i1);
        }
        if (arrayNum == null) arrayNum = new ArrayList<>();
        for (int i1 = 0; i1 < i; i1++) {
            arrayNum.add(0);
        }
    }

    public int cal(ArrayList<Integer> cal) {
        int index = 0;
        for (int i = 0; i < P.size(); i++) {
            index += cal.get(i) * P.get(i);
        }
        return arrayNum.get(index);
    }
}
