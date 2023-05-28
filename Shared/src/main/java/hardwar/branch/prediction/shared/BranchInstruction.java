package hardwar.branch.prediction.shared;


public class BranchInstruction {
    private final Bit[] opcode;

    private final Bit[] instructionAddress;

    private final Bit[] jumpAddress;

    public BranchInstruction(Bit[] opcode, Bit[] sourceAddress, Bit[] targetAddress) {
        this.opcode = opcode;
        this.instructionAddress = sourceAddress;
        this.jumpAddress = targetAddress;
    }

    public Bit[] getOpcode() {
        return opcode;
    }

    public Bit[] getInstructionAddress() {
        return instructionAddress;
    }

    public Bit[] getJumpAddress() {
        return jumpAddress;
    }

    @Override
    public String toString() {
        return "BranchInstruction{" +
                "opcode=" + Bit.arrayToString(opcode) +
                ", sourceAddress=" + Bit.arrayToString(instructionAddress) +
                ", targetAddress=" + Bit.arrayToString(jumpAddress) +
                '}';
    }
}
