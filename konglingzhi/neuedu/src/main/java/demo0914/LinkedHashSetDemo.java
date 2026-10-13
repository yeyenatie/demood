package demo0914;

import java.util.LinkedHashSet;

public class LinkedHashSetDemo {
    public static void main(String[] args) {
        LinkedHashSet<String> set =new LinkedHashSet<String>();
        set.add("1号");
        set.add("2号");
        set.add("3号");
        set.add("2号");
        System.out.println(set);
    }
}
