package demo0304;

import java.util.Scanner;

public class jijiepanduan {
    public static void main(String[] args) {
        Scanner sa = new Scanner(System.in);
        System.out.println("请输入一个月份");
        int month = sa.nextInt();
        switch (month) {
            case 3:
            case 4:
            case 5:
                System.out.println("春季");
                break;
            case 6:
            case 7:
            case 8:
                System.out.println("夏季");
                break;
            case 9:
            case 10:
            case 11:
                System.out.println("秋季");
                break;
            case 12:
            case 1:
            case 2:
                System.out.println("冬季");
                break;
            default:
                System.out.println("输入有误，请输入1-12的月份");
                break;
        }
        sa.close();
    }
}
