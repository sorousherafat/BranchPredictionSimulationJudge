package hardwar.branch.prediction.judge.args;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class NonEmptyStringValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (value == null || value.isEmpty()) {
            throw new ParameterException("Parameter '" + name + "' must not be empty");
        }
    }
}
