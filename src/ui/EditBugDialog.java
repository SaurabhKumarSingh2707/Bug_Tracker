package ui;

import model.Bug;
import service.BugService;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog for editing bug details
 */
public class EditBugDialog extends JDialog {
    private BugService bugService;
    private Bug bug;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Bug.Priority> priorityComboBox;
    private JComboBox<Bug.Status> statusComboBox;
    private JTextField assignedToField;
    
    public EditBugDialog(JFrame parent, BugService bugService, Bug bug) {
        super(parent, "Edit Bug - Bug #" + bug.getId(), true);
        this.bugService = bugService;
        this.bug = bug;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(600, 550);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Form panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Bug ID (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(createBoldLabel("Bug ID:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(new JLabel("#" + bug.getId()), gbc);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(createBoldLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        titleField = new JTextField(bug.getTitle(), 30);
        panel.add(titleField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(createBoldLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(bug.getDescription(), 8, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        panel.add(scrollPane, gbc);
        
        // Priority
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createBoldLabel("Priority:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        priorityComboBox = new JComboBox<>(Bug.Priority.values());
        priorityComboBox.setSelectedItem(bug.getPriority());
        panel.add(priorityComboBox, gbc);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        panel.add(createBoldLabel("Status:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        statusComboBox = new JComboBox<>(Bug.Status.values());
        statusComboBox.setSelectedItem(bug.getStatus());
        panel.add(statusComboBox, gbc);
        
        // Assigned To
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        panel.add(createBoldLabel("Assigned To:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        assignedToField = new JTextField(bug.getAssignedTo(), 30);
        panel.add(assignedToField, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setPreferredSize(new Dimension(150, 35));
        saveButton.addActionListener(e -> saveChanges());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(150, 35));
        cancelButton.addActionListener(e -> dispose());
        
        panel.add(saveButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private void saveChanges() {
        // Validate inputs
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String assignedTo = assignedToField.getText().trim();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (assignedTo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the person to assign this bug to.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update bug
        Bug.Priority priority = (Bug.Priority) priorityComboBox.getSelectedItem();
        Bug.Status status = (Bug.Status) statusComboBox.getSelectedItem();
        
        boolean success = bugService.updateBug(bug.getId(), title, description, 
                                              priority, status, assignedTo);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Bug updated successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update bug.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
