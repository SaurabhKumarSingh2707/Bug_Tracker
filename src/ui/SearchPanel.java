package ui;

import model.Bug;
import model.Bug.Priority;
import model.Bug.Status;
import service.BugService;
import util.ColorScheme;
import util.ModernButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Advanced search and filter panel
 */
public class SearchPanel extends JPanel {
    private BugService bugService;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JComboBox<String> priorityFilter;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    
    public SearchPanel(BugService bugService) {
        this.bugService = bugService;
        setLayout(new BorderLayout(15, 15));
        setBackground(ColorScheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createResultsPanel(), BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(ColorScheme.BACKGROUND);
        
        // Title
        JLabel titleLabel = new JLabel("🔍 Advanced Search & Filter");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Search and filter controls
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Search field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlsPanel.add(searchLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        gbc.gridwidth = 3;
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        controlsPanel.add(searchField, gbc);
        
        // Status filter
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0; gbc.gridwidth = 1;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlsPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        statusFilter = new JComboBox<>(new String[]{"ALL", "NEW", "IN_PROGRESS", "RESOLVED", "CLOSED", "REOPENED"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlsPanel.add(statusFilter, gbc);
        
        // Priority filter
        gbc.gridx = 2;
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlsPanel.add(priorityLabel, gbc);
        
        gbc.gridx = 3;
        priorityFilter = new JComboBox<>(new String[]{"ALL", "CRITICAL", "HIGH", "MEDIUM", "LOW"});
        priorityFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlsPanel.add(priorityFilter, gbc);
        
        // Search button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        ModernButton searchButton = new ModernButton("Search", ColorScheme.PRIMARY);
        searchButton.addActionListener(e -> performSearch());
        buttonPanel.add(searchButton);
        
        ModernButton clearButton = new ModernButton("Clear Filters", ColorScheme.SECONDARY);
        clearButton.addActionListener(e -> clearFilters());
        buttonPanel.add(clearButton);
        
        controlsPanel.add(buttonPanel, gbc);
        
        panel.add(controlsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(ColorScheme.BACKGROUND);
        
        JLabel resultsLabel = new JLabel("Search Results");
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultsLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        panel.add(resultsLabel, BorderLayout.NORTH);
        
        // Results table
        String[] columns = {"ID", "Title", "Priority", "Status", "Assigned To", "Created By", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultsTable.setRowHeight(35);
        resultsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        resultsTable.getTableHeader().setBackground(ColorScheme.PRIMARY);
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        resultsTable.setSelectionBackground(new Color(230, 240, 255));
        
        // Double-click to view details
        resultsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = resultsTable.getSelectedRow();
                    if (row != -1) {
                        int bugId = (int) tableModel.getValueAt(row, 0);
                        showBugDetails(bugId);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Load all bugs initially
        performSearch();
        
        return panel;
    }
    
    private void performSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        String statusValue = (String) statusFilter.getSelectedItem();
        String priorityValue = (String) priorityFilter.getSelectedItem();
        
        List<Bug> allBugs = bugService.getAllBugs();
        
        // Apply filters
        List<Bug> filteredBugs = allBugs.stream()
            .filter(bug -> {
                // Keyword filter
                if (!keyword.isEmpty()) {
                    return bug.getTitle().toLowerCase().contains(keyword) ||
                           bug.getDescription().toLowerCase().contains(keyword) ||
                           bug.getAssignedTo().toLowerCase().contains(keyword);
                }
                return true;
            })
            .filter(bug -> {
                // Status filter
                if (!"ALL".equals(statusValue)) {
                    return bug.getStatus().toString().equals(statusValue);
                }
                return true;
            })
            .filter(bug -> {
                // Priority filter
                if (!"ALL".equals(priorityValue)) {
                    return bug.getPriority().toString().equals(priorityValue);
                }
                return true;
            })
            .collect(Collectors.toList());
        
        // Update table
        tableModel.setRowCount(0);
        for (Bug bug : filteredBugs) {
            tableModel.addRow(new Object[]{
                bug.getId(),
                bug.getTitle(),
                bug.getPriority(),
                bug.getStatus(),
                bug.getAssignedTo(),
                "N/A",  // Created by placeholder
                bug.getCreatedDate()
            });
        }
    }
    
    private void clearFilters() {
        searchField.setText("");
        statusFilter.setSelectedIndex(0);
        priorityFilter.setSelectedIndex(0);
        performSearch();
    }
    
    private void showBugDetails(int bugId) {
        Bug bug = bugService.getBugById(bugId);
        if (bug != null) {
            String details = String.format(
                "Bug #%d: %s\n\n" +
                "Description: %s\n\n" +
                "Priority: %s\n" +
                "Status: %s\n" +
                "Assigned To: %s\n" +
                "Created: %s\n" +
                "Updated: %s",
                bug.getId(), bug.getTitle(),
                bug.getDescription(),
                bug.getPriority(), bug.getStatus(),
                bug.getAssignedTo(),
                bug.getCreatedDate(), bug.getUpdatedDate()
            );
            
            JOptionPane.showMessageDialog(this, details, "Bug Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void refresh() {
        performSearch();
    }
}
