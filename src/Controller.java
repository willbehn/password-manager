import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Controller {
    PasswordManager passwordManager;
    Gui gui;

    Controller(){
        passwordManager = new PasswordManager();
        updatePasswordMap();
        updateUserMap();
        gui = new Gui(this);
    }


    public Boolean logIn(String username,String password){
        Boolean logInStatus = passwordManager.checkCorrectPass(username,password);
        System.out.println(logInStatus);

        return logInStatus;
    }

    void addUser(String username, String password){
        passwordManager.addUser(username, password);
    }

    public void addPassword(String domain, String userInfo, String password){
        try {
            passwordManager.addPassword(domain, userInfo, password);
            
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException
                | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String decryptPassword(String[] passwordData){

        String decryptedPassword = "";

        try {
            decryptedPassword = passwordManager.decryptPassword(passwordData);

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return decryptedPassword;
    }

    public HashMap<String, String[]> getAllEncryptedPasswords(){
        return passwordManager.getAllPasswordsEcrypted();
    }

    public void updatePasswordMap(){
        passwordManager.readAllPasswordsFromFile();
    }

    public void updateUserMap(){
        passwordManager.readAllUsersFromFile();
    }

    public String getSelectedUser(){
        return passwordManager.getSelectedUser();
    }
}
