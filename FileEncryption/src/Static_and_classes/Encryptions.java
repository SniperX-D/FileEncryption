/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Static_and_classes;

import java.awt.Color;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author daniz
 */
public class Encryptions {
    
    
    public static int encrypt_mul(int num, int x){
        x  = x+ (1-(x%2));
        
        int temp = num;
        temp = (temp*x)%256;
        return temp;       
    }
    public static int decrypt_mul(int num, int x){
        x = x+ (1-(x%2));
        int temp = num;
        while (temp%x != 0) {
            temp+= 256;
        }
        temp /= x;
        return temp;
    }
    public static int encrypt_kisar(int num, int x){
        int temp = num;
        temp += x;
        temp =(temp%256);
        return temp;     
    }
    public static int decrypt_kisar(int num, int x){
       int temp = num;
       temp -= x;
       if (temp < 0) {
           temp = 256 - Math.abs(temp);
       }
       temp = temp%256;
       return temp;
    }
    public static int encrypt_decrypt_oposite(int num){
        return 255 - num;
    }
    public static Color[][] encrypt_color_array_moveright(Color[][] matrix){
        Color[][] result = new Color[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            Color temp = matrix[i][matrix[i].length -1];
            for (int j = 0; j < matrix[i].length-1; j++) {
                result[i][j + 1] = matrix[i][j];
            }
            result[i][0] = temp;
        }
        return result;
    }
    public static Color[][] decrypt_color_array_moveright(Color[][] matrix){
        Color[][] result = new Color[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            Color temp = matrix[i][0];
            for (int j = matrix[i].length-1; j > 0; j--) {
                result[i][j - 1] = matrix[i][j];
            }
            result[i][matrix[i].length -1] = temp;
        }
        return result;
    }
    public static Color[][] encrypt_color_array_movedown(Color[][] matrix){
        Color[][] result = new Color[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            Color temp = matrix[matrix.length - 1][i];
            for (int j = 0; j < matrix.length-1; j++) {
                result[j + 1][i] = matrix[j][i];
            }
            result[0][i] = temp;
        }
        return result;
    }
    public static Color[][] decrypt_color_array_movedown(Color[][] matrix){
        Color[][] result = new Color[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            Color temp = matrix[0][i];
            for (int j = matrix.length-1; j > 0; j--) {
                result[j - 1][i] = matrix[j][i];
            }
            result[matrix.length - 1][i] = temp;
        }
        return result;
    }
    
