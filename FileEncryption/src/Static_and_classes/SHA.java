/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Static_and_classes;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Daniel Zabelin
 */
public enum SHA {
    SHA512("SHA-512"),
    SHA384("SHA-384"),
    SHA256("SHA-256");

    private final String SHA_Code;
    
    SHA(String code){
        this.SHA_Code = code;
    }
    
    public String ConvertToSHA(String word){
        String result="";
        try {
            MessageDigest md = MessageDigest.getInstance(this.SHA_Code);
            md.update(word.getBytes());
            byte[] db = md.digest();
            result = DatatypeConverter.printHexBinary(db).toLowerCase();
        } catch (Exception e) {
        }
        return result;
    }
    public String ConvertToSHA(byte[] word){
        String result="";
        try {
            MessageDigest md = MessageDigest.getInstance(this.SHA_Code);
            md.update(word);
            byte[] db = md.digest();
            result = DatatypeConverter.printHexBinary(db).toLowerCase();
        } catch (Exception e) {
        }
        return result;
    }
}

