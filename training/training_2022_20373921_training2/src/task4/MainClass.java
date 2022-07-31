package src.task4;

public class MainClass {
    public static void main(String[] args) {
        Tray tray = new Tray();
        Producer producer = new Producer(tray);
        Consumer consumer = new Consumer(tray);
        producer.start();
        consumer.start();
    }
}
