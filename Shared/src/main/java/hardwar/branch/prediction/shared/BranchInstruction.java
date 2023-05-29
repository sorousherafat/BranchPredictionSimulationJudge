package hardwar.branch.prediction.shared;


public class BranchInstruction {
    private final Bit[] opcode;

    private final Bit[] instructionAddress;

    private final Bit[] jumpAddress;

    public BranchInstruction(Bit[] opcode, Bit[] instructionAddress, Bit[] jumpAddress) {
        this.opcode = opcode;
        this.instructionAddress = instructionAddress;
        this.jumpAddress = jumpAddress;
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
