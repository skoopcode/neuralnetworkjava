
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import neuralnetwork.Algorithms;
import neuralnetwork.Layer;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.Neuron;
import neuralnetwork.activation.HyperbolicTangentActivation;
import reader.DigitImageReader;
import digit.DigitImage;
import digit.MapImages;
import neuralnetwork.BackPropagation;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class RunNeuralNetwork {

    public static void main(String[] args) throws IOException {
        DigitImageReader training = new DigitImageReader("/resources/train/train-labels.idx1-ubyte", "/resources/train/train-images.idx3-ubyte");
        List<DigitImage> imageList = new ArrayList<DigitImage>();

        NeuralNetwork neuralNetwork = new NeuralNetwork();

        Layer inputLayer = new Layer();
        for (int i = 0; i < DigitImageReader.IMAGE_SIZE; i++) {
            Neuron neuron = new Neuron(new HyperbolicTangentActivation());
            inputLayer.AddNeuron(neuron);
        }

        int numberOfHiddenLayerNeurons = 20;
        //int numberOfHiddenLayerNeurons = Algorithms.CalculateSizeHiddenLayer(DigitImageReader.IMAGE_SIZE, 10);
        //set previous layer
        Layer hiddenLayer = new Layer(inputLayer);
        for (int i = 0; i < numberOfHiddenLayerNeurons; i++) {
            Neuron neuron = new Neuron(new HyperbolicTangentActivation());
            hiddenLayer.AddNeuron(neuron);
        }

        //set previous layer
        Layer outputLayer = new Layer(hiddenLayer);
        for (int i = 0; i < 10; i++) {
            Neuron neuron = new Neuron(new HyperbolicTangentActivation());
            outputLayer.AddNeuron(neuron);
        }

        neuralNetwork.AddLayer(inputLayer);
        neuralNetwork.AddLayer(hiddenLayer);
        neuralNetwork.AddLayer(outputLayer);

        MapImages mappedImages = new MapImages(training.LoadDigitImages());
        BackPropagation backpropagator = new BackPropagation(neuralNetwork, 0.1, 0.9);
        backpropagator.Train(mappedImages, 0.5);
    }
}
