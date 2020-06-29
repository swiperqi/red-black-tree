package main.java;

public class Test {
    public static void main(String[] args) {
        RBTree tree = new RBTree();
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (Integer n : nums) {
            tree.put(n);
        }
        tree.print();
        System.out.println("remove 7");
        tree.remove(7);
        tree.print();
        System.out.println("remove 2");
        tree.remove(2);
        tree.print();
    }
}
