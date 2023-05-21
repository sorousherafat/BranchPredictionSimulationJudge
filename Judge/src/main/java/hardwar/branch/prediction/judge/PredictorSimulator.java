package hardwar.branch.prediction.judge;

import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PredictorSimulator {
    private final BranchPredictor predictor;

    public PredictorSimulator(BranchPredictor predictor) {
        this.predictor = predictor;
    }

    public List<BranchResult> simulate(List<BranchInstruction> instructions) {
        return instructions.stream()
                .map(predictor::predict)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
