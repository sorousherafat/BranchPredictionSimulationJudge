package hardwar.branch.prediction.workload;

import hardwar.branch.prediction.shared.Bit;
import hardwar.branch.prediction.shared.BranchInstruction;
import hardwar.branch.prediction.shared.BranchResult;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ActualWorkloadGenerator {
    private static final Random random = new SecureRandom();

    private static final Function<Integer, Bit> randomBitGenerator = i -> Bit.of(random.nextDouble() < 0.5);

    private final int branchCount;

    private final int opcodeLength;

    private final int addressLength;

    private final int distinctAddressCount;

    private final double takenResultRate;

    private final List<Bit[]> sourceAddresses;

    private final List<Bit[]> targetAddresses;

    public ActualWorkloadGenerator(int branchCount, int opcodeLength, int addressLength, int distinctAddressCount, double takenResultRate) {
        this.branchCount = branchCount;
        this.opcodeLength = opcodeLength;
        this.addressLength = addressLength;
        this.distinctAddressCount = distinctAddressCount;
        this.takenResultRate = takenResultRate;
        this.sourceAddresses = new ArrayList<>(this.distinctAddressCount);
        this.targetAddresses = new ArrayList<>(this.distinctAddressCount);
        initAddressList(this.sourceAddresses);
        initAddressList(this.targetAddresses);
    }

    public Workload generate() {
        List<BranchInstruction> instructions = new ArrayList<>(this.branchCount);
        List<BranchResult> results = new ArrayList<>(this.branchCount);
        IntStream.range(0, this.branchCount).forEach(i -> {
            instructions.add(generateInstruction());
            results.add(generateResult());
        });

        return new Workload(instructions, results);
    }

    private void initAddressList(List<Bit[]> addresses) {
        IntStream.range(0, this.distinctAddressCount)
                .forEach(i -> addresses.add(generateFromFunction(this.addressLength, randomBitGenerator)));
    }

    private BranchInstruction generateInstruction() {
        Bit[] opcode = generateOpcode();
        BranchAddress address = generateAddress();
        Bit[] sourceAddress = address.getSourceAddress();
        Bit[] targetAddress = address.getTargetAddress();
        return new BranchInstruction(opcode, sourceAddress, targetAddress);
    }

    private BranchResult generateResult() {
        Supplier<BranchResult> supplier = () -> BranchResult.of(random.nextDouble() < this.takenResultRate);
        return supplier.get();
    }

    private Bit[] generateOpcode() {
        return generateFromFunction(this.opcodeLength, randomBitGenerator);
    }

    private BranchAddress generateAddress() {
        int addressIndex = random.nextInt(this.distinctAddressCount);
        return new BranchAddress(sourceAddresses.get(addressIndex), targetAddresses.get(addressIndex));
    }

    private Bit[] generateFromFunction(int length, Function<Integer, Bit> function) {
        Bit[] bits = new Bit[length];
        IntStream.range(0, length).forEach(i -> bits[i] = function.apply(i));
        return bits;
    }
}
