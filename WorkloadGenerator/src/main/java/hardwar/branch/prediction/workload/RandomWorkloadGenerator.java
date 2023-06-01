package hardwar.branch.prediction.workload;

public class RandomWorkloadGenerator implements WorkloadGenerator {
    private final ActualWorkloadGenerator actualGenerator;

    public RandomWorkloadGenerator() {
        this.actualGenerator = new ActualWorkloadGeneratorBuilder().build();
    }

    @Override
    public Workload generate() {
        return actualGenerator.generate();
    }
}
