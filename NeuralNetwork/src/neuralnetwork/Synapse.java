package neuralnetwork;

import java.io.Serializable;

/**
 *
 * @author Stef Dijksman
 */
public class Synapse implements Serializable{
    private Neuron source;
    private double weight;
    
    public Neuron getSource()
    {
        return source;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public void setWeight(double weight)
    {
        this.weight = weight;
    }
}
