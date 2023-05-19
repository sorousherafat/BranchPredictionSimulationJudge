package hardwar.branch.prediction.shared;

public enum BranchResult {
    TAKEN,
    NOT_TAKEN;

    public static boolean isTaken(BranchResult branchResult) {
        return branchResult == TAKEN;
    }
}
