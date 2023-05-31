package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.shared.BranchResult;

import java.util.function.Supplier;

public class SampleWorkloadGeneratorBuilder {
    private int branchCount = 1000;
    private int opcodeLength = 6;
    private int addressLength = 8;
    private int distinctAddressCount = 8;
    private double takenResultRate = 0.8;

    public SampleWorkloadGeneratorBuilder setBranchCount(int branchCount) {
        this.branchCount = branchCount;
        return this;
    }

    public SampleWorkloadGeneratorBuilder setOpcodeLength(int opcodeLength) {
        this.opcodeLength = opcodeLength;
        return this;
    }

    public SampleWorkloadGeneratorBuilder setAddressLength(int addressLength) {
        this.addressLength = addressLength;
        return this;
    }

    public SampleWorkloadGeneratorBuilder setDistinctAddressCount(int distinctAddressCount) {
        this.distinctAddressCount = distinctAddressCount;
        return this;
    }

    public SampleWorkloadGeneratorBuilder setTakenResultRate(double takenResultRate) {
        this.takenResultRate = takenResultRate;
        return this;
    }

    public SampleWorkloadGenerator build() {
        return new SampleWorkloadGenerator(branchCount, opcodeLength, addressLength, distinctAddressCount, takenResultRate);
    }
}