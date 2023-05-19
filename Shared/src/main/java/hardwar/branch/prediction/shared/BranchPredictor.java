package hardwar.branch.prediction.shared;

public interface BranchPredictor extends Monitorable {
    /**
     * Predict if the branch is taken or not
     *
     * @param branchAddress the PC at branch instruction
     * @return predicted result of branch
     */
    BranchResult predict(Bit[] branchAddress);

    /**
     * The dynamic predictor will update its state based on the new data
     *
     * @param branchAddress the branch address
     * @param actual        the actual result of branch (taken or not)
     */
    void update(Bit[] branchAddress, BranchResult actual);
}
