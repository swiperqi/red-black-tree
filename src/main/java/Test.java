package main.java;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
//        RBTree<Integer> tree = new RBTree<>();
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        for (Integer n : nums) {
//            tree.put(n);
//        }
//        tree.print();
//        System.out.println("remove 7");
//        tree.remove(7);
//        tree.print();
//        System.out.println("remove 2");
//        tree.remove(2);
//        tree.print();

        RBTree<CompareValue> tree = new RBTree<>();
        List<CompareValue> list = new ArrayList<>();
        for (Integer n : nums) {
            list.add(new CompareValue(n));
        }
        list.forEach(tree::put);
        tree.print();
    }
}
