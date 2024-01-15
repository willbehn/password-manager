import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Enumeration;

import javax.swing.*;

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

    public Gui(Controller controller){

        //FlatLaf stuff
        FlatMacDarkLaf.setup();

        //Vanlig swing stuff
        // try {
        //     UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        // } catch (Exception e) { 
        //     System.exit(1);
        // }

        windowWidth = 420;
        windowHeight = 220;

        this.controller = controller;
        setFont (new java.awt.Font("MONOSPACE", java.awt.Font.PLAIN, 14));
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


    JFrame createFrame(int widt, int height){
        JFrame newFrame = new JFrame("Password manager");

        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setLocationRelativeTo(null);
        newFrame.setSize(widt,height);

        return newFrame;
    }

    //setFont går igjennom alle UI komponentene sine font konstaneter og endrer tilmny font
    private static void setFont (Font newFont){
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {

            Object key = keys.nextElement();
            Object value = UIManager.get (key);

            if (value != null && value instanceof Font)
                UIManager.put (key, newFont);
        }
    }

    private void setLabelSize(JLabel field){
        field.setMinimumSize(new Dimension(150, 50));
        field.setPreferredSize(new Dimension(150, 50));
        field.setMaximumSize(new Dimension(150, 50));
    }
    

    public void newPasswordWindow(){
        newPasswordWindow = createFrame(250,180);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JLabel newDomainLabel = new JLabel("Service", SwingConstants.CENTER);
        JLabel userInfoLabel = new JLabel("Userinfo", SwingConstants.CENTER);
        JLabel newPasswordLabel = new JLabel("Password", SwingConstants.CENTER);


        JTextField newDomainField = new JTextField(10);
        JTextField userInfoField = new JTextField(10);
        JTextField newPasswordField = new JPasswordField(10);
    
        JButton addPasswordButton = new JButton("Add password");
        addPasswordButton.addActionListener(new AddPasswordButton(newDomainField, userInfoField, newPasswordField));

        JPanel underPanel = new JPanel();
        //underPanel.setLayout(new GridLayout(3,3));

        underPanel.add(newDomainLabel);
        underPanel.add(newDomainField);
        underPanel.add(userInfoLabel);
        underPanel.add(userInfoField);
        underPanel.add(newPasswordLabel);
        underPanel.add(newPasswordField);

        mainPanel.add(addPasswordButton, BorderLayout.PAGE_END);
        mainPanel.add(underPanel, BorderLayout.CENTER);

        newPasswordWindow.add(mainPanel);

        newPasswordWindow.setVisible(true);
    }
    

    public void newUserWindow(){
        newUserWindow = createFrame(250, 180);

        JLabel userLabel,passwordLabel,topLabel;
        JTextField userField,passwordField;

        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        topLabel = new JLabel("Add new user:", SwingConstants.CENTER);
        topLabel.setFont(topLabel.getFont().deriveFont(fontSize));

        userLabel = new JLabel("Username", SwingConstants.CENTER);
        userField = new JTextField(10);


        passwordLabel = new JLabel("Password", SwingConstants.CENTER);
        passwordField = new JPasswordField(10);


        JButton addUserButton = new JButton("Add");
        addUserButton.addActionListener(new AddUserButton(userField, passwordField));

        mainPanel.add(topLabel, BorderLayout.PAGE_START);

        JPanel underPanel = new JPanel();

        underPanel.add(userLabel);
        underPanel.add(userField);
        underPanel.add(passwordLabel);
        underPanel.add(passwordField);
        underPanel.add(addUserButton);

        mainPanel.add(underPanel, BorderLayout.CENTER);
        newUserWindow.add(mainPanel);
        newUserWindow.setVisible(true);
    }

    public void passwordWindow(){
        passwordWindow = createFrame(windowWidth, windowHeight);

        selectedUser = controller.getSelectedUser();
        System.out.println(selectedUser);

        timer(); //Starter timer som logger ut brukeren som er er logget inn når det har gått 90 sekunder

        passwordMainPanel = new JPanel();
        passwordMainPanel.setLayout(new BorderLayout());

        JToolBar topToolBar = createTopToolBar();

        passwordMainPanel.add(topToolBar, BorderLayout.PAGE_START);

        JPanel allPasswordsPanel = createPasswordPanel("");
        
        JScrollPane scrollPane = new JScrollPane(allPasswordsPanel);

        passwordMainPanel.add(scrollPane, BorderLayout.CENTER);
        passwordWindow.add(passwordMainPanel);
        passwordWindow.setVisible(true);
    }

    public JToolBar createTopToolBar(){
        JToolBar topToolBar = new JToolBar();

        JTextField searchField = new JTextField(15);

        JButton searchButton = new JButton("<html><font size='4' color=white>" + "Search" + "</font></html>");
        searchButton.addActionListener(new SearchButton(searchField));
        
        JButton newPasswordButton = new JButton("<html><font size='4' color=white>" + "Add new password" + "</font></html>");
        newPasswordButton.addActionListener(new NewPasswordButton());

        topToolBar.add(searchField);
        topToolBar.add(searchButton);
        topToolBar.add(newPasswordButton);

        topToolBar.setBackground(new Color(0,102,204));

        return topToolBar;
    }

    //Lager og returnerer et JPanel med alle passord. Hvilke passord man har med kan spesifiseres med et searchTerm
    public JPanel createPasswordPanel(String searchTerm){
        JPanel allPasswordsPanel = new JPanel();

        allPasswordsPanel.setLayout(new BoxLayout(allPasswordsPanel, BoxLayout.PAGE_AXIS));


        HashMap<String, String[]> allPasswordsEncrypted = controller.getAllEncryptedPasswords();

        if (allPasswordsEncrypted.size() >= 1){
            for (String[] passwordData : allPasswordsEncrypted.values()){

                if (!passwordData[4].equals(selectedUser)){continue;}

                JPanel passwordPanel = new JPanel();

                JLabel passwordInfo = new JLabel("<html><font size='4' color=white>" + passwordData[0] + "</font> <br> <font size='5'color=#66B2FF>" + passwordData[1] + "</font></html>");
                setLabelSize(passwordInfo);
               
                JButton copyPasswordButton = new JButton("Copy password");
                copyPasswordButton.addActionListener(new copyToClipBoardButton(passwordData));

                JButton detailsButton = new JButton("Details");
                detailsButton.addActionListener(new DetailsButton(passwordData));

                passwordPanel.add(passwordInfo);
                passwordPanel.add(copyPasswordButton);
                passwordPanel.add(detailsButton); 

                if (!searchTerm.equals("") && !passwordData[0].contains(searchTerm)){
                    continue;

                } else { 
                    allPasswordsPanel.add(passwordPanel);
                }
            }
        }
        setFont (new java.awt.Font("MONOSPACE", java.awt.Font.PLAIN, 14)); //TODO finn bedre sted til fontsetting
        return allPasswordsPanel;
    }

    public void search(String searchTerm){
        JPanel searchResult = createPasswordPanel(searchTerm);

        passwordMainPanel.removeAll();
        
        JScrollPane scrollPane = new JScrollPane(searchResult);
        JToolBar topToolBar = createTopToolBar();

        passwordMainPanel.add(topToolBar, BorderLayout.PAGE_START);

        passwordMainPanel.add(scrollPane, BorderLayout.CENTER);
        passwordWindow.add(passwordMainPanel);

        passwordWindow.revalidate();
        passwordWindow.repaint();
    }


    public void startWindow(){
        logInWindow = createFrame(250,180);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("The all in one password manager");

        failedLogInLabel = new JLabel("Wrong username or password", SwingConstants.CENTER);
        failedLogInLabel.setForeground(Color.RED);
        failedLogInLabel.setVisible(false);

        mainPanel.add(failedLogInLabel, BorderLayout.PAGE_START);

        JPanel logInPanel = new JPanel();

        JButton logInButton = new JButton("login");
        logInButton.setBackground(new Color(0,128,255));
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
        detailsWindow.setSize(250,180);

        JPanel mainPanel = new JPanel();
        
        JLabel domainLabel = new JLabel("Service: " + passwordData[0]);
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

            if(username.equals("") || password.equals("")){
                return; //TODO vis gi beskjed til bruker at alle feltene må være fylt inn
            }

            controller.addUser(username, password);
            newUserWindow.dispose();

            logInWindow.setVisible(true);
        }
    }

    class AddPasswordButton implements ActionListener{
        JTextField newDomainField, userInfoField, passwordField;

        AddPasswordButton(JTextField newDomain, JTextField userInfo, JTextField password){
            this.newDomainField = newDomain;
            this.userInfoField = userInfo;
            this.passwordField = password;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            
            String newDomain = newDomainField.getText();
            String userInfo = userInfoField.getText();
            String password = passwordField.getText();

            if (newDomain.equals("") || userInfo.equals("") || password.equals("")){
                return; //TODO vis gi beskjed til bruker at alle feltene må være fylt inn
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

