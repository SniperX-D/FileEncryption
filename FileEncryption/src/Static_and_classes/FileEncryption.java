

package Static_and_classes;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/**
 *
 * @author daniz
 */
public class FileEncryption {
    byte[] Data;
    String extention;
    int Mode = 0;
    File f;
    public FileEncryption(String extention, File f){
        this.extention = extention;
        this.f = f;
        Mode = 0;
    }
    
    public void Encrypt(String key) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] tempData = Files.readAllBytes(f.toPath());
        byte[] extentionsave = getExtention().getBytes();
        byte[] newData = concatenate(tempData, extentionsave);
        this.Data = newData;
        Mode = 1;
    }
    public void Decrypt(String key) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {      
        this.Data = Files.readAllBytes(f.toPath());
        Mode = 2;
    }
    public File writeFile() throws FileNotFoundException, IOException{
        
        if (extention == null) {
            int lastpsik = lastbyte((byte)('.'), Data);
            
            byte[] newData = new byte[lastpsik -1];
            for (int i = 0; i < lastpsik -1; i++) {
                newData[i] = Data[i];
            }
            int lastpsik2 = new String(Data).lastIndexOf(".");
            extention = new String(Data).substring(lastpsik2, new String(Data).length());
            Data = newData;
        }
        
        File out = new File(f.getParent(), createOriginalOutputName(f, extention));
        try (FileOutputStream fos = new FileOutputStream(out.getAbsoluteFile())) {
            fos.write(Data);
        }
        return new File(out.getAbsolutePath());
    }
    public int lastbyte(byte b, byte[] data){
        if (data.length == 0) {
            return 0;
        }
        if (data[data.length - 1] == b) {
            return data.length;
        }
        return lastbyte(b, Arrays.copyOf(data, data.length -1));
    }
    public byte[] concatenate(byte[] a, byte[] b) {
    int aLen = a.length;
    int bLen = b.length;

    @SuppressWarnings("unchecked")
    byte[] c = (byte[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
    System.arraycopy(a, 0, c, 0, aLen);
    System.arraycopy(b, 0, c, aLen, bLen);

    return c;
    }
    public String getExtention(){
        String name = f.getName();
        int dotIndex = name.lastIndexOf('.');
        return name.substring(dotIndex, name.length());
    }
    private String createOriginalOutputName(final File file, String extension) {
        
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        String baseName = name.substring(0, dotIndex);
        if (Mode == 1) {
            return baseName + extension;
        }
        else {
            return baseName + "_Decrypted" +extension;
        }
    }
}
