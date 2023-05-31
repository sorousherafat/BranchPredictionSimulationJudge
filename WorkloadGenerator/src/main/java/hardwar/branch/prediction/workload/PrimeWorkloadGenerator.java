package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.judge.ListUtils;
import hardwar.branch.prediction.judge.PredictorProvider;
import hardwar.branch.prediction.judge.PredictorSimulator;
import hardwar.branch.prediction.judge.ReflectivePredictorProvider;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;

public class PrimeWorkloadGenerator implements WorkloadGenerator {
    private static final int MAX_TRY_COUNT = 8192;

    private final ActualWorkloadGenerator actualGenerator;

    private final WorkloadTester tester;

    private final PredictorSimulator simulator;

    public PrimeWorkloadGenerator(List<String> predictorNames, String basePredictorName) {
        this.actualGenerator = new ActualWorkloadGeneratorBuilder().build();
        this.tester = new WorkloadTester(predictorNames, basePredictorName);
        PredictorProvider provider = new ReflectivePredictorProvider(basePredictorName);
        BranchPredictor predictor = provider.getPredictor();
        this.simulator = new PredictorSimulator(predictor);
    }

    public Workload generate() {
        int tryCount = 0;
        while (true) {
            Workload actualWorkload = actualGenerator.generate();
            boolean passedTest = tester.test(actualWorkload);
            if (passedTest) {
                List<BranchInstruction> instruction = actualWorkload.getInstruction();
                List<BranchResult> predictionResult = simulator.simulate(instruction, actualWorkload.getResult());
                double hitRate = ListUtils.getSimilarity(actualWorkload.getResult(), predictionResult);
                System.out.printf("Hit rate: %f\n", hitRate);
                return new Workload(instruction, predictionResult);
            }

            tryCount += 1;
            if (tryCount >= MAX_TRY_COUNT)
                throw new RuntimeException("Exceeded maximum try count");
        }
    }
}
