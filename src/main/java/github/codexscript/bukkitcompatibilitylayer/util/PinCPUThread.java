package github.codexscript.bukkitcompatibilitylayer.util;

public class PinCPUThread implements Runnable {

    private final int seconds;

    public PinCPUThread(int seconds) {
        this.seconds = seconds;
    }
    public void run() {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;
        while (elapsedTime < this.seconds * 1000L) {
            double value = Math.random(); // Random value
            for (int i = 0; i < 1000000; i++) {
                value = Math.sin(value);
            }
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }
}
