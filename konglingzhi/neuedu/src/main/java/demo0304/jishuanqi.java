package demo0304;

import java.util.Scanner;

public class jishuanqi {
    public static void main(String[] args) {
        Scanner as = new Scanner(System.in);
        System.out.println("请输入第一个数字");
        int a = as.nextInt();
        System.out.println("请输入第二个数字");
        int b = as.nextInt();
        System.out.println("请输入操作符(1代表加，2代表减，3代表乘，4代表除)");
        int c = as.nextInt();
        switch (c) {
            case 1:
                System.out.println(a + b);
                break;
            case 2:
                System.out.println(a - b);
                break;
            case 3:
                System.out.println(a * b);
                break;
            case 4:
                System.out.println(a / b);
                break;
            default:
                System.out.println("运算符错误");
                break;
        }
        as.close();
    }
}
