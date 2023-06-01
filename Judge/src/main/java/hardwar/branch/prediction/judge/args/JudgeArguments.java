package hardwar.branch.prediction.judge.args;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class JudgeArguments {
    @Parameter(names = {"--instruction", "-i"}, description = "Path to json file containing instructions", converter = ReadFileConverter.class, validateWith = NonEmptyStringValidator.class, help = true, required = true)
    private File instructionFile;

    @Parameter(names = {"--result", "-r"}, description = "Path to json file containing branch results", converter = ReadFileConverter.class, validateWith = NonEmptyStringValidator.class, help = true, required = true)
    private File resultFile;

    @Parameter(names = {"--expected-result", "-e"}, description = "Path to json file containing expected predicted branch results by the predictor", converter = ReadFileConverter.class, validateWith = NonEmptyStringValidator.class, help = true, required = true)
    private File expectedResultFile;

    @Parameter(names = {"--predictor", "-p"}, description = "Name of the predictor to judge", validateWith = NonEmptyStringValidator.class, help = true, required = true)
    private String predictorName;

    public File getInstructionFile() {
        return instructionFile;
    }

    public File getResultFile() {
        return resultFile;
    }

    public File getExpectedResultFile() {
        return expectedResultFile;
    }

    public String getPredictorName() {
        return predictorName;
    }

    private static class ReadFileConverter implements IStringConverter<File> {
        public ReadFileConverter() {
        }

        public File convert(String value) {
            File file = new File(value);
            if (!file.exists())
                throw new ParameterException("File '" + value + "' does not exist");
            return file;
        }
    }
}
