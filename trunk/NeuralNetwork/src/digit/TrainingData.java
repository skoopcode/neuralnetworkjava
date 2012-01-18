package digit;

public class TrainingData {

    private double[][] inputs;
    private double[][] outputs;

    public TrainingData(double[][] inputs, double[][] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public double[][] GetInputs() {
        return inputs;
    }

    public double[][] GetOutputs() {
        return outputs;
    }
}
