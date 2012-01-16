import com.sun.org.apache.xml.internal.security.algorithms.Algorithm;
import digit.DigitImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import neuralnetwork.Algorithms;
import neuralnetwork.Layer;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.Neuron;
import neuralnetwork.activation.HyperbolicTangentActivation;
import reader.DigitImageReader;

/**
 *
 * @author Marcel
 */
public class RunNeuralNetwork {
    public static void main(String[] args) throws IOException
    {
        DigitImageReader training = new DigitImageReader("/resources/train/train-labels.idx1-ubyte", "/resources/train/train-images.idx3-ubyte");
        List<DigitImage> imageList = new ArrayList<DigitImage>();
        imageList = training.LoadDigitImages();
        
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        
        Layer inputLayer = new Layer();        
        for(int i = 0; i < DigitImageReader.IMAGE_SIZE; i++)
        {
            Neuron neuron = new Neuron(new HyperbolicTangentActivation());
            inputLayer.AddNeuron(neuron);
        }
        
        Layer hiddenLayer = new Layer();        
        for(int i = 0; i < Algorithms.CalculateSizeHiddenLayer(DigitImageReader.IMAGE_SIZE, 10); i++)
        {
            Neuron neuron = new Neuron(new HyperbolicTangentActivation());
            hiddenLayer.AddNeuron(neuron);
        }
        
        Layer outputLayer = new Layer();
        for(int i = 0; i < 10; i++)
        {
            Neuron neuron = new Neuron(new HyperbolicTangentActivation());
            outputLayer.AddNeuron(neuron);
        }
        
        neuralNetwork.AddLayer(inputLayer);
        neuralNetwork.AddLayer(hiddenLayer);
        neuralNetwork.AddLayer(outputLayer);
        
    }
}
