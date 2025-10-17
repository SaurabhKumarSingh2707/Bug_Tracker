package com.bugtracker.view;

import com.bugtracker.model.*;
import com.bugtracker.service.AuthService;
import com.bugtracker.service.BugService;
import com.bugtracker.service.UserService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for creating a new bug
 */
public class CreateBugDialog extends JDialog {
    private AuthService authService;
    private UserService userService;
    private BugService bugService;
    private MainFrame parentFrame;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<BugPriority> priorityCombo;
    private JComboBox<BugSeverity> severityCombo;
    private JTextField projectField;
    private JComboBox<User> assignedToCombo;
    private JTextArea stepsArea;

    public CreateBugDialog(MainFrame parent, AuthService authService, 
                          UserService userService, BugService bugService) {
        super(parent, "Create New Bug", true);
        this.parentFrame = parent;
        this.authService = authService;
        this.userService = userService;
        this.bugService = bugService;
        
        initComponents();
    }

    private void initComponents() {
        setSize(600, 650);
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

        // Priority
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Priority *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        priorityCombo = new JComboBox<>(BugPriority.values());
        priorityCombo.setSelectedItem(BugPriority.MEDIUM);
        formPanel.add(priorityCombo, gbc);
        row++;

        // Severity
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Severity *:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        severityCombo = new JComboBox<>(BugSeverity.values());
        severityCombo.setSelectedItem(BugSeverity.MAJOR);
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
        
        JButton createButton = new JButton("Create Bug");
        createButton.setBackground(new Color(39, 174, 96));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setOpaque(true);
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.setFont(new Font("Arial", Font.BOLD, 12));
        createButton.addActionListener(e -> handleCreate());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(127, 140, 141));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleCreate() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
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
            Bug bug = new Bug();
            bug.setTitle(title);
            bug.setDescription(description);
            bug.setPriority(priority);
            bug.setSeverity(severity);
            bug.setProjectName(project.isEmpty() ? null : project);
            bug.setReportedBy(authService.getCurrentUser().getUserId());
            bug.setAssignedTo(assignedTo != null ? assignedTo.getUserId() : null);
            bug.setStepsToReproduce(steps.isEmpty() ? null : steps);

            bugService.createBug(bug);
            
            JOptionPane.showMessageDialog(this, 
                "Bug created successfully!\nBug ID: " + bug.getBugId(), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            parentFrame.refreshData();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error creating bug: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
