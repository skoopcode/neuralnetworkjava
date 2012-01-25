package neural;

import java.io.*;
import java.util.*;

/**
 * @author Marcel Saarloos
 * @author Stef Dijksman
 */
public class NeuralNetwork implements Serializable {

    private String name;
    private List<Layer> layers;
    private Layer input;
    private Layer output;

    public NeuralNetwork(String name) {
        this.name = name;
        layers = new ArrayList<Layer>();
    }

    public void addLayer(Layer layer) {
        layers.add(layer);

        if (layers.size() == 1) {
            input = layer;
        }

        if (layers.size() > 1) {
            //clear the output flag on the previous output layer, but only if we have more than 1 layer
            Layer previousLayer = layers.get(layers.size() - 2);
            previousLayer.setNextLayer(layer);
        }

        output = layers.get(layers.size() - 1);
    }

    public void setInputs(double[] inputs) {
        if (input != null) {

            int biasCount = input.hasBias() ? 1 : 0;

            if (input.getNeurons().size() - biasCount != inputs.length) {
                throw new IllegalArgumentException("The number of inputs must equal the number of neurons in the input layer");
            } else {
                List<Neuron> neurons = input.getNeurons();
                for (int i = biasCount; i < neurons.size(); i++) {
                    neurons.get(i).setOutput(inputs[i - biasCount]);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public double[] getOutput() {

        double[] outputs = new double[output.getNeurons().size()];

        for (int i = 1; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            layer.feedForward();
        }

        int i = 0;
        for (Neuron neuron : output.getNeurons()) {
            outputs[i] = neuron.getOutput();
            i++;
        }

        return outputs;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public double[] getWeights() {

        List<Double> weights = new ArrayList<Double>();

        for (Layer layer : layers) {

            for (Neuron neuron : layer.getNeurons()) {

                for (Synapse synapse : neuron.getInputs()) {
                    weights.add(synapse.getWeight());
                }
            }
        }

        double[] allWeights = new double[weights.size()];

        int i = 0;
        for (Double weight : weights) {
            allWeights[i] = weight;
            i++;
        }

        return allWeights;
    }

    public void writeObjectToFile() {
        String fileName = "HandwrittenDigits.ser";

        File f = new File(fileName);
        if (f.exists()) {
            File reName = new File("HandwrittenDigits-" + new Date().getTime() + ".ser");
            f.renameTo(reName);
        }

        System.out.println("Writing trained neural network to file " + fileName);

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

    public static NeuralNetwork readSavedNeuralNetworkFile() {
        NeuralNetwork nn = null;
        String fileName = "HandwrittenDigits.ser";
        File f = new File(fileName);

        if (f.exists()) {
            System.out.println("Trying to read the existing Neural Network");

            try {
                InputStream file = new FileInputStream(fileName);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput oi = new ObjectInputStream(buffer);

                try {
                    nn = (NeuralNetwork) oi.readObject();
                } finally {
                    oi.close();
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("Something went wrong --- ClassNotFoundError: \n" + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Something went wrong --- IOException: \n" + ex.getMessage());
            }
        }
        return nn;
    }
}
