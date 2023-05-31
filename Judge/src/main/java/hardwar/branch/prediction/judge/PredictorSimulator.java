package hardwar.branch.prediction.judge;

import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PredictorSimulator {
    private final BranchPredictor predictor;

    public PredictorSimulator(BranchPredictor predictor) {
        this.predictor = predictor;
    }

    public List<BranchResult> simulate(List<BranchInstruction> instructions, List<BranchResult> results) {
        if (instructions.size() != results.size())
            throw new RuntimeException("Instructions and results should have same size");

        return IntStream.range(0, instructions.size())
                .mapToObj(i -> {
                    BranchInstruction instruction = instructions.get(i);
                    BranchResult result = results.get(i);
                    BranchResult predictedResult = predictor.predict(instruction);
                    predictor.update(instruction, result);
                    return predictedResult;
                }).collect(Collectors.toList());
        // ...
    }
}
