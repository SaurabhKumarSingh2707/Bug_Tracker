package ui;

import model.User;
import model.Bug;
import util.ColorScheme;
import util.ModernButton;
import util.CardPanel;
import service.BugService;
import service.AuthService;
import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

/**
 * Modern main application frame with dashboard
 */
public class ModernBugTrackerFrame extends JFrame {
    private BugService bugService;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public ModernBugTrackerFrame() {
        bugService = new BugService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Bug Tracker - Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logout();
            }
        });
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ColorScheme.BACKGROUND);
        
        // Add panels
        contentPanel.add(new DashboardPanel(bugService), "dashboard");
        contentPanel.add(new BugListPanel(bugService, this), "bugs");
        contentPanel.add(new AddBugPanel(bugService, this), "add");
        contentPanel.add(new SearchPanel(bugService), "search");
        contentPanel.add(new ActivityLogPanel(), "activity");
        contentPanel.add(new ReportsPanel(bugService), "reports");
        contentPanel.add(new StatisticsPanel(bugService), "stats");
        
        if (AuthService.getCurrentUser().getUserType() == User.UserType.ADMIN || 
            AuthService.getCurrentUser().getUserType() == User.UserType.MANAGER) {
            contentPanel.add(new UserManagementPanel(), "users");
        }
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Show dashboard by default
        showPanel("dashboard");
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(new Color(45, 52, 71));
        sidebar.setLayout(new BorderLayout());
        
        // Header
        JPanel header = createSidebarHeader();
        sidebar.add(header, BorderLayout.NORTH);
        
        // Menu
        JPanel menu = createSidebarMenu();
        sidebar.add(menu, BorderLayout.CENTER);
        
        // Footer
        JPanel footer = createSidebarFooter();
        sidebar.add(footer, BorderLayout.SOUTH);
        
        return sidebar;
    }
    
    private JPanel createSidebarHeader() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(37, 43, 58));
        panel.setPreferredSize(new Dimension(250, 100));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel iconLabel = new JLabel("🐛", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        panel.add(iconLabel, BorderLayout.NORTH);
        
        JLabel titleLabel = new JLabel("Bug Tracker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSidebarMenu() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 52, 71));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        addMenuItem(panel, "📊 Dashboard", "dashboard");
        addMenuItem(panel, "📋 Bug List", "bugs");
        addMenuItem(panel, "➕ Add Bug", "add");
        addMenuItem(panel, "� Search", "search");
        addMenuItem(panel, "🔔 Activity Log", "activity");
        addMenuItem(panel, "�📈 Reports", "reports");
        addMenuItem(panel, "📊 Statistics", "stats");
        
        User currentUser = AuthService.getCurrentUser();
        if (currentUser.getUserType() == User.UserType.ADMIN || 
            currentUser.getUserType() == User.UserType.MANAGER) {
            addMenuItem(panel, "👥 Users", "users");
        }
        
        return panel;
    }
    
    private void addMenuItem(JPanel panel, String text, String panelName) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(230, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 52, 71));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setOpaque(true);
                button.setContentAreaFilled(true);
                button.setBackground(ColorScheme.PRIMARY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(false);
                button.setBackground(new Color(45, 52, 71));
            }
        });
        
        button.addActionListener(e -> showPanel(panelName));
        
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private JPanel createSidebarFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(37, 43, 58));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        User currentUser = AuthService.getCurrentUser();
        
        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(new Color(37, 43, 58));
        
        JLabel nameLabel = new JLabel(currentUser.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel roleLabel = new JLabel(currentUser.getUserType().toString());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(getUserTypeColor(currentUser.getUserType()));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        userInfo.add(nameLabel);
        userInfo.add(roleLabel);
        
        panel.add(userInfo, BorderLayout.CENTER);
        
        ModernButton logoutBtn = new ModernButton("Logout", ColorScheme.ERROR);
        logoutBtn.setPreferredSize(new Dimension(220, 35));
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.addActionListener(e -> logout());
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(37, 43, 58));
        btnPanel.add(logoutBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Color getUserTypeColor(User.UserType userType) {
        switch (userType) {
            case ADMIN: return ColorScheme.ADMIN_COLOR;
            case MANAGER: return ColorScheme.MANAGER_COLOR;
            case DEVELOPER: return ColorScheme.DEVELOPER_COLOR;
            case TESTER: return ColorScheme.TESTER_COLOR;
            default: return Color.WHITE;
        }
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        
        // Refresh panel data
        Component[] components = contentPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof BugListPanel && panelName.equals("bugs")) {
                ((BugListPanel) comp).refreshBugList();
            } else if (comp instanceof StatisticsPanel && panelName.equals("stats")) {
                ((StatisticsPanel) comp).refreshStatistics();
            } else if (comp instanceof UserManagementPanel && panelName.equals("users")) {
                ((UserManagementPanel) comp).refreshUserList();
            } else if (comp instanceof DashboardPanel && panelName.equals("dashboard")) {
                ((DashboardPanel) comp).refreshDashboard();
            }
        }
    }
    
    public void refreshAllPanels() {
        showPanel("bugs");
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
            
            SwingUtilities.invokeLater(() -> {
                ModernLoginFrame loginFrame = new ModernLoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }
}
