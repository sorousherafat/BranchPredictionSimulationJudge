package hardwar.branch.prediction.judged.PAs;


import hardwar.branch.prediction.shared.*;
import hardwar.branch.prediction.shared.devices.*;

import java.util.Arrays;

public class PAs implements BranchPredictor {

    private final int branchInstructionSize;
    private final int KSize;
    private final HashMode hashMode;
    private final ShiftRegister SC; // saturating counter register
    private final RegisterBank PABHR; // per address Branch History Register
    private final Cache<Bit[], Bit[]> PSPHT; // Per Set Predication History Table

    public PAs(){
        this(4,2,8,4,HashMode.XOR);
    }

    public PAs(int BHRSize, int SCSize, int branchInstructionSize, int KSize, HashMode hashMode) {
        this.branchInstructionSize = branchInstructionSize;
        this.KSize = KSize;
        this.hashMode = hashMode;

        // Initialize the PABHR with the given bhr and branch instruction size
        PABHR = new RegisterBank(branchInstructionSize, BHRSize);

        // Initializing the PAPHT with K bit as PHT selector and 2^BHRSize row as each PHT entries
        // number and SCSize as block size
        PSPHT = new PerAddressPredictionHistoryTable(KSize, (int) Math.pow(2, BHRSize), SCSize);

        // Initialize the saturating counter
        SC = new SIPORegister("sc", SCSize, null);
    }

    /**
     * predicts the result of a branch instruction based on the per address BHR and hash value of branch
     * instruction address
     *
     * @param branchInstruction the branch instruction
     * @return the predicted outcome of the branch instruction (taken or not taken)
     */
    @Override
    public BranchResult predict(BranchInstruction branchInstruction) {
        // instruction address
        Bit[] instructionAddress = branchInstruction.getInstructionAddress();

        // select the BHR based on branch instruction address
        ShiftRegister correspondingBHR = PABHR.read(instructionAddress);

        // get PSPHT entry by concatenating the branch address and BHR
        Bit[] cacheEntry = getCacheEntry(instructionAddress, correspondingBHR.read());

        // Get the associated block with the cacheEntry from the PSPHT
        Bit[] cacheBlock = PSPHT.setDefault(cacheEntry, getDefaultBlock());

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
        PSPHT.put(getCacheEntry(instructionAddress, correspondingBHR.read()), nValue);

        // update branch history
        correspondingBHR.insert(isTaken ? Bit.ONE : Bit.ZERO);
        PABHR.write(instructionAddress, correspondingBHR.read());
    }

    @Override
    public String monitor() {
        return "PAs predictor snapshot: \n" + PABHR.monitor() + SC.monitor() + PSPHT.monitor();
    }

    private Bit[] getCacheEntry(Bit[] branchAddress, Bit[] BHRValue) {
        // hash the branch address
        Bit[] hashKSize = CombinationalLogic.hash(branchAddress, KSize, hashMode);

        // Concatenate the Hash bits with the BHR bits
        Bit[] cacheEntry = new Bit[hashKSize.length + BHRValue.length];
        System.arraycopy(hashKSize, 0, cacheEntry, 0, hashKSize.length);
        System.arraycopy(BHRValue, 0, cacheEntry, hashKSize.length, BHRValue.length);

        return cacheEntry;
    }


    private Bit[] getDefaultBlock() {
        Bit[] defaultBlock = new Bit[SC.getLength()];
        Arrays.fill(defaultBlock, Bit.ZERO);
        return defaultBlock;
    }
}
