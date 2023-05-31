package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.shared.Bit;

public class BranchAddress {
    private final Bit[] sourceAddress;

    private final Bit[] targetAddress;

    public BranchAddress(Bit[] sourceAddress, Bit[] targetAddress) {
        this.sourceAddress = sourceAddress;
        this.targetAddress = targetAddress;
    }

    public Bit[] getSourceAddress() {
        return sourceAddress;
    }

    public Bit[] getTargetAddress() {
        return targetAddress;
    }
}
