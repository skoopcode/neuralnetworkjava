
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import neuralnetwork.Layer;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.Neuron;
import reader.DigitImageReader;
import digit.DigitImage;
import digit.MapImages;
import digit.TrainingData;
import neuralnetwork.BackPropagation;
import neuralnetwork.activation.LinearActivation;
import neuralnetwork.activation.SigmoidActivation;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class RunNeuralNetwork {

    public static void main(String[] args) throws IOException {
        DigitImageReader trainingData = new DigitImageReader("/resources/train/train-labels.idx1-ubyte", "/resources/train/train-images.idx3-ubyte");
        DigitImageReader testData = new DigitImageReader("/resources/test/t10k-labels.idx1-ubyte", "/resources/test/t10k-images.idx3-ubyte");
        List<DigitImage> imageList = new ArrayList<DigitImage>();

        NeuralNetwork neuralNetwork = new NeuralNetwork();
        Neuron inputBias = new Neuron(new LinearActivation());
        inputBias.SetOutput(1);

        Layer inputLayer = new Layer(null, inputBias);

        for (int i = 0; i < DigitImageReader.IMAGE_SIZE; i++) {
            Neuron neuron = new Neuron(new SigmoidActivation());
            inputLayer.AddNeuron(neuron);
        }

        Neuron hiddenBias = new Neuron(new LinearActivation());
        hiddenBias.SetOutput(1);

        Layer hiddenLayer = new Layer(inputLayer, hiddenBias);

        int numberOfHiddenLayerNeurons = 20;
        //int numberOfHiddenLayerNeurons = Algorithms.CalculateSizeHiddenLayer(DigitImageReader.IMAGE_SIZE, 10);
        //set previous layer
        //Layer hiddenLayer = new Layer(inputLayer);
        for (int i = 0; i < numberOfHiddenLayerNeurons; i++) {
            Neuron neuron = new Neuron(new SigmoidActivation());
            hiddenLayer.AddNeuron(neuron);
        }

        //set previous layer
        Layer outputLayer = new Layer(hiddenLayer);
        for (int i = 0; i < 10; i++) {
            Neuron neuron = new Neuron(new SigmoidActivation());
            outputLayer.AddNeuron(neuron);
        }

        neuralNetwork.AddLayer(inputLayer);
        neuralNetwork.AddLayer(hiddenLayer);
        neuralNetwork.AddLayer(outputLayer);

        MapImages mappedImages = new MapImages(trainingData.LoadDigitImages());
        BackPropagation backpropagator = new BackPropagation(neuralNetwork, 0.1, 0.1);
        backpropagator.train(mappedImages, 0.5);
        neuralNetwork.SaveNeuralNetwerkToTextFile();
        
        MapImages testDataGenerator = new MapImages(testData.LoadDigitImages());
        TrainingData testTrainingData = testDataGenerator.GetTrainingData();

//        for (int i = 0; i < testTrainingData.GetInputs().length; i++) {
//            double[] input = testTrainingData.GetInputs()[i];
//            double[] output = testTrainingData.GetOutputs()[i];
//
//            int digit = 0;
//            boolean found = false;
//            while (digit < 10 && !found) {
//                found = (output[digit] == 1);
//                digit++;
//            }
//
//            neuralNetwork.SetInputs(input);
//            double[] receivedOutput = neuralNetwork.GetOutput();
//
//            double max = receivedOutput[0];
//            double recognizedDigit = 0;
//            for (int j = 0; j < receivedOutput.length; j++) {
//                if (receivedOutput[j] > max) {
//                    max = receivedOutput[j];
//                    recognizedDigit = j;
//                }
//            }
//
//            System.out.println("Recognized " + (digit - 1) + " as " + recognizedDigit + ". Corresponding output value was " + max);
//        }
    }
}
