package pl.patrykheciak.gierka;

public class FPSCounter {

    private long lastUpdate = System.currentTimeMillis();
    private int framesElapsed = 0;
    private double fps = 0;


    public double newFrame() {
        long now = System.currentTimeMillis();
        long dt = now - lastUpdate; // in ms

        if (dt > 1000) {
            double meanFrameTime = ((double) dt) / framesElapsed;
            fps = 1000.0 / meanFrameTime;
            lastUpdate = now;
            framesElapsed = 0;
        }
        framesElapsed++;
        return fps;


    }
}
