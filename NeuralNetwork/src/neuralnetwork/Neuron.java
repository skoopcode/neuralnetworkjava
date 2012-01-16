package neuralnetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import neuralnetwork.activation.ActivationFunction;

/**
 *
 * @author Stef Dijksman, Marcel Saarloos
 */
public class Neuron implements Serializable{

    private ActivationFunction af;
    private List<Synapse> inputSynapses;
    private double error;
            
    public Neuron(ActivationFunction activationFunction) {
        inputSynapses = new ArrayList<Synapse>();
        af = activationFunction;
        error = 0;
    }
    public void addInputSynapse(Synapse input)
    {
        inputSynapses.add(input);
    }
    
    public List<Synapse> getInputSynapses()
    {
        return this.inputSynapses;
    }
    
    public double[] getWeights() 
    {
        double[] weights = new double[inputSynapses.size()];

        int i = 0;
        for(Synapse synapse : inputSynapses) {
            weights[i] = synapse.getWeight();
            i++;
        }
        return weights;
    }
    
    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }
    
    public void ActivateNeuron(Neuron neuron)
    {
        
    }
    
}
