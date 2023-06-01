package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;

public class Workload {
    private final List<BranchInstruction> instruction;

    private final List<BranchResult> result;

    private final List<BranchResult> expectedResult;

    public Workload(List<BranchInstruction> instruction, List<BranchResult> result, List<BranchResult> expectedResult) {
        this.instruction = instruction;
        this.result = result;
        this.expectedResult = expectedResult;
    }

    public List<BranchInstruction> getInstruction() {
        return instruction;
    }

    public List<BranchResult> getResult() {
        return result;
    }

    public List<BranchResult> getExpectedResult() {
        return expectedResult;
    }
}
