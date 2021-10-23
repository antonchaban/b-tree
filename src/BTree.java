import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

class BTree {
    Node root;
    int t;

    public BTree() {
    }

    public BTree(int t) {
        this.root = null;
        this.t = t;
    }

    public void traverse() {
        if (root != null) {
            ArrayList<Integer> integers = new ArrayList<>();
            ArrayList<String> valuesArray = new ArrayList<>();
            root.traverse(integers, valuesArray);
            for (int i = 0; i < integers.size(); i++) {
                int elem = integers.get(i);
                String val = valuesArray.get(i);
                System.out.printf("%d : %s |", elem, val);
            }
        }
        System.out.println();
    }

    public String search(int key) {
        String res;
        int[] comparisons = new int[1];
        Node node;
        if (root == null) node = null;
        else node = root.search(key, comparisons);
        if (node == null){
            res = "Error";
        }
        else {
            System.out.println("Number of comparisons to find " + key + ": " + comparisons[0]);
            res = node.values[node.findKey(key)];
        }

        return res;
    }

    public void insert(int key, String value) {
        if (root == null) { // створюємо вузол якщо немає
            root = new Node(t, true);
            root.keys[0] = key;
            root.values[0] = value;
            root.num = 1;
        } else {
            if (root.num == 2 * t - 1) { // якщо рут заповнився - він стає дочірнім вузлом нового рута
                Node s = new Node(t, false);
                s.child[0] = root;
                s.splitChild(0, root); // новий рут має 2 дочірніх
                int i = 0;
                if (s.keys[0] < key)
                    i++;
                s.child[i].insertNotFull(key, value);

                root = s;
            } else
                root.insertNotFull(key, value);
        }
    }

    public void remove(int key) {
        if (root == null) {
            System.out.println("The tree is empty");
            return;
        }
        root.remove(key);
        if (root.num == 0) {
            if (root.isLeaf)
                root = null;
            else
                root = root.child[0];
        }
    }


    //////////

    private ArrayList<String> getFileContent(String pathname) {
        ArrayList<String> strings = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(pathname))) {
            strings.addAll(Arrays.asList(dis.readUTF().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public void restoreTree() {
        ArrayList<String> intArr = getFileContent("dataInt.bin");
        ArrayList<String> strArr = getFileContent("dataStr.bin");
        this.t = Integer.parseInt(intArr.get(0));
        for (int i = 1; i < strArr.size(); i++) {
            insert(Integer.parseInt(intArr.get(i)), strArr.get(i));
        }
    }

    public void saveToFile() throws IOException {
        ArrayList<Integer> intArr = new ArrayList<>();
        ArrayList<String> strArr = new ArrayList<>();
        root.traverse(intArr, strArr);
        StringBuilder builderInt = new StringBuilder();
        StringBuilder builderStr = new StringBuilder();
        builderInt.append(this.t).append("\n");
        for (int i = 0; i < intArr.size(); i++) {
            int intEl = intArr.get(i);
            String strEl = strArr.get(i);
            builderInt.append(intEl).append("\n");
            builderStr.append(strEl).append("\n");
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("dataInt.bin"))) {
            dos.writeUTF(builderInt.toString());
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("dataStr.bin"))) {
            dos.writeUTF(builderStr.toString());
        }

    }
}