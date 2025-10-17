package com.bugtracker.view;

import com.bugtracker.model.*;
import com.bugtracker.service.AuthService;
import com.bugtracker.service.BugService;
import com.bugtracker.service.UserService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main application frame with bug management
 */
public class MainFrame extends JFrame {
    private AuthService authService;
    private UserService userService;
    private BugService bugService;
    
    private JTable bugTable;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;
    private JLabel statsLabel;
    private JTextField searchField;

    public MainFrame(AuthService authService, UserService userService, BugService bugService) {
        this.authService = authService;
        this.userService = userService;
        this.bugService = bugService;
        
        initComponents();
        refreshBugList();
        updateStatistics();
    }

    private void initComponents() {
        setTitle("Bug Tracker - " + authService.getCurrentUser().getDisplayName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top panel
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);

        // Center panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());
        splitPane.setDividerLocation(750);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Title
        JLabel titleLabel = new JLabel("🐛 Bug Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        // User info and buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(44, 62, 80));

        User currentUser = authService.getCurrentUser();
        JLabel userLabel = new JLabel("👤 " + currentUser.getDisplayName() + 
                " (" + currentUser.getRole() + ")");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(new Color(236, 240, 241));

        JButton profileButton = createButton("Profile", new Color(52, 152, 219));
        profileButton.addActionListener(e -> showProfileDialog());

        JButton logoutButton = createButton("Logout", new Color(231, 76, 60));
        logoutButton.addActionListener(e -> handleLogout());

        rightPanel.add(userLabel);
        rightPanel.add(profileButton);
        rightPanel.add(logoutButton);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        JButton newBugButton = createButton("➕ New Bug", new Color(46, 204, 113));
        newBugButton.addActionListener(e -> showCreateBugDialog());
        
        JButton refreshButton = createButton("🔄 Refresh", new Color(52, 152, 219));
        refreshButton.addActionListener(e -> refreshBugList());
        
        JButton myBugsButton = createButton("📋 My Bugs", new Color(155, 89, 182));
        myBugsButton.addActionListener(e -> showMyBugs());
        
        JButton assignedButton = createButton("✓ Assigned to Me", new Color(243, 156, 18));
        assignedButton.addActionListener(e -> showAssignedBugs());
        
        toolbarPanel.add(newBugButton);
        toolbarPanel.add(refreshButton);
        toolbarPanel.add(myBugsButton);
        toolbarPanel.add(assignedButton);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addActionListener(e -> searchBugs());
        
        JButton searchButton = createButton("🔍 Search", new Color(52, 152, 219));
        searchButton.addActionListener(e -> searchBugs());
        
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Statistics
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Bug table
        String[] columns = {"ID", "Title", "Status", "Priority", "Severity", "Assigned To"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bugTable = new JTable(tableModel);
        bugTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bugTable.setRowHeight(25);
        bugTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showBugDetails();
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(bugTable);

        // Assemble left panel
        JPanel topSection = new JPanel(new BorderLayout(5, 5));
        topSection.add(toolbarPanel, BorderLayout.NORTH);
        topSection.add(searchPanel, BorderLayout.CENTER);
        topSection.add(statsLabel, BorderLayout.SOUTH);

        leftPanel.add(topSection, BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        rightPanel.setBackground(new Color(236, 240, 241));

        // Title
        JLabel detailsLabel = new JLabel("Bug Details");
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Details area
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Select a bug to view details");
        
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);

        // Action buttons
        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JButton editButton = createButton("✏️ Edit Bug", new Color(243, 156, 18));
        editButton.addActionListener(e -> editSelectedBug());
        
        JButton commentButton = createButton("💬 Add Comment", new Color(52, 152, 219));
        commentButton.addActionListener(e -> addCommentDialog());
        
        JButton deleteButton = createButton("🗑️ Delete Bug", new Color(231, 76, 60));
        deleteButton.addActionListener(e -> deleteSelectedBug());
        
        actionPanel.add(editButton);
        actionPanel.add(commentButton);
        actionPanel.add(deleteButton);

        rightPanel.add(detailsLabel, BorderLayout.NORTH);
        rightPanel.add(detailsScrollPane, BorderLayout.CENTER);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void refreshBugList() {
        List<Bug> bugs = bugService.getAllBugs();
        updateTable(bugs);
        updateStatistics();
    }

    private void updateTable(List<Bug> bugs) {
        tableModel.setRowCount(0);
        for (Bug bug : bugs) {
            String assignedTo = "Unassigned";
            if (bug.getAssignedTo() != null) {
                userService.getUserById(bug.getAssignedTo()).ifPresent(
                    u -> {}
                );
                User user = userService.getUserById(bug.getAssignedTo()).orElse(null);
                if (user != null) assignedTo = user.getDisplayName();
            }
            
            tableModel.addRow(new Object[]{
                bug.getBugId(),
                bug.getTitle(),
                bug.getStatus(),
                bug.getPriority(),
                bug.getSeverity(),
                assignedTo
            });
        }
    }

    private void showBugDetails() {
        int selectedRow = bugTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        String bugId = (String) tableModel.getValueAt(selectedRow, 0);
        bugService.getBugById(bugId).ifPresent(bug -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            StringBuilder details = new StringBuilder();
            
            details.append("BUG ID: ").append(bug.getBugId()).append("\n");
            details.append("═══════════════════════════════════════\n\n");
            details.append("TITLE: ").append(bug.getTitle()).append("\n\n");
            details.append("DESCRIPTION:\n").append(bug.getDescription()).append("\n\n");
            details.append("STATUS: ").append(bug.getStatus()).append("\n");
            details.append("PRIORITY: ").append(bug.getPriority()).append("\n");
            details.append("SEVERITY: ").append(bug.getSeverity()).append("\n\n");
            
            if (bug.getProjectName() != null) {
                details.append("PROJECT: ").append(bug.getProjectName()).append("\n");
            }
            
            details.append("REPORTED BY: ");
            userService.getUserById(bug.getReportedBy()).ifPresent(
                u -> details.append(u.getDisplayName()));
            
            details.append("\nASSIGNED TO: ");
            if (bug.getAssignedTo() != null) {
                userService.getUserById(bug.getAssignedTo()).ifPresent(
                    u -> details.append(u.getDisplayName()));
            } else {
                details.append("Unassigned");
            }
            
            details.append("\n\nCREATED: ").append(bug.getCreatedAt().format(formatter));
            details.append("\nUPDATED: ").append(bug.getUpdatedAt().format(formatter));
            
            if (!bug.getComments().isEmpty()) {
                details.append("\n\n───── COMMENTS ─────\n");
                for (Comment comment : bug.getComments()) {
                    details.append("\n[").append(comment.getUsername()).append("] ");
                    details.append(comment.getCreatedAt().format(formatter)).append("\n");
                    details.append(comment.getContent()).append("\n");
                }
            }
            
            detailsArea.setText(details.toString());
            detailsArea.setCaretPosition(0);
        });
    }

    private void updateStatistics() {
        BugService.BugStatistics stats = bugService.getStatistics();
        statsLabel.setText(String.format(
            "📊 Total: %d | 🆕 New: %d | 📂 Open: %d | ⏳ In Progress: %d | ✅ Resolved: %d | 🔴 Critical: %d | 🟠 High: %d",
            stats.total, stats.newBugs, stats.open, stats.inProgress, 
            stats.resolved, stats.critical, stats.high
        ));
    }

    private void showMyBugs() {
        String userId = authService.getCurrentUser().getUserId();
        List<Bug> myBugs = bugService.getBugsReportedBy(userId);
        updateTable(myBugs);
    }

    private void showAssignedBugs() {
        String userId = authService.getCurrentUser().getUserId();
        List<Bug> assignedBugs = bugService.getBugsAssignedTo(userId);
        updateTable(assignedBugs);
    }

    private void searchBugs() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshBugList();
        } else {
            List<Bug> results = bugService.searchBugs(query);
            updateTable(results);
        }
    }

