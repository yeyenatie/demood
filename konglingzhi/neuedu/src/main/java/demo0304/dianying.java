package demo0304;

import java.util.Scanner;

public class dianying {
    public static void main(String[] args) {
        Scanner as = new Scanner(System.in);
        System.out.println("请输入年龄");
        int age = as.nextInt();
        System.out.println("请输入当前是否是工作日(1=周末，0=工作日)");
        int isWeekend = as.nextInt();
        double s = 80;
        if (age < 6 && age >= 0) {
            System.out.println("免费");
        } else if (age >= 6 && age <= 18) {
            s = s * 0.5;
            if (isWeekend == 1) {
                s = s * 0.7;
                System.out.printf("周末七折 %.2f", s);
            } else if (isWeekend == 0) {
                System.out.printf("工作日半价 %.2f", s);
            } else {
                System.out.println("输入错误");
            }
        } else if (age >= 19 && age <= 60) {
            if (isWeekend == 1) {
                s = s * 0.9;
                System.out.printf("周末九折 %.2f", s);
            } else if (isWeekend == 0) {
                System.out.printf("工作日原价 %.2f", s);
            } else {
                System.out.println("输入错误");
            }
        } else if (age > 60) {
            if (isWeekend == 1) {
                s = 30;
                System.out.printf("周末 %.2f", s);
            } else if (isWeekend == 0) {
                s = 50;
                System.out.printf("工作日 %.2f", s);
            } else {
                System.out.println("输入错误");
            }
        } else {
            System.out.println("年龄输入非法");
        }
        as.close();
    }
}