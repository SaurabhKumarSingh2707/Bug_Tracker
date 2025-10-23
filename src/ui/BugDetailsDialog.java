package ui;

import model.Bug;
import model.User;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog for displaying bug details
 */
public class BugDetailsDialog extends JDialog {
    private Bug bug;
    private User currentUser;
    private Runnable onBugUpdatedCallback;
    
    public BugDetailsDialog(JFrame parent, Bug bug, User currentUser, Runnable onBugUpdatedCallback) {
        super(parent, "Bug Details - Bug #" + bug.getId(), true);
        this.bug = bug;
        this.currentUser = currentUser;
        this.onBugUpdatedCallback = onBugUpdatedCallback;
        initializeUI();
    }
    
    // Backward compatibility constructor
    public BugDetailsDialog(JFrame parent, Bug bug) {
        this(parent, bug, null, null);
    }
    
    private void initializeUI() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Bug ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(createBoldLabel("Bug ID:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(new JLabel("#" + bug.getId()), gbc);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(createBoldLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(new JLabel(bug.getTitle()), gbc);
        
        // Priority
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(createBoldLabel("Priority:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel priorityLabel = new JLabel(bug.getPriority().toString());
        if (bug.getPriority() == Bug.Priority.CRITICAL) {
            priorityLabel.setForeground(Color.RED);
            priorityLabel.setFont(priorityLabel.getFont().deriveFont(Font.BOLD));
        } else if (bug.getPriority() == Bug.Priority.HIGH) {
            priorityLabel.setForeground(new Color(255, 140, 0));
            priorityLabel.setFont(priorityLabel.getFont().deriveFont(Font.BOLD));
        }
        mainPanel.add(priorityLabel, gbc);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        mainPanel.add(createBoldLabel("Status:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel statusLabel = new JLabel(bug.getStatus().toString());
        if (bug.getStatus() == Bug.Status.RESOLVED || bug.getStatus() == Bug.Status.CLOSED) {
            statusLabel.setForeground(new Color(0, 128, 0));
        }
        mainPanel.add(statusLabel, gbc);
        
        // Assigned To
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        mainPanel.add(createBoldLabel("Assigned To:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(new JLabel(bug.getAssignedTo()), gbc);
        
        // Created Date
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        mainPanel.add(createBoldLabel("Created Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(new JLabel(bug.getFormattedCreatedDate()), gbc);
        
        // Updated Date
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        mainPanel.add(createBoldLabel("Updated Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(new JLabel(bug.getFormattedUpdatedDate()), gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(createBoldLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea descriptionArea = new JTextArea(bug.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(getBackground());
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(scrollPane, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Fix Bug button (only show if user is logged in and bug is not closed)
        if (currentUser != null && bug.getStatus() != Bug.Status.CLOSED) {
            JButton fixButton = new JButton("🔧 Fix Bug");
            fixButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            fixButton.setBackground(new Color(34, 197, 94));
            fixButton.setForeground(Color.WHITE);
            fixButton.setFocusPainted(false);
            fixButton.setBorderPainted(false);
            fixButton.setPreferredSize(new Dimension(120, 35));
            fixButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            fixButton.addActionListener(e -> handleFixBug());
            
            // Disable if already resolved
            if (bug.getStatus() == Bug.Status.RESOLVED) {
                fixButton.setText("✓ Already Fixed");
                fixButton.setEnabled(false);
                fixButton.setBackground(new Color(150, 150, 150));
            }
            
            buttonPanel.add(fixButton);
        }
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void handleFixBug() {
        FixBugDialog fixDialog = new FixBugDialog((Frame) getParent(), bug, currentUser);
        fixDialog.setVisible(true);
        
        if (fixDialog.wasFixed()) {
            // Refresh the bug details
            dispose();
            
            // Call the callback to refresh the parent view
            if (onBugUpdatedCallback != null) {
                onBugUpdatedCallback.run();
            }
        }
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
