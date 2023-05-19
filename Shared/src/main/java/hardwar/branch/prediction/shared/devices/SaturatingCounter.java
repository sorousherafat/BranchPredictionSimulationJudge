package hardwar.branch.prediction.shared.devices;

import hardwar.branch.prediction.shared.Bit;

import java.util.Arrays;
import java.util.Collections;

public class SaturatingCounter implements Register {
    private final int size;
    private final Bit[] register;

    /**
     * create a new saturating counter with the specific size and default value.
     * if the default value is null, the register is zero-filled by default.
     *
     * @param size         the size of the register
     * @param defaultValue the default value to initialize the register with
     */
    public SaturatingCounter(int size, Bit[] defaultValue) {
        this.size = size;
        this.register = new Bit[size];

        if (defaultValue == null) {
            clear();
        } else {
            // fill all the register with default value
            System.arraycopy(Arrays.copyOf(defaultValue, size), 0, this.register, 0, size);
        }
    }

    /**
     * Returns a copy of the current value of the saturating counter.
     *
     * @return a copy of the current value of the saturating counter
     */
    @Override
    public Bit[] read() {
        return Arrays.copyOf(register, size);
    }

    /**
     * load data into register
     *
     * @param bits data to be load in register
     */
    @Override
    public void load(Bit[] bits) {
        if (size >= 0) System.arraycopy(bits, 0, register, 0, size);
    }

    /**
     * Increments or decrements the saturating counter based on the value of the input bit.
     * If the input bit is 1, the counter is incremented by setting the rightmost 0 bit to 1
     * and all subsequent bits to 0. If all bits are already set to 1, then the counter saturates
     * at the maximum value. If the input bit is 0, the counter is decremented by setting the
     * rightmost 1 bit to 0 and all subsequent bits to 1. If all bits are already set to 0, then
     * the counter saturates at the minimum value.
     *
     * @param bit the input bit to insert into the counter
     */
    @Override
    public void insert(Bit bit) {
        int value = toNumber();
        if (bit == Bit.ONE) {
            if (value == Math.pow(2, size) - 1) return;
            else value++;
        } else {
            if (value == 0) return;
            else value--;
        }

        // Convert the updated value to a binary string representation
        String binaryString = Integer.toBinaryString(value);

        // Create a Bit array from the binary string
        Bit[] bits = new Bit[size];
        for (int i = 0; i < size; i++) {
            if (i < size - binaryString.length()) {
                bits[i] = Bit.ZERO;
            } else {
                bits[i] = binaryString.charAt(i - (size - binaryString.length())) == '1' ? Bit.ONE : Bit.ZERO;
            }
        }

        // Use the load method to set the new counter value
        load(bits);
    }

    @Override
    public int getLength() {
        return register.length;
    }

    /**
     * Resets the value of the saturating counter to its default value.
     */
    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.register[i] = Bit.ZERO;
        }
    }


    /**
     * Converts the current value of the saturating counter to an integer.
     * The most significant bit of the counter is assumed to be the leftmost bit
     * in the register array.
     *
     * @return the integer value of the saturating counter
     */
    public int toNumber() {
        int result = 0;
        for (Bit bit : register) result = (result << 1) | (bit == Bit.ONE ? 1 : 0);
        return result;
    }


    /**
     * Returns the contents of the register as a binary string.
     * Each bit is represented as a 0 or 1 character.
     *
     * @return the binary string representation of the register
     */
    @Override
    public String monitor() {
        StringBuilder sb = new StringBuilder();
        int registerWidth = this.register.length * 4; // each bit takes up 3 characters (1 for the border and 2 for the bit value and space)
        int labelWidth = "counter".length(); // add 2 for the borders
        int boxWidth = registerWidth + labelWidth + 2; // add 2 for the borders
        int lastBarIndex = registerWidth + labelWidth;
        String line = String.join("", Collections.nCopies(boxWidth, "-")); // create a line of dashes for the top and bottom borders
        sb.append("+").append(line, 0, lastBarIndex + 2).append("+\n");
        sb.append("|").append("counter").append(" ");
        for (Bit bit : this.register) {
            sb.append(" | ").append(bit.getValue() ? "1" : "0");
        }
        sb.append(" |\n");
        sb.append("+").append(line, 0, lastBarIndex + 2).append("+\n");
        return sb.toString();
    }

}
