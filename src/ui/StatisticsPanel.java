package ui;

import service.BugService;
import service.BugService.BugStats;
import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying bug statistics
 */
public class StatisticsPanel extends JPanel {
    private BugService bugService;
    
    private JLabel totalBugsLabel;
    private JLabel newBugsLabel;
    private JLabel inProgressBugsLabel;
    private JLabel resolvedBugsLabel;
    private JLabel closedBugsLabel;
    private JLabel criticalBugsLabel;
    
    public StatisticsPanel(BugService bugService) {
        this.bugService = bugService;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Bug Tracker Statistics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Stats panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("Refresh Statistics");
        refreshButton.addActionListener(e -> refreshStatistics());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        refreshStatistics();
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Statistics Overview"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Total Bugs
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(createStatLabel("Total Bugs:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        totalBugsLabel = createValueLabel("0");
        panel.add(totalBugsLabel, gbc);
        
        // New Bugs
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(createStatLabel("New Bugs:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        newBugsLabel = createValueLabel("0");
        panel.add(newBugsLabel, gbc);
        
        // In Progress Bugs
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panel.add(createStatLabel("In Progress:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inProgressBugsLabel = createValueLabel("0");
        panel.add(inProgressBugsLabel, gbc);
        
        // Resolved Bugs
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        panel.add(createStatLabel("Resolved:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        resolvedBugsLabel = createValueLabel("0");
        panel.add(resolvedBugsLabel, gbc);
        
        // Closed Bugs
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        panel.add(createStatLabel("Closed:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        closedBugsLabel = createValueLabel("0");
        panel.add(closedBugsLabel, gbc);
        
        // Critical Bugs
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        panel.add(createStatLabel("Critical Priority:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        criticalBugsLabel = createValueLabel("0");
        criticalBugsLabel.setForeground(Color.RED);
        panel.add(criticalBugsLabel, gbc);
        
        return panel;
    }
    
    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }
    
    public void refreshStatistics() {
        BugStats stats = bugService.getStatistics();
        
        totalBugsLabel.setText(String.valueOf(stats.totalBugs));
        newBugsLabel.setText(String.valueOf(stats.newBugs));
        inProgressBugsLabel.setText(String.valueOf(stats.inProgressBugs));
        resolvedBugsLabel.setText(String.valueOf(stats.resolvedBugs));
        closedBugsLabel.setText(String.valueOf(stats.closedBugs));
        criticalBugsLabel.setText(String.valueOf(stats.criticalBugs));
    }
}
