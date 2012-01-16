package neuralnetwork.activation;

/**
 *
 * @author Marcel
 */
public class HyperbolicTangentActivation implements ActivationFunction {
    
    @Override
    public double activate(double weight) {
        return Math.tanh(weight);
    }

    @Override
    public double derivative(double weight) {
        return 1 - Math.pow(Math.tanh(weight), 2);
    }
    
}
