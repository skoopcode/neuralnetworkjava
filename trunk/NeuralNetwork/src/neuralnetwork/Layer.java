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
    private Layer previousLayer;

    public Layer() {
        this.neurons = new ArrayList<Neuron>();
    }

    public Layer(Layer previousLayer) {
        this.neurons = new ArrayList<Neuron>();
        this.previousLayer = previousLayer;
    }

     public void AddNeuron(Neuron neuron) {

        neurons.add(neuron);

        if(previousLayer != null) {
            for(Neuron previousLayerNeuron : previousLayer.GetNeurons()) {
                neuron.AddInputSynapse(new Synapse(previousLayerNeuron, 0.1*(Math.random() * -0.5))); //initialize with a random weight between -1 and 1
            }
        }
    }

    public void AddNeuron(Neuron neuron, double[] weights) {

        neurons.add(neuron);

        if(previousLayer != null) {

            if(previousLayer.GetNeurons().size() != weights.length) {
                throw new IllegalArgumentException("The number of weights supplied must be equal to the number of neurons in the previous layer");
            }

            else {
                List<Neuron> previousLayerNeurons = previousLayer.GetNeurons();
                for(int i = 0; i < previousLayerNeurons.size(); i++) {
                    neuron.AddInputSynapse(new Synapse(previousLayerNeurons.get(i), weights[i]));
                }
            }
        }
    }
    
    public List<Neuron> GetNeurons() 
    {
        return this.neurons;
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
        return previousLayer;
    }
    
    public boolean IsOutputLayer() {
        return nextlayer == null;
    }
}