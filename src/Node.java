import java.util.ArrayList;

class Node {
    int[] keys;
    String[] values;
    int t;
    Node[] child;
    int num;
    boolean isLeaf;

    public Node(int t, boolean isLeaf) {
        this.t = t;
        this.isLeaf = isLeaf;
        this.keys = new int[2 * this.t - 1];
        this.values = new String[2 * this.t - 1];
        this.child = new Node[2 * this.t];
        this.num = 0;
    }

    public int findKey(int key) {
        int idx = 0;
        while (idx < num && keys[idx] < key)
            ++idx;
        return idx;
    }


    public void insertNotFull(int key, String value) {
        int i = num - 1;

        if (isLeaf) {
            while (i >= 0 && keys[i] > key) { // пошук позиції якщо вузол є листком дерева
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
                i--;
            }
            keys[i + 1] = key;
            values[i + 1] = value;
            num++;
        } else {
            while (i >= 0 && keys[i] > key) // пошук позиції якщо вузол не є листком дерева
                i--;
            if (child[i + 1].num == 2 * t - 1) { // якщо дочірній заповнений то розділяємо
                splitChild(i + 1, child[i + 1]);
                if (keys[i + 1] < key)
                    i++;
            }
            child[i + 1].insertNotFull(key, value);
        }
    }

