package neuralnetwork;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Marcel
 */
public class NeuralNetwork implements Serializable{

    private List<Layer> layers;
    private Layer input;
    private Layer output;

    public NeuralNetwork() {
        layers = new ArrayList<Layer>();
    }

    public void AddLayer(Layer layer) {
        layers.add(layer);

        if (layers.size() == 1) {
            input = layer;
        }
        if (layers.size() > 1) {
            Layer previousLayer = layers.get(layers.size() - 2);
            previousLayer.SetNextLayer(layer);
        }
        output = layer;
    }

    public void SaveNeuralNetwerkToTextFile() {
        String fileName = "HandwrittenDigitsNetwork.ser";

        File f = new File(fileName);
        if (f.exists()) {
            File renameFile = new File("HandwrittenDigitsNetwork-" + new Date().getTime() + ".ser");
            f.renameTo(renameFile);
            f = new File(fileName);
        }
        System.out.println("Writing trained network to file " + fileName);

        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            System.out.println("Could not write to file: " + fileName);
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.flush();
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("Could not write to file: " + fileName);
            }
        }
    }
    
    public NeuralNetwork ReadSaveNeuralNetworkFile()
    {
        NeuralNetwork nn = null;
        String fileName = "HandwrittenDigitsNetwork.ser";
        File f = new File(fileName);
        
        if (f.exists())
        {
            System.out.println("Trying to read the existing NeuralNetwork");
            
            try{
                InputStream file = new FileInputStream(fileName);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput oi = new ObjectInputStream(buffer);
                
                try{
                    nn = (NeuralNetwork)oi.readObject();
                }
                finally
                {
                    oi.close();
                }
            }
            catch (ClassNotFoundException ex)
            {
                System.out.println("Something went wrong --- ClassNotFoundError: \n" + ex.getMessage());
            }
            catch(IOException ex)
            {
                System.out.println("Something went wrong --- IOException: \n" + ex.getMessage());
            }
        }
        return  nn;
    }
}
