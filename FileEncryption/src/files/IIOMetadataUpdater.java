package files;



import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IIOMetadataUpdater {

   public static void writeData(BufferedImage img, File in, String Title, String value) throws IOException{
       File out = new File(in.getParent(), createOutputName(in));
       try (ImageInputStream input = ImageIO.createImageInputStream(in);
            ImageOutputStream output = ImageIO.createImageOutputStream(out)) {
            
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            ImageReader reader = readers.next(); // TODO: Validate that there are readers

            reader.setInput(input);
            IIOImage image = reader.readAll(0, null);
            IIOMetadata mtd = image.getMetadata();
            mtd = addTextEntry(mtd, Title, value);
            System.out.println("added");
            image = new IIOImage(img, null, mtd);
            ImageWriter writer = ImageIO.getImageWriter(reader); // TODO: Validate that there are writers
            writer.setOutput(output);
            writer.write(image);
        }
   }
   public static String readValue(File in, String Title) throws IOException{
       try (ImageInputStream input = ImageIO.createImageInputStream(in)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            ImageReader reader = readers.next(); // TODO: Validate that there are readers

            reader.setInput(input);
            String value = getTextEntry(reader.getImageMetadata(0), Title);
            return value;
       }
   }
    public static String createOutputName(final File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        String baseName = name.substring(0, dotIndex);
        return baseName + "_Encryption-Copy.png";
    }

    private static IIOMetadata addTextEntry(final IIOMetadata metadata, final String key, final String value) throws IIOInvalidTreeException {
        IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
        textEntry.setAttribute("keyword", key);
        textEntry.setAttribute("value", value);

        IIOMetadataNode text = new IIOMetadataNode("Text");
        text.appendChild(textEntry);

        IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
        root.appendChild(text);
        metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
        
        return metadata;
    }

    private static String getTextEntry(final IIOMetadata metadata, final String key) {
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
        NodeList entries = root.getElementsByTagName("TextEntry");

        for (int i = 0; i < entries.getLength(); i++) {
            IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
            if (node.getAttribute("keyword").equals(key)) {
                return node.getAttribute("value");
            }
        }

        return null;
    }
}