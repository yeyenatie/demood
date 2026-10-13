package demo0304;

// Java 中的人机交互 Scammer

import java.util.Scanner;

public class Sc {
    public static void main(String[] args) {
//        开启人机交互窗口
        Scanner sc = new Scanner(System.in);
//        给出提示信息
        System.out.println("请输入学生姓名");
        String a = sc.next();
        System.out.println("请输入学生成绩");
//        使用变量来接受用户输入的数据,注意数据类型
        int i = sc.nextInt();
        if (i <= 100 && i >= 90) {
            System.out.println(a + "成绩是A");
        } else if (i >= 80) {
            System.out.println(a + "成绩是B");
        } else if (i >= 70) {
            System.out.println(a + "成绩是C");
        } else if (i >= 60) {
            System.out.println(a + "成绩是D");
        } else {
            System.out.println(a + "成绩是E");
        }
//        释放资源
        sc.close();
    }
}
