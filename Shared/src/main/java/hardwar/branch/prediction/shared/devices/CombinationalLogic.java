package hardwar.branch.prediction.shared.devices;

import hardwar.branch.prediction.shared.Bit;
import hardwar.branch.prediction.shared.CountMode;

import java.util.Arrays;

public final class CombinationalLogic {
    private CombinationalLogic() {
        // make the constructor private to avoid instantiating.
    }

    public static Bit[] count(Bit[] input, boolean up, CountMode mode) {
        if (mode == CountMode.SATURATING) {
            return saturateCount(input, up);
        } else throw new UnsupportedOperationException();
    }


    /**
     * Increments or decrements the saturating counter based on the value of the input bit.
     * If the input bit is 1, the counter is incremented by setting the rightmost 0 bit to 1
     * and all subsequent bits to 0. If all bits are already set to 1, then the counter saturates
     * at the maximum value. If the input bit is 0, the counter is decremented by setting the
     * rightmost 1 bit to 0 and all subsequent bits to 1. If all bits are already set to 0, then
     * the counter saturates at the minimum value.
     *
     * @param input the comb logic input
     * @param up    the counter direction
     */
    private static Bit[] saturateCount(Bit[] input, boolean up) {
        // counter bits number
        int len = input.length;

        // convert to decimal
        int numberInDecimal = Bit.toNumber(input);

        // if the counter is saturated (upper limit) then return the same bits.
        if (numberInDecimal == Math.pow(2, len) - 1 && up) {
            return Arrays.copyOf(input, len);
        }
        // if the counter is saturated (lower limit) then return the same bits.
        else if (numberInDecimal == 0 && !up) {
            Bit[] zeroFilled = new Bit[len];
            Arrays.fill(zeroFilled, Bit.ZERO);
            return zeroFilled;
        }
        // count up or down
        else {

            if (up) numberInDecimal++;
            else numberInDecimal--;

            // Convert the updated value to a binary string representation
            String binaryString = Integer.toBinaryString(numberInDecimal);

            // Create a Bit array from the binary string
            Bit[] bits = new Bit[len];
            for (int i = 0; i < len; i++) {
                if (i < len - binaryString.length()) {
                    bits[i] = Bit.ZERO;
                } else {
                    bits[i] = binaryString.charAt(i - (len - binaryString.length())) == '1' ? Bit.ONE : Bit.ZERO;
                }
            }

            return bits;
        }
    }
}
