package service.impl;


import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import entity.Category;
import service.CategoryService;

import java.util.List;


public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao = new CategoryDaoImpl();


    @Override
    public boolean addCategory(Category category) {
        // 名称判重：去掉首尾空格后比较
        String name = category.getName().trim();
        Category exist = categoryDao.findByName(name);
        if (exist != null) {
            return false;
        }
        category.setName(name);
        return categoryDao.add(category);
    }


    @Override
    public boolean deleteCategory(int id) {
        return categoryDao.deleteById(id);
    }


    @Override
    public boolean updateCategory(Category category) {
        return categoryDao.update(category);
    }


    @Override
    public List<Category> findAllCategory() {
        return categoryDao.findAll();
    }


    @Override
    public Category findCategoryById(int id) {
        return categoryDao.findById(id);
    }
}
