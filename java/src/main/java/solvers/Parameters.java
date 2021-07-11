package solvers;

public class Parameters {

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

    private int maxPositions = 2000;

    private boolean writeIntermediateResults = true;




}
