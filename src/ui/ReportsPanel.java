package ui;

import model.Bug;
import model.Bug.Priority;
import model.Bug.Status;
import service.BugService;
import util.ColorScheme;
import util.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reports and analytics panel
 */
public class ReportsPanel extends JPanel {
    private BugService bugService;
    
    public ReportsPanel(BugService bugService) {
        this.bugService = bugService;
        setLayout(new BorderLayout(15, 15));
        setBackground(ColorScheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initializeUI();
    }
    
    private void initializeUI() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorScheme.BACKGROUND);
        
        JLabel titleLabel = new JLabel("📈 Reports & Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        contentPanel.setBackground(ColorScheme.BACKGROUND);
        
        contentPanel.add(createStatisticsPanel());
        contentPanel.add(createExportPanel());
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("📊 Bug Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        statsArea.setBackground(new Color(250, 250, 250));
        statsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        updateStatistics(statsArea);
        
        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateStatistics(JTextArea statsArea) {
        List<Bug> allBugs = bugService.getAllBugs();
        
        // Count by status
        Map<Status, Long> byStatus = allBugs.stream()
            .collect(Collectors.groupingBy(Bug::getStatus, Collectors.counting()));
        
        // Count by priority
        Map<Priority, Long> byPriority = allBugs.stream()
            .collect(Collectors.groupingBy(Bug::getPriority, Collectors.counting()));
        
        // Count by assignee
        Map<String, Long> byAssignee = allBugs.stream()
            .collect(Collectors.groupingBy(Bug::getAssignedTo, Collectors.counting()));
        
        StringBuilder stats = new StringBuilder();
        stats.append("═══════════════════════════════════════════════════════════════\n");
        stats.append("                     BUG TRACKER STATISTICS                     \n");
        stats.append("═══════════════════════════════════════════════════════════════\n\n");
        
        stats.append("TOTAL BUGS: ").append(allBugs.size()).append("\n\n");
        
        stats.append("─── BY STATUS ───\n");
        for (Status status : Status.values()) {
            long count = byStatus.getOrDefault(status, 0L);
            stats.append(String.format("  %-15s : %d\n", status, count));
        }
        
        stats.append("\n─── BY PRIORITY ───\n");
        for (Priority priority : Priority.values()) {
            long count = byPriority.getOrDefault(priority, 0L);
            stats.append(String.format("  %-15s : %d\n", priority, count));
        }
        
        stats.append("\n─── TOP ASSIGNEES ───\n");
        byAssignee.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .limit(5)
            .forEach(entry -> stats.append(String.format("  %-20s : %d bugs\n", 
                entry.getKey(), entry.getValue())));
        
        stats.append("\n─── COMPLETION RATE ───\n");
        long resolved = byStatus.getOrDefault(Status.RESOLVED, 0L);
        long closed = byStatus.getOrDefault(Status.CLOSED, 0L);
        long total = allBugs.size();
        if (total > 0) {
            double rate = ((resolved + closed) * 100.0) / total;
            stats.append(String.format("  Resolved/Closed: %.1f%%\n", rate));
        } else {
            stats.append("  No bugs to calculate rate\n");
        }
        
        stats.append("\n═══════════════════════════════════════════════════════════════\n");
        stats.append("Generated: ").append(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        stats.append("═══════════════════════════════════════════════════════════════\n");
        
        statsArea.setText(stats.toString());
    }
    
    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("📁 Export Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        ModernButton exportCsvButton = new ModernButton("Export to CSV", ColorScheme.PRIMARY);
        exportCsvButton.addActionListener(e -> exportToCSV());
        buttonPanel.add(exportCsvButton);
        
        ModernButton exportTextButton = new ModernButton("Export Statistics", ColorScheme.SECONDARY);
        exportTextButton.addActionListener(e -> exportStatistics());
        buttonPanel.add(exportTextButton);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void exportToCSV() {
        try {
            String filename = "bug_report_" + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            
            FileWriter writer = new FileWriter(filename);
            writer.write("ID,Title,Description,Priority,Status,Assigned To,Created By,Created Date,Updated Date\n");
            
            List<Bug> bugs = bugService.getAllBugs();
            for (Bug bug : bugs) {
                writer.write(String.format("%d,\"%s\",\"%s\",%s,%s,\"%s\",\"%s\",%s,%s\n",
                    bug.getId(),
                    bug.getTitle().replace("\"", "\"\""),
                    bug.getDescription().replace("\"", "\"\""),
                    bug.getPriority(),
                    bug.getStatus(),
                    bug.getAssignedTo().replace("\"", "\"\""),
                    "N/A",  // Created by placeholder
                    bug.getCreatedDate(),
                    bug.getUpdatedDate()
                ));
            }
            
            writer.close();
            JOptionPane.showMessageDialog(this, 
                "Report exported successfully!\n\nFile: " + filename,
                "Export Complete", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error exporting report: " + e.getMessage(),
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportStatistics() {
        try {
            String filename = "bug_statistics_" + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            
            JTextArea tempArea = new JTextArea();
            updateStatistics(tempArea);
            
            FileWriter writer = new FileWriter(filename);
            writer.write(tempArea.getText());
            writer.close();
            
            JOptionPane.showMessageDialog(this, 
                "Statistics exported successfully!\n\nFile: " + filename,
                "Export Complete", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error exporting statistics: " + e.getMessage(),
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refresh() {
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }
}
