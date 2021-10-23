import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BTree tree = new BTree(50);
        //tree.restoreTree();
        /*Random rand = new Random();
        for(int i = 0; i < 1000; i++){
            tree.insert(rand.nextInt(10000));
        }*/


        tree.insert(1, "Bukreev");
        tree.insert(2, "Boiko");
        tree.insert(3, "Bublik");
        tree.insert(4, "Horduz");
        tree.insert(5, "Dudchenko");
        tree.insert(6, "Ivanchenko");
        tree.insert(7, "Karvanskiy");
        tree.insert(8, "Karpov");
        tree.insert(9, "Kashtalian");
        tree.insert(10, "Kobylinskiy");
        tree.insert(11, "Korol");
        tree.insert(12, "Krivoruk");
        tree.insert(13, "Kuksyuk");
        tree.insert(14, "Mytiev");
        tree.insert(15, "Ochkas");
        tree.insert(16, "Sachko");
        tree.insert(17, "Sachko");
        tree.insert(18, "Fediay");
        tree.insert(19, "Filonenko");
        tree.insert(20, "Filianin");
        tree.insert(21, "Hamad");
        tree.insert(22, "Hodnev");
        tree.insert(23, "Tsukanova");
        tree.insert(24, "Chaban");
        tree.insert(25, "Shevchuk");
        tree.insert(26, "Yaremchuk");
        tree.traverse();

        System.out.println(tree.search(24));
        tree.search(18);
        tree.search(13);
        tree.search(12);
        tree.search(8);
        tree.search(6);
        tree.search(15);
        tree.search(11);
        tree.search(1);
        tree.search(3);
        tree.search(25);
        tree.search(21);
        tree.search(2);
        tree.search(17);
        tree.search(19);


       /* tree.remove(2);
        tree.remove(6);
        System.out.println();
        tree.traverse();*/


        tree.saveToFile();
        /*tree.restoreTree();
        tree.traverse();
        System.out.println(tree.search(3));*/
    }
}
