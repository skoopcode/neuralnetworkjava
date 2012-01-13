
import digit.DigitImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import reader.DigitImageReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    }
}
