package IR.Value;

import IR.Type.Type;

import java.util.ArrayList;

public class Argument extends Value {
    private String memName;
    private ArrayList<Integer> size;
    private int dim;

    public Argument(String name, Type type) {
        super(name, type);
        this.size = new ArrayList<>();
    }

    @Override
    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public ArrayList<Integer> getSize() {
        return size;
    }

    public void setSize(ArrayList<Integer> size) {
        this.size = size;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
}
