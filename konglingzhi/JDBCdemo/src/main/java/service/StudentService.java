package service;

import entity.Student;

import java.util.List;

public interface StudentService {

    List<Student> listAll();

    int add(Student stu);

    int update(Student stu);

    int remove(Integer id);
}
