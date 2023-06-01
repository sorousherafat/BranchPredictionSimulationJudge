package hardwar.branch.prediction.judge;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import hardwar.branch.prediction.judge.args.JudgeArguments;
import hardwar.branch.prediction.judge.serializers.FileReader;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;

public class Judge {
    public static void main(String[] args) {
        JudgeArguments arguments = new JudgeArguments();
        JCommander commander = JCommander.newBuilder().addObject(arguments).build();
        try {
            commander.parse(args);
        } catch (ParameterException exception) {
            System.err.println(exception.getMessage());
            commander.usage();
            System.exit(1);
        }

        FileReader fileReader = new FileReader();
        List<BranchInstruction> instructions = fileReader.readInstructions(arguments.getInstructionFile());
        List<BranchResult> results = fileReader.readResults(arguments.getResultFile());
        List<BranchResult> expectedResults = fileReader.readResults(arguments.getExpectedResultFile());

        PredictorSimulator simulator = getSimulator(arguments.getPredictorName());
        List<BranchResult> actualResults = simulator.simulate(instructions, results);

        long equalResults = ListUtils.countEqualElements(actualResults, expectedResults);
        long total = results.size();
        boolean testPassed = actualResults.equals(expectedResults);
        if (!testPassed) {
            throw new TestFailedException(equalResults, total);
        }
        System.out.println("Test passed, " + equalResults + " out of " + total + " assertions passed");
    }

    private static PredictorSimulator getSimulator(String predictorName) {
        PredictorProvider provider = new ReflectivePredictorProvider(predictorName);
        BranchPredictor predictor = provider.getPredictor();
        return new PredictorSimulator(predictor);
    }
}
