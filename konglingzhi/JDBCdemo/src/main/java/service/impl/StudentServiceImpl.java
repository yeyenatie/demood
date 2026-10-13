package service.impl;

import dao.StudentDao;
import entity.Student;
import service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private StudentDao studentDao = new StudentDao();

    @Override
    public List<Student> listAll() {
        return studentDao.findAll();
    }

    @Override
    public int add(Student stu) {
        return studentDao.insert(stu);
    }

    @Override
    public int update(Student stu) {
        return studentDao.update(stu);
    }

    @Override
    public int remove(Integer id) {
        return studentDao.deleteById(id);
    }
}
