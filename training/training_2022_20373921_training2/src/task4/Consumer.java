package src.task4;

public class Consumer extends Thread {
    private final Tray tray;
    
    public void getCargo() {
        synchronized (tray) { // 其实可以将synchronized直接写到tray中
            for (int i = 1; i <= 10; i++) {
                if (tray.getCargo() == 0) { // 空，get不了
                    try {
                        tray.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                tray.setCargo(0);
                System.out.println("Consumer get:" + i);
                
                tray.notifyAll();
            }
        }
    }
    
    @Override
    public void run() {
        this.getCargo();
    }
    
    public Consumer(Tray tray) {
        this.tray = tray;
    }
}
