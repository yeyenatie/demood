package demo0903;

public class unit1 {
    public static void main(String[] args) {
        int a = 3;
        a = 5;
        System.out.println(a);


        boolean flag;
        flag = true;
        System.out.println(flag);

        int stuNo = 1002;
        String sex = "男";
        double score = 98.5;
        System.out.println(stuNo + "\n" + sex + "\n" + score);
        System.out.println("\n");
        System.out.println(17 % 10);
        int d = 10;
        System.out.println(d++);
        System.out.println(--d);
        System.out.println(d--);
        System.out.println(++d);
        System.out.println(d + sex);

        a += 7;
        a /= 3;
        System.out.println(a);

        System.out.println(a >= d);

        System.out.println(1 == 1);
    }
}
