package hardwar.branch.prediction.judge.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import hardwar.branch.prediction.shared.BranchInstruction;

import java.io.IOException;
import java.util.BitSet;

public class BranchInstructionSerializer extends JsonSerializer<BranchInstruction> {
    @Override
    public void serialize(BranchInstruction branchInstruction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("opcode", branchInstruction.getOpcode());
        jsonGenerator.writeObjectField("instructionAddress", branchInstruction.getInstructionAddress());
        jsonGenerator.writeObjectField("jumpAddress", branchInstruction.getJumpAddress());
        jsonGenerator.writeEndObject();
    }
}
