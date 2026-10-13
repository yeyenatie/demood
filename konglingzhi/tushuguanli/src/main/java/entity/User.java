package entity;


public class User {


    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String phone;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getRealName() {
        return realName;
    }


    public void setRealName(String realName) {
        this.realName = realName;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "用户编号：" + id +
                ", 账号：" + username +
                ", 姓名：" + realName +
                ", 电话：" + phone;
    }
}