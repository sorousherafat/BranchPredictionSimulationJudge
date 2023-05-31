package hardwar.branch.prediction.shared.devices;


import hardwar.branch.prediction.shared.Bit;
import hardwar.branch.prediction.shared.Monitorable;

public interface ShiftRegister extends Monitorable {
    Bit[] read();

    void load(Bit[] bits);

    void insert(Bit bit);

    int getLength();

    void clear();
}
