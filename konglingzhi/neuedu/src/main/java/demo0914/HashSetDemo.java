package demo0914;

import java.util.HashSet;
import java.util.Set;

public class HashSetDemo {
    public static void main(String[] args) {
        Set<String> set=new HashSet<String>();
        set.add("1号");
        set.add("2号");
        set.add("3号");
        set.add("4号");
        set.add("5号");
        set.add("6号");
        System.out.println(set);
    }
}
