package com.neu.controller;

import com.neu.entity.Book;
import com.neu.mapper.BookMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin//允许跨域处理
@RestController
@RequestMapping("/book")//统一接口前缀，一下路径不再加/book
public class BookController {
    @Resource
    private BookMapper bookMapper;

    //    查询全部图书
    @GetMapping("/list")
    public List<Book> getBookList() {
        List<Book> list = bookMapper.selectAll();
        return list;
    }

    //    根据id查询
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Integer id) {
        Book book = bookMapper.selectById(id);
        return book;
    }

    //    按书名模糊查询
//    localhost:8080/book/search?name=xxx
    @GetMapping("/search")
//    用@RequestParam接受参数，如?name=xxx，问号后的参数,不传name则查询全部
    public List<Book> searchBooks(@RequestParam(required = false) String name) {
        List<Book> list = bookMapper.selectBookByName(name);
        return list;
    }

    //    新增
    @PostMapping("/save")
//    @RequestBody接受请求体中的JSON数据，并转换为Java对象
    public Map<String, Object> save(@RequestBody Book book) {
        Map<String, Object> result = new HashMap<>();
        int insertResult = bookMapper.insertBook(book);
        if (insertResult > 0) {
            result.put("code", 200);
            result.put("msg", "新增成功,新id=" + book.getId());
        } else {
            result.put("code", 500);
            result.put("msg", "新增失败");
        }
        return result;
    }

    //    修改
    @PutMapping("/update")
//    @RequestBody接受请求体中的JSON数据，并转换为Java对象
    public Map<String, Object> update(@RequestBody Book book) {
        Map<String, Object> result = new HashMap<>();
        int updateResult = bookMapper.updateBook(book);
        if (updateResult > 0) {
            result.put("code", 200);
            result.put("msg", "修改成功");
        } else {
            result.put("code", 500);
            result.put("msg", "未找到id=" + book.getId() + "的图书");
        }
        return result;
    }

    //    删除
    @DeleteMapping("/delete/{id}")
//    @PathVariable接受路径参数，如/delete/1
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        int deleteResult = bookMapper.deleteBook(id);
        if (deleteResult > 0) {
            result.put("code", 200);
            result.put("msg", "删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "未找到id=" + id + "的图书");
        }
        return result;
    }

}
