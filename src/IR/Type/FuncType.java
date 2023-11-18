package IR.Type;

public class FuncType implements Type {
    int retype;

    public FuncType(int retype) {
        this.retype = retype;
    }

    @Override
    public String toString() {
        if (retype == 1) return "void";
        else return "i32";
    }

    public int getRetype() {
        return retype;
    }
}
