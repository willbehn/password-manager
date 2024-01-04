import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.*;


public class Gui {
    JFrame logInWindow, passwordWindow, newPasswordWindow;
    JTextField passwordTextField;
    JTextField usernameTextField;
    Controller controller;
    String selectedUser;

    int windowWidth, windowHeight;


    //For nytt passord vindu;
    JLabel newDomainLabel, userInfoLabel, newPasswordLabel, newPasswordConfirmLabel;
    JTextField newDomainField, userInfoField, newPasswordField, newPasswordConfirmField;

    public Gui(Controller controller){

        //FlatLaf stuff
        FlatMacDarkLaf.setup();

        //Vanlig swing stuff
        // try {
        //     UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        // } catch (Exception e) { 
        //     System.exit(1);
        // }

        windowWidth = 450;
        windowHeight = 300;
        
        
        this.controller = controller;
        startWindow();
    }

    public void timer(){
        Timer timer = new Timer(90000, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                passwordWindow.dispose();
                passwordTextField.setText("");
                logInWindow.setVisible(true);
            }
            
        });
        timer.setRepeats(false);
        timer.start();
    }
    

    public void newPasswordWindow(){
        newPasswordWindow = new JFrame("New password setup");
        newPasswordWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newPasswordWindow.setLocation(500,500);
        newPasswordWindow.setSize(windowWidth,windowHeight);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2));
        
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        newDomainLabel = new JLabel("Enter password domain");
        newDomainField = new JTextField(10);

        userInfoLabel = new JLabel("Enter user info");
        userInfoField = new JTextField(10);

        newPasswordLabel = new JLabel("Enter new password");
        newPasswordField = new JPasswordField(10);

        newPasswordConfirmLabel = new JLabel("Confirm new password");
        newPasswordConfirmField = new JPasswordField(10);

        JButton addPasswordButton = new JButton("Add password");
        addPasswordButton.addActionListener(new AddPasswordButton());

        mainPanel.add(newDomainLabel);
        mainPanel.add(newDomainField);

        mainPanel.add(userInfoLabel);
        mainPanel.add(userInfoField);

        mainPanel.add(newPasswordLabel);
        mainPanel.add(newPasswordField);

        mainPanel.add(newPasswordConfirmLabel);
        mainPanel.add(newPasswordConfirmField);

        mainPanel.add(addPasswordButton);

        newPasswordWindow.add(mainPanel);

        newPasswordWindow.setVisible(true);
    }

    public void newUserWindow(){
        newPasswordWindow = new JFrame("New user setup");
        newPasswordWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newPasswordWindow.setLocation(500,500);
        newPasswordWindow.setSize(windowWidth,windowHeight);

        JLabel userLabel,userConfirmLabel,passwordLabel, passwordConfirmLabel;
        JTextField userField,userConfirmField,passwordField, passwordConfirmField;

        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2));
        

        userLabel = new JLabel("Enter username");
        userField = new JTextField(10);

        userConfirmLabel = new JLabel("Confirm username");
        userConfirmField = new JTextField(10);

        passwordLabel = new JLabel("Enter password");
        passwordField = new JPasswordField(10);

        passwordConfirmLabel = new JLabel("Confirm password");
        passwordConfirmField = new JPasswordField(10);

        JButton addUserButton = new JButton("Add user");
        //addUserButton.addActionListener();

        mainPanel.add(userLabel);
        mainPanel.add(userField);

        mainPanel.add(userConfirmLabel);
        mainPanel.add(userConfirmField);

        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        mainPanel.add(passwordConfirmLabel);
        mainPanel.add(passwordConfirmField);

        mainPanel.add(addUserButton);

        newPasswordWindow.add(mainPanel);

        newPasswordWindow.setVisible(true);
    }

    public void passwordWindow(){
        passwordWindow = new JFrame("Password manager");
        passwordWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        passwordWindow.setLocation(500,500);
        passwordWindow.setSize(windowWidth,windowHeight);

        selectedUser = controller.getSelectedUser();
        System.out.println(selectedUser);

        timer();

        JPanel mainPanel = new JPanel();

        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setLayout(new BorderLayout());

        //===Top panel stuff===
        JPanel topPanel = new JPanel();
        //topPanel.setLayout(new GridLayout(1,4));


        JButton searchButton = new JButton("Search");

        // JButton resetButton = new JButton("Reset");
        // resetButton.setBackground(Color.red);


        JTextField searchField = new JTextField(15);

        JButton newPasswordButton = new JButton("Add new password");
        newPasswordButton.addActionListener(new NewPasswordButton());

        
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(newPasswordButton);
        // topPanel.add(resetButton);

        mainPanel.add(topPanel, BorderLayout.PAGE_START);

        JPanel allPasswordsPanel = new JPanel();
        allPasswordsPanel.setLayout(new GridLayout(0, 3));


        HashMap<String, String[]> allPasswordsEncrypted = controller.getAllEncryptedPasswords();

        Boolean colored = true;

        if (allPasswordsEncrypted.size() >= 1){
            for (String[] passwordData : allPasswordsEncrypted.values()){

                if (!passwordData[4].equals(selectedUser)){continue;}

                System.out.println(Arrays.toString(passwordData));
                float fontSize = 14;
                
                JLabel passwordInfo = new JLabel("<html>" + passwordData[0] + "<br>" + passwordData[1] + "<html>");
                passwordInfo.setFont(passwordInfo.getFont().deriveFont(fontSize));

                if (!colored){
                    passwordInfo.setForeground(Color.GRAY);
                    colored = true;
                } else {colored = false;}


                JButton copyPasswordButton = new JButton("Copy password");
                //copyPasswordButton.setSize(new Dimension(5,5));
                copyPasswordButton.addActionListener(new copyToClipBoardButton(passwordData));

                JButton editButton = new JButton("Details");
    
                allPasswordsPanel.add(passwordInfo);
                allPasswordsPanel.add(copyPasswordButton);
                allPasswordsPanel.add(editButton);
                
            }
        }
        

        JScrollPane scrollPane = new JScrollPane(allPasswordsPanel);

        mainPanel.add(scrollPane, BorderLayout.CENTER);


        passwordWindow.add(mainPanel);
        passwordWindow.setVisible(true);
    }


    public void startWindow(){
        logInWindow = new JFrame("Password manager");
        logInWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logInWindow.setLocation(500,500);
        logInWindow.setSize(250,180);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("The all in one password manager");
        float fontSize = 16;
        titleLabel.setFont(titleLabel.getFont().deriveFont(fontSize));
        
        //titleLabel.setBackground(Color.white);

        mainPanel.add(titleLabel, BorderLayout.PAGE_START);

        JPanel logInPanel = new JPanel();



        JButton logInButton = new JButton("login");
        logInButton.setBackground(Color.green.darker());
        logInButton.addActionListener(new LogInButton());
        
        usernameTextField = new JTextField(10);
        JLabel usernameInstruction = new JLabel("Username");

        passwordTextField = new JPasswordField(10);
        JLabel passwordInstruction = new JLabel("Password");
        
        logInPanel.add(usernameInstruction);
        logInPanel.add(usernameTextField);

        logInPanel.add(passwordInstruction);
        logInPanel.add(passwordTextField);
        logInPanel.add(logInButton);

        JPanel registerPanel = new JPanel();


        JLabel newUserInstruction = new JLabel("New user?");
        JButton registerUserButton = new JButton("Register");
        registerUserButton.addActionListener(new RegisterUserButton());

        registerPanel.add(newUserInstruction);
        registerPanel.add(registerUserButton);

        mainPanel.add(logInPanel, BorderLayout.CENTER);
        mainPanel.add(registerPanel, BorderLayout.PAGE_END);

        logInWindow.add(mainPanel);
        logInWindow.setVisible(true);
    }


    class LogInButton implements ActionListener{
        @Override 
        public void actionPerformed(ActionEvent e){
            String password = passwordTextField.getText();
            String username = usernameTextField.getText().stripLeading();
            
            if(controller.logIn(username,password)){
                logInWindow.setVisible(false);
                passwordWindow();

            } else {System.out.println("Error");}
        }
    }

    class RegisterUserButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            logInWindow.setVisible(false);
            newUserWindow();
        }
    }

    class AddPasswordButton implements ActionListener{
        String newDomain, userInfo, password, passwordConfirm;

        @Override
        public void actionPerformed(ActionEvent e){
            
            newDomain = newDomainField.getText();
            userInfo = userInfoField.getText();
            password = newPasswordField.getText();
            passwordConfirm = newPasswordConfirmField.getText();


            // if (!password.equals(passwordConfirm)){ //TODO
            //     return;
            // }

            controller.addPassword(newDomain, userInfo, password);
            controller.updatePasswordMap();
            newPasswordWindow.dispose();

            passwordWindow();
            

        }
    }

    class NewPasswordButton implements ActionListener{
        @Override 
        public void actionPerformed(ActionEvent e){
            passwordWindow.dispose();
            newPasswordWindow();
        }
    }

    class copyToClipBoardButton implements ActionListener{
        String[] passwordData;

        copyToClipBoardButton(String[] passwordData){
            this.passwordData = passwordData;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            String passwordDecrypted = controller.decryptPassword(passwordData);
            
            StringSelection stringSelection = new StringSelection(passwordDecrypted);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            System.out.println("Copied to clipboard");
        }
    }
}