    public void splitChild(int i, Node y) {

        Node z = new Node(y.t, y.isLeaf);
        z.num = t - 1;

        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
            z.values[j] = y.values[j + t];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < t; j++)
                z.child[j] = y.child[j + t];
        }
        y.num = t - 1;

        for (int j = num; j >= i + 1; j--)
            child[j + 1] = child[j];
        child[i + 1] = z;

        for (int j = num - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
            values[j + 1] = values[j];
        }

        keys[i] = y.keys[t - 1];
        values[i] = y.values[t - 1];

        num++;
    }

    public void traverse(ArrayList<Integer> integers, ArrayList<String> valuesArray) {
        int i;
        for (i = 0; i < num; i++) {
            if (!isLeaf)
                child[i].traverse(integers, valuesArray);
            integers.add(keys[i]);
            valuesArray.add(values[i]);
        }

        if (!isLeaf) {
            child[i].traverse(integers, valuesArray);
        }
    }

    public Node search(int key, int[] comparisons) {
        int high = num;
        int low = 0;
        int medium = (high + low) / 2;
        while (low <= high) {
            comparisons[0]++;
            medium = (high + low) / 2;
            if(keys[medium] == key) {
                comparisons[0] += 1;
                return this;
            } else if (keys[medium] < key) {
                comparisons[0] += 2;
                low = medium + 1;
            } else {
                high = medium - 1;
            }
        }

        if (isLeaf)
            return null;
        return child[medium].search(key, comparisons);


    }

    public void remove(int key) {
        int idx = findKey(key);
        if (idx < num && keys[idx] == key) {
            if (isLeaf)
                removeFromLeaf(idx);
            else
                removeFromNonLeaf(idx);
        } else {
            if (isLeaf) {
                System.out.printf("The key %d not in tree\n", key);
                return;
            }

            boolean flag = idx == num;
            if (child[idx].num < t)
                fill(idx);
            if (flag && idx > num)
                child[idx - 1].remove(key);
            else
                child[idx].remove(key);
        }
    }

    public void removeFromLeaf(int idx) {
        for (int i = idx + 1; i < num; ++i) {
            keys[i - 1] = keys[i];
            values[i - 1] = values[i];
        }
        num--;
    }

    public void removeFromNonLeaf(int idx) {
        int key = keys[idx];

        if (child[idx].num >= t) {
            int predInt = getPredInt(idx);
            String predStr = getPredStr(idx);
            values[idx] = predStr;
            keys[idx] = predInt;
            child[idx].remove(predInt);
        } else if (child[idx + 1].num >= t) {
            int succ = getSuc(idx);
            keys[idx] = succ;
            child[idx + 1].remove(succ);
        } else {
            merge(idx);
            child[idx].remove(key);
        }
    }

    public int getPredInt(int idx) { // шукаємо крайній правий вузол із лівого піддерева
        Node cur = child[idx];
        while (!cur.isLeaf)
            cur = cur.child[cur.num];
        return cur.keys[cur.num - 1];
    }

    public String getPredStr(int idx) {
        Node cur = child[idx];
        while (!cur.isLeaf)
            cur = cur.child[cur.num];
        return cur.values[cur.num - 1];
    }

    public int getSuc(int idx) {
        Node cur = child[idx + 1];
        while (!cur.isLeaf)
            cur = cur.child[0];
        return cur.keys[0];
    }


    public void fill(int idx) {

        if (idx != 0 && child[idx - 1].num >= t)
            borrowFromPrev(idx);

        else if (idx != num && child[idx + 1].num >= t)
            borrowFromNext(idx);
        else {
            if (idx != num)
                merge(idx);
            else
                merge(idx - 1);
        }
    }

    public void borrowFromPrev(int idx) {
        Node child = this.child[idx];
        Node sibling = this.child[idx - 1];

        for (int i = child.num - 1; i >= 0; --i) {
            child.keys[i + 1] = child.keys[i];
            child.values[i + 1] = child.values[i];
        }


        if (!child.isLeaf) {
            for (int i = child.num; i >= 0; --i) {
                child.child[i + 1] = child.child[i];
            }

        }

        child.keys[0] = keys[idx - 1];
        child.values[0] = values[idx - 1];
        if (!child.isLeaf)
            child.child[0] = sibling.child[sibling.num];

        keys[idx - 1] = sibling.keys[sibling.num - 1];
        values[idx - 1] = sibling.values[sibling.num - 1];
        child.num += 1;
        sibling.num -= 1;
    }

    public void borrowFromNext(int idx) {
        Node child = this.child[idx];
        Node sibling = this.child[idx + 1];

        child.keys[child.num] = keys[idx];
        child.values[child.num] = values[idx];

        if (!child.isLeaf)
            child.child[child.num + 1] = sibling.child[0];

        keys[idx] = sibling.keys[0];
        values[idx] = sibling.values[0];

        for (int i = 1; i < sibling.num; ++i) {
            sibling.keys[i - 1] = sibling.keys[i];
            sibling.values[i - 1] = sibling.values[i];
        }


        if (!sibling.isLeaf) {
            for (int i = 1; i <= sibling.num; ++i)
                sibling.child[i - 1] = sibling.child[i];
        }
        child.num += 1;
        sibling.num -= 1;
    }

    public void merge(int idx) {
        Node child = this.child[idx];
        Node sibling = this.child[idx + 1];

        child.keys[t - 1] = keys[idx];
        child.values[t - 1] = values[idx];

        for (int i = 0; i < sibling.num; ++i) {
            child.keys[i + t] = sibling.keys[i];
            child.values[i + t] = sibling.values[i];
        }


        if (!child.isLeaf) {
            for (int i = 0; i <= sibling.num; ++i)
                child.child[i + t] = sibling.child[i];
        }

        for (int i = idx + 1; i < num; ++i) {
            keys[i - 1] = keys[i];
            values[i - 1] = values[i];
        }

        for (int i = idx + 2; i <= num; ++i)
            this.child[i - 1] = this.child[i];

        child.num += sibling.num + 1;
        num--;
    }
}




































            /*
            if (medium == 0 && keys[medium] > key) {
                break;
            } else if (medium == num - 1 && keys[medium] < key) {
                medium++;
                break;
            } else if (keys[medium] < key && keys[medium + 1] > key) {
                medium++;
                break;
            } else if (keys[medium] > key && keys[medium - 1] < key) {
                break;
            } else if (keys[medium] > key) {
                high = medium - 1;
            } else if (keys[medium] < key) {
                low = medium + 1;
            } else {
                return this;
            }*/