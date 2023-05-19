package hardwar.branch.prediction.shared;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public enum Bit {
    ZERO(false),
    ONE(true);

    private final boolean value;

    Bit(boolean value) {
        this.value = value;
    }

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
