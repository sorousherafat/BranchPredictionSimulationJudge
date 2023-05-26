package hardwar.branch.prediction.judge.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import hardwar.branch.prediction.shared.BranchInstruction;

import java.io.IOException;
import java.util.BitSet;

public class BranchInstructionDeserializer extends JsonDeserializer<BranchInstruction> {

    @Override
    public BranchInstruction deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();
        JsonNode opcodeNode = node.get("opcode");
        JsonNode sourceAddressNode = node.get("sourceAddress");
        JsonNode targetAddressNode = node.get("targetAddress");

        BitSet opcode = convertJsonArrayToBitSet(opcodeNode);
        BitSet sourceAddress = convertJsonArrayToBitSet(sourceAddressNode);
        BitSet targetAddress = convertJsonArrayToBitSet(targetAddressNode);

        return new BranchInstruction(opcode, sourceAddress, targetAddress);
    }

    private BitSet convertJsonArrayToBitSet(JsonNode node) {
        BitSet bitSet = new BitSet();
        for (JsonNode elementNode : node) {
            int value = elementNode.asInt();
            bitSet.set(value);
        }
        return bitSet;
    }
}