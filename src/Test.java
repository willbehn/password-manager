import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, IOException{
        PasswordManager passwordManager = new PasswordManager();

        Scanner inputScanner = new Scanner(System.in);

        System.out.println("==Master password==");
        String passord = inputScanner.nextLine().strip();

        if (!passwordManager.checkCorrectPass(passord)){
            return;
        }

        while (true){

            System.out.println("1 to add password, 2 to get all password");

            String input = inputScanner.nextLine().strip();

            if (input.equals("1")){
                System.out.print("Domain: ");
                String domain = inputScanner.nextLine().strip();

                System.out.print("New user: ");
                String newUser = inputScanner.nextLine().strip();

                System.out.print("New password: ");
                String newPass = inputScanner.nextLine().strip();

                passwordManager.addPassword(domain, newUser, newPass);
               

            } else if (input.equals("2")){
                //passwordManager.testDecryptAllPasswords();
            } 
            
        }
    }



    public void settPassordTest(){
        String password = "123456";

        
    }












    // public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeySpecException{
    //     PasswordManager pm = new PasswordManager();
        
    //     String masterPass = "123456";
    //     //pm.addMasterPass(masterPass);

    //     if (!pm.checkCorrectPass(masterPass)){
    //         System.out.println("feil passord");
    //     }
        
        
    //     // String nyBruker = "Bruker4";
    //     // String passord = "Passord4";
    //     // String domene = "abc.no";
    //     // pm.addPassword(domene, nyBruker, passord);

    //     pm.test();
    // }
}
