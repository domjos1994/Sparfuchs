package de.domjos.sparfuchs.utils.general;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * This is the class for the En- and Decryption of Files and Strings
 * @author Dominic Joas
 * @version 1.0
 */
public final class CryptoUtils {
    private static final String STR_KEY = "b76e7392ad10fee7";

    static void encrypt(String srcFile, String encryptedDstFile, String password) throws Exception {
        try (FileInputStream srcStream = new FileInputStream(srcFile); FileOutputStream encStream = new FileOutputStream(encryptedDstFile)) {
            encrypt(srcStream, encStream, password);
        }
    }

    private static void encrypt(InputStream inpStream, OutputStream encryptedOutStream, String password)throws GeneralSecurityException, IOException {
        SecretKey secKey = new SecretKeySpec( hashPwd( password), "AES");
        Cipher    cipher = Cipher.getInstance("AES");
        byte[]    byteBuffer = new byte[64 * 1024];
        int       n;
        cipher.init( Cipher.ENCRYPT_MODE, secKey );
        try (inpStream; encryptedOutStream; CipherOutputStream cos = new CipherOutputStream(encryptedOutStream, cipher)) {
            while ((n = inpStream.read(byteBuffer)) > 0) {
                cos.write(byteBuffer, 0, n);
            }
        }
    }
    
    public static void decrypt(String encryptedSrcFile, String decryptedDstFile, String password) throws Exception {
        try (FileInputStream encStream = new FileInputStream(encryptedSrcFile); FileOutputStream decStream = new FileOutputStream(decryptedDstFile)) {
            decrypt(encStream, decStream, password);
        }
    }

    private static void decrypt(InputStream encryptedInpStream, OutputStream decryptedOutStream, String password) throws GeneralSecurityException, IOException {
        SecretKey secKey = new SecretKeySpec( hashPwd( password), "AES");
        Cipher    cipher = Cipher.getInstance("AES");
        byte[]    byteBuffer = new byte[64 * 1024];
        int       n;
        cipher.init( Cipher.DECRYPT_MODE, secKey );
        try (encryptedInpStream; decryptedOutStream; CipherInputStream cis = new CipherInputStream(encryptedInpStream, cipher)) {
            while ((n = cis.read(byteBuffer)) > 0) {
                decryptedOutStream.write(byteBuffer, 0, n);
            }
        }
    }

    private static byte[] hashPwd(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance( "MD5" );
        md.update( password.getBytes(StandardCharsets.ISO_8859_1) );
        return md.digest();
    }

    /**
     * The Method to encrypt Strings
     * @param str The String which has to be encrypted
     * @return The EncryptedString
     */
    public static String encrypt(String str) throws Exception {
        if(str.equals("")) {
            return "";
        } else {
            SecretKey key = new SecretKeySpec(STR_KEY.getBytes(Charset.defaultCharset()), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
            byte[] enc = cipher.doFinal(utf8);
            return Base64.getEncoder().encodeToString(enc);
        }
    }

    /**
     * The Method to Decrypt Strings
     * @param str The String which has to be Decrypted
     * @return The DecryptedString
     */
    public static String decrypt(String str) throws Exception {
        if(str.equals("")) {
            return "";
        } else {
            SecretKey key = new SecretKeySpec(STR_KEY.getBytes(Charset.defaultCharset()), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dec = Base64.getDecoder().decode(str);
            byte[] utf8 = cipher.doFinal(dec);
            return new String(utf8, StandardCharsets.UTF_8);
        }
    }
}
