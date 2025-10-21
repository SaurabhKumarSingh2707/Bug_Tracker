package ui;

import model.Bug;
import util.ColorScheme;
import util.CardPanel;
import service.BugService;
import service.AuthService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Modern dashboard panel with statistics cards
 */
public class DashboardPanel extends JPanel {
    private BugService bugService;
    
    public DashboardPanel(BugService bugService) {
        this.bugService = bugService;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorScheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBackground(ColorScheme.BACKGROUND);
        
        // Statistics cards
        JPanel statsPanel = createStatsPanel();
        mainContent.add(statsPanel, BorderLayout.NORTH);
        
        // Recent bugs
        JPanel recentBugsPanel = createRecentBugsPanel();
        mainContent.add(recentBugsPanel, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + AuthService.getCurrentUser().getFullName() + "! 👋");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(dateLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(ColorScheme.BACKGROUND);
        
        BugService.BugStats stats = bugService.getStatistics();
        
        panel.add(createStatCard("Total Bugs", String.valueOf(stats.totalBugs), 
            ColorScheme.PRIMARY, "📊"));
        panel.add(createStatCard("Active", String.valueOf(stats.newBugs + stats.inProgressBugs), 
            ColorScheme.WARNING, "🔥"));
        panel.add(createStatCard("Resolved", String.valueOf(stats.resolvedBugs), 
            ColorScheme.SUCCESS, "✅"));
        panel.add(createStatCard("Critical", String.valueOf(stats.criticalBugs), 
            ColorScheme.CRITICAL, "⚠️"));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color, String emoji) {
        CardPanel card = new CardPanel(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(0, 120));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(emojiLabel, BorderLayout.EAST);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRecentBugsPanel() {
        CardPanel card = new CardPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Recent Bugs");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        
        JPanel bugsList = new JPanel();
        bugsList.setLayout(new BoxLayout(bugsList, BoxLayout.Y_AXIS));
        bugsList.setBackground(Color.WHITE);
        
        List<Bug> recentBugs = bugService.getAllBugs();
        int count = Math.min(5, recentBugs.size());
        
        for (int i = 0; i < count; i++) {
            Bug bug = recentBugs.get(i);
            bugsList.add(createBugItem(bug));
            if (i < count - 1) {
                bugsList.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(bugsList);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createBugItem(Bug bug) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.DIVIDER, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Left side
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        JLabel idLabel = new JLabel("#" + bug.getId());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        JLabel titleLabel = new JLabel(bug.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        JLabel assignedLabel = new JLabel("Assigned to: " + bug.getAssignedTo());
        assignedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        assignedLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        leftPanel.add(idLabel, BorderLayout.NORTH);
        leftPanel.add(titleLabel, BorderLayout.CENTER);
        leftPanel.add(assignedLabel, BorderLayout.SOUTH);
        
        // Right side
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);
        
        JLabel priorityLabel = new JLabel(bug.getPriority().toString());
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        priorityLabel.setForeground(Color.WHITE);
        priorityLabel.setOpaque(true);
        priorityLabel.setBackground(getPriorityColor(bug.getPriority()));
        priorityLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        
        JLabel statusLabel = new JLabel(bug.getStatus().toString());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.DIVIDER, 1, true),
            BorderFactory.createEmptyBorder(3, 7, 3, 7)
        ));
        
        rightPanel.add(priorityLabel);
        rightPanel.add(statusLabel);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private Color getPriorityColor(Bug.Priority priority) {
        switch (priority) {
            case CRITICAL: return ColorScheme.CRITICAL;
            case HIGH: return ColorScheme.HIGH;
            case MEDIUM: return ColorScheme.MEDIUM;
            case LOW: return ColorScheme.LOW;
            default: return ColorScheme.TEXT_SECONDARY;
        }
    }
    
    public void refreshDashboard() {
        // Remove and recreate panels to refresh data
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }
}
