package hardwar.branch.prediction.judge.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import hardwar.branch.prediction.shared.BranchInstruction;

import java.io.IOException;
import java.util.BitSet;

public class BranchInstructionSerializer extends StdSerializer<BranchInstruction> {

    public BranchInstructionSerializer() {
        super(BranchInstruction.class);
    }

    @Override
    public void serialize(BranchInstruction instruction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        writeBitSetField(jsonGenerator, "opcode", instruction.getOpcode());
        writeBitSetField(jsonGenerator, "sourceAddress", instruction.getInstructionAddress());
        writeBitSetField(jsonGenerator, "targetAddress", instruction.getJumpAddress());
        jsonGenerator.writeEndObject();
    }

    private void writeBitSetField(JsonGenerator jsonGenerator, String fieldName, BitSet bitSet) throws IOException {
        jsonGenerator.writeArrayFieldStart(fieldName);
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            jsonGenerator.writeNumber(i);
        }
        jsonGenerator.writeEndArray();
    }
}
