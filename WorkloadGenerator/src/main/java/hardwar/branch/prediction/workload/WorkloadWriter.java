package hardwar.branch.prediction.workload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hardwar.branch.prediction.judge.serializers.BranchInstructionDeserializer;
import hardwar.branch.prediction.judge.serializers.BranchInstructionSerializer;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WorkloadWriter {
    private final ObjectMapper objectMapper;

    public WorkloadWriter() {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(BranchInstruction.class, new BranchInstructionSerializer());
        module.addDeserializer(BranchInstruction.class, new BranchInstructionDeserializer());
        objectMapper.registerModule(module);
    }

    public void write(Workload workload) {
        write(workload, "instruction.json", "result.json", "expected-result.json");
    }

    public void write(Workload workload, String instructionFilePath, String resultFilePath, String expectedResultFilePath) {
        write(workload, new File(instructionFilePath), new File(resultFilePath), new File(expectedResultFilePath));
    }

    public void write(Workload workload, File instructionFile, File resultFile, File expectedResultFile) {
        List<BranchInstruction> instruction = workload.getInstruction();
        List<BranchResult> result = workload.getResult();
        List<BranchResult> expectedResult = workload.getExpectedResult();
        try {
            objectMapper.writeValue(instructionFile, instruction);
            objectMapper.writeValue(resultFile, result);
            objectMapper.writeValue(expectedResultFile, expectedResult);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
