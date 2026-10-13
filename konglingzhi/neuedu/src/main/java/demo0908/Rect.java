package demo0908;

import java.util.Scanner;

public class Rect {
    public static void main(String[] args) {
        printRect(5, 10);
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个年份：");
        int year = sc.nextInt();
        Panduan(year);
        abs(-10);
    }


    public static void printRect(int a, int b){
        for(int i = 0; i < a; i++){
            for(int j = 0; j < b; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }



    public static void Panduan(int a){
        if(a % 400 == 0 || (a % 4 == 0 && a % 100 != 0)){
            System.out.println(a + "闰年");
        }else{
            System.out.println(a + "平年");
        }
    }


    public static int abs(int a){
        return Math.abs(a);
    }
    public static double abs(double b){
        return Math.abs(b);
    }
}
