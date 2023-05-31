package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;

public class Workload {
    private final List<BranchInstruction> instructions;

    private final List<BranchResult> results;

    public Workload(List<BranchInstruction> instructions, List<BranchResult> results) {
        this.instructions = instructions;
        this.results = results;
    }

    public List<BranchInstruction> getInstructions() {
        return instructions;
    }

    public List<BranchResult> getResults() {
        return results;
    }
}
