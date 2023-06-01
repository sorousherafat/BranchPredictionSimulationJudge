package hardwar.branch.prediction.workload;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class Program {
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


        WorkloadGenerator generator = getGeneratorByArguments(arguments, commander);
        Workload workload = generator.generate();
        WorkloadWriter writer = new WorkloadWriter();
        writer.write(workload);
    }

    private static WorkloadGenerator getGeneratorByArguments(WorkloadGeneratorArguments arguments, JCommander commander) {
        if (isForActual(arguments))
            return new RandomWorkloadGenerator();

        if (isForDefault(arguments))
            return new DefaultWorkloadGenerator(arguments.getBasePredictor());

        if (isForPrime(arguments))
            return new PrimeWorkloadGenerator(arguments.getPredictors(), arguments.getBasePredictor());

        commander.usage();
        System.exit(1);
        return null;
    }

    private static boolean isForActual(WorkloadGeneratorArguments arguments) {
        return (arguments.getBasePredictor() == null || arguments.getBasePredictor().isEmpty()) &&
                (arguments.getPredictors() == null || arguments.getPredictors().isEmpty());
    }

    private static boolean isForDefault(WorkloadGeneratorArguments arguments) {
        return (arguments.getBasePredictor() != null && !arguments.getBasePredictor().isEmpty()) &&
                (arguments.getPredictors() == null || arguments.getPredictors().isEmpty());
    }

    private static boolean isForPrime(WorkloadGeneratorArguments arguments) {
        return (arguments.getBasePredictor() != null && !arguments.getBasePredictor().isEmpty()) &&
                (arguments.getPredictors() != null || !arguments.getPredictors().isEmpty());
    }
}
