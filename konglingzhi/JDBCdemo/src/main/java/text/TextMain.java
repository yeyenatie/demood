package text;

import entity.Student;
import service.StudentService;
import service.impl.StudentServiceImpl;

import java.util.List;
import java.util.Scanner;

public class TextMain {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        StudentService studentService=new StudentServiceImpl();
        while (true){
            System.out.println("==================================");
            System.out.println("1.查询所有学生");
            System.out.println("2.添加学生");
            System.out.println("3.修改学生");
            System.out.println("4.删除学生");
            System.out.println("5.退出系统");
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    List<Student> students = studentService.listAll();
                    System.out.println("=".repeat(20));
                    for (Student s : students){
                        System.out.println(s.getId()+""+s.getName()+""+ s.getAge());
                    }
                    System.out.println("=".repeat(20));
                    break;
                case 2:
                    System.out.println("请输入学生姓名：");
                    String name1 = sc.next();
                    System.out.println("请输入学生年龄：");
                    int age1 = sc.nextInt();
                    Student student = new Student(name1,age1);
                    studentService.add(student);
                    System.out.println("添加成功！");
                    break;
                case 3:
                    System.out.println("请输入学生ID：");
                    int id1 = sc.nextInt();
                    System.out.println("请输入学生姓名：");
                    String name2 = sc.next();
                    System.out.println("请输入学生年龄：");
                    int age2 = sc.nextInt();
                    Student student2 = new Student(id1,name2, age2);
                    studentService.update(student2);
                    System.out.println("修改成功！");
                    break;
                case 4:
                    System.out.println("请输入学生ID：");
                    int id2 = sc.nextInt();
                    studentService.remove(id2);
                    System.out.println("删除成功！");
                    break;
                case 5:
                    System.out.println("退出系统！");
                    return;
            }
        }
    }
}
