package dongwuxingweimoni;

public abstract class zhulei implements Xingwei{
    protected String name;

    public zhulei(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void chifan() {
        System.out.println(name + "正在吃饭");
    }

    @Override
    public void shuijiao() {
        System.out.println(name + "正在睡觉");
    }
}
