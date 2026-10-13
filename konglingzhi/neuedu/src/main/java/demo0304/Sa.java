package demo0304;

public class Sa {
    public static void main(String[] args) {
        int score = 100;
        switch (score / 10) {
            case 10:
            case 9:
                System.out.println("成绩等级为A");
                break;
            case 8:
                System.out.println("成绩等级为B");
                break;
            case 7:
                System.out.println("成绩等级为C");
                break;
            case 6:
                System.out.println("成绩等级为D");
                break;
            default:
                System.out.println("成绩等级为E");
        }
    }
}
