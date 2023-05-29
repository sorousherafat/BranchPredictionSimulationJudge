package hardwar.branch.prediction.judge;

public class TestFailedException extends RuntimeException {
    public TestFailedException(long passed, long total) {
        super("Test failed, " + passed + " out of " + total + " assertions passed");
    }
}
