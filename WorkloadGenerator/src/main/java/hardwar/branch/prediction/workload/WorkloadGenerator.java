package hardwar.branch.prediction.workload;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import hardwar.branch.prediction.judge.args.JudgeArguments;

import java.util.List;

public class WorkloadGenerator {
    private static final int MAX_TRY_COUNT = 8192;

    private final SampleWorkloadGenerator sampleGenerator;

    private final WorkloadTester tester;

    public WorkloadGenerator(List<String> predictorNames, String basePredictorName) {
        this.sampleGenerator = new SampleWorkloadGeneratorBuilder().build();
        this.tester = new WorkloadTester(predictorNames, basePredictorName);
    }

    Workload generate() {
        int tryCount = 0;
        while (true) {
            Workload workload = sampleGenerator.generate();
            boolean passedTest = tester.test(workload);
            if (passedTest)
                return workload;
            tryCount += 1;
            if (tryCount >= MAX_TRY_COUNT)
                throw new RuntimeException("Exceeded maximum try count");
        }
    }

    public static void main(String[] args) {
        WorkloadGeneratorArguments arguments = new WorkloadGeneratorArguments();
        JCommander commander = JCommander.newBuilder().addObject(arguments).build();
        try {
            commander.parse(args);
        } catch (ParameterException exception) {
            System.err.println(exception.getMessage());
            commander.usage();
            System.exit(1);
        }

        WorkloadGenerator generator = new WorkloadGenerator(arguments.getPredictors(), arguments.getBasePredictor());
        Workload workload = generator.generate();
        WorkloadWriter writer = new WorkloadWriter();
        writer.write(workload);
    }
}
