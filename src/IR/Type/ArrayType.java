package IR.Type;

import java.util.ArrayList;

public class ArrayType implements Type {
    private ArrayList<Integer> arraySize;
    private ArrayList<Integer> P;

    //    private Type elementType;
    private Type fatherType;

    public ArrayType() {
        this.arraySize = new ArrayList<>();
        this.P = new ArrayList<>();
    }

    public Type getElementType() {
        if (arraySize.size() == 1) {
            return new IntegerType();
        } else {
            ArrayType arrayType = new ArrayType();
            for (int i = 1; i < arraySize.size(); i++) {
                arrayType.addSize(arraySize.get(i));
            }
            return arrayType;
        }
    }

    public void addSize(int num) {
        arraySize.add(num);
    }

    public void setArraySize(ArrayList<Integer> arraySize) {
        this.arraySize = arraySize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(arraySize.get(0)).append(" x ").append(getElementType()).append("]");
        return sb.toString();
    }

    public ArrayList<Integer> getArraySize() {
        return arraySize;
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

    public ArrayList<Integer> getP() {
        return P;
    }

}
