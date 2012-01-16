package neuralnetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcel
 */
public class Layer implements Serializable {

    private List<Neuron> neurons;
    private Layer nextlayer;

    public Layer() {
        this.neurons = new ArrayList<Neuron>();
    }

    public void AddNeuron(Neuron neuron) {
        neurons.add(neuron);
    }

    public void SetNextLayer(Layer layer) {
        this.nextlayer = layer;
    }

    public Layer GetNextLayer() {
        return nextlayer;
    }

    public boolean IsOutputLayer() {
        return nextlayer == null;
    }
}