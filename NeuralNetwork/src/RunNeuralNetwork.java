
import java.io.IOException;
import neuralnetwork.Layer;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.Neuron;
import reader.DigitImageReader;
import digit.MapImages;
import digit.TrainingData;
import neuralnetwork.Algorithms;
import neuralnetwork.BackPropagation;
import neuralnetwork.activation.SigmoidActivation;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class RunNeuralNetwork {

    public static void main(String[] args) throws IOException {
        DigitImageReader trainingData = new DigitImageReader("/resources/train/train-labels.idx1-ubyte", "/resources/train/train-images.idx3-ubyte");
        DigitImageReader testData = new DigitImageReader("/resources/test/t10k-labels.idx1-ubyte", "/resources/test/t10k-images.idx3-ubyte");

        NeuralNetwork neuralNetwork = new NeuralNetwork();

        NeuralNetwork temporaryNetwork = NeuralNetwork.ReadSaveNeuralNetworkFile();

        if (temporaryNetwork == null) {
            Layer inputLayer = new Layer();

            for (int i = 0; i < DigitImageReader.IMAGE_SIZE; i++) {
                Neuron neuron = new Neuron(new SigmoidActivation());
                neuron.SetOutput(0);
                inputLayer.AddNeuron(neuron);
            }
            Layer hiddenLayer = new Layer(inputLayer);

            //int numberOfHiddenLayerNeurons = 20;

            long numberOfHiddenLayerNeurons = Algorithms.CalculateSizeHiddenLayer(DigitImageReader.IMAGE_SIZE, 10);
            //set previous layer
            //Layer hiddenLayer = new Layer(inputLayer);
            for (int i = 0; i < 25; i++) {
                Neuron neuron = new Neuron(new SigmoidActivation());
                neuron.SetOutput(0);
                hiddenLayer.AddNeuron(neuron);
            }

            //set previous layer
            Layer outputLayer = new Layer(hiddenLayer);
            for (int i = 0; i < 10; i++) {
                Neuron neuron = new Neuron(new SigmoidActivation());
                neuron.SetOutput(0);
                outputLayer.AddNeuron(neuron);
            }

            neuralNetwork.AddLayer(inputLayer);
            neuralNetwork.AddLayer(hiddenLayer);
            neuralNetwork.AddLayer(outputLayer);
            
            MapImages mappedImages = new MapImages(trainingData.LoadDigitImages());
            BackPropagation backpropagator = new BackPropagation(neuralNetwork, 0.1, 0.5);
            backpropagator.Train(mappedImages, 0.1);

            neuralNetwork.SaveNeuralNetwerkToTextFile();
        } else {
            neuralNetwork = temporaryNetwork;
        }

        MapImages testDataGenerator = new MapImages(testData.LoadDigitImages());
        TrainingData testTrainingData = testDataGenerator.GetTestData();

        int[] correct = new int[10000];

        for (int i = 0; i < testTrainingData.GetInputs().length; i++) {
            double[] input = testTrainingData.GetInputs()[i];
            double[] output = testTrainingData.GetOutputs()[i];

            int digit = 0;
            boolean found = false;
            while (digit < 10 && !found) {
                found = (output[digit] == 1);
                digit++;
            }

            neuralNetwork.SetInputs(input);
            double[] receivedOutput = neuralNetwork.GetOutput();

            double max = receivedOutput[0];
            double recognizedDigit = 0;
            for (int j = 0; j < receivedOutput.length; j++) {
                if (receivedOutput[j] > max) {
                    max = receivedOutput[j];
                    recognizedDigit = j;
                }
            }

            if (recognizedDigit == digit - 1) {
                correct[i] = 1;
            }
        }
        double sum = 0;
        for (int i = 0; i < correct.length; i++) {
            sum += correct[i];
        }
        System.out.println("Percentage correct: " + ((sum / 10000.0) * 100.0) + "%");
    }
}
