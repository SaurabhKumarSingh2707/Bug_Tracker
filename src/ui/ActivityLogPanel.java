package ui;

import model.ActivityLog;
import service.ActivityLogService;
import util.ColorScheme;
import util.ModernButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Activity log panel to show all system activities
 */
public class ActivityLogPanel extends JPanel {
    private ActivityLogService activityLogService;
    private JTable activityTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> actionFilter;
    private JLabel countLabel;
    
    public ActivityLogPanel() {
        this.activityLogService = new ActivityLogService();
        setLayout(new BorderLayout(0, 0));
        setBackground(ColorScheme.BACKGROUND);
        
        initializeUI();
    }
    
    private void initializeUI() {
        // Main content panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with filters
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Activity table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Load initial data
        loadActivities();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 15));
        headerPanel.setBackground(ColorScheme.BACKGROUND);
        
        // Left side - Title and count
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(ColorScheme.BACKGROUND);
        
        JLabel titleLabel = new JLabel("🔔 Activity Log");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        countLabel = new JLabel("Loading activities...");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        countLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanel.add(titleLabel);
        leftPanel.add(countLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right side - Filter controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        filterPanel.setBackground(ColorScheme.BACKGROUND);
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        filterPanel.add(filterLabel);
        
        actionFilter = new JComboBox<>(new String[]{
            "All Activities", "User Login", "User Logout", "User Registered",
            "Bug Created", "Bug Updated", "Bug Deleted"
        });
        actionFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        actionFilter.setPreferredSize(new Dimension(180, 35));
        actionFilter.setBackground(Color.WHITE);
        actionFilter.addActionListener(e -> loadActivities());
        filterPanel.add(actionFilter);
        
        ModernButton refreshButton = new ModernButton("🔄 Refresh", ColorScheme.PRIMARY);
        refreshButton.setPreferredSize(new Dimension(120, 35));
        refreshButton.addActionListener(e -> {
            loadActivities();
            JOptionPane.showMessageDialog(this, "Activity log refreshed!", "Refreshed", JOptionPane.INFORMATION_MESSAGE);
        });
        filterPanel.add(refreshButton);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(ColorScheme.BACKGROUND);
        
        // Activity table
        String[] columns = {"Timestamp", "User", "Action", "Details", "Bug"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        activityTable = new JTable(tableModel);
        activityTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        activityTable.setRowHeight(45);
        activityTable.setShowGrid(true);
        activityTable.setGridColor(new Color(230, 230, 230));
        activityTable.setSelectionBackground(new Color(230, 240, 255));
        activityTable.setSelectionForeground(ColorScheme.TEXT_PRIMARY);
        activityTable.setIntercellSpacing(new Dimension(10, 5));
        
        // Table header
        JTableHeader header = activityTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(ColorScheme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Column widths
        activityTable.getColumnModel().getColumn(0).setPreferredWidth(180);  // Timestamp
        activityTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // User
        activityTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Action
        activityTable.getColumnModel().getColumn(3).setPreferredWidth(450);  // Details
        activityTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // Bug
        
        // Center align Bug ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        activityTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Custom renderer for action column
        activityTable.getColumnModel().getColumn(2).setCellRenderer(new ActionCellRenderer());
        
        // Scroll pane with styled border
        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadActivities() {
        tableModel.setRowCount(0);
        
        String selectedFilter = (String) actionFilter.getSelectedItem();
        String filter = convertFilterToAction(selectedFilter);
        
        List<ActivityLog> activities = activityLogService.getAllActivities();
        
        int displayCount = 0;
        for (ActivityLog activity : activities) {
            // Apply filter
            if (!"ALL".equals(filter) && !activity.getAction().equals(filter)) {
                continue;
            }
            
            tableModel.addRow(new Object[]{
                activity.getTimestamp(),
                activity.getUsername(),
                formatAction(activity.getAction()),
                activity.getDetails(),
                activity.getBugId() > 0 ? "#" + activity.getBugId() : "-"
            });
            displayCount++;
        }
        
        // Update count label
        if (displayCount == activities.size()) {
            countLabel.setText(String.format("Showing all %d activities", activities.size()));
        } else {
            countLabel.setText(String.format("Showing %d of %d activities", displayCount, activities.size()));
        }
        
        // Show message if no activities
        if (activities.isEmpty()) {
            countLabel.setText("No activities recorded yet. Start using the system to see activity logs!");
        }
    }
    
    private String convertFilterToAction(String displayName) {
        switch (displayName) {
            case "User Login": return "USER_LOGIN";
            case "User Logout": return "USER_LOGOUT";
            case "User Registered": return "USER_REGISTERED";
            case "Bug Created": return "BUG_CREATED";
            case "Bug Updated": return "BUG_UPDATED";
            case "Bug Deleted": return "BUG_DELETED";
            default: return "ALL";
        }
    }
    
    private String formatAction(String action) {
        switch (action) {
            case "USER_LOGIN": return "🔓 Login";
            case "USER_LOGOUT": return "🔒 Logout";
            case "USER_REGISTERED": return "👤 Registered";
            case "BUG_CREATED": return "➕ Created Bug";
            case "BUG_UPDATED": return "✏️ Updated Bug";
            case "BUG_DELETED": return "🗑️ Deleted Bug";
            default: return action;
        }
    }
    
    public void refresh() {
        loadActivities();
    }
    
    // Custom cell renderer for action column
    class ActionCellRenderer extends DefaultTableCellRenderer {
        public ActionCellRenderer() {
            setHorizontalAlignment(JLabel.LEFT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            
            if (!isSelected) {
                String action = value.toString();
                if (action.contains("Login") || action.contains("Registered")) {
                    setForeground(ColorScheme.SUCCESS);
                } else if (action.contains("Logout")) {
                    setForeground(ColorScheme.TEXT_SECONDARY);
                } else if (action.contains("Created")) {
                    setForeground(ColorScheme.INFO);
                } else if (action.contains("Updated")) {
                    setForeground(ColorScheme.WARNING);
                } else if (action.contains("Deleted")) {
                    setForeground(ColorScheme.ERROR);
                }
            } else {
                setForeground(table.getSelectionForeground());
            }
            
            return c;
        }
    }
}
