package solvers;

public class Parameters {

    private int maxPositions = 2000;

    private int translationX = 0;
    private int translationY = 0;

    private boolean writeIntermediateResults = true;

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
