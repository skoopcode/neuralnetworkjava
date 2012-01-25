package digit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import reader.DigitImageReader;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class MapImages {

    Map<Integer, List<DigitImage>> labelToDigitImageListMap;
    int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public MapImages(List<DigitImage> digitImages) {
        labelToDigitImageListMap = new HashMap<Integer, List<DigitImage>>();

        for (DigitImage digitImage : digitImages) {

            if (labelToDigitImageListMap.get(digitImage.GetDigitImageLabel()) == null) {
                labelToDigitImageListMap.put(digitImage.GetDigitImageLabel(), new ArrayList<DigitImage>());
            }

            labelToDigitImageListMap.get(digitImage.GetDigitImageLabel()).add(digitImage);
        }
    }

    public TrainingData GetTrainingData() {
        digits = Shuffle(digits);

        double[][] inputs = new double[10][DigitImageReader.IMAGE_SIZE];
        double[][] outputs = new double[10][10];

        for (int i = 0; i < 10; i++) {
            inputs[i] = GetRandomImageForLabel(digits[i]).GetDigitImagePixels();
            outputs[i] = GetOutputFor(digits[i]);
        }

        return new TrainingData(inputs, outputs);
    }

    public TrainingData GetTestData()
    {
        digits = Shuffle(digits);

        double[][] inputs = new double[10000][DigitImageReader.IMAGE_SIZE];
        double[][] outputs = new double[10000][10];
        
        int index  = 0;
        for (int i = 0; i < 10000; i++) {            
            if (i > 10)
            {
                index++;
            }
            else {
                index = i;
            }
            if (index == 10)
            {
                index = 0;
            }
            
            inputs[i] = GetRandomImageForLabel(digits[index]).GetDigitImagePixels();
            outputs[i] = GetOutputFor(digits[index]);
        }

        return new TrainingData(inputs, outputs);
    }
    
    private int[] Shuffle(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {

            int index = random.nextInt(i + 1);

            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }

        return array;
    }

    private DigitImage GetRandomImageForLabel(int label) {
        Random random = new Random();
        List<DigitImage> images = labelToDigitImageListMap.get(label);
        return images.get(random.nextInt(images.size()));
    }

    private double[] GetOutputFor(int label) {
        double[] output = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        output[label] = 1;
        return output;
    }
}
