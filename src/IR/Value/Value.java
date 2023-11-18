package IR.Value;

import IR.Type.Type;
import IR.Use;

import java.util.ArrayList;

public class Value {
    private String name;

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    private Type type;
    private ArrayList<Use> useList;
    private ArrayList<User> userList;
    public static int valNumber = -1;


    public Value(String name, Type type) {
        this.name = name;
        this.type = type;
        this.useList = new ArrayList<>();
        this.userList = new ArrayList<>();
    }

    public ArrayList<Use> getUseList() {
        return useList;
    }

    public void addUse(Use use) {
        useList.add(use);
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void removeOneUseByUser(User user) {
    }

    public void removeUseByUser(User user) {
    }

    public int getValNumber() {
        valNumber++;
        return valNumber;
    }

    public void setValNumber() {
        valNumber = -1;
    }

    public String getMemName() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
