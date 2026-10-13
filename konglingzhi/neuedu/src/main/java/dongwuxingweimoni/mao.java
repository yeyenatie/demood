package dongwuxingweimoni;

public class mao extends zhulei implements Xingwei{
    public mao(String name) {
        super(name);
    }
    @Override
    public void jiaosheng() {
        System.out.println(name+"喵喵叫");
    }
}
