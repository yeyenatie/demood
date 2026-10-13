package dongwuxingweimoni;

public class cheshi {
    public static void asd(Xingwei zhulei){
        zhulei.chifan();
        zhulei.shuijiao();
        zhulei.jiaosheng();
    }

    public static void main(String[] args) {
        gou gou = new gou(" 球球 ");
        mao mao = new mao(" 团子 ");
        asd(gou);
        asd(mao);
    }
}
