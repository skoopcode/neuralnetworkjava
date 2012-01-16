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
    private Layer previouslayer;

    public Layer() {
        this.neurons = new ArrayList<Neuron>();
    }

    public Layer(Layer previousLayer) {
        this();
        this.previouslayer = previousLayer;
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