package ui;

import model.User;
import service.BugService;
import service.AuthService;
import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

/**
 * Main application frame for the Bug Tracker
 */
public class BugTrackerFrame extends JFrame {
    private BugService bugService;
    private JTabbedPane tabbedPane;
    private BugListPanel bugListPanel;
    private AddBugPanel addBugPanel;
    private StatisticsPanel statisticsPanel;
    private UserManagementPanel userManagementPanel;
    
    public BugTrackerFrame() {
        bugService = new BugService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Bug Tracker - Welcome " + AuthService.getCurrentUser().getFullName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logout();
            }
        });
        setLocationRelativeTo(null);
        
        // Top panel with user info and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        User currentUser = AuthService.getCurrentUser();
        JLabel userLabel = new JLabel("  Logged in as: " + currentUser.getFullName() + 
                                      " (" + currentUser.getUsername() + ") - " + currentUser.getUserType());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Color code by user type
        switch (currentUser.getUserType()) {
            case ADMIN:
                userLabel.setForeground(new Color(220, 20, 60)); // Crimson
                break;
            case MANAGER:
                userLabel.setForeground(new Color(30, 144, 255)); // DodgerBlue
                break;
            case DEVELOPER:
                userLabel.setForeground(new Color(34, 139, 34)); // ForestGreen
                break;
            case TESTER:
                userLabel.setForeground(new Color(255, 140, 0)); // DarkOrange
                break;
        }
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(logoutButton);
        
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        add(topPanel, BorderLayout.NORTH);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Initialize panels
        bugListPanel = new BugListPanel(bugService, this);
        addBugPanel = new AddBugPanel(bugService, this);
        statisticsPanel = new StatisticsPanel(bugService);
        
        // Add tabs
        tabbedPane.addTab("Bug List", new ImageIcon(), bugListPanel, "View and manage bugs");
        tabbedPane.addTab("Add New Bug", new ImageIcon(), addBugPanel, "Create a new bug report");
        tabbedPane.addTab("Statistics", new ImageIcon(), statisticsPanel, "View bug statistics");
        
        // Add User Management tab only for Admin and Manager
        if (AuthService.getCurrentUser().getUserType() == User.UserType.ADMIN || 
            AuthService.getCurrentUser().getUserType() == User.UserType.MANAGER) {
            userManagementPanel = new UserManagementPanel();
            tabbedPane.addTab("User Management", new ImageIcon(), userManagementPanel, "View registered users");
        }
        
        // Add change listener to refresh panels when tab changes
        tabbedPane.addChangeListener(e -> {
            Component selectedComponent = tabbedPane.getSelectedComponent();
            if (selectedComponent == bugListPanel) {
                bugListPanel.refreshBugList();
            } else if (selectedComponent == statisticsPanel) {
                statisticsPanel.refreshStatistics();
            } else if (selectedComponent == userManagementPanel && userManagementPanel != null) {
                userManagementPanel.refreshUserList();
            }
        });
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            AuthService.logout();
            DatabaseManager.closeConnection();
            dispose();
            
            // Show login frame
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }
    
    // Method to switch to bug list tab
    public void switchToBugListTab() {
        tabbedPane.setSelectedIndex(0);
        bugListPanel.refreshBugList();
    }
    
    // Method to refresh all panels
    public void refreshAllPanels() {
        bugListPanel.refreshBugList();
        statisticsPanel.refreshStatistics();
    }
}
