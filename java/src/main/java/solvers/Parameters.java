package solvers;

import java.time.Duration;

public class Parameters {

    private int maxPositions = 2000;

    private int translationX = 0;
    private int translationY = 0;

    private boolean writeIntermediateResults = true;

    private Duration timeout = Duration.ZERO;

    public int getMaxPositions() {
        return maxPositions;
    }

    public void setMaxPositions(int maxPositions) {
        this.maxPositions = maxPositions;
    }

    public boolean isWriteIntermediateResults() {
        return writeIntermediateResults;
    }

    public void setWriteIntermediateResults(boolean writeIntermediateResults) {
        this.writeIntermediateResults = writeIntermediateResults;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public int getTranslationX() {
        return translationX;
    }

    public void setTranslationX(int translationX) {
        this.translationX = translationX;
    }

    public int getTranslationY() {
        return translationY;
    }

    public void setTranslationY(int translationY) {
        this.translationY = translationY;
    }
}
