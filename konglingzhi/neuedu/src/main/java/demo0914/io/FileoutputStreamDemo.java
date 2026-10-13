package demo0914.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileoutputStreamDemo {
    public static void main(String[] args) {
        try {
            FileOutputStream fos=new FileOutputStream("test.txt");
            String str = "Hello, World!";
            byte[] bytes = str.getBytes();
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
