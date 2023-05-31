package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;

public class Workload {
    private final List<BranchInstruction> instruction;

    private final List<BranchResult> result;

    public Workload(List<BranchInstruction> instruction, List<BranchResult> result) {
        this.instruction = instruction;
        this.result = result;
    }

    public List<BranchInstruction> getInstruction() {
        return instruction;
    }

    public List<BranchResult> getResult() {
        return result;
    }
}
