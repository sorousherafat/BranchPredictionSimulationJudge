package hardwar.branch.prediction.workload;

public class ActualWorkloadGeneratorBuilder {
    private int branchCount = 20;
    private int opcodeLength = 6;
    private int addressLength = 8;
    private int distinctAddressCount = 8;
    private double takenResultRate = 0.8;

    public ActualWorkloadGeneratorBuilder setBranchCount(int branchCount) {
        this.branchCount = branchCount;
        return this;
    }

    public ActualWorkloadGeneratorBuilder setOpcodeLength(int opcodeLength) {
        this.opcodeLength = opcodeLength;
        return this;
    }

    public ActualWorkloadGeneratorBuilder setAddressLength(int addressLength) {
        this.addressLength = addressLength;
        return this;
    }

    public ActualWorkloadGeneratorBuilder setDistinctAddressCount(int distinctAddressCount) {
        this.distinctAddressCount = distinctAddressCount;
        return this;
    }

    public ActualWorkloadGeneratorBuilder setTakenResultRate(double takenResultRate) {
        this.takenResultRate = takenResultRate;
        return this;
    }

    public ActualWorkloadGenerator build() {
        return new ActualWorkloadGenerator(branchCount, opcodeLength, addressLength, distinctAddressCount, takenResultRate);
    }
}