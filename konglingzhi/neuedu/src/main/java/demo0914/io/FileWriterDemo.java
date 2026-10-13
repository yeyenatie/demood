package demo0914.io;

import java.io.FileWriter;

public class FileWriterDemo {
    public static void main(String[] args) {
        try(FileWriter fw = new FileWriter("test.txt", true)) {
            fw.write("字符流测试");
            fw.write("\n");
            fw.write("字符流测试2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
