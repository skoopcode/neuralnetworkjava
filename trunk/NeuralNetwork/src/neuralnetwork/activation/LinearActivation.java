package neuralnetwork.activation;

import java.io.Serializable;

public class LinearActivation implements ActivationFunction, Serializable {
    @Override
    public double Activate(double weight) {
        return weight;
    }

    @Override
    public double Derivative(double weight) {
        return 0;
    }
}
