package ui;

import model.Bug;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog for displaying bug details
 */
public class BugDetailsDialog extends JDialog {
    private Bug bug;
    
    public BugDetailsDialog(JFrame parent, Bug bug) {
        super(parent, "Bug Details - Bug #" + bug.getId(), true);
        this.bug = bug;
        initializeUI();
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
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
