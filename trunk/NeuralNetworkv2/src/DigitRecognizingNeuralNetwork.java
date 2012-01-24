
import digit.generator.DigitTrainingDataGenerator;

import java.io.IOException;
import neural.Backpropagator;
import neural.Layer;
import neural.NeuralNetwork;
import neural.Neuron;
import neural.activators.LinearActivationStrategy;
import neural.activators.SigmoidActivationStrategy;
import neural.generator.TrainingData;
import reader.DigitImageReader;

/**
 * @author Marcel Saarloos
 * @author Stef Dijksman
 */
public class DigitRecognizingNeuralNetwork {

    public static void main(String[] args) throws IOException {

        DigitImageReader trainingService = new DigitImageReader("/resources/train/train-labels.idx1-ubyte", "/resources/train/train-images.idx3-ubyte");
        DigitImageReader testService = new DigitImageReader("/resources/test/t10k-labels.idx1-ubyte", "/resources/test/t10k-images.idx3-ubyte");

        NeuralNetwork neuralNetwork = new NeuralNetwork("Digit Recognizing Neural Network");

        NeuralNetwork tempNetwork = neuralNetwork.readSavedNeuralNetworkFile();

        if (tempNetwork == null) {

            Neuron inputBias = new Neuron(new LinearActivationStrategy());
            inputBias.setOutput(1);

            Layer inputLayer = new Layer(null, inputBias);

            for (int i = 0; i < DigitImageReader.IMAGE_SIZE; i++) {
                Neuron neuron = new Neuron(new SigmoidActivationStrategy());
                neuron.setOutput(0);
                inputLayer.addNeuron(neuron);
            }

            Neuron hiddenBias = new Neuron(new LinearActivationStrategy());
            hiddenBias.setOutput(1);

            Layer hiddenLayer = new Layer(inputLayer, hiddenBias);

            //long numberOfHiddenNeurons = Math.round((2.0 / 3.0) * (DigitImageReader.IMAGE_SIZE) + 10); //543
            int numberOfHiddenNeurons = 50;

            for (int i = 0; i < numberOfHiddenNeurons; i++) {
                Neuron neuron = new Neuron(new SigmoidActivationStrategy());
                neuron.setOutput(0);
                hiddenLayer.addNeuron(neuron);
            }

            Layer outputLayer = new Layer(hiddenLayer);

            //10 output neurons - 1 for each digit
            for (int i = 0; i < 10; i++) {
                Neuron neuron = new Neuron(new SigmoidActivationStrategy());
                neuron.setOutput(0);
                outputLayer.addNeuron(neuron);
            }

            neuralNetwork.addLayer(inputLayer);
            neuralNetwork.addLayer(hiddenLayer);
            neuralNetwork.addLayer(outputLayer);
        } else {
            neuralNetwork = tempNetwork;
        }

        if (neuralNetwork != null) {
            DigitTrainingDataGenerator trainingDataGenerator = new DigitTrainingDataGenerator(trainingService.loadDigitImages());

            Backpropagator tempBack = Backpropagator.readSavedObjectFile();

            Backpropagator backpropagator;
            if (tempBack == null) {
                backpropagator = new Backpropagator(neuralNetwork, 0.1, 0.5); //neural network, learning rate, momentum
                backpropagator.setFinished(false);
            } else {
                backpropagator = tempBack;
            }

            if (!backpropagator.getFinished()) {
                try {
                    backpropagator.train(trainingDataGenerator, 0.05);
                } finally {
                    neuralNetwork.writeObjectToFile();
                    backpropagator.writeObjectToFile();
                }
            }

            if (neuralNetwork != null && backpropagator.getFinished()) {
                DigitTrainingDataGenerator testDataGenerator = new DigitTrainingDataGenerator(testService.loadDigitImages());
                TrainingData testData = testDataGenerator.getTrainingData();

                for (int i = 0; i < testData.getInputs().length; i++) {
                    double[] input = testData.getInputs()[i];
                    double[] output = testData.getOutputs()[i];

                    int digit = 0;
                    boolean found = false;
                    while (digit < 10 && !found) {
                        found = (output[digit] == 1);
                        digit++;
                    }

                    neuralNetwork.setInputs(input);
                    double[] receivedOutput = neuralNetwork.getOutput();

                    double max = receivedOutput[0];
                    double recognizedDigit = 0;
                    for (int j = 0; j < receivedOutput.length; j++) {
                        if (receivedOutput[j] > max) {
                            max = receivedOutput[j];
                            recognizedDigit = j;
                        }
                    }

                    System.out.println("Recognized " + (digit - 1) + " as " + recognizedDigit + ". Corresponding output value was " + max);
                }
            }
        }
    }
}
