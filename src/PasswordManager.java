import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class PasswordManager {
    private AESEncryptDecrypt AESutil;
    private SecretKey key;
    private String passwordsFile, userFile, algorithm,selectedUser;
    private HashMap<String,String[]> allPasswordsEncrypted,allUsers;

    PasswordManager() {
        AESutil = new AESEncryptDecrypt();
        algorithm = "AES/CBC/PKCS5Padding";
        passwordsFile = "src/passwords.txt";
        userFile = "src/key.txt";
        allPasswordsEncrypted = new HashMap<String, String[]>(); 
        allUsers = new HashMap<String, String[]>(); //username,[userId,hashedKey,salt]
    }

    Boolean checkCorrectPass(String username,String password) {
        try{
            if (!allUsers.containsKey(username)){return false;}

            String[] userData = allUsers.get(username); //Exception

            String checkPassHash = AESutil.stringHashing(password);

            String correctPassHash = userData[0];
            String salt = userData[1];
    
            if (checkPassHash.equals(correctPassHash)){
                key = AESutil.getKeyFromPassword(password, salt);
                selectedUser = username;

                return true;
            }
            
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            e.printStackTrace();
        } return false;
    }

   
    void getDetails(){
        //TODO
    }


    void addUser(String username, String password){
        byte[] salt = AESutil.generateRandByte();

        String saltString = Base64.getEncoder().encodeToString(salt);

        try {
            String hashedPassword = AESutil.stringHashing(password);
            
            Writer output = new BufferedWriter(new FileWriter(userFile, true));
            output.append(AESutil.stringHashing(hashedPassword) + "," + salt + "," + username);
            output.close();

        } catch (NoSuchAlgorithmException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    void addPassword(String domain, String userInfo, String newPassword) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException{
        Writer output = new BufferedWriter(new FileWriter(passwordsFile, true));
        byte[] iv = AESutil.generateRandByte();

        String cipherText = AESutil.encryptPassword(algorithm, newPassword, key, new IvParameterSpec(iv));

        output.append(domain + "," + userInfo + "," + cipherText + "," + Base64.getEncoder().encodeToString(iv) + "," + selectedUser + "\n");
        output.close();
    } 
    

    String decryptPassword(String[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException{
            byte[] iv = Base64.getDecoder().decode(data[3]);
            String plainText = AESutil.decryptPassword(algorithm, data[2], key, new IvParameterSpec(iv));

            return plainText;
    }

    void readAllPasswordsFromFile(){
        allPasswordsEncrypted.clear(); //Resetter hashMappet slik at det ikke blir dobbelt opp

        try{
            Scanner scanner = new Scanner(new File(passwordsFile));
            scanner.nextLine();

            while (scanner.hasNextLine()){
                String[] data = scanner.nextLine().strip().split(",");
                allPasswordsEncrypted.put(data[0], data); 
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
    }

    void readAllUsersFromFile(){
        allUsers.clear();

        try{
            Scanner scanner = new Scanner(new File(userFile));
            scanner.nextLine();

            while (scanner.hasNextLine()){
                String[] data = scanner.nextLine().strip().split(",");
                allUsers.put(data[2], data); 
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
    }

    HashMap<String, String[]> getAllPasswordsEcrypted(){
        return allPasswordsEncrypted;
    }

    String getSelectedUser(){
        return selectedUser;
    }

}
