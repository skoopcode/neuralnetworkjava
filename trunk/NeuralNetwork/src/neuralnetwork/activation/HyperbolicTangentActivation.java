package neuralnetwork.activation;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class HyperbolicTangentActivation implements ActivationFunction {
    
    @Override
    public double Activate(double weight) {
        return Math.tanh(weight);
    }

    @Override
    public double Derivative(double weight) {
        return 1 - Math.pow(Math.tanh(weight), 2);
    }
    
}
