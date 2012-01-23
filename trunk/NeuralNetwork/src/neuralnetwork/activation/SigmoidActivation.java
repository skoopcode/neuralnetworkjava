package neuralnetwork.activation;

import java.io.Serializable;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class SigmoidActivation implements ActivationFunction, Serializable{

    @Override
    public double Activate(double weight) {
        return 1.0 / (1 + Math.exp(-1.0 * weight));
    }

    @Override
    public double Derivative(double weight) {
        return weight * (1.0 - weight);
    }
    
}
