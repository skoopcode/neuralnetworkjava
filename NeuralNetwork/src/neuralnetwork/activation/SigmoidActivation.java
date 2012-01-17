/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork.activation;

/**
 *
 * @author Marcel
 */
public class SigmoidActivation implements ActivationFunction{

    @Override
    public double activate(double weight) {
        return 1.0 / (1 + Math.exp(-1.0 * weight));
    }

    @Override
    public double derivative(double weight) {
        return weight * (1.0 - weight);
    }
    
}
