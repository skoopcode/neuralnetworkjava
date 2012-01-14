package neuralnetwork;

/**
 *
 * @author Marcel
 */
public class Algorithms {

    public static long CalculateSizeHiddenLayer(int sizeInputLayer, int sizeOutputLayer) {
        return Math.round((2.0 / 3.0) * sizeInputLayer + sizeOutputLayer);
    }
}
