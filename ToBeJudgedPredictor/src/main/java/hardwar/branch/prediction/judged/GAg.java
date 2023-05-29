package hardwar.branch.prediction.judged;

import hardwar.branch.prediction.shared.*;
import hardwar.branch.prediction.shared.devices.*;

import java.util.Arrays;

public class GAg implements BranchPredictor {

    private final ShiftRegister BHR; // branch history register
    private final Cache<Bit[], Bit[]> PHT; // page history table

    private final ShiftRegister SC; // saturated counter value

    /**
     * Creates a new GAg predictor with the given BHR register size and initializes the BHR and PHT.
     *
     * @param BHRSize the size of the BHR register
     * @param SCSize  the size of the register which hold the saturating counter value
     */
    public GAg(int BHRSize, int SCSize) {
        // Initialize the BHR register with the given size and null input
        this.BHR = new SIPORegister("bhr", BHRSize, null);

        // Initialize the PHT with a size of 2^size and each entry having a saturating counter of size "size"
        PHT = new PageHistoryTable((int) Math.pow(2, BHRSize), SCSize);

        // Initialize the SC register
        SC = new SIPORegister("sc", SCSize, null);
    }

    /**
     * Predicts the result of a branch instruction based on the global branch history
     *
     * @param branchInstruction the branch instruction
     * @return the predicted outcome of the branch instruction (taken or not taken)
     */
    @Override
    public BranchResult predict(BranchInstruction branchInstruction) {
        // Read the current value of the BHR register
        Bit[] BHRValue = BHR.read();

        // Get the associated block with the current value of the BHR register from the PHT
        Bit[] cacheBlock = PHT.setDefault(BHRValue, getDefaultBlock());

        // load the block into the register
        SC.load(cacheBlock);

        // Return the predicted outcome of the branch instruction based on the value of the MSB
        return cacheBlock[0].getValue() ? BranchResult.TAKEN : BranchResult.NOT_TAKEN;
    }

    /**
     * Updates the values in the cache based on the actual branch result
     *
     * @param instruction the branch instruction
     * @param actual      the actual result of the branch condition
     */
    @Override
    public void update(BranchInstruction instruction, BranchResult actual) {
        // get the cache value


        // check the predication result
        boolean isTaken = actual == BranchResult.TAKEN;

        // update saturating counter
        Bit[] nValue = CombinationalLogic.count(SC.read(), isTaken, CountMode.SATURATING);

        // add updated value to the cache
        PHT.put(BHR.read(), nValue);

        // update global history
        BHR.insert(isTaken ? Bit.ONE : Bit.ZERO);
    }

    public BranchResult predictAndUpdate(BranchInstruction branchInstruction, BranchResult actual) {
        BranchResult br = predict(branchInstruction);
        System.out.println("The predication is : " + br);
        System.out.println("Before Update: \n" + monitor());
        update(branchInstruction, actual);
        System.out.println("After Update: \n" + monitor());

        return br;
    }

    private Bit[] getDefaultBlock() {
        Bit[] defaultBlock = new Bit[SC.getLength()];
        Arrays.fill(defaultBlock, Bit.ZERO);
        return defaultBlock;
    }

    @Override
    public String monitor() {
        return "GAg predictor snapshot: \n" + BHR.monitor() + SC.monitor() + PHT.monitor();
    }


    /*
    Test Util

    TODO: remove this section in final result
     */
    public static void main(String[] args) {
        GAg gag = new GAg(4, 2);

        Bit[] opcode;
        Bit[] instructionAddress;
        Bit[] jumpAddress;


        for (int i = 0; i < 1000; i++) {

            opcode = getRandomBitSerial(6);
            instructionAddress = getRandomBitSerial(10);
            jumpAddress = getRandomBitSerial(16);

            BranchInstruction bi = new BranchInstruction(
                    opcode,
                    instructionAddress,
                    jumpAddress
            );

            BranchResult br = getRandomBR();
            BranchResult r = gag.predictAndUpdate(bi, br);
            System.out.println("PC value is: " + Bit.arrayToString(bi.getInstructionAddress()) + " Branch result is: " + br);
        }


    }

    private static Bit[] getRandomBitSerial(int size) {
        Bit[] rPC = new Bit[size];
        for (int i = 0; i < size; i++) {
            Bit b = Math.random() > 0.5 ? Bit.ONE : Bit.ZERO;
            rPC[i] = b;
        }
        return rPC;
    }

    private static BranchResult getRandomBR() {
        return Math.random() < 0.75 ? BranchResult.TAKEN : BranchResult.NOT_TAKEN;
    }
}
