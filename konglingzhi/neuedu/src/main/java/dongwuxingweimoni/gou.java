package dongwuxingweimoni;

public class gou extends zhulei implements Xingwei {

    public gou(String name) {
        super(name);
    }

    @Override
    public void jiaosheng() {
        System.out.println(name + "汪汪叫");
    }
}
