package neural.generator;

import java.io.Serializable;

/**
 * @author Marcel Saarloos
 * @author Stef Dijksman
 */
public class TrainingData implements Serializable {

    private double[][] inputs;
    private double[][] outputs;

    public TrainingData(double[][] inputs, double[][] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public double[][] getInputs() {
        return inputs;
    }

    public double[][] getOutputs() {
        return outputs;
    }
}
