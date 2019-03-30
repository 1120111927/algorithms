package yadong0305.algorithms.ST;

import java.util.Scanner;

public class testST {

    public static void main(String[] args) {
        //ST<String, Integer> st = new SequentialSearchST<>();
        //ST<String, Integer> st = new BinarySearchST<>(2);
        ST<String, Integer> st = new BST<>();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; scanner.hasNext(); i++) {
            st.put(scanner.next(), i);
        }
        for (String key: st.keys()) {
            System.out.println(key + " " + st.get(key));
        }
    }
}
