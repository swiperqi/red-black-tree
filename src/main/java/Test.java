package main.java;

public class Test {
    public static void main(String[] args) {
        RBTree tree = new RBTree();
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (Integer n : nums) {
            try {
                tree.put(n);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(n);
                return;
            }
        }
        tree.print();
    }
}
