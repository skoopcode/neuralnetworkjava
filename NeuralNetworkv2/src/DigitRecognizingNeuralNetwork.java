
import digit.generator.DigitTrainingDataGenerator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
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

    private static NeuralNetwork nn;
    private static Backpropagator backpropagator;

    public static void main(String[] args) throws IOException {
        AddShutdownHook shutDown = new AddShutdownHook();
        shutDown.attachShutDownHook();

        DigitImageReader trainingService = new DigitImageReader("/resources/train/train-labels.idx1-ubyte", "/resources/train/train-images.idx3-ubyte");
        DigitImageReader testService = new DigitImageReader("/resources/test/t10k-labels.idx1-ubyte", "/resources/test/t10k-images.idx3-ubyte");
        
        NeuralNetwork tempNetwork = NeuralNetwork.readSavedNeuralNetworkFile();

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
            nn = new NeuralNetwork("");
            nn.addLayer(inputLayer);
            nn.addLayer(hiddenLayer);
            nn.addLayer(outputLayer);
        } else {
            nn = tempNetwork;
        }

        if (nn != null) {
            DigitTrainingDataGenerator trainingDataGenerator = new DigitTrainingDataGenerator(trainingService.loadDigitImages());

            Backpropagator tempBack = Backpropagator.readSavedObjectFile();

            if (tempBack == null) {
                backpropagator = new Backpropagator(nn, 0.1, 0.1); //neural network, learning rate, momentum
                backpropagator.setFinished(false);
            } else {
                backpropagator = tempBack;
            }

            if (!backpropagator.getFinished()) {

                JFrame frame = new JFrame("Train Neural Network");

                frame.setSize(400, 400);
                Container contentPane = frame.getContentPane();

                JButton button = new JButton("Stop Training");
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        backpropagator.stopTraining(500);
                        backpropagator.setFinished(false);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(DigitRecognizingNeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.exit(0);
                    }
                });
                contentPane.add(button, BorderLayout.CENTER);
                frame.setVisible(true);

                backpropagator.train(trainingDataGenerator, 0.05);
            }
            
            if (nn != null && backpropagator.getFinished()) {
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

                    nn.setInputs(input);
                    double[] receivedOutput = nn.getOutput();

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

    public static class AddShutdownHook {

        public void attachShutDownHook() {
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    backpropagator.writeObjectToFile();
                    nn.writeObjectToFile();
                }
            });
            System.out.println("Shut Down Hook Attached.");
        }
    }
}
