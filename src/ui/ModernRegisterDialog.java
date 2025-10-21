package ui;

import util.ColorScheme;
import util.ModernButton;
import model.User;
import service.AuthService;
import database.UserDAO;
import javax.swing.*;
import java.awt.*;

/**
 * Modern registration dialog with split-screen design
 */
public class ModernRegisterDialog extends JDialog {
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<User.UserType> userTypeComboBox;
    
    public ModernRegisterDialog(JFrame parent) {
        super(parent, "Create Account", true);
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(1100, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Main container with split layout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        
        // Left side - Branding panel
        JPanel leftPanel = createBrandingPanel();
        mainPanel.add(leftPanel);
        
        // Right side - Registration form
        JPanel rightPanel = createRegistrationFormPanel();
        mainPanel.add(rightPanel);
        
        add(mainPanel);
    }
    
    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Diagonal gradient - opposite direction from login
                GradientPaint gp = new GradientPaint(
                    w, 0, ColorScheme.GRADIENT_END,
                    0, h, ColorScheme.GRADIENT_START
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        // Content container
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        // Icon
        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(iconLabel);
        
        content.add(Box.createVerticalStrut(30));
        
        // Title
        JLabel titleLabel = new JLabel("Join Our Team");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titleLabel);
        
        content.add(Box.createVerticalStrut(15));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Start Managing Bugs Like a Pro");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 230));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(subtitleLabel);
        
        content.add(Box.createVerticalStrut(40));
        
        // Benefits
        String[] benefits = {
            "🚀 Get Started in Seconds",
            "🔒 Secure & Private",
            "👨‍💼 Role-Based Access",
            "📊 Powerful Analytics",
            "🤝 Team Collaboration"
        };
        
        for (String benefit : benefits) {
            JLabel benefitLabel = new JLabel(benefit);
            benefitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            benefitLabel.setForeground(new Color(255, 255, 255, 200));
            benefitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(benefitLabel);
            content.add(Box.createVerticalStrut(10));
        }
        
        panel.add(content, gbc);
        return panel;
    }
    
    private JPanel createRegistrationFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        // Form container
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.weightx = 1.0;
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.insets = new Insets(0, 0, 8, 0);
        
        // Header
        JLabel headerLabel = new JLabel("Create Your Account");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        formContainer.add(headerLabel, formGbc);
        
        formGbc.gridy++;
        formGbc.insets = new Insets(0, 0, 35, 0);
        
        JLabel subtitleLabel = new JLabel("Fill in your details to get started");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        formContainer.add(subtitleLabel, formGbc);
        
        formGbc.gridy++;
        formGbc.insets = new Insets(0, 0, 20, 0);
        
        // Full Name
        formContainer.add(createFieldPanel("FULL NAME", fullNameField = new JTextField()), formGbc);
        formGbc.gridy++;
        
        // Username
        formContainer.add(createFieldPanel("USERNAME", usernameField = new JTextField()), formGbc);
        formGbc.gridy++;
        
        // Email
        formContainer.add(createFieldPanel("EMAIL ADDRESS", emailField = new JTextField()), formGbc);
        formGbc.gridy++;
        
        // Password
        formContainer.add(createPasswordPanel("PASSWORD", passwordField = new JPasswordField()), formGbc);
        formGbc.gridy++;
        
        // Confirm Password
        formContainer.add(createPasswordPanel("CONFIRM PASSWORD", confirmPasswordField = new JPasswordField()), formGbc);
        formGbc.gridy++;
        
        // User Type
        formContainer.add(createComboPanel(), formGbc);
        formGbc.gridy++;
        
        // Buttons
        formGbc.insets = new Insets(15, 0, 0, 0);
        
        // Create Account button
        ModernButton createButton = new ModernButton("Create Account", ColorScheme.SUCCESS);
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        createButton.setPreferredSize(new Dimension(420, 50));
        createButton.addActionListener(e -> register());
        formContainer.add(createButton, formGbc);
        
        formGbc.gridy++;
        formGbc.insets = new Insets(15, 0, 0, 0);
        
        // Cancel button
        ModernButton cancelButton = new ModernButton("Cancel", new Color(108, 117, 125));
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cancelButton.setPreferredSize(new Dimension(420, 50));
        cancelButton.addActionListener(e -> dispose());
        formContainer.add(cancelButton, formGbc);
        
        // Add spacer at bottom
        formGbc.gridy++;
        formGbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), formGbc);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, gbc);
        return panel;
    }
    
    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(ColorScheme.TEXT_SECONDARY);
        panel.add(label, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(420, 48));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        // Focus listener
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.PRIMARY, 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        panel.add(field, gbc);
        return panel;
    }
    
    private JPanel createPasswordPanel(String labelText, JPasswordField field) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(ColorScheme.TEXT_SECONDARY);
        panel.add(label, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setEchoChar('●');
        field.setPreferredSize(new Dimension(420, 48));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        // Focus listener
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.PRIMARY, 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        panel.add(field, gbc);
        return panel;
    }
    
    private JPanel createComboPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);
        
        JLabel label = new JLabel("USER TYPE");
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(ColorScheme.TEXT_SECONDARY);
        panel.add(label, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        userTypeComboBox = new JComboBox<>(User.UserType.values());
        userTypeComboBox.setSelectedItem(User.UserType.DEVELOPER);
        userTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userTypeComboBox.setPreferredSize(new Dimension(420, 48));
        userTypeComboBox.setBackground(Color.WHITE);
        userTypeComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        panel.add(userTypeComboBox, gbc);
        return panel;
    }
    
    private void register() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        User.UserType userType = (User.UserType) userTypeComboBox.getSelectedItem();
        
        // Debug output
        System.out.println("Registration attempt:");
        System.out.println("Full Name: " + fullName);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password length: " + password.length());
        System.out.println("User Type: " + userType);
        
        // Validation
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }
        
        if (!email.contains("@")) {
            showError("Please enter a valid email address.");
            return;
        }
        
        if (UserDAO.usernameExists(username)) {
            showError("Username already exists. Please choose another.");
            return;
        }
        
        if (UserDAO.emailExists(email)) {
            showError("Email already registered. Please use another email.");
            return;
        }
        
        // Register user
        System.out.println("Calling AuthService.register...");
        boolean result = AuthService.register(username, email, password, fullName, userType);
        System.out.println("Registration result: " + result);
        
        if (result) {
            showSuccess("Account created successfully! You can now sign in.");
            dispose();
        } else {
            showError("Registration failed. Database error occurred. Please check console for details.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
