package ui;

import database.BugDAO;
import model.Bug;
import model.Bug.Status;
import model.User;
import service.ActivityLogService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Dialog for marking a bug as fixed/resolved
 */
public class FixBugDialog extends JDialog {
    private Bug bug;
    private User currentUser;
    private JTextArea fixDescriptionArea;
    private JComboBox<String> resolutionTypeCombo;
    private JTextField hoursSpentField;
    private JCheckBox closeImmediatelyCheckBox;
    private boolean wasFixed = false;
    
    public FixBugDialog(Frame parent, Bug bug, User currentUser) {
        super(parent, "Fix Bug #" + bug.getId(), true);
        this.bug = bug;
        this.currentUser = currentUser;
        
        initializeUI();
        setLocationRelativeTo(parent);
    }
    
    private void initializeUI() {
        setSize(600, 500);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(34, 197, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Fix Bug: " + bug.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        // Bug Info
        JLabel infoLabel = new JLabel(String.format(
            "ID: #%d | Priority: %s | Current Status: %s",
            bug.getId(), bug.getPriority(), bug.getStatus()
        ));
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(240, 240, 240));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Resolution Type
        JPanel resolutionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        resolutionPanel.setBackground(Color.WHITE);
        resolutionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel resolutionLabel = new JLabel("Resolution Type:");
        resolutionLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        String[] resolutionTypes = {
            "Fixed - Code Change",
            "Fixed - Configuration",
            "Fixed - Database Update",
            "Cannot Reproduce",
            "Works as Designed",
            "Duplicate",
            "Won't Fix"
        };
        resolutionTypeCombo = new JComboBox<>(resolutionTypes);
        resolutionTypeCombo.setPreferredSize(new Dimension(250, 30));
        
        resolutionPanel.add(resolutionLabel);
        resolutionPanel.add(resolutionTypeCombo);
        panel.add(resolutionPanel);
        
        panel.add(Box.createVerticalStrut(10));
        
        // Fix Description
        JLabel descLabel = new JLabel("Fix Description / Resolution Details:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(descLabel);
        
        panel.add(Box.createVerticalStrut(5));
        
        fixDescriptionArea = new JTextArea(8, 40);
        fixDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fixDescriptionArea.setLineWrap(true);
        fixDescriptionArea.setWrapStyleWord(true);
        fixDescriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        fixDescriptionArea.setText("Describe what was done to fix this bug:\n\n• Root cause:\n• Solution applied:\n• Testing performed:\n• Additional notes:");
        
        JScrollPane scrollPane = new JScrollPane(fixDescriptionArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        panel.add(scrollPane);
        
        panel.add(Box.createVerticalStrut(15));
        
        // Hours Spent
        JPanel hoursPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        hoursPanel.setBackground(Color.WHITE);
        hoursPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel hoursLabel = new JLabel("Hours Spent on Fix:");
        hoursLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        hoursSpentField = new JTextField(10);
        hoursSpentField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hoursSpentField.setText("0.0");
        
        hoursPanel.add(hoursLabel);
        hoursPanel.add(hoursSpentField);
        panel.add(hoursPanel);
        
        panel.add(Box.createVerticalStrut(10));
        
        // Close Immediately Option
        closeImmediatelyCheckBox = new JCheckBox("Close bug immediately (skip RESOLVED status)");
        closeImmediatelyCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        closeImmediatelyCheckBox.setBackground(Color.WHITE);
        closeImmediatelyCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(closeImmediatelyCheckBox);
        
        panel.add(Box.createVerticalStrut(10));
        
        // Info Label
        JLabel infoLabel = new JLabel("<html><i>Note: This will update the bug status to RESOLVED (or CLOSED if checked above) and log your fix details.</i></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(infoLabel);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Mark as Fixed Button
        JButton fixButton = new JButton("Mark as Fixed");
        fixButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fixButton.setBackground(new Color(34, 197, 94));
        fixButton.setForeground(Color.WHITE);
        fixButton.setFocusPainted(false);
        fixButton.setBorderPainted(false);
        fixButton.setPreferredSize(new Dimension(150, 35));
        fixButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fixButton.addActionListener(e -> handleFixBug());
        
        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelButton.setBackground(new Color(239, 68, 68));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());
        
        panel.add(cancelButton);
        panel.add(fixButton);
        
        return panel;
    }
    
    private void handleFixBug() {
        // Validate input
        if (fixDescriptionArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please provide a description of the fix.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            fixDescriptionArea.requestFocus();
            return;
        }
        
        // Validate hours
        double hoursSpent = 0.0;
        try {
            hoursSpent = Double.parseDouble(hoursSpentField.getText().trim());
            if (hoursSpent < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid number for hours spent (e.g., 2.5)",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            hoursSpentField.requestFocus();
            return;
        }
        
        // Confirm action
        String newStatus = closeImmediatelyCheckBox.isSelected() ? "CLOSED" : "RESOLVED";
        int confirm = JOptionPane.showConfirmDialog(this,
            "Mark this bug as " + newStatus + "?\n\nResolution: " + resolutionTypeCombo.getSelectedItem(),
            "Confirm Fix",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            // Update bug status
            Status oldStatus = bug.getStatus();
            Status newBugStatus = closeImmediatelyCheckBox.isSelected() ? Status.CLOSED : Status.RESOLVED;
            bug.setStatus(newBugStatus);
            
            // Update in database
            boolean success = BugDAO.updateBug(bug);
            
            if (success) {
                // Create fix details message
                String resolutionType = (String) resolutionTypeCombo.getSelectedItem();
                String fixDetails = fixDescriptionArea.getText().trim();
                
                String activityDetails = String.format(
                    "Bug fixed by %s\n\nResolution Type: %s\nHours Spent: %.2f\n\nFix Details:\n%s",
                    currentUser.getFullName(),
                    resolutionType,
                    hoursSpent,
                    fixDetails
                );
                
                // Log the activity
                ActivityLogService activityService = new ActivityLogService();
                
                model.ActivityLog fixLog = new model.ActivityLog(
                    currentUser.getId(),
                    currentUser.getUsername(),
                    "BUG_FIXED",
                    activityDetails,
                    bug.getId()
                );
                activityService.logActivity(fixLog);
                
                // Log status change
                model.ActivityLog statusLog = new model.ActivityLog(
                    currentUser.getId(),
                    currentUser.getUsername(),
                    "STATUS_CHANGED",
                    String.format("Status changed from %s to %s", oldStatus, newBugStatus),
                    bug.getId()
                );
                activityService.logActivity(statusLog);
                
                // Log time spent if provided
                if (hoursSpent > 0) {
                    logTimeSpent(hoursSpent, resolutionType + " - " + fixDetails);
                }
                
                wasFixed = true;
                
                // Success message
                JOptionPane.showMessageDialog(this,
                    "Bug successfully marked as " + newBugStatus + "!\n\n" +
                    "Resolution: " + resolutionType + "\n" +
                    "Time logged: " + hoursSpent + " hours",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update bug status. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "An error occurred while fixing the bug:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void logTimeSpent(double hours, String description) {
        try {
            String sql = "INSERT INTO time_logs (bug_id, user_id, username, hours_spent, description, log_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            
            java.sql.Connection conn = database.DatabaseManager.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, bug.getId());
            pstmt.setInt(2, currentUser.getId());
            pstmt.setString(3, currentUser.getUsername());
            pstmt.setDouble(4, hours);
            pstmt.setString(5, description);
            pstmt.setString(6, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            pstmt.executeUpdate();
            pstmt.close();
            
        } catch (Exception e) {
            System.err.println("Error logging time: " + e.getMessage());
        }
    }
    
    public boolean wasFixed() {
        return wasFixed;
    }
}
