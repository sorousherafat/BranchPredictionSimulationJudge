package hardwar.branch.prediction.judged;

import hardwar.branch.prediction.shared.Bit;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

public class ToBeJudgedSimulator implements BranchPredictor {
    private static final String NOT_IMPLEMENTED_MESSAGE = "Replace this dummy class with the one you want to test.";

    @Override
    public BranchResult predict(BranchInstruction instruction) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public void update(BranchInstruction instruction, BranchResult result) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public String monitor() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_MESSAGE);
    }
}
