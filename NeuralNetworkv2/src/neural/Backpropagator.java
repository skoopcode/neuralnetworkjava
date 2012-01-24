package neural;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import neural.generator.TrainingData;
import neural.generator.TrainingDataGenerator;

/**
 * @author Marcel Saarloos
 * @author Stef Dijksman
 */
public class Backpropagator implements Serializable {

    private NeuralNetwork neuralNetwork;
    private double learningRate;
    private double momentum;
    private int currentEpoch;
    private double[] errors;
    private double sum;
    private boolean finished;

    public Backpropagator(NeuralNetwork neuralNetwork, double learningRate, double momentum) {
        this.neuralNetwork = neuralNetwork;
        this.learningRate = learningRate;
        this.momentum = momentum;
    }
    
    public void setFinished(boolean bool) {
        finished = bool;
    }

    public boolean getFinished() {
        return finished;
    }
    
    public void train(TrainingDataGenerator generator, double errorThreshold) {

        double error;
        
        double average = 10;        
        int epoch = 1;
        int samples = 60000;
        
        if (errors.length <= 0)
        {
            errors = new double[samples];
        }
        if (sum <= 0)
        {
            sum = 0.0;
        }
        if (currentEpoch > 1)
        {
            epoch = currentEpoch;
        }
        
        System.out.println("Epoch; Error; Avg; Time;");
        
        while(average > errorThreshold){
            TrainingData trainingData = generator.getTrainingData();
            error = backpropagate(trainingData.getInputs(), trainingData.getOutputs());

            sum -= errors[epoch % samples];
            errors[epoch % samples] = error;
            sum += errors[epoch % samples];

            if (epoch > samples) {
                average = sum / samples;
            }

            System.out.println(epoch + ": " + error + "; " + average + "; " + getDateTime() + ";");
            epoch++;
            currentEpoch = epoch;
        }
        
        finished = true;
    }

    public double backpropagate(double[][] inputs, double[][] expectedOutputs) {

        double error = 0;

        Map<Synapse, Double> synapseNeuronDeltaMap = new HashMap<Synapse, Double>();

        for (int i = 0; i < inputs.length; i++) {

            double[] input = inputs[i];
            double[] expectedOutput = expectedOutputs[i];

            List<Layer> layers = neuralNetwork.getLayers();

            neuralNetwork.setInputs(input);
            double[] output = neuralNetwork.getOutput();

            //First step of the backpropagation algorithm. Backpropagate errors from the output layer all the way up
            //to the first hidden layer
            for (int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for (int k = 0; k < layer.getNeurons().size(); k++) {
                    Neuron neuron = layer.getNeurons().get(k);
                    double neuronError = 0;

                    if (layer.isOutputLayer()) {
                        //the order of output and expected determines the sign of the delta. if we have output - expected, we subtract the delta
                        //if we have expected - output we add the delta.
                        neuronError = neuron.getDerivative() * (output[k] - expectedOutput[k]);
                    } else {
                        neuronError = neuron.getDerivative();

                        double sum = 0;
                        List<Neuron> downstreamNeurons = layer.getNextLayer().getNeurons();
                        for (Neuron downstreamNeuron : downstreamNeurons) {

                            int l = 0;
                            boolean found = false;
                            while (l < downstreamNeuron.getInputs().size() && !found) {
                                Synapse synapse = downstreamNeuron.getInputs().get(l);

                                if (synapse.getSourceNeuron() == neuron) {
                                    sum += (synapse.getWeight() * downstreamNeuron.getError());
                                    found = true;
                                }

                                l++;
                            }
                        }

                        neuronError *= sum;
                    }

                    neuron.setError(neuronError);
                }
            }

            //Second step of the backpropagation algorithm. Using the errors calculated above, update the weights of the
            //network
            for (int j = layers.size() - 1; j > 0; j--) {
                Layer layer = layers.get(j);

                for (Neuron neuron : layer.getNeurons()) {

                    for (Synapse synapse : neuron.getInputs()) {

                        double delta = learningRate * neuron.getError() * synapse.getSourceNeuron().getOutput();

                        if (synapseNeuronDeltaMap.get(synapse) != null) {
                            double previousDelta = synapseNeuronDeltaMap.get(synapse);
                            delta += momentum * previousDelta;
                        }

                        synapseNeuronDeltaMap.put(synapse, delta);
                        synapse.setWeight(synapse.getWeight() - delta);
                    }
                }
            }

            output = neuralNetwork.getOutput();
            error += error(output, expectedOutput);
        }

        return error;
    }

    public double error(double[] actual, double[] expected) {

        if (actual.length != expected.length) {
            throw new IllegalArgumentException("The lengths of the actual and expected value arrays must be equal");
        }

        double sum = 0;

        for (int i = 0; i < expected.length; i++) {
            sum += Math.pow(expected[i] - actual[i], 2);
        }

        return sum / 2;
    }
    
    private String getDateTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public void writeObjectToFile() {
        String fileName = "BackPropagation.ser";
        
        File f = new File(fileName);
        if (f.exists()) {
            File reName = new File("BackPropagation-" + new Date().getTime() + ".ser");
            f.renameTo(reName);
        }
        
        System.out.println("Writing Backpropagation Data to file " + fileName);

        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            System.out.println("Could not write to file: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.flush();
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("Could not write to file: " + fileName);
                e.printStackTrace();
            }
        }
    }

    public static Backpropagator readSavedObjectFile() {
        Backpropagator bp = null;
        String fileName = "BackPropagation.ser";
        File f = new File(fileName);

        if (f.exists()) {
            System.out.println("Trying to read the existing NeuralNetwork");

            try {
                InputStream file = new FileInputStream(fileName);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput oi = new ObjectInputStream(buffer);

                try {
                    bp = (Backpropagator) oi.readObject();
                } finally {
                    oi.close();
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("Something went wrong --- ClassNotFoundError: \n" + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Something went wrong --- IOException: \n" + ex.getMessage());
            }
        }
        return bp;
    }
}