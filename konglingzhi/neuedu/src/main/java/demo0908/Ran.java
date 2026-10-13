package demo0908;

import java.util.Random;
import java.util.Scanner;

public class Ran {
    public static void main(String[] args) {
        Random random = new Random();
        int ran =random.nextInt(100) + 1;
        Scanner sc = new Scanner(System.in);
        System.out.println("请猜出1-100之间的数字");
        int i=sc.nextInt();
        int s=1;
        while(i!=ran){
            if(i>ran){
                System.out.println("太大了,你还有"+(6-s)+"次机会");
                i=sc.nextInt();
            }else{
                System.out.println("太小了,你还有"+(6-s)+"次机会");
                i=sc.nextInt();
            }
            s++;
            if (s==6){
                System.out.println("你猜了"+s+"次，次数过多");
                System.out.println("游戏结束");
                return;
            }
        }
        System.out.println("恭喜你，猜对了");
    }
}
