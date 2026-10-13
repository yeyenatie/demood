package demo0310;

public class Telephone {
    String brand;
    String number;
    double dialledTime;
    double rate;
    double balance;

    public Telephone(String brand, String number, double balance) {
        this.brand = brand;
        this.number = number;
        this.balance = balance;
    }

    public void callcost() {
        double cost = dialledTime * rate;
        balance -= cost;
        System.out.println("话费: " + cost);
    }

    public void recharge(double amount) {
        balance += amount;
        System.out.println("充值: " + amount);
    }

    public void display() {
        System.out.println("品牌: " + brand);
        System.out.println("号码: " + number);
        System.out.println("通话时长: " + dialledTime);
        System.out.println("费率: " + rate);
        System.out.println("余额: " + balance);
    }
    public static void main(String[] args) {
        Telephone tel = new Telephone("TCL", "8309600", 100);
        tel.rate = 0.2;
        tel.dialledTime = 150;
        tel.display();
        tel.callcost();
        tel.recharge(50);
        tel.display();
    }
}
class Example2 {
    public static void main(String[] args) {
        Telephone tel;
        tel = new Telephone("TCL", "8309600", 100);
        tel.rate = 0.2;
        tel.dialledTime = 150;
        tel.display();
        tel.callcost();
        tel.recharge(50);
        tel.display();
    }
}