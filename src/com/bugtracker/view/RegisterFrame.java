package com.bugtracker.view;

import com.bugtracker.model.UserRole;
import com.bugtracker.service.AuthService;
import com.bugtracker.service.BugService;
import com.bugtracker.service.UserService;
import javax.swing.*;
import java.awt.*;

/**
 * Registration frame for creating new user accounts
 */
public class RegisterFrame extends JFrame {
    private AuthService authService;
    private UserService userService;
    private BugService bugService;
    private JFrame parentFrame;
    
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<UserRole> roleComboBox;
    private JLabel messageLabel;

    public RegisterFrame(AuthService authService, UserService userService, 
                        BugService bugService, JFrame parentFrame) {
        this.authService = authService;
        this.userService = userService;
        this.bugService = bugService;
        this.parentFrame = parentFrame;
        
        initComponents();
    }

    private void initComponents() {
        setTitle("Bug Tracker - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        int row = 0;

        // Full Name
        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(createLabel("Full Name"), gbc);
        gbc.gridy = row++;
        fullNameField = createTextField();
        formPanel.add(fullNameField, gbc);

        // Username
        gbc.gridy = row++;
        formPanel.add(createLabel("Username"), gbc);
        gbc.gridy = row++;
        usernameField = createTextField();
        formPanel.add(usernameField, gbc);

        // Email
        gbc.gridy = row++;
        formPanel.add(createLabel("Email"), gbc);
        gbc.gridy = row++;
        emailField = createTextField();
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridy = row++;
        formPanel.add(createLabel("Password"), gbc);
        gbc.gridy = row++;
        passwordField = createPasswordField();
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridy = row++;
        formPanel.add(createLabel("Confirm Password"), gbc);
        gbc.gridy = row++;
        confirmPasswordField = createPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        // Role
        gbc.gridy = row++;
        formPanel.add(createLabel("Role"), gbc);
        gbc.gridy = row++;
        roleComboBox = new JComboBox<>(UserRole.values());
        roleComboBox.setSelectedItem(UserRole.DEVELOPER);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setPreferredSize(new Dimension(300, 35));
        formPanel.add(roleComboBox, gbc);

        // Message label
        gbc.gridy = row++;
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(messageLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(39, 174, 96));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setOpaque(true);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setPreferredSize(new Dimension(300, 40));
        registerButton.addActionListener(e -> handleRegister());

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(127, 140, 141));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(300, 35));
        backButton.addActionListener(e -> backToLogin());

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // Add panels to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        return field;
    }

    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        UserRole role = (UserRole) roleComboBox.getSelectedItem();

        // Validation
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please fill in all fields", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match", true);
            return;
        }

        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters", true);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showMessage("Please enter a valid email address", true);
            return;
        }

        try {
            authService.register(username, email, password, fullName, role);
            JOptionPane.showMessageDialog(this, 
                "Registration successful! Please login.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), true);
        } catch (Exception e) {
            showMessage("Registration failed: " + e.getMessage(), true);
        }
    }

    private void backToLogin() {
        parentFrame.setVisible(true);
        dispose();
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setForeground(isError ? Color.RED : new Color(46, 204, 113));
    }
}
