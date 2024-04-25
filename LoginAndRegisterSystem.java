package net.codejava.sql;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class LoginAndRegisterSystem {

    private JFrame frmLogin;
    private JTextField textField;
    private JPasswordField passwordField;
    private MainMenu mainMenu;
    Connection connection = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginAndRegisterSystem window = new LoginAndRegisterSystem();
                    window.frmLogin.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginAndRegisterSystem() {
        initialize();
        connectToDatabase();
        mainMenu = new MainMenu();
    }

    private void initialize() {
        frmLogin = new JFrame();
        frmLogin.setTitle("Login");
        frmLogin.getContentPane().setBackground(new Color(106, 90, 205));
        frmLogin.setForeground(Color.BLACK);
        frmLogin.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 15));
        frmLogin.setBounds(200, 200, 700, 500);
        frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmLogin.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Welcome!");
        lblNewLabel.setBounds(225, 25, 300, 50);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        frmLogin.getContentPane().add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(130, 160, 130, 30);
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 20));
        frmLogin.getContentPane().add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(130, 230, 100, 30);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 20));
        frmLogin.getContentPane().add(lblPassword);

        textField = new JTextField();
        textField.setBounds(290, 160, 180, 30);
        textField.setFont(new Font("Tahoma", Font.BOLD, 15));
        frmLogin.getContentPane().add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(290, 230, 180, 30);
        passwordField.setFont(new Font("Tahoma", Font.BOLD, 15));
        frmLogin.getContentPane().add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setForeground(Color.BLUE);
        btnLogin.setBackground(Color.LIGHT_GRAY);
        btnLogin.setBounds(150, 330, 89, 23);
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnLogin.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent arg0) {
                loginUser();
            }
        });
        frmLogin.getContentPane().add(btnLogin);

        JButton btnRegister = new JButton("Registration");
        btnRegister.setForeground(Color.BLUE);
        btnRegister.setBackground(Color.LIGHT_GRAY);
        btnRegister.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnRegister.setBounds(400, 330, 130, 23);
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRegistration();
            }
        });
        frmLogin.getContentPane().add(btnRegister);
    }
    
    public void showMainMenu() {
        mainMenu.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;instanceName=meirbek\\sqlexpress;databaseName=IT_management_system;encrypt=true;trustServerCertificate=true", "sa", "mikotanir");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    private void loginUser() {
        String username = textField.getText();
        String password = passwordField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE username=? AND password=?");
            statement.setString(1, username);
            statement.setString(2, password);
            if (statement.executeQuery().next()) {
                
                // Open the main menu after successful login
                showMainMenu();
                // Close the login window
                frmLogin.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openRegistration() {
        Register_S register = new Register_S(connection);
        register.showRegistrationForm();
        frmLogin.setVisible(false);
    }

    class Register_S {
        private Connection connection;
        private JFrame frmRegister;
        private JTextField mailField;
        private JTextField nameField;
        private JTextField phoneField;
        private JTextField userField;
        private JPasswordField passwordField;

        public Register_S(Connection connection) {
            this.connection = connection;
            initialize();
            
        }

        private void initialize() {
            frmRegister = new JFrame();
            frmRegister.setTitle("Register");
            frmRegister.getContentPane().setBackground(new Color(30, 144, 255));
            frmRegister.setBounds(200, 200, 700, 500); // Установка размера окна
            frmRegister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frmRegister.getContentPane().setLayout(null);

            JLabel lblNewLabel = new JLabel("Registration Form");
            lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
            lblNewLabel.setBounds(250, 25, 200, 50);
            frmRegister.getContentPane().add(lblNewLabel);
            
            JLabel lblName = new JLabel("Name");
            lblName.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblName.setBounds(50, 100, 100, 30);
            frmRegister.getContentPane().add(lblName);

            nameField = new JTextField();
            nameField.setBounds(160, 100, 180, 30);
            frmRegister.getContentPane().add(nameField);
            nameField.setColumns(10);

            JLabel lblEmail = new JLabel("Email");
            lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblEmail.setBounds(50, 150, 100, 30);
            frmRegister.getContentPane().add(lblEmail);

            mailField = new JTextField();
            mailField.setBounds(160, 150, 180, 30);
            frmRegister.getContentPane().add(mailField);
            mailField.setColumns(10);

            JLabel lblPhone = new JLabel("Phone");
            lblPhone.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblPhone.setBounds(50, 200, 100, 30);
            frmRegister.getContentPane().add(lblPhone);

            phoneField = new JTextField();
            phoneField.setBounds(160, 200, 180, 30);
            frmRegister.getContentPane().add(phoneField);
            phoneField.setColumns(10);

            JLabel lblUsername = new JLabel("Username");
            lblUsername.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblUsername.setBounds(50, 250, 100, 30);
            frmRegister.getContentPane().add(lblUsername);

            userField = new JTextField();
            userField.setBounds(160, 250, 180, 30);
            frmRegister.getContentPane().add(userField);
            userField.setColumns(10);
            
            JLabel lblPassword = new JLabel("Password");
            lblPassword.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblPassword.setBounds(50, 300, 100, 30);
            frmRegister.getContentPane().add(lblPassword);

            passwordField = new JPasswordField();
            passwordField.setBounds(160, 300, 180, 30);
            frmRegister.getContentPane().add(passwordField);

            JButton btnRegister = new JButton("Register");
            btnRegister.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    registerUser();
                }
            });
            btnRegister.setFont(new Font("Tahoma", Font.BOLD, 15));
            btnRegister.setBounds(250, 360, 150, 30);
            frmRegister.getContentPane().add(btnRegister);
        }

        public void showRegistrationForm() {
            frmRegister.setVisible(true);
        }

        private void registerUser() {
            String email = mailField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();
            String username = userField.getText();
            String password = new String(passwordField.getPassword());

            try {
                // Сначала вставляем данные в таблицу UserDetails
                PreparedStatement statement = connection.prepareStatement("INSERT INTO UserDetails (name, email, phone) VALUES (?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phone);
                statement.executeUpdate();

                // Затем вставляем данные в таблицу Users
                statement = connection.prepareStatement("INSERT INTO Users (username,email, password) VALUES (?,?, ?)");
                statement.setString(1, username);
                statement.setString(2, email);
                statement.setString(3, password);
                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    
                    // После успешной регистрации показываем главное меню
                    showMainMenu();
                    frmRegister.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed", "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error occurred during registration", "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
