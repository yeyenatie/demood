package demo0914.itr;

import java.util.Random;

public class TicketDemo {
    public static void main(String[] args) {
        TicketTask task = new TicketTask();
//        Thread t1 = new Thread(task, "窗口1");
//        Thread t2 = new Thread(task, "窗口2");
//        Thread t3 = new Thread(task, "窗口3");
//        Thread t4 = new Thread(task, "窗口4");
//        Thread t5 = new Thread(task, "窗口5");
//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
        for (int i=1; i<=5; i++){
            Thread t = new Thread(task, "窗口"+i);
            t.start();
        }
    }
}
class TicketTask implements Runnable {
    private int ticket = 100;
    private Random random=new Random();
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (ticket <= 0) {
                    break;
                }
                int sellNum = random.nextInt(5)+1;
                if (sellNum>ticket){
                    sellNum=ticket;
                }
                ticket-=sellNum;
                System.out.printf("%s卖出%d张票，剩余:%d%n", Thread.currentThread().getName(), sellNum, ticket);
            }
            try  {
                Thread.sleep(random.nextInt(200));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
