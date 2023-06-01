package hardwar.branch.prediction.predictors.PAp;


import hardwar.branch.prediction.shared.*;
import hardwar.branch.prediction.shared.devices.*;

import java.util.Arrays;

public class PAp implements BranchPredictor {

    private final int branchInstructionSize;

    private final ShiftRegister SC; // saturating counter register

    private final RegisterBank PABHR; // per address branch history register

    private final Cache<Bit[], Bit[]> PAPHT; // Per Address Predication History Table

    public PAp(int BHRSize, int SCSize, int branchInstructionSize) {
        this.branchInstructionSize = branchInstructionSize;

        // Initialize the PABHR with the given bhr and branch instruction size
        PABHR = new RegisterBank(branchInstructionSize, BHRSize);

        // Initializing the PAPHT with BranchInstructionSize as PHT Selector and 2^BHRSize row as each PHT entries
        // number and SCSize as block size
        PAPHT = new PerAddressPredictionHistoryTable(branchInstructionSize, (int) Math.pow(2, BHRSize), SCSize);

        // Initialize the SC register
        SC = new SIPORegister("sc", SCSize, null);
    }

    @Override
    public BranchResult predict(BranchInstruction branchInstruction) {
        // instruction address
        Bit[] instructionAddress = branchInstruction.getInstructionAddress();

        // select the BHR based on branch instruction address
        ShiftRegister correspondingBHR = PABHR.read(instructionAddress);

        // get PAPHT entry by concatenating the branch address and BHR
        Bit[] cacheEntry = getCacheEntry(instructionAddress, correspondingBHR.read());

        // Get the associated block with the cacheEntry from the PAPHT
        Bit[] cacheBlock = PAPHT.setDefault(cacheEntry, getDefaultBlock());

        // load the block into the register
        SC.load(cacheBlock);

        // Return the predicted outcome of the branch instruction based on the value of the MSB
        return cacheBlock[0].getValue() ? BranchResult.TAKEN : BranchResult.NOT_TAKEN;
    }

    @Override
    public void update(BranchInstruction instruction, BranchResult actual) {
        // check the predication result
        boolean isTaken = actual == BranchResult.TAKEN;

        // update saturating counter
        Bit[] nValue = CombinationalLogic.count(SC.read(), isTaken, CountMode.SATURATING);

        // get register number
        Bit[] instructionAddress = instruction.getInstructionAddress();

        // get register from register bank
        ShiftRegister correspondingBHR = PABHR.read(instructionAddress);

        // update the PAPHT
        PAPHT.put(getCacheEntry(instructionAddress, correspondingBHR.read()), nValue);

        // update branch history
        correspondingBHR.insert(isTaken ? Bit.ONE : Bit.ZERO);
        PABHR.write(instructionAddress, correspondingBHR.read());
    }


    private Bit[] getCacheEntry(Bit[] branchAddress, Bit[] BHRValue) {
        // Concatenate the branch address bits with the BHR bits
        Bit[] cacheEntry = new Bit[branchAddress.length + BHRValue.length];
        System.arraycopy(branchAddress, 0, cacheEntry, 0, branchInstructionSize);
        System.arraycopy(BHRValue, 0, cacheEntry, branchAddress.length, BHRValue.length);
        return cacheEntry;
    }

    /**
     * @return a zero series of bits as default value of cache block
     */
    private Bit[] getDefaultBlock() {
        Bit[] defaultBlock = new Bit[SC.getLength()];
        Arrays.fill(defaultBlock, Bit.ZERO);
        return defaultBlock;
    }

    @Override
    public String monitor() {
        return "PAp predictor snapshot: \n" + PABHR.monitor() + SC.monitor() + PAPHT.monitor();
    }
}
