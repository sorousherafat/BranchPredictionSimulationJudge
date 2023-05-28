package hardwar.branch.prediction.judge;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import hardwar.branch.prediction.judge.args.CommandLineArguments;
import hardwar.branch.prediction.judge.serializers.FileReader;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchPredictor;
import hardwar.branch.prediction.shared.BranchResult;

import java.util.List;

public class Judge {
    public static void main(String[] args) {
        CommandLineArguments commandLineArguments = new CommandLineArguments();
        JCommander commander = JCommander.newBuilder().addObject(commandLineArguments).build();
        try {
            commander.parse(args);
        } catch (ParameterException exception) {
            System.err.println(exception.getMessage());
            commander.usage();
            System.exit(1);
        }

        FileReader fileReader = new FileReader();
        List<BranchInstruction> instructions = fileReader.readInstructions(commandLineArguments.getInstructionFile());
        List<BranchResult> expectedResults = fileReader.readResults(commandLineArguments.getResultFile());

        PredictorSimulator simulator = getSimulator(commandLineArguments.getPredictorName());
        List<BranchResult> actualResults = simulator.simulate(instructions);

        boolean testPassed = actualResults.equals(expectedResults);
        if (!testPassed) {
            long equalResults = ListUtils.countEqualElements(actualResults, expectedResults);
            long total = expectedResults.size();
            throw new TestFailedException(equalResults, total);
        }
    }

    private static PredictorSimulator getSimulator(String predictorName) {
        PredictorProvider provider = new ReflectivePredictorProvider(predictorName);
        BranchPredictor predictor = provider.getPredictor();
        return new PredictorSimulator(predictor);
    }
}
