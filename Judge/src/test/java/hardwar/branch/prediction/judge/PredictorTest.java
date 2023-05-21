package hardwar.branch.prediction.judge;

import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PredictorTest {

    @Test
    void testPredictor() {
        CommandLineArgument argument = new CommandLineArgument(System.getProperties());
        List<BranchInstruction> instructions = ListUtils.readListFromFile(argument.getInstructionFile());
        List<BranchResult> expectedResults = ListUtils.readListFromFile(argument.getResultFile());
        PredictorSimulator simulator = getSimulator(argument.getPredictorName());
        List<BranchResult> actualResults = simulator.simulate(instructions);
        reportHitRate(expectedResults, actualResults);
        assertEquals(actualResults, expectedResults);
    }

    private PredictorSimulator getSimulator(String predictorName) {
        PredictorProvider provider = new ReflectivePredictorProvider(predictorName);
        BranchPredictor predictor = provider.getPredictor();
        return new PredictorSimulator(predictor);
    }

    private void reportHitRate(List<BranchResult> expectedResults, List<BranchResult> actualResults) {
        long hitCount = ListUtils.countEqualElements(actualResults, expectedResults);
        long totalCount = actualResults.size();
        double hitRate = (double) hitCount / totalCount;
        System.out.printf("Hit rate = %f\n", hitRate);
    }
}