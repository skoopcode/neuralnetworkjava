package neural.activators;

/**
 * @author Marcel Saarloos
 * @author Stef Dijksman
 */
public interface ActivationStrategy {
    double activate(double weightedSum);
    double derivative(double weightedSum);
}
