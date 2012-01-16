package neuralnetwork.activation;

/**
 *
 * @author Marcel
 */
public interface ActivationFunction {    
    double activate(double weight);
    double derivative(double weight);
}
