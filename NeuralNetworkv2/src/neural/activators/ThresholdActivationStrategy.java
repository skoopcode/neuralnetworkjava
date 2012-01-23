package neural.activators;

import java.io.Serializable;

/**
 * @author Marcel Saarloos
 * @author Stef Dijksman
 */

public class ThresholdActivationStrategy implements ActivationStrategy, Serializable {

    private double threshold;

    public ThresholdActivationStrategy(double threshold) {
        this.threshold = threshold;
    }

    public double activate(double weightedSum) {
        return weightedSum > threshold ? 1 : 0;
    }

    public double derivative(double weightedSum) {
        return 0;
    }

}
