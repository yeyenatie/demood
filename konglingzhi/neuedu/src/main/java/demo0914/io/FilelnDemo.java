package demo0914.io;

import java.io.FileInputStream;

public class FilelnDemo {
    public static void main(String[] args) {
        try {
            FileInputStream fis=new FileInputStream("test.txt");
            int b;
            while ((b=fis.read())!=-1){
                System.out.print((char)b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
