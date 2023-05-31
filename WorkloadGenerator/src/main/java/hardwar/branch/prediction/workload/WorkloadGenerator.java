package hardwar.branch.prediction.workload;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public interface WorkloadGenerator {
    Workload generate();
}
