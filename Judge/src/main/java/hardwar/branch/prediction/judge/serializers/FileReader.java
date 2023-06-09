package hardwar.branch.prediction.judge.serializers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileReader {

    private final ObjectMapper objectMapper;

    public FileReader() {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(BranchInstruction.class, new BranchInstructionSerializer());
        module.addDeserializer(BranchInstruction.class, new BranchInstructionDeserializer());
        objectMapper.registerModule(module);
    }

    public List<BranchInstruction> readInstructions(File file) {
        try {
            return objectMapper.readValue(file, new TypeReference<List<BranchInstruction>>() {});
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<BranchResult> readResults(File file) {
        try {
            return objectMapper.readValue(file, new TypeReference<List<BranchResult>>() {});
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
