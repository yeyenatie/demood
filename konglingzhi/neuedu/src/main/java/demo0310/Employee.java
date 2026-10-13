package demo0310;

import java.util.Scanner;

public class Employee {
    private String name;
    private int age;
    private  double salaey;
    public void showName() {
        System.out.println("员工的姓名是:"+name);
    }
    public void showAge() {
        System.out.println("员工的年龄是:"+age);
    }
    public void showSalaey() {
        System.out.println("员工领取的工资是:"+salaey);
    }
    public void updateName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalaey() {
        return salaey;
    }

    public void setSalaey(double salaey) {
        this.salaey = salaey;
    }
}
class TestEmployee {
    public static void main(String[] args) {
        Employee e = new Employee();
        e.setName("李四");
        e.showName();
        e.showAge();
        e.updateName("张三");
        e.showSalaey();
    }
}
