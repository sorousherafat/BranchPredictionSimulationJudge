package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.shared.BranchPredictor;

import java.util.Map.Entry;

public class PredictorCompareResult {
    private final Entry<String, BranchPredictor> predictor;

    private final double hitRate;

    public PredictorCompareResult(Entry<String, BranchPredictor> predictor, double hitRate) {
        this.predictor = predictor;
        this.hitRate = hitRate;
    }

    public Entry<String, BranchPredictor> getPredictor() {
        return predictor;
    }

    public double getHitRate() {
        return hitRate;
    }
}