    public static String Create_Key(int height, int width, int min){
        String matrix100 = ",";
        matrix100 = build(matrix100, min*min);
        System.out.println("100: " + matrix100);
        String result_temp = "";
        boolean b = true;
        size[][] matrix = Div_matrix(height, width, min);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if ((matrix[i][j].height != min || matrix[i][j].width != min)) {
                    String temp = ",";
                    temp = build(temp, matrix[i][j].height * matrix[i][j].width);
                    result_temp += temp + "@";
                }
                else if (b){
                    result_temp += matrix100 + "@";
                    b = false;
                }
            }
        }
        return result_temp;
    }
    private static String build(String s, int size){
        Random R = new Random();
        for (int i = 0; i < size; i++) {
            int x =0;
            do {                
                x = R.nextInt(size);
            } while (s.contains("," + x + ","));
            s += x +",";
        }
        return s;
    }
    private static size[][] Div_matrix(int height, int width, int min){
        size[][] result_temp;
        int modx = height % min;
        int mody = width % min;
        int divx = height / min;
        int divy = width / min;
        int new_x=divx;
        int new_y=divy;
        if (modx > 0) {
            new_x +=1;
        }
        if (mody > 0) {
            new_y +=1;
        }
        result_temp = new size[new_x][new_y];
        for (int i = 0; i < result_temp.length; i++) {
            int temp_width = width;
            int temp_height = height;
            for (int j = 0; j < result_temp[i].length; j++) {
                if (temp_height >= min) {
                    if (temp_width >= min) {
                        result_temp[i][j] = new size(min, min);
                        temp_width -=min;
                    }
                    else {
                        result_temp[i][j] = new size(min, temp_width % min);
                    }
                }
                else{
                    if (temp_width >= min) {
                        result_temp[i][j] = new size(temp_height % min, min);
                        temp_width -=min;
                    }
                    else {
                        result_temp[i][j] = new size(temp_height % min, width % min);
                    }
                }
            }
            height -= min;
        }
        return result_temp;
    }
    public static Color[][] encrypt(String s, Color[][] matrix,int min){
        Color[][] result = new Color[matrix.length][matrix[0].length];
        String[] blocks = s.split("@");
        size[][] sizes = Div_matrix(matrix.length, matrix[0].length, min);
        int index = 0;
        int y = 0;
        int x = 0;
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < sizes[i].length; j++) {
                if (sizes[i][j].height == min && sizes[i][j].width == min) {
                    index = 0;
                }
                String[] key = blocks[index%blocks.length].substring(1, blocks[index%blocks.length].length()).split(",");
                int index_key = 0;
                for (int k = 0; k < sizes[i][j].height; k++) {
                    for (int l = 0; l < sizes[i][j].width; l++) {
                        int h = (Integer.valueOf(key[(index_key%key.length)]))/(sizes[i][j].width);
                        int w = (((Integer.valueOf(key[(index_key%key.length)])))%sizes[i][j].width);
                        Color c = matrix[h + x][w + y];
                        result[k + x][l + y] = c;
                        index_key ++;
                    }
                }
                y += min;
                index++;
                
            }
            x += min;
            y = 0;
        }
        return result;
    }
    public static Color[][] decrypt(String s, Color[][] matrix, int min){
        Color[][] result = new Color[matrix.length][matrix[0].length];
        String[] blocks = s.split("@");
        size[][] sizes = Div_matrix(matrix.length, matrix[0].length, min);
        int index = 0;
        int y = 0;
        int x = 0;
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < sizes[i].length; j++) {
                if (sizes[i][j].height == min && sizes[i][j].width == min) {
                    index = 0;
                }
                String[] key = blocks[index%blocks.length].substring(1, blocks[index%blocks.length].length()).split(",");
                int index_key = 0;
                for (int k = 0; k < sizes[i][j].height; k++) {
                    for (int l = 0; l < sizes[i][j].width; l++) {    
                        Color c =  matrix[k + x][l + y];
                        int w = (Integer.valueOf(key[(index_key%key.length)]))%sizes[i][j].width;
                        int h = (Integer.valueOf(key[(index_key%key.length)]))/(sizes[i][j].width);
                        result[h + x][w + y] = c;
                        index_key ++;
                    }
                }
                y += min;
                index++;
            }
            x += min;
            y = 0;
            
        }
        return result;
    }
    public static String encrypt_notepad(String word, char[] code){
        char[] arr = word.toCharArray();
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            int temp = (int)(arr[i]);
            temp = ((temp + (int)(code[index])));
            arr[i] = (char)(temp);
            index = (index + 1)% code.length;
        }
        return (new String (arr));
    }
    public static String decrypt_notepad(String word, char[] code){
        char[] arr = word.toCharArray();
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            int temp = (int)(arr[i]);
            temp = ((temp - (int)(code[index])));
            arr[i] = (char)(temp);
            index = (index +1)%code.length;
        }
        return (new String (arr));
    }
    public static String CreateKey_Rnd(){
        String s = "";
        int Length = 50;
        Random R = new Random();
        for (int i = 0; i < Length; i++) {
            Boolean b = true;
            while (b) {                
                char c = (char)(R.nextInt(93) + 33);
                if (!s.contains(String.valueOf(c)) && c!= '(' && c!='|' && c!= '?' && c!='.' && c!= '*' && c!= '+' && c!='{' && c!='[' && c!='$' && c!=')' && c!='^') {
                    s += c;
                    b = false;
                }
            }
        }
        return s;
    }
    public static String encrypt_Rnd(String word, String key){
        String ret="";
        char[] carr = word.toCharArray();
        char[] keyarr = key.toCharArray();
        for (int i = 0; i < carr.length; i++) {
            int temp = carr[i];
            if (temp !=32 ) {
                Random R = new Random();
                String s = "";
                while (temp != 1) {                
                    int x = R.nextInt(temp - 1) + 1;
                    if (temp >= x) {
                        temp -= x;
                        s += keyarr[x];
                    }
                }
                s += keyarr[1] + keyarr[0];
                ret += s;
            }
        }
        return ret;
    }
    public static String decrypt_Rnd(String word, String key){
        char[] keyarr = key.toCharArray();
        String ret="";
        String split = String.valueOf(keyarr[0]);
        System.out.println("Split: " + split);
        String[] chars = {};
        try {
            chars = word.split(split);
        } catch (java.util.regex.PatternSyntaxException e) {
            System.out.println(e);
            
            split += split;
            chars = word.split(split);
        }
        
        
        System.out.println("len: "+chars.length);
        for (String s : chars) {
            int charr = 0;
            char[] arr = s.toCharArray();
            for (char c : arr) {
                for (int i = 0; i < keyarr.length; i++) {
                    if (c == keyarr[i]) {
                        charr += i;
                        break;
                    }
                }
            }
            ret += (char)(charr);
        }
        System.out.println("len2: " + ret.length());
        return ret;
    }
    public static String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec("RandomInitVector".getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            System.out.println("Cipher Fail");
        }

        return null;
    }

    public static String decrypt(String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec("RandomInitVector".getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            System.out.println("Cipher Fail");
        }

        return null;
    }

    
    public static byte[] encrypt(String key, byte[] value) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(value);
    }

    public static byte[] decrypt(String key, byte[] encrypted) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(encrypted);
    }
    
    
    public static String compress(String s){
        int index = 0;
        int index2 =0;
        String[] lines = new String[4];
        for (int i = 0; i < 4; i++) {
            String temp = "";
            for (int j = 0; j < 16; j++) {
                temp += s.charAt(index);
                index ++;
            }
            lines[index2] = temp;
            index2 ++;
        }
        String line = lines[0];
        for (int i = 1; i < lines.length; i++) {
            String nline = "";
            for (int j = 0; j < lines[i].length(); j++) {
                int c = line.charAt(j);
                int c2 = lines[i].charAt(j);
                c = Math.abs(c - c2) % 93;
                c += 33;
                nline += (char)c;
            }
            line = nline;
            
        }
        return line;
    }
    
}
    

class size{
    int height;
    int width;

    public size(int height, int width) {
        this.height = height;
        this.width = width;
    }
    
}




