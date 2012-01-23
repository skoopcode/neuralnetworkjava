package reader;

import digit.DigitImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * @author Stef Dijksman
 * @author Marcel Saarloos
 */
public class DigitImageReader {

    private String _labelFileLocation;
    private String _imageFileLocation;
    
    private static final int MAGIC_LABEL = 2049;
    private static final int MAGIC_IMAGE = 2051;
    public static final int ROWS = 28;
    public static final int COLUMNS = 28;
    public static final int IMAGE_SIZE = ROWS * COLUMNS;

    public DigitImageReader(String label, String image) {
        this._labelFileLocation = label;
        this._imageFileLocation = image;
    }

    public List<DigitImage> loadDigitImages() throws IOException {
        List<DigitImage> images = new ArrayList<DigitImage>();

        InputStream labelInputStream = this.getClass().getResourceAsStream(_labelFileLocation);
        InputStream imageInputStream = this.getClass().getResourceAsStream(_imageFileLocation);

        //http://commons.apache.org/io/
        byte[] labelBytes = IOUtils.toByteArray(labelInputStream);
        byte[] imageBytes = IOUtils.toByteArray(imageInputStream);

        byte[] labelMagic = Arrays.copyOfRange(labelBytes, 0, 4);
        byte[] imageMagic = Arrays.copyOfRange(imageBytes, 0, 4);

        //controles
        if (ByteBuffer.wrap(labelBytes).getInt() != MAGIC_LABEL) {
            throw new IOException("Bad magic number label file");
        }
        if (ByteBuffer.wrap(imageBytes).getInt() != MAGIC_IMAGE) {
            throw new IOException("Bad magic number image file");
        }

        //controleer of het aantal images gelijk is aan het aantal labels
        int numberOfLabels = ByteBuffer.wrap(labelBytes, 4, 8).getInt();
        int numberOfImages = ByteBuffer.wrap(imageBytes, 4, 8).getInt();

        if (numberOfImages != numberOfLabels) {
            throw new IOException("Het aantal images komt niet overeen met het aantal labels");
        }

        int numRows = ByteBuffer.wrap(imageBytes, 8, 12).getInt();
        int numCols = ByteBuffer.wrap(imageBytes, 12, 16).getInt();

        if (numRows != ROWS || numCols != COLUMNS) {
            throw new IOException("Bad image. Rows and columns do not equal " + ROWS + "x" + COLUMNS);
        }

        for(int i =0; i<numberOfImages; i++)
        {
            int label = labelBytes[(8+i)];
            byte[] imagePixels = Arrays.copyOfRange(imageBytes, (i * IMAGE_SIZE) + 16, (i*IMAGE_SIZE) + 16 + IMAGE_SIZE);
            
            images.add(new DigitImage(label, imagePixels));
        }
        
        return images;
    }
}
