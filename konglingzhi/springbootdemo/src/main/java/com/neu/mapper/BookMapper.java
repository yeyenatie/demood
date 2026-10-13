package com.neu.mapper;

import com.neu.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//mapper接口
public interface BookMapper {

    //    查询全部图书
    List<Book> selectAll();

    //    按id查询单本
    Book selectById(Integer id);

    //    按书名模糊查询
//    @Param("bookName")一定要加，当mapper方法有等多个参数或要在xml要用#{}引用字符串时，需要给参数起名，xml里才能用#{bookName}取到
    List<Book> selectBookByName(@Param("bookName") String BookName);

    //    新增
    int insertBook(Book book);

    //    修改
    int updateBook(Book book);

    //    删除
    int deleteBook(Integer id);

}
