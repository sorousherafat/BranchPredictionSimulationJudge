package hardwar.branch.prediction.judge.args;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.List;

public class NonEmptyStringListValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (value == null || value.isEmpty()) {
            throw new ParameterException("Parameter '" + name + "' must not be empty");
        }
        String[] parsedValue = value.split("\\s*");
        if (parsedValue.length < 2)
            throw new ParameterException("Parameter '" + name + "' must contain at least two elements");
    }
}
