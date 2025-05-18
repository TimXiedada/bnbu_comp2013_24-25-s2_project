package net.xiedada.eventinfo.ui.swingui;
import net.xiedada.eventinfo.userutilities.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;

import net.xiedada.eventinfo.exceptions.*;
public class LoginForm extends JFrame {

    private User user;

    public User getLoggedInUser() {
        return user;
    }

    public LoginForm() {
        super("Login");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("User name:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(100, 80, 80, 25);
        panel.add(registerButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(190, 80, 80, 25);
        panel.add(exitButton);

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            try {
                User user = User.findUserWithUserName(username);
                if (user != null && user.login(username, password)) {
                    JOptionPane.showMessageDialog(panel, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false); // Hide the login form
                    dispose(); // Dispose of the login form
                    // Proceed to the main application
                    SwingUIMain.launchMainPanel(user);
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            new RegisterForm(); // Open the RegisterForm window
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }
}
