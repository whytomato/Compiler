package IR.Type;

public class PointerType implements Type {


    private Type type;

    public PointerType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "i32*";
    }
}
