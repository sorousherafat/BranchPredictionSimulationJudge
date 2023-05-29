package hardwar.branch.prediction.shared.devices;

/*
 * our serial-in, parallel-out register Model
 * read below assumption about pre-defined Register
 * ------------------------------------------------------
 * ASSUMPTIONS
 * 1) The data in register won't change unless one of it functions is
 * called and the function changes the register field.
 *
 * 2) The default behavior of register on insert function is to shift all
 * the values to right and add the new bit to the first element of array
 * (think of it as queue first in -> first out)
 *
 * 3) data of register can be read in parallel manner
 *
 * 4) use this model for general purpose registers like PC
 * -------------------------------------------------------
 */


import hardwar.branch.prediction.shared.Bit;

import java.util.Arrays;
import java.util.Collections;

public class SIPORegister implements ShiftRegister {
    private final Bit[] register;
    private final int size;
    public final String name;

    /**
     * Creates a new register with the specified size and default value.
     * If default_value is null, the register is zero-filled by default.
     *
     * @param name         the register name
     * @param size         the size of the register
     * @param defaultValue the default value to initialize the register with
     */
    public SIPORegister(String name, int size, Bit[] defaultValue) {
        this.name = name;
        this.size = size;
        this.register = new Bit[size];
        if (defaultValue == null) {
            // fill all the register with zero values
            clear();
        } else {
            // fill all the register with default value
            System.arraycopy(Arrays.copyOf(defaultValue, size), 0, this.register, 0, size);
        }
    }

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
     * Inserts a new bit at the beginning of the register and shifts all existing bits
     * to the right. The new bit is represented using a Bit enum.
     *
     * @param bit the Bit enum representing the new bit to be inserted
     */
    public void insert(Bit bit) {
        // Shift all existing bits to the right by one position
        for (int i = this.register.length - 1; i > 0; i--) {
            this.register[i] = this.register[i - 1];
        }

        // Insert the new bit at the beginning of the register
        this.register[0] = bit;
    }

    @Override
    public int getLength() {
        return register.length;
    }


    /**
     * clear the register and set teh register value to 0
     */
    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.register[i] = Bit.ZERO;
        }
    }

    /**
     * Returns the contents of the register as a binary string.
     * Each bit is represented as a 0 or 1 character.
     *
     * @return the binary string representation of the register
     */
    public String monitor() {
        StringBuilder sb = new StringBuilder();
        int registerWidth = this.register.length * 4; // each bit takes up 3 characters (1 for the border and 2 for the bit value and space)
        int labelWidth = name.length(); // add 2 for the borders
        int boxWidth = registerWidth + labelWidth + 2; // add 2 for the borders
        int lastBarIndex = registerWidth + labelWidth;
        String line = String.join("", Collections.nCopies(boxWidth, "-")); // create a line of dashes for the top and bottom borders
        sb.append("+").append(line, 0, lastBarIndex + 2).append("+\n");
        sb.append("|").append(name).append(" ");
        for (Bit bit : this.register) {
            sb.append(" | ").append(bit.getValue() ? "1" : "0");
        }
        sb.append(" |\n");
        sb.append("+").append(line, 0, lastBarIndex + 2).append("+\n");
        return sb.toString();
    }

    // simple test
    public static void main(String[] args) {
        ShiftRegister r = new SIPORegister("t", 4, new Bit[]{Bit.ZERO, Bit.ONE, Bit.ZERO, Bit.ZERO});
        Bit[] data = r.read();
        System.out.println(Arrays.toString(data));
        r.insert(Bit.ONE);
        data = r.read();
        System.out.println(Arrays.toString(data));
    }
}



