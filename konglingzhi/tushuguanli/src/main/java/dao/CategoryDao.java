package dao;


import java.util.List;

import entity.Category;


public interface CategoryDao {

    boolean add(Category category);

    boolean deleteById(int id);

    boolean update(Category category);

    List<Category> findAll();

    Category findById(int id);

    Category findByName(String name);
}
