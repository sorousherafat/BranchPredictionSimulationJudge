package hardwar.branch.prediction.judge.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import hardwar.branch.prediction.shared.Bit;
import hardwar.branch.prediction.shared.BranchInstruction;

import java.io.IOException;
import java.util.BitSet;

public class BranchInstructionDeserializer extends JsonDeserializer<BranchInstruction> {
    @Override
    public BranchInstruction deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Bit[] opcode = null;
        Bit[] instructionAddress = null;
        Bit[] jumpAddress = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();

            switch (fieldName) {
                case "opcode":
                    opcode = jsonParser.readValueAs(Bit[].class);
                    break;
                case "instructionAddress":
                    instructionAddress = jsonParser.readValueAs(Bit[].class);
                    break;
                case "jumpAddress":
                    jumpAddress = jsonParser.readValueAs(Bit[].class);
                    break;
            }
        }

        return new BranchInstruction(opcode, instructionAddress, jumpAddress);
    }
}