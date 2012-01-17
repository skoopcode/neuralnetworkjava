package neuralnetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class Layer implements Serializable {

    private List<Neuron> neurons;
    private Layer nextlayer;
    private Layer previouslayer;

    public Layer() {
        this.neurons = new ArrayList<Neuron>();
    }

    public Layer(Layer previousLayer) {
        this.neurons = new ArrayList<Neuron>();
        this.previouslayer = previousLayer;
    }

    public void AddNeuron(Neuron neuron) {
        neurons.add(neuron);
    }
    
    public void FeedForward() {
        for(int i = 0; i < neurons.size(); i++) {
            neurons.get(i).ActivateNeuron();
        }
    }
    
    public void SetNextLayer(Layer layer) {
        this.nextlayer = layer;
    }

    public Layer GetNextLayer() {
        return nextlayer;
    }

    public Layer GetPreviousLayer() {
        return previouslayer;
    }
    
    public boolean IsOutputLayer() {
        return nextlayer == null;
    }
}