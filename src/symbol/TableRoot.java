package symbol;

import java.util.HashMap;

public class TableRoot {
    private static TableRoot tableRoot = null;
    private HashMap<Integer, SymbolTable> tableHashMap;
    private int num;

    private TableRoot() {
        tableHashMap = new HashMap<>();
        num = 0;
    }

    public static TableRoot getInstance() {
        if (tableRoot == null) {
            tableRoot = new TableRoot();
        }
        return tableRoot;
    }

    public void save() {
        if (num == 0 || tableRoot.getSymbolTable().getFatherName() == null) {
            num += 1;
            tableHashMap.put(num, new SymbolTable(num));
        } else {
            tableHashMap.put(num + 1, new SymbolTable(num + 1, tableRoot.getSymbolTable().getFatherName()));
            num += 1;
        }
    }

    public void save(String name) {
        num += 1;
        tableHashMap.put(num, new SymbolTable(num, name));
    }

    public boolean search(Boolean flag, String token) {
        if (!flag) {
            return tableHashMap.get(num).getDirectory().containsKey(token);
        } else {
            int i = num;
            while (i >= 1) {
                if (tableHashMap.get(i).getDirectory().containsKey(token)) return true;
                i--;
            }
            return false;
        }
    }

    public int getNum() {
        return num;
    }

    public void delete() {
        tableHashMap.remove(num);
        num--;
    }

    public SymbolTable getSymbolTable() {
        return tableHashMap.get(num);
    }

    public SymbolTable getSymbolTable(int num) {
        return tableHashMap.get(num);
    }

    public Symbol getSymbol(String name) {
        return tableHashMap.get(1).getDirectory().get(name);
    }

    public void printf() {
        for (int i = 1; i <= num; i++) {
            System.out.println(i);
            tableHashMap.get(i).printf();
        }
    }

    public HashMap<Integer, SymbolTable> getTableHashMap() {
        return tableHashMap;
    }

    public Symbol getConst(String name) {
        int i = num;
        while (i >= 1) {
            if (tableHashMap.get(i).getDirectory().containsKey(name))
                return tableHashMap.get(i).getDirectory().get(name);
            i--;
        }
        return null;
    }

}
