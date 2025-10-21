package ui;

import util.ColorScheme;
import util.ModernButton;
import service.AuthService;
import javax.swing.*;
import java.awt.*;

/**
 * Modern login frame with gradient background and professional design
 */
public class ModernLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public ModernLoginFrame() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Bug Tracker - Sign In");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main container with split layout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        
        // Left side - Gradient panel with branding
        JPanel leftPanel = createBrandingPanel();
        mainPanel.add(leftPanel);
        
        // Right side - Login form
        JPanel rightPanel = createLoginFormPanel();
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
                
                // Diagonal gradient
                GradientPaint gp = new GradientPaint(
                    0, 0, ColorScheme.GRADIENT_START, 
                    w, h, ColorScheme.GRADIENT_END
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
        JLabel iconLabel = new JLabel("🐛");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(iconLabel);
        
        content.add(Box.createVerticalStrut(30));
        
        // Title
        JLabel titleLabel = new JLabel("Bug Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titleLabel);
        
        content.add(Box.createVerticalStrut(15));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Track, Manage & Resolve Bugs Efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 230));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(subtitleLabel);
        
        content.add(Box.createVerticalStrut(40));
        
        // Features
        String[] features = {
            "✓ Role-based Access Control",
            "✓ Priority Management",
            "✓ Real-time Dashboard",
            "✓ Team Collaboration"
        };
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            featureLabel.setForeground(new Color(255, 255, 255, 200));
            featureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(featureLabel);
            content.add(Box.createVerticalStrut(10));
        }
        
        panel.add(content, gbc);
        return panel;
    }
    
    private JPanel createLoginFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Form container
        JPanel formContainer = new JPanel();
        formContainer.setBackground(Color.WHITE);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(welcomeLabel);
        
        formContainer.add(Box.createVerticalStrut(8));
        
        JLabel subtitleLabel = new JLabel("Sign in to continue to Bug Tracker");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(subtitleLabel);
        
        formContainer.add(Box.createVerticalStrut(50));
        
        // Username section
        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        usernameLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(usernameLabel);
        
        formContainer.add(Box.createVerticalStrut(8));
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        usernameField.setMaximumSize(new Dimension(380, 48));
        usernameField.setPreferredSize(new Dimension(380, 48));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Focus listener for border color change
        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.PRIMARY, 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        formContainer.add(usernameField);
        
        formContainer.add(Box.createVerticalStrut(28));
        
        // Password section
        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passwordLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(passwordLabel);
        
        formContainer.add(Box.createVerticalStrut(8));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setEchoChar('●');
        passwordField.setMaximumSize(new Dimension(380, 48));
        passwordField.setPreferredSize(new Dimension(380, 48));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.addActionListener(e -> login());
        
        // Focus listener for border color change
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.PRIMARY, 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 2, true),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        formContainer.add(passwordField);
        
        formContainer.add(Box.createVerticalStrut(40));
        
        // Sign In button
        ModernButton signInButton = new ModernButton("Sign In");
        signInButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        signInButton.setMaximumSize(new Dimension(380, 50));
        signInButton.setPreferredSize(new Dimension(380, 50));
        signInButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInButton.addActionListener(e -> login());
        formContainer.add(signInButton);
        
        formContainer.add(Box.createVerticalStrut(20));
        
        // Divider with "OR"
        JPanel dividerPanel = new JPanel(new GridBagLayout());
        dividerPanel.setBackground(Color.WHITE);
        dividerPanel.setMaximumSize(new Dimension(380, 30));
        dividerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GridBagConstraints divGbc = new GridBagConstraints();
        divGbc.gridy = 0;
        divGbc.fill = GridBagConstraints.HORIZONTAL;
        divGbc.insets = new Insets(0, 0, 0, 10);
        
        JSeparator leftSep = new JSeparator();
        divGbc.gridx = 0;
        divGbc.weightx = 1;
        dividerPanel.add(leftSep, divGbc);
        
        JLabel orLabel = new JLabel("OR");
        orLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        orLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        divGbc.gridx = 1;
        divGbc.weightx = 0;
        divGbc.insets = new Insets(0, 10, 0, 10);
        dividerPanel.add(orLabel, divGbc);
        
        JSeparator rightSep = new JSeparator();
        divGbc.gridx = 2;
        divGbc.weightx = 1;
        divGbc.insets = new Insets(0, 10, 0, 0);
        dividerPanel.add(rightSep, divGbc);
        
        formContainer.add(dividerPanel);
        
        formContainer.add(Box.createVerticalStrut(20));
        
        // Create Account button
        ModernButton createAccountButton = new ModernButton("Create New Account", new Color(108, 117, 125));
        createAccountButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        createAccountButton.setMaximumSize(new Dimension(380, 50));
        createAccountButton.setPreferredSize(new Dimension(380, 50));
        createAccountButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createAccountButton.addActionListener(e -> showRegisterDialog());
        formContainer.add(createAccountButton);
        
        panel.add(formContainer, gbc);
        return panel;
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        System.out.println("Login attempt - Username: " + username);
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }
        
        boolean loginSuccess = AuthService.login(username, password);
        System.out.println("Login result: " + loginSuccess);
        
        if (loginSuccess) {
            System.out.println("Login successful! Current user: " + AuthService.getCurrentUser().getFullName());
            
            // Close login frame first
            dispose();
            
            // Open main application
            SwingUtilities.invokeLater(() -> {
                System.out.println("Creating ModernBugTrackerFrame...");
                try {
                    ModernBugTrackerFrame mainFrame = new ModernBugTrackerFrame();
                    mainFrame.setVisible(true);
                    System.out.println("ModernBugTrackerFrame displayed successfully.");
                } catch (Exception e) {
                    System.err.println("Error creating main frame: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } else {
            showError("Invalid username or password.");
            passwordField.setText("");
        }
    }
    
    private void showRegisterDialog() {
        ModernRegisterDialog dialog = new ModernRegisterDialog(this);
        dialog.setVisible(true);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
