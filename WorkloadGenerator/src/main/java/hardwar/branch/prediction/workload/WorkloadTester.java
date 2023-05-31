package hardwar.branch.prediction.workload;

import java.util.List;

public class WorkloadTester {
    private final String basePredictorName;

    private final PredictorComparator comparator;

    public WorkloadTester(List<String> predictorNames, String basePredictorName) {
        this.basePredictorName = basePredictorName;
        this.comparator = new PredictorComparator(predictorNames);
    }

    public boolean test(Workload workload) {
        List<String> comparisonResult = comparator.compare(workload);
        String bestPredictorName = comparisonResult.get(0);
        return bestPredictorName.equals(basePredictorName);
    }
}
