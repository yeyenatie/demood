package demo0908;

public class Num {
    public static void main(String[] args) {
        int i;
        int sum = 0;
        i = 1;
        while (i <= 100) {
            if (i % 2 != 0) {
                sum += i;
            }
            i++;
        }
        System.out.println("1-100之间所有奇数的和为：" + sum);


        for (i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(i + "*" + j + "=" + (i * j));
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}
