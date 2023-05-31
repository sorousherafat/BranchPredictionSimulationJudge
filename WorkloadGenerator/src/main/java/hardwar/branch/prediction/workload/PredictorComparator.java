package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.judge.ListUtils;
import hardwar.branch.prediction.judge.PredictorProvider;
import hardwar.branch.prediction.judge.PredictorSimulator;
import hardwar.branch.prediction.judge.ReflectivePredictorProvider;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PredictorComparator {
    private final Map<String, BranchPredictor> predictors;

    public PredictorComparator(List<String> predictorNames) {
        this.predictors = new TreeMap<>();
        predictorNames.forEach(name -> {
            PredictorProvider provider = new ReflectivePredictorProvider(name);
            BranchPredictor predictor = provider.getPredictor();
            predictors.put(name, predictor);
        });
    }

    public List<String> compare(Workload workload) {
        List<PredictorCompareResult> compareResults = getCompareResults(workload);
        return getPredictorNames(compareResults);
    }

    private List<PredictorCompareResult> getCompareResults(Workload workload) {
        return predictors.entrySet().stream()
                .map(predictor -> new PredictorCompareResult(predictor, getHitRate(predictor.getValue(), workload)))
                .sorted((first, second) -> Double.compare(second.getHitRate(), first.getHitRate()))
                .collect(Collectors.toList());
    }

    private List<String> getPredictorNames(List<PredictorCompareResult> compareResults) {
        return compareResults.stream()
                .map(compareResult -> compareResult.getPredictor().getKey())
                .collect(Collectors.toList());
    }

    private double getHitRate(BranchPredictor predictor, Workload workload) {
        PredictorSimulator simulator = new PredictorSimulator(predictor);
        List<BranchResult> expectedResult = workload.getResult();
        List<BranchResult> actualResult = simulator.simulate(workload.getInstruction());
        return ListUtils.getSimilarity(expectedResult, actualResult);
    }
}
