package neuralnetwork;

import java.io.Serializable;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class Synapse implements Serializable {

    private Neuron sourceNeuron;
    private double weight;

    public Synapse(Neuron sourceNeuron, double weight) {
        this.sourceNeuron = sourceNeuron;
        this.weight = weight;
    }

    public Neuron GetSourceNeuron() {
        return sourceNeuron;
    }

    public double GetWeight() {
        return weight;
    }

    public void SetWeight(double weight) {
        this.weight = weight;
    }
}
