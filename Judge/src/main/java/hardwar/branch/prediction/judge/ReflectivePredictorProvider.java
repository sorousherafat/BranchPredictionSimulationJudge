package hardwar.branch.prediction.judge;

import hardwar.branch.prediction.shared.BranchPredictor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectivePredictorProvider implements PredictorProvider {
    private static final String PREDICTOR_PACKAGE_NAME = "hardwar.branch.prediction.judged";

    private static String getQualifiedTypeName(String typeName) {
        return PREDICTOR_PACKAGE_NAME + "." + getRealTypeName(typeName);
    }

    private static String getRealTypeName(String typeName) {
      String[] split = typeName.split("/");
      return split[split.length - 1];
    }

    private static void displayErrorAndExit(String message) {
        throw new RuntimeException(message);
    }
    
    private final String predictorName;

    public ReflectivePredictorProvider(String predictorName) {
        this.predictorName = getQualifiedTypeName(predictorName);
    }

    public BranchPredictor getPredictor() {
        Class<?> predictorClass = getPredictorClass();
        Constructor<?> predictorConstructor = getConstructor(predictorClass);
        Object predictorObject = getObject(predictorConstructor);
        return getPredictor(predictorClass, predictorObject);
    }

    private BranchPredictor getPredictor(Class<?> derivedClass, Object reflectedInstance) {
        if (!derivedClass.isInstance(reflectedInstance)) {
            displayErrorAndExit("Generated object is not an instance of class: " + predictorName);
        }
        return (BranchPredictor) reflectedInstance;
    }

    private Object getObject(Constructor<?> relfectedConstructor) {
        Object reflectedInstance = null;
        try {
            reflectedInstance = relfectedConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            displayErrorAndExit("An error occurred when instantiating an object of class: " + predictorName + "\n" + exception.getMessage());
        }
        return reflectedInstance;
    }

    private Constructor<?> getConstructor(Class<?> derivedClass) {
        Constructor<?> relfectedConstructor = null;
        try {
            relfectedConstructor = derivedClass.getConstructor();
        } catch (NoSuchMethodException exception) {
            displayErrorAndExit("Could not find a parameterless constructor for class: " + predictorName);
        }
        return relfectedConstructor;
    }

    private Class<?> getPredictorClass() {
        Class<?> reflectedClass = null;
        try {
            reflectedClass = Class.forName(predictorName);
        } catch (ClassNotFoundException exception) {
            displayErrorAndExit("Could not find the class: " + predictorName);
        }
        return reflectedClass;
    }
}
