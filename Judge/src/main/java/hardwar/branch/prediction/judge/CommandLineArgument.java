package hardwar.branch.prediction.judge;

import java.io.File;
import java.util.Properties;

public class CommandLineArgument {
    private final String predictorName;
    private final File instructionFile;
    private final File resultFile;

    public CommandLineArgument(Properties properties) {
        this.predictorName = System.getProperty("predictor");
        this.instructionFile = new File(System.getProperty("instruction"));
        this.resultFile = new File(System.getProperty("result"));
    }

    public String getPredictorName() {
        return predictorName;
    }

    public File getInstructionFile() {
        return instructionFile;
    }

    public File getResultFile() {
        return resultFile;
    }
}
