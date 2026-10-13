package com.neu.entity;

import lombok.Data;

//lombok自动生成get set toString方法
@Data
//实体类，映射book表
public class Book {
    //    数据库字段（_连接）与实体类属性（小驼峰命名）对应
    private Integer id;
    private String bookName;
    private String author;
    private Double price;
    private Integer stock;
    private Integer cid;
}
