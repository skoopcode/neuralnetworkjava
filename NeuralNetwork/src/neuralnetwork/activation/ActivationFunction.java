package neuralnetwork.activation;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public interface ActivationFunction {

    double Activate(double weight);
    double Derivative(double weight);
}
