package hardwar.branch.prediction.judge;

import hardwar.branch.prediction.shared.BranchPredictor;

public interface PredictorProvider {
    BranchPredictor getPredictor();
}
