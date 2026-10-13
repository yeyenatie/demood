package demo0914.io;

import java.io.*;

public class BufferedDemo {
    public static void main(String[] args) {
        try(
                BufferedReader br =new BufferedReader(new FileReader("test.txt"));
                BufferedWriter bw =new BufferedWriter(new FileWriter("copyTest.txt"));
        ) {
            String line;
            while ((line=br.readLine())!=null) {
                bw.write(line);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
