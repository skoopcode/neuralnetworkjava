package digit;

/**
 *
 * @author Marcel
 */
public class DigitImage {

    private int _label;
    private double[] _pixelData;

    public DigitImage(int label, byte[] pixels) {
        this._label = label;

        this._pixelData = new double[pixels.length];

        for (int i = 0; i < this._pixelData.length; i++) {
            this._pixelData[i] = pixels[i] & 0xFF; //convert to unsigned
        }
        //http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html
        OtsuThresholdImage();
    }

    public int GetDigitImageLabel() {
        return _label;
    }

    public double[] GetDigitImagePixels() {
        return _pixelData;
    }

    private void OtsuThresholdImage() {
        //I used this algorithm to convert the grayscale of the image into black and white
        int[] histData = new int[256];
        
        for(double pixel : _pixelData)
        {
            histData[(int) pixel]++;
        }

        // Total number of pixels
        int total = _pixelData.length;

        float sum = 0;
        for (int t = 0; t < histData.length; t++) {
            sum += t * histData[t];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < histData.length; t++) {
            wB += histData[t];               // Weight Background
            if (wB == 0) {
                continue;
            }

            wF = total - wB;                 // Weight Foreground
            if (wF == 0) {
                break;
            }

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        
        for(int i = 0; i< _pixelData.length; i++)
        {
            // if pixel smaller or equal to the threshold, then 0, else 1
            _pixelData[i] = _pixelData[i] <= threshold ? 0 : 1;
        }
    }
}
