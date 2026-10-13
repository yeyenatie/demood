package demo0914.io;

import java.io.File;

public class FileDemo {
    public static void main(String[] args) {
        File file =new File("test.txt");
        System.out.println("是否存在："+file.exists());
        System.out.println("是否是文件："+file.isFile());
        System.out.println("是否是目录："+file.isDirectory());
        try {
            boolean newFile=file.createNewFile();
            System.out.println("创建文件："+newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dir =new File("demoDir");
        dir.mkdir();
        System.out.println(file.getName());
        System.out.println(dir.getAbsolutePath());
//        file.delete();
    }
}
