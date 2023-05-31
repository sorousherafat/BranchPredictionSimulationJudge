package hardwar.branch.prediction.shared.devices;

/*
 * Our Register Bank Model
 * read below assumptions about pre-defined Register Bank
 * ------------------------------------------------------
 * ASSUMPTIONS:
 * 1) The write and read action will return a copy of the specific object.
 * This design is used to avoid data manipulation out of the memory.
 *
 * 2) The Register Bank Memory is designed in a lazy manner. i.e. the registers
 * are not initialized in the model initialization. If any specific register is
 * needed then it will be initialized.
 * -------------------------------------------------------
 */

import hardwar.branch.prediction.shared.Monitorable;
import hardwar.branch.prediction.shared.Bit;


import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class RegisterBank implements Monitorable {
    private final int registerSize;
    private final int selectorSize;

    Map<String, ShiftRegister> RB;

    /**
     * @param selectorSize number of bits which is needed for selecting a register from register bank
     * @param registerSize number of bits which is used for each register in register bank
     */
    public RegisterBank(int selectorSize, int registerSize) {
        this.registerSize = registerSize;
        this.selectorSize = selectorSize;
        RB = new TreeMap<>();
    }

    /**
     * read the specified register from the register bank. If the selector is not associated with any
     * register then a new register will be initialized.
     *
     * @param selector the value which is used for reading from the memory bank
     * @return a shift register associated to that address ( a copy of it )
     * @throws IllegalArgumentException if the selector is not in legal bound.
     */
    public ShiftRegister read(Bit[] selector) {
        // check the arguments
        if (selector.length != selectorSize)
            throw new IllegalArgumentException("register bank selector is not valid");

        String selectorToString = Bit.arrayToString(selector);
        if (!RB.containsKey(selectorToString)) {
            Bit[] defaultBlock = new Bit[registerSize];
            Arrays.fill(defaultBlock, Bit.ZERO);
            RB.put(selectorToString, new SIPORegister("r", registerSize, defaultBlock));

        }
        return new SIPORegister("r", registerSize, RB.get(selectorToString).read());
    }

    /**
     * write the value on a specific register. if the
     *
     * @param selector      the value which is used for reading from the memory bank
     * @param registerValue the value which is written on specific register.
     * @throws IllegalArgumentException if the selector is not in legal bound or the registerValue size is not as same as
     *                                  register size
     */
    public void write(Bit[] selector, Bit[] registerValue) {
        // check the arguments
        if (selector.length != selectorSize)
            throw new IllegalArgumentException("register bank selector is not valid");

        if (registerValue.length != this.registerSize)
            throw new IllegalArgumentException("registerValue size is not as same as register size");

        String selectorToString = Bit.arrayToString(selector);
        if (RB.containsKey(selectorToString)) {
            ShiftRegister correspondingRegister = RB.get(selectorToString);
            correspondingRegister.load(registerValue);
        } else {
            RB.put(selectorToString, new SIPORegister("r", registerSize, registerValue));
        }
    }

    @Override
    public String monitor() {
        StringBuilder sb = new StringBuilder();
        sb.append("+----------------------------------+\n");
        sb.append(String.format("| %-19s | %-10s |\n", "Register Number", "Value"));
        sb.append("|---------------------|------------|\n");

        for (Map.Entry<String, ShiftRegister> entry : RB.entrySet()) {
            String regNumber = entry.getKey();
            Bit[] block = entry.getValue().read();
            if (regNumber.length() > 16) {
                String address16 = regNumber.substring(0, 16);
                sb.append(String.format("| %-16s... | %-10s |\n", address16, Bit.arrayToString(block)));
            } else {
                sb.append(String.format("| %-19s | %-10s |\n", regNumber, Bit.arrayToString(block)));
            }
            sb.append("+----------------------------------+\n");

        }

        return sb.toString();
    }
}
