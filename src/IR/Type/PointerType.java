package IR.Type;

public class PointerType implements Type {


    private Type type;

    public PointerType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getByteSize() {
        int size = 1;
        if (type instanceof ArrayType arrayType) {
            for (Integer i : arrayType.getArraySize()) {
                size *= i;
            }
        }
        return size * 4;
    }

    @Override
    public String toString() {
        return type.toString() + "*";
    }
}
