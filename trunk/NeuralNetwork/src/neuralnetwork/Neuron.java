package neuralnetwork;

import java.io.Serializable;
import neuralnetwork.activation.ActivationFunction;

/**
 *
 * @author Marcel
 */
public class Neuron implements Serializable{

    private ActivationFunction af;
            
    public Neuron(ActivationFunction activationFunction) {
        af = activationFunction;
    }
    
    public void ActivateNeuron(Neuron neuron)
    {
        
    }
    
}
