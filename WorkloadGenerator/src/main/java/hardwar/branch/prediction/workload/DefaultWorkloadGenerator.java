package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.judge.ListUtils;
import hardwar.branch.prediction.judge.PredictorProvider;
import hardwar.branch.prediction.judge.PredictorSimulator;
import hardwar.branch.prediction.judge.ReflectivePredictorProvider;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;

public class DefaultWorkloadGenerator implements WorkloadGenerator {

    private final ActualWorkloadGenerator actualGenerator;

    private final PredictorSimulator simulator;

    public DefaultWorkloadGenerator(String predictorName) {
        this.actualGenerator = new ActualWorkloadGeneratorBuilder().build();
        PredictorProvider provider = new ReflectivePredictorProvider(predictorName);
        BranchPredictor predictor = provider.getPredictor();
        this.simulator = new PredictorSimulator(predictor);
    }

    public Workload generate() {
        Workload actualWorkload = actualGenerator.generate();
        List<BranchInstruction> instruction = actualWorkload.getInstruction();
        List<BranchResult> result = actualWorkload.getResult();
        List<BranchResult> expectedResult = simulator.simulate(instruction, result);
        double hitRate = ListUtils.getSimilarity(expectedResult, result);
        System.out.printf("Hit rate: %f\n", hitRate);
        return new Workload(instruction, result, expectedResult);
    }
}
