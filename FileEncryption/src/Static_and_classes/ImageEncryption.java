/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Static_and_classes;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

/**
 *
 * @author daniz
 */
public class ImageEncryption {
    private int[][] pixels;
    private String bytes;

   

    public String getBytes() {
        return bytes;
    }
    public ImageEncryption(BufferedImage image) {
        pixels = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                pixels[i][j] = (int)(image.getRGB(j, (image.getHeight() - 1 - i)));
            }
        }
    }
    public Color[][] ConvertToColorMatrix(){
        Color[][] result = new Color[pixels.length][];
        for (int i = 0; i < pixels.length; i++) {
            result[i] = new Color[pixels[i].length];
            for (int j = 0; j < pixels[i].length; j++) {
                if (pixels[i][j] >> 24 != 0) {
                    int pixel = pixels[i][j];
                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;
                    result[i][j] = new Color(red, green, blue, alpha);
                }
                else {
                    result[i][j] = new Color(0, 0, 0, 0);
                }
            }
        }
        return result;
    }
    public File writepngimage(File f, BufferedImage img) throws IOException{
        String pat = f.getAbsolutePath();
        int dotIndex = pat.lastIndexOf('.');
        String basepat = pat.substring(0, dotIndex);
        String path = basepat + ".png";
        ImageIO.write(img, "png", new File(path));
        return new File(path);
    }
    public void convertToPixlelMatrix(Color[][] Colors) {
        int[][] result = new int[Colors.length][];
        for (int i = 0; i < Colors.length; i++) {
            result[i] = new int[Colors[i].length];
            for (int j = 0; j < Colors[i].length; j++) {
                if (Colors[i][j].getRGB() >> 24 != 0) {
                    int a = Colors[i][j].getAlpha();
                    int r = Colors[i][j].getRed();
                    int g = Colors[i][j].getGreen();
                    int b = Colors[i][j].getBlue();
                    result[i][j] = (int)((new Color(r,g,b,a)).getRGB());
                }
                else {
                    result[i][j] = (int)((new Color(0,0,0,0)).getRGB());
                }
            }
        }
        this.pixels = result;
    }

    public BufferedImage ConvertToBufferedImage(int Type){
        BufferedImage Image = new BufferedImage(pixels[0].length, pixels.length, Type);
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {             
                Image.setRGB(j, (pixels.length - 1 - i), pixels[i][j]);               
            }
        }
        return Image;
    }
    public void encryptcolors(String kisar, String mul, String key, String Hash, int min){
        int indexk = 0;
        int indexm = 0;
        Color[][] Colors = ConvertToColorMatrix();
        for (int i = 0; i < Colors.length; i++) {
            for (int j = 0; j < Colors[i].length; j++) {
                if ((Colors[i][j].getRGB() >> 24) != 0) {
                    int a = Colors[i][j].getAlpha();   a = Encryptions.encrypt_kisar(a, (int)(kisar.charAt(indexk)));    a = Encryptions.encrypt_mul(a, (int)(mul.charAt(indexm)));
                    int r = Colors[i][j].getRed();     r = Encryptions.encrypt_kisar(r, (int)(kisar.charAt(indexk)));    r = Encryptions.encrypt_mul(r, (int)(mul.charAt(indexm)));
                    int g = Colors[i][j].getGreen();   g = Encryptions.encrypt_kisar(g, (int)(kisar.charAt(indexk)));    g = Encryptions.encrypt_mul(g, (int)(mul.charAt(indexm)));
                    int b = Colors[i][j].getBlue();    b = Encryptions.encrypt_kisar(b, (int)(kisar.charAt(indexk)));    b = Encryptions.encrypt_mul(b, (int)(mul.charAt(indexm)));
                    Colors[i][j] = new Color(r, g, b, a);
                    indexk = ((indexk+1)%kisar.length());
                    indexm = ((indexm + 1)%mul.length());
                }
                else {
                    indexk = ((indexk+1)%kisar.length());
                    indexm = ((indexm + 1)%mul.length());
                }
            }
        }
        Colors = Encryptions.encrypt(key, Colors, min);
        convertToPixlelMatrix(Colors);
        
//        for (int i = 0; i < pixels.length; i++) {
//            System.out.println("before: " + pixels[i].length);
//            this.pixels[i] = Encryptions.encrypt(Encryptions.compress(Hash), pixels[i].toString()).getBytes();
//            byte[] temp = Encryptions.decrypt(Encryptions.compress(Hash), pixels[i].toString()).getBytes();
//            System.out.println("after: " + pixels[i].length);
//            System.out.println("--------------------------------------------------------------");
//        }
    }
    public void decryptcolors(String kisar, String mul, String key,String Hash, int min) {
//        for (int i = 0; i < pixels.length; i++) {
//            System.out.println(i + pixels[i].length);
//            this.pixels[i] = Encryptions.decrypt(Encryptions.compress(Hash), pixels[i]);
//        }
        int indexk = 0;
        int indexm = 0;
        Color[][] Colors = ConvertToColorMatrix();
        Colors = Encryptions.decrypt(key, Colors, min);
        for (int i = 0; i < Colors.length; i++) {
            for (int j = 0; j < Colors[i].length; j++) {
                if ((Colors[i][j].getRGB() >> 24) != 0) {
                    int a = Colors[i][j].getAlpha();    a = Encryptions.decrypt_mul(a, (int)(mul.charAt(indexm)));    a = Encryptions.decrypt_kisar(a, (int)(kisar.charAt(indexk)));    
                    int r = Colors[i][j].getRed();      r = Encryptions.decrypt_mul(r, (int)(mul.charAt(indexm)));    r = Encryptions.decrypt_kisar(r, (int)(kisar.charAt(indexk)));    
                    int g = Colors[i][j].getGreen();    g = Encryptions.decrypt_mul(g, (int)(mul.charAt(indexm)));    g = Encryptions.decrypt_kisar(g, (int)(kisar.charAt(indexk)));
                    int b = Colors[i][j].getBlue();     b = Encryptions.decrypt_mul(b, (int)(mul.charAt(indexm)));    b = Encryptions.decrypt_kisar(b, (int)(kisar.charAt(indexk)));
                    Colors[i][j] = new Color(r, g, b, a);
                    indexk = ((indexk+1)%kisar.length());
                    indexm = ((indexm + 1)%mul.length());
                }
                else {
                    indexk = ((indexk+1)%kisar.length());
                    indexm = ((indexm + 1)%mul.length());
                }
            }
        }
        convertToPixlelMatrix(Colors);
    }
}