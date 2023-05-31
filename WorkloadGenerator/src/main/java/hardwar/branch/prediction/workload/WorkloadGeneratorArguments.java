package hardwar.branch.prediction.workload;

import com.beust.jcommander.Parameter;
import hardwar.branch.prediction.judge.args.NonEmptyStringListValidator;
import hardwar.branch.prediction.judge.args.NonEmptyStringValidator;

import java.util.List;

public class WorkloadGeneratorArguments {
    @Parameter(names = {"--predictor", "-p"}, description = "Name of the base predictor", validateWith = NonEmptyStringValidator.class, help = true, required = true)
    private String basePredictor;

    @Parameter(description = "List of all the predictors to check", validateWith = NonEmptyStringListValidator.class, help = true, required = true)
    private List<String> predictors;

    public String getBasePredictor() {
        return basePredictor;
    }

    public List<String> getPredictors() {
        return predictors;
    }
}
