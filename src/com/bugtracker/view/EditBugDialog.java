package com.bugtracker.view;

import com.bugtracker.model.*;
import com.bugtracker.service.AuthService;
import com.bugtracker.service.BugService;
import com.bugtracker.service.UserService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for editing an existing bug
 */
public class EditBugDialog extends JDialog {
    private AuthService authService;
    private UserService userService;
    private BugService bugService;
    private MainFrame parentFrame;
    private Bug bug;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<BugStatus> statusCombo;
    private JComboBox<BugPriority> priorityCombo;
    private JComboBox<BugSeverity> severityCombo;
    private JTextField projectField;
    private JComboBox<User> assignedToCombo;
    private JTextArea stepsArea;

    public EditBugDialog(MainFrame parent, AuthService authService, 
                        UserService userService, BugService bugService, Bug bug) {
        super(parent, "Edit Bug - " + bug.getBugId(), true);
        this.parentFrame = parent;
        this.authService = authService;
        this.userService = userService;
        this.bugService = bugService;
        this.bug = bug;
        
        initComponents();
        loadBugData();
    }

    private void initComponents() {
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Title
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Title *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        titleField = new JTextField(30);
        formPanel.add(titleField, gbc);
        row++;

        // Description
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Description *:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.gridheight = 3;
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descScrollPane, gbc);
        row += 3;

        gbc.gridheight = 1;

        // Status
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Status *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        statusCombo = new JComboBox<>(BugStatus.values());
        formPanel.add(statusCombo, gbc);
        row++;

        // Priority
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Priority *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        priorityCombo = new JComboBox<>(BugPriority.values());
        formPanel.add(priorityCombo, gbc);
        row++;

        // Severity
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Severity *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        severityCombo = new JComboBox<>(BugSeverity.values());
        formPanel.add(severityCombo, gbc);
        row++;

        // Project
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Project:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        projectField = new JTextField(30);
        formPanel.add(projectField, gbc);
        row++;

        // Assigned To
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Assign To:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        assignedToCombo = new JComboBox<>();
        assignedToCombo.addItem(null); // Unassigned option
        List<User> users = userService.getActiveUsers();
        for (User user : users) {
            assignedToCombo.addItem(user);
        }
        formPanel.add(assignedToCombo, gbc);
        row++;

        // Steps to Reproduce
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Steps to Reproduce:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.gridheight = 3;
        stepsArea = new JTextArea(4, 30);
        stepsArea.setLineWrap(true);
        JScrollPane stepsScrollPane = new JScrollPane(stepsArea);
        formPanel.add(stepsScrollPane, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton updateButton = new JButton("Update Bug");
        updateButton.setBackground(new Color(230, 126, 34));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorderPainted(false);
        updateButton.setOpaque(true);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setFont(new Font("Arial", Font.BOLD, 12));
        updateButton.addActionListener(e -> handleUpdate());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(127, 140, 141));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBugData() {
        titleField.setText(bug.getTitle());
        descriptionArea.setText(bug.getDescription());
        statusCombo.setSelectedItem(bug.getStatus());
        priorityCombo.setSelectedItem(bug.getPriority());
        severityCombo.setSelectedItem(bug.getSeverity());
        projectField.setText(bug.getProjectName() != null ? bug.getProjectName() : "");
        stepsArea.setText(bug.getStepsToReproduce() != null ? bug.getStepsToReproduce() : "");
        
        // Set assigned to
        if (bug.getAssignedTo() != null) {
            userService.getUserById(bug.getAssignedTo()).ifPresent(user -> {
                assignedToCombo.setSelectedItem(user);
            });
        }
    }

    private void handleUpdate() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        BugStatus status = (BugStatus) statusCombo.getSelectedItem();
        BugPriority priority = (BugPriority) priorityCombo.getSelectedItem();
        BugSeverity severity = (BugSeverity) severityCombo.getSelectedItem();
        String project = projectField.getText().trim();
        User assignedTo = (User) assignedToCombo.getSelectedItem();
        String steps = stepsArea.getText().trim();

        // Validation
        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Title and Description are required", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            bug.setTitle(title);
            bug.setDescription(description);
            bug.setStatus(status);
            bug.setPriority(priority);
            bug.setSeverity(severity);
            bug.setProjectName(project.isEmpty() ? null : project);
            bug.setAssignedTo(assignedTo != null ? assignedTo.getUserId() : null);
            bug.setStepsToReproduce(steps.isEmpty() ? null : steps);

            bugService.updateBug(bug);
            
            JOptionPane.showMessageDialog(this, 
                "Bug updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            parentFrame.refreshData();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error updating bug: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
