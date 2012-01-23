package neuralnetwork;

import digit.MapImages;
import digit.TrainingData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class BackPropagation {
    
    private NeuralNetwork neuralNetwork;
    private double learningRate;
    private double momentum;
    private double currentEpoch;

    public BackPropagation(NeuralNetwork neuralNetwork, double learningRate, double momentum) {
        this.neuralNetwork = neuralNetwork;
        this.learningRate = learningRate;
        this.momentum = momentum;
    }

    public void Train(MapImages generator, double errorThreshold) {

        double error;
        double sum = 0.0;
        double average = 1;
        int epoch = 1;
        int samples = 60000;
        double[] errors = new double[samples];

        //checkt of de gemiddelde waarde al kleiner is dan de ingestelde foutwaarde
        while(average > errorThreshold) {
           TrainingData trainingData = generator.GetTrainingData();
            //bereken de error met backpropogate method
            error = Backpropagate(trainingData.GetInputs(), trainingData.GetOutputs());

            // bereken de som met de errorwaarde, voeg de error toe aan de errors lijst
            sum -= errors[epoch % samples];
            //System.out.println(epoch % samples);
            errors[epoch % samples] = error;
            sum += errors[epoch % samples];

            //als het aantal keer uitvoeren gelijk is aan het aantal samples dan moet er begonnen worden met berekenen van het gemiddelde
            //TODO: kijken of het ook zonder werkt?
            if(epoch > samples) {
                average = sum / samples;
            }

            System.out.println("Error for epoch " + epoch + ": " + error + ". Average: " + average);
            epoch++;
            currentEpoch = epoch;
        } 
    }

    public double Backpropagate(double[][] inputs, double[][] expectedOutputs) {

        double error = 0;

        Map<Synapse, Double> synapseNeuronDeltaMap = new HashMap<Synapse, Double>();

        for (int i = 0; i < inputs.length; i++) {

            double[] input = inputs[i];
            double[] expectedOutput = expectedOutputs[i];

            List<Layer> layers = neuralNetwork.GetLayers();

            neuralNetwork.SetInputs(input);
            double[] output = neuralNetwork.GetOutput();

            //First step of the backpropagation algorithm. Backpropagate errors from the output layer all the way up
            //to the first hidden layer
            for (int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for (int k = 0; k < layer.GetNeurons().size(); k++) {
                    Neuron neuron = layer.GetNeurons().get(k);
                    double neuronError = 0;

                    if (layer.IsOutputLayer()) {
                        //the order of output and expected determines the sign of the delta. if we have output - expected, we subtract the delta
                        //if we have expected - output we add the delta.
                        neuronError = neuron.GetDerivative() * (output[k] - expectedOutput[k]);
                    } else {
                        neuronError = neuron.GetDerivative();

                        double sum = 0;
                        List<Neuron> downstreamNeurons = layer.GetNextLayer().GetNeurons();
                        for (Neuron downstreamNeuron : downstreamNeurons) {

                            int l = 0;
                            boolean found = false;
                            while (l < downstreamNeuron.GetInputs().size() && !found) {
                                Synapse synapse = downstreamNeuron.GetInputs().get(l);

                                if (synapse.GetSourceNeuron() == neuron) {
                                    //hoe berekent hij het gewicht en de error?
                                    sum += (synapse.GetWeight() * downstreamNeuron.GetError());
                                    found = true;
                                }

                                l++;
                            }
                        }

                        neuronError *= sum;
                    }

                    neuron.SetError(neuronError);
                }
            }

            //Second step of the backpropagation algorithm. Using the errors calculated above, update the weights of the
            //network
            for(int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for(Neuron neuron : layer.GetNeurons()) {

                    for(Synapse synapse : neuron.GetInputs()) {

                        double newLearningRate = learningRate;
                        double delta = newLearningRate * neuron.GetError() * synapse.GetSourceNeuron().GetOutput();

                        if(synapseNeuronDeltaMap.get(synapse) != null) {
                            double previousDelta = synapseNeuronDeltaMap.get(synapse);
                            delta += momentum * previousDelta;
                        }

                        synapseNeuronDeltaMap.put(synapse, delta);
                        synapse.SetWeight(synapse.GetWeight() - delta);
                    }
                }
            }

            output = neuralNetwork.GetOutput();
            error += Error(output, expectedOutput);
        }

        return error;
    }

    public double Error(double[] actual, double[] expected) {

        if (actual.length != expected.length) {
            throw new IllegalArgumentException("The lengths of the actual and expected value arrays must be equal");
        }

        double sum = 0;

        for (int i = 0; i < expected.length; i++) {
            sum += Math.pow(expected[i] - actual[i], 2);
        }

        return sum / 2;
    }
}
