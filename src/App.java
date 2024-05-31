import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class App
{
    public static final String betweenFunc = "\\left(\\left(\\frac{\\left(-\\frac{\\left|POSA-x\\ +\\ .5\\right|-.5}{\\left|\\left|POSA-x\\ +.5\\right|-.5\\right|}\\right)+1}{2}\\right)\\cdot\\ \\left(YLEVEL\\right)\\right)";
    public static final int THRESHOLD = 100;

    public static void main(String[] args) throws Exception
    {
        BufferedImage image = ImageIO.read(new File(args[0]));

        WritableRaster raster = image.getRaster();

        String result = "0";

        for (int x = 0; x < raster.getWidth(); x++)
        {
            ArrayList<Integer> arr = new ArrayList<>();

            for (int y = 0; y < raster.getHeight(); y++)
            {
                int[] pixel = new int[4];
                raster.getPixel(x, y, pixel);

                int average = pixel[3]; // just use A channel
                if (average > THRESHOLD)
                {
                    arr.add(raster.getHeight() - y);
                }
            }

            Collections.shuffle(arr);
            if (arr.size() > 0)
            {
                result += " + " + betweenFunc.replaceAll("POSA", "" + x).replace("YLEVEL", Integer.toString(arr.get(0)));
            }
        }

        System.out.println(result);
        Path path = Paths.get("output.txt");
        byte[] strToBytes = result.getBytes();

        Files.write(path, strToBytes);
    }
}
