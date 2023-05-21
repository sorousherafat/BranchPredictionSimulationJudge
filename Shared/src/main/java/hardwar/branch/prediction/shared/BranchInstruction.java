package hardwar.branch.prediction.shared;

import java.util.BitSet;

public class BranchInstruction {
    private final BitSet opcode;

    private final BitSet sourceAddress;

    private final BitSet targetAddress;

    public BranchInstruction(BitSet opcode, BitSet sourceAddress, BitSet targetAddress) {
        this.opcode = opcode;
        this.sourceAddress = sourceAddress;
        this.targetAddress = targetAddress;
    }

    public BitSet getOpcode() {
        return opcode;
    }

    public BitSet getSourceAddress() {
        return sourceAddress;
    }

    public BitSet getTargetAddress() {
        return targetAddress;
    }
}
