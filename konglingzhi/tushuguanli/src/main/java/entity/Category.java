package entity;


public class Category {
    private Integer id;
    private String name;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "分类编号：" + id +
                ", 分类名：" + name;
    }
}