    private void showCreateBugDialog() {
        new CreateBugDialog(this, authService, userService, bugService).setVisible(true);
    }

    private void editSelectedBug() {
        int selectedRow = bugTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bug to edit", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bugId = (String) tableModel.getValueAt(selectedRow, 0);
        bugService.getBugById(bugId).ifPresent(bug -> {
            new EditBugDialog(this, authService, userService, bugService, bug).setVisible(true);
        });
    }

    private void addCommentDialog() {
        int selectedRow = bugTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bug", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bugId = (String) tableModel.getValueAt(selectedRow, 0);
        String comment = JOptionPane.showInputDialog(this, "Enter your comment:", 
            "Add Comment", JOptionPane.PLAIN_MESSAGE);
        
        if (comment != null && !comment.trim().isEmpty()) {
            User currentUser = authService.getCurrentUser();
            Comment newComment = new Comment(null, bugId, currentUser.getUserId(), 
                currentUser.getDisplayName(), comment.trim());
            bugService.addComment(bugId, newComment);
            showBugDetails();
            JOptionPane.showMessageDialog(this, "Comment added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedBug() {
        int selectedRow = bugTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a bug to delete", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String bugId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete bug " + bugId + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            bugService.deleteBug(bugId);
            refreshBugList();
            detailsArea.setText("Select a bug to view details");
        }
    }

    private void showProfileDialog() {
        new ProfileDialog(this, authService, userService).setVisible(true);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            authService.logout();
            new LoginFrame(authService, userService, bugService).setVisible(true);
            dispose();
        }
    }

    public void refreshData() {
        refreshBugList();
        showBugDetails();
    }
}
