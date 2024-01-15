import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class AESEncryptDecrypt{

    //Generer og returnerer randome byte
    public static byte[] generateRandByte(){
        byte[] randByte = new byte[16];

        new SecureRandom().nextBytes(randByte);
        return randByte;
    }

    //Tar inn et password og salt, og generer en symmetrisk hemmelig nøkkel
    public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536,256);

        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        return secretKey;
    }

    //Tar inn plaintext og en hemmelig nøkkel, krypterer plaintexten med spesifiert algoritme og returnerer ciphertexten.
    public static String encryptPassword(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException{
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString((cipherText));
    } 

    //Tar inn ciphertext og en hemmelig nøkkel, dekrypterer ciphertexten med spesifiert algoritme og returnerer plaintexten.
    public static String decryptPassword(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException{
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(plainText);
    } 

    //Tar inn og hasher en String med SHA-256
    String stringHashing(String text) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hash);
    }
}