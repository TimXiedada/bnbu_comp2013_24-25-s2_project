package net.xiedada.eventinfo.ui.swingui;

import net.xiedada.eventinfo.userutilities.*;
import net.xiedada.eventinfo.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;

public class RegisterForm extends JFrame {

    public RegisterForm() {
        super("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel userTypeLabel = new JLabel("User Type:");
        JComboBox<String> userTypeComboBox = new JComboBox<>(new String[] { "Administrator", "Event Organizer", "Customer" });
        formPanel.add(userTypeLabel);
        formPanel.add(userTypeComboBox);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> {
            String userType = (String) userTypeComboBox.getSelectedItem();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if ("Administrator".equals(userType)) {
                    String adminPassword = JOptionPane.showInputDialog(this, "Enter admin password:", "Admin Authentication", JOptionPane.PLAIN_MESSAGE);
                    if (!"iLoveBNBU".equals(adminPassword)) {
                        JOptionPane.showMessageDialog(this, "Incorrect admin password.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    new Administrator(username, password);
                } else if ("Event Organizer".equals(userType)) {
                    new EventOrganizer(username, password);
                } else if ("Customer".equals(userType)) {
                    new Customer(username, password);
                }

                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IllegalArgumentException | BadStatusException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }
}
