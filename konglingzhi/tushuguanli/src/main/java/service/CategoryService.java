package service;


import entity.Category;

import java.util.List;


public interface CategoryService {

    boolean addCategory(Category category);

    boolean deleteCategory(int id);

    boolean updateCategory(Category category);

    List<Category> findAllCategory();

    Category findCategoryById(int id);
}
