package hardwar.branch.prediction.shared;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Bit {
    ZERO(false),
    ONE(true);

    private final boolean value;

    Bit(boolean value) {
        this.value = value;
    }

    /**
     * Converts the current value of the saturating counter to an integer.
     * The most significant bit of the counter is assumed to be the leftmost bit
     * in the register array.
     *
     * @return the integer value of the saturating counter
     */
    public static int toNumber(Bit[] array) {
        int result = 0;
        for (Bit bit : array) result = (result << 1) | (bit == ONE ? 1 : 0);
        return result;
    }

    public static Bit of(boolean value) {
        return value ? ONE : ZERO;
    }

    /**
     * @return the value assigned to bit enum
     */
    public boolean getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    public static String arrayToString(Bit[] array) {
        return Arrays.stream(array)
                .map(Bit::toString)
                .collect(Collectors.joining());
    }
}
