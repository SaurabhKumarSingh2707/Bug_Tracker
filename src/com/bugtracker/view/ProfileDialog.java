package com.bugtracker.view;

import com.bugtracker.model.User;
import com.bugtracker.service.AuthService;
import com.bugtracker.service.UserService;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Dialog for viewing and editing user profile
 */
public class ProfileDialog extends JDialog {
    private AuthService authService;
    private UserService userService;
    
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField departmentField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ProfileDialog(JFrame parent, AuthService authService, UserService userService) {
        super(parent, "User Profile", true);
        this.authService = authService;
        this.userService = userService;
        
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        setSize(500, 550);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title
        JLabel titleLabel = new JLabel("👤 User Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        int row = 0;

        // User info (read-only)
        User user = authService.getCurrentUser();
        
        gbc.gridy = row++;
        JLabel usernameLabel = new JLabel("Username: " + user.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridy = row++;
        JLabel roleLabel = new JLabel("Role: " + user.getRole());
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(roleLabel, gbc);

        gbc.gridy = row++;
        if (user.getCreatedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JLabel createdLabel = new JLabel("Member since: " + user.getCreatedAt().format(formatter));
            createdLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(createdLabel, gbc);
        }

        gbc.gridy = row++;
        formPanel.add(new JSeparator(), gbc);

        // Editable fields
        gbc.gridy = row++;
        formPanel.add(createLabel("Full Name"), gbc);
        gbc.gridy = row++;
        fullNameField = createTextField();
        formPanel.add(fullNameField, gbc);

        gbc.gridy = row++;
        formPanel.add(createLabel("Email"), gbc);
        gbc.gridy = row++;
        emailField = createTextField();
        formPanel.add(emailField, gbc);

        gbc.gridy = row++;
        formPanel.add(createLabel("Department"), gbc);
        gbc.gridy = row++;
        departmentField = createTextField();
        formPanel.add(departmentField, gbc);

        gbc.gridy = row++;
        formPanel.add(new JSeparator(), gbc);

        gbc.gridy = row++;
        JLabel changePassLabel = new JLabel("Change Password");
        changePassLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(changePassLabel, gbc);

        gbc.gridy = row++;
        formPanel.add(createLabel("Old Password"), gbc);
        gbc.gridy = row++;
        oldPasswordField = createPasswordField();
        formPanel.add(oldPasswordField, gbc);

        gbc.gridy = row++;
        formPanel.add(createLabel("New Password"), gbc);
        gbc.gridy = row++;
        newPasswordField = createPasswordField();
        formPanel.add(newPasswordField, gbc);

        gbc.gridy = row++;
        formPanel.add(createLabel("Confirm New Password"), gbc);
        gbc.gridy = row++;
        confirmPasswordField = createPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton saveButton = new JButton("Save Profile");
        saveButton.setBackground(new Color(41, 128, 185));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setOpaque(true);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setPreferredSize(new Dimension(150, 35));
        saveButton.addActionListener(e -> handleSave());
        
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(127, 140, 141));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setOpaque(true);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.setPreferredSize(new Dimension(150, 35));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

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
        JTextField field = new JTextField(25);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(350, 30));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(25);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(350, 30));
        return field;
    }

    private void loadUserData() {
        User user = authService.getCurrentUser();
        fullNameField.setText(user.getFullName() != null ? user.getFullName() : "");
        emailField.setText(user.getEmail() != null ? user.getEmail() : "");
        departmentField.setText(user.getDepartment() != null ? user.getDepartment() : "");
    }

    private void handleSave() {
        User user = authService.getCurrentUser();
        
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String department = departmentField.getText().trim();
        
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        try {
            // Update profile info
            if (!fullName.isEmpty()) user.setFullName(fullName);
            if (!email.isEmpty()) {
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter a valid email address", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                user.setEmail(email);
            }
            user.setDepartment(department.isEmpty() ? null : department);
            
            // Update password if provided
            if (!oldPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty()) {
                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "All password fields are required to change password", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, 
                        "New passwords do not match", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (newPassword.length() < 6) {
                    JOptionPane.showMessageDialog(this, 
                        "Password must be at least 6 characters", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!authService.changePassword(oldPassword, newPassword)) {
                    JOptionPane.showMessageDialog(this, 
                        "Old password is incorrect", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            userService.updateUser(user);
            
            JOptionPane.showMessageDialog(this, 
                "Profile updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error updating profile: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
