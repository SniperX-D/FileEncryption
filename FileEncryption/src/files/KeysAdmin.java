/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author daniz
 */
public class KeysAdmin {
    
    
    
    public static String getKey(String Path, String eKey) throws IOException {
        String bytes = IIOMetadataUpdater.readValue(new File(Path), "key");
        System.out.println(bytes);
        if (bytes != null) {
            try {
                bytes =  Static_and_classes.Encryptions.decrypt(Static_and_classes.Encryptions.compress(eKey), bytes);
                return bytes;
            } catch(Exception e){
                return null;
            }
        }
        return null;
    }    
    public static void writeKey(BufferedImage img, String Path, String key, String eKey) throws IOException, Exception{
        String bytes = IIOMetadataUpdater.readValue(new File(Path), eKey);
        if (bytes == null) {
            eKey = Static_and_classes.Encryptions.compress(eKey);
            bytes = Static_and_classes.Encryptions.encrypt(eKey, key);
        }
        IIOMetadataUpdater.writeData(img, new File(Path), "key", bytes);
    }
}
