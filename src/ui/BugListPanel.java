package ui;

import model.Bug;
import service.BugService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for displaying and managing the list of bugs
 */
public class BugListPanel extends JPanel {
    private BugService bugService;
    private JFrame parentFrame;
    private JTable bugTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    
    public BugListPanel(BugService bugService, JFrame parentFrame) {
        this.bugService = bugService;
        this.parentFrame = parentFrame;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel with search and filter
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel with table
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel with buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Load initial data
        refreshBugList();
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Search and Filter"));
        
        // Search field
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchButton.addActionListener(e -> performSearch());
        
        // Filter combo box
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        String[] filterOptions = {"All Bugs", "New", "In Progress", "Resolved", "Closed", 
                                  "Priority: Critical", "Priority: High", "Priority: Medium", "Priority: Low"};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterComboBox.setPreferredSize(new Dimension(180, 30));
        filterComboBox.addActionListener(e -> applyFilter());
        
        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resetButton.addActionListener(e -> {
            searchField.setText("");
            filterComboBox.setSelectedIndex(0);
            refreshBugList();
        });
        
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(filterLabel);
        panel.add(filterComboBox);
        panel.add(resetButton);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create table
        String[] columnNames = {"ID", "Title", "Priority", "Status", "Assigned To", "Created Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bugTable = new JTable(tableModel);
        bugTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bugTable.setRowHeight(25);
        bugTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        bugTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bugTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        bugTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        bugTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        bugTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        bugTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(bugTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Bug List"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewBugDetails());
        
        JButton editButton = new JButton("Edit Bug");
        editButton.addActionListener(e -> editBug());
        
        JButton deleteButton = new JButton("Delete Bug");
        deleteButton.addActionListener(e -> deleteBug());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshBugList());
        
        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        
        return panel;
    }
    
    public void refreshBugList() {
        List<Bug> bugs = bugService.getAllBugs();
        updateTable(bugs);
    }
    
    private void updateTable(List<Bug> bugs) {
        tableModel.setRowCount(0);
        for (Bug bug : bugs) {
            Object[] row = {
                bug.getId(),
                bug.getTitle(),
                bug.getPriority(),
                bug.getStatus(),
                bug.getAssignedTo(),
                bug.getFormattedCreatedDate()
            };
            tableModel.addRow(row);
        }
    }
    
    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            refreshBugList();
        } else {
            List<Bug> searchResults = bugService.searchBugs(keyword);
            updateTable(searchResults);
        }
    }
    
    private void applyFilter() {
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        List<Bug> filteredBugs;
        
        switch (selectedFilter) {
            case "New":
                filteredBugs = bugService.filterByStatus(Bug.Status.NEW);
                break;
            case "In Progress":
                filteredBugs = bugService.filterByStatus(Bug.Status.IN_PROGRESS);
                break;
            case "Resolved":
                filteredBugs = bugService.filterByStatus(Bug.Status.RESOLVED);
                break;
            case "Closed":
                filteredBugs = bugService.filterByStatus(Bug.Status.CLOSED);
                break;
            case "Priority: Critical":
                filteredBugs = bugService.filterByPriority(Bug.Priority.CRITICAL);
                break;
            case "Priority: High":
                filteredBugs = bugService.filterByPriority(Bug.Priority.HIGH);
                break;
            case "Priority: Medium":
                filteredBugs = bugService.filterByPriority(Bug.Priority.MEDIUM);
                break;
            case "Priority: Low":
                filteredBugs = bugService.filterByPriority(Bug.Priority.LOW);
                break;
            default:
                filteredBugs = bugService.getAllBugs();
        }
        
        updateTable(filteredBugs);
    }
    
    private void viewBugDetails() {
        int selectedRow = bugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bug to view.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int bugId = (int) tableModel.getValueAt(selectedRow, 0);
        Bug bug = bugService.getBugById(bugId);
        
        if (bug != null) {
            BugDetailsDialog dialog = new BugDetailsDialog(parentFrame, bug);
            dialog.setVisible(true);
        }
    }
    
    private void editBug() {
        int selectedRow = bugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bug to edit.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int bugId = (int) tableModel.getValueAt(selectedRow, 0);
        Bug bug = bugService.getBugById(bugId);
        
        if (bug != null) {
            EditBugDialog dialog = new EditBugDialog(parentFrame, bugService, bug);
            dialog.setVisible(true);
            refreshBugList();
            if (parentFrame instanceof BugTrackerFrame) {
                ((BugTrackerFrame) parentFrame).refreshAllPanels();
            } else if (parentFrame instanceof ModernBugTrackerFrame) {
                ((ModernBugTrackerFrame) parentFrame).refreshAllPanels();
            }
        }
    }
    
    private void deleteBug() {
        int selectedRow = bugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bug to delete.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int bugId = (int) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this bug?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bugService.deleteBug(bugId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Bug deleted successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshBugList();
                if (parentFrame instanceof BugTrackerFrame) {
                    ((BugTrackerFrame) parentFrame).refreshAllPanels();
                } else if (parentFrame instanceof ModernBugTrackerFrame) {
                    ((ModernBugTrackerFrame) parentFrame).refreshAllPanels();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bug.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
