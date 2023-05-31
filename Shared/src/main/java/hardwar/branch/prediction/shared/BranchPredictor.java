package hardwar.branch.prediction.shared;

public interface BranchPredictor extends Monitorable {
    /**
     * Predict if the branch is taken or not
     *
     * @param instruction the branch instruction
     * @return predicted result of branch
     */
    BranchResult predict(BranchInstruction instruction);

    /**
     * The dynamic predictor will update its state based on the branch condition result
     *
     * @param instruction the branch instruction
     * @param result      the actual result of branch (taken or not)
     */
    void update(BranchInstruction instruction, BranchResult result);
}
