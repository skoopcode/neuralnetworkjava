package neuralnetwork.activation;

import java.io.Serializable;

/**
 *
 * @author Marcel
 */
public class HyperbolicTangentActivation implements ActivationFunction, Serializable {

    @Override
    public double activate(double weight) {
        return Math.tanh(weight);
    }

    @Override
    public double derivative(double weight) {
        return 1 - Math.pow(Math.tanh(weight), 2);
    }
    
}
