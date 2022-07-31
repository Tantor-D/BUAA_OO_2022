package src.task4;

public class Producer extends Thread {
    private final Tray tray;
    
    public void putCargo() {
        
        synchronized (tray) {
            for (int i = 1; i <= 10; i++) {
                while (tray.getCargo() != 0) {
                    try {
                        tray.wait(); // 有货物，暂时退出
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tray.setCargo(i);
                System.out.println("Producer put:" + i);
                
                try {
                    Thread.sleep((int) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tray.notifyAll();
            }
        }
    }
    
    @Override
    public void run() {
        this.putCargo();
    }
    
    public Producer(Tray tray) {
        this.tray = tray;
    }
}
