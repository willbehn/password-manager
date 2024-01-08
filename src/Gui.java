import java.awt.*;
import java.awt.Desktop.Action;
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
    JFrame logInWindow, passwordWindow, newPasswordWindow, newUserWindow, detailsWindow;
    JPanel passwordMainPanel;
    JTextField passwordTextField;
    JTextField usernameTextField;
    Controller controller;
    String selectedUser;
    JLabel failedLogInLabel;
    float fontSize = 14;

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

        windowWidth = 400;
        windowHeight = 250;
        
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
        newPasswordWindow.setLocationRelativeTo(null);
        newPasswordWindow.setSize(windowWidth,windowHeight);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2));
        
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
        newUserWindow = new JFrame("New user setup");
        newUserWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newUserWindow.setLocationRelativeTo(null);
        newUserWindow.setSize(windowWidth,windowHeight);

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
        addUserButton.addActionListener(new AddUserButton(userField, passwordField));

        mainPanel.add(userLabel);
        mainPanel.add(userField);

        mainPanel.add(userConfirmLabel);
        mainPanel.add(userConfirmField);

        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        mainPanel.add(passwordConfirmLabel);
        mainPanel.add(passwordConfirmField);

        mainPanel.add(addUserButton);

        newUserWindow.add(mainPanel);

        newUserWindow.setVisible(true);
    }

    public void passwordWindow(){
        passwordWindow = new JFrame("Password manager");
        passwordWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        passwordWindow.setLocationRelativeTo(null);
        passwordWindow.setSize(windowWidth,windowHeight);

        selectedUser = controller.getSelectedUser();
        System.out.println(selectedUser);

        timer();

        passwordMainPanel = new JPanel();
        passwordMainPanel.setLayout(new BorderLayout());

        JToolBar topToolBar = createToolBar();

        passwordMainPanel.add(topToolBar, BorderLayout.PAGE_START);

        JPanel allPasswordsPanel = createPasswordPanel("");
        
        JScrollPane scrollPane = new JScrollPane(allPasswordsPanel);

        passwordMainPanel.add(scrollPane, BorderLayout.CENTER);
        passwordWindow.add(passwordMainPanel);
        passwordWindow.setVisible(true);
    }

    public JToolBar createToolBar(){
        JToolBar topToolBar = new JToolBar();


        JTextField searchField = new JTextField(15);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButton(searchField));
        
        JButton newPasswordButton = new JButton("Add new password");
        newPasswordButton.addActionListener(new NewPasswordButton());

        topToolBar.add(searchField);
        topToolBar.add(searchButton);
        topToolBar.add(newPasswordButton);

        return topToolBar;
    }


    public JPanel createPasswordPanel(String term){
        JPanel allPasswordsPanel = new JPanel();

        allPasswordsPanel.setLayout(new GridLayout(0, 3));


        HashMap<String, String[]> allPasswordsEncrypted = controller.getAllEncryptedPasswords();

        Boolean colored = true;

        if (allPasswordsEncrypted.size() >= 1){
            for (String[] passwordData : allPasswordsEncrypted.values()){

                if (!passwordData[4].equals(selectedUser)){continue;}

                float fontSize = 14;
                
                JLabel passwordInfo = new JLabel("<html>" + passwordData[0] + "<br>" + passwordData[1] + "<html>");
                passwordInfo.setFont(passwordInfo.getFont().deriveFont(fontSize));

                if (!colored){
                    passwordInfo.setForeground(Color.GRAY);
                    colored = true;
                } else {colored = false;}


                JButton copyPasswordButton = new JButton("Copy password");
                copyPasswordButton.addActionListener(new copyToClipBoardButton(passwordData));

                JButton detailsButton = new JButton("Details");
                detailsButton.addActionListener(new DetailsButton(passwordData));


                if (term.equals("")){
                    allPasswordsPanel.add(passwordInfo);
                    allPasswordsPanel.add(copyPasswordButton);
                    allPasswordsPanel.add(detailsButton);

                } else if (passwordData[0].contains(term)){ //TODO fiks dårlig kode her
                    allPasswordsPanel.add(passwordInfo);
                    allPasswordsPanel.add(copyPasswordButton);
                    allPasswordsPanel.add(detailsButton);   
                }

            }
        }

        return allPasswordsPanel;

    }

    public void search(String searchTerm){
        JPanel searchResult = createPasswordPanel(searchTerm);

        passwordMainPanel.removeAll();
        
        JScrollPane scrollPane = new JScrollPane(searchResult);
        JToolBar topToolBar = createToolBar();

        passwordMainPanel.add(topToolBar, BorderLayout.PAGE_START);

        passwordMainPanel.add(scrollPane, BorderLayout.CENTER);
        passwordWindow.add(passwordMainPanel);

        passwordWindow.revalidate();
        passwordWindow.repaint();
    }


    public void startWindow(){
        logInWindow = new JFrame("Password manager");
        logInWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logInWindow.setLocationRelativeTo(null);
        logInWindow.setSize(250,160);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("The all in one password manager");

        failedLogInLabel = new JLabel("Wrong username or password");
        failedLogInLabel.setForeground(Color.RED);
        failedLogInLabel.setFont(titleLabel.getFont().deriveFont(fontSize));
        failedLogInLabel.setVisible(false);

        mainPanel.add(failedLogInLabel, BorderLayout.PAGE_START);



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


    void detailsWindow(String[] passwordData){

        detailsWindow = new JFrame();
        detailsWindow.setLocation(passwordWindow.getX() + 20, passwordWindow.getY() + 20);
        detailsWindow.setSize(220,180);

        JPanel mainPanel = new JPanel();
        

        JLabel domainLabel = new JLabel("Domain: " + passwordData[0]);
        JLabel usernameLabel = new JLabel("Username: " + passwordData[1]);
        JLabel passwordLabel = new JLabel("Password: " + controller.decryptPassword(passwordData));

        JButton editButton = new JButton("Edit");
        JButton removeButton = new JButton("Remove");


        mainPanel.add(domainLabel);
        mainPanel.add(usernameLabel);
        mainPanel.add(passwordLabel);

        mainPanel.add(editButton);
        mainPanel.add(removeButton);        

        detailsWindow.add(mainPanel);
        detailsWindow.setVisible(true);
    }


    class SearchButton implements ActionListener{
        JTextField searchField;

        SearchButton(JTextField searField){
            this.searchField = searField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText();

            search(searchTerm);
            
        }
    }

    class DetailsButton implements ActionListener{
        String[] passwordData;

        DetailsButton(String[] passwordData){
            this.passwordData = passwordData;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            detailsWindow(passwordData);
        }
    }


    class LogInButton implements ActionListener{
        @Override 
        public void actionPerformed(ActionEvent e){
            String password = passwordTextField.getText();
            String username = usernameTextField.getText().stripLeading();
            
            if(controller.logIn(username,password)){
                logInWindow.setVisible(false);
                failedLogInLabel.setVisible(false);
                passwordWindow();

            } else {
                failedLogInLabel.setVisible(true);
            }
        }
    }

    class RegisterUserButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            logInWindow.setVisible(false);
            newUserWindow();
        }
    }

    class AddUserButton implements ActionListener{
        JTextField usernameField, passwordField;

        AddUserButton(JTextField usernameField, JTextField passwordField){
            this.usernameField = usernameField;
            this.passwordField = passwordField;
        }

        @Override 
        public void actionPerformed(ActionEvent e){
            String username = usernameField.getText();
            String password = passwordField.getText();

            controller.addUser(username, password);
            newUserWindow.dispose();

            logInWindow.setVisible(true);
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


            if (!password.equals(passwordConfirm)){
                newPasswordConfirmField.setBackground(Color.RED);
                return;
            }

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
