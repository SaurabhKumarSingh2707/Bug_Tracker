package ui;

import model.Bug;
import service.BugService;
import javax.swing.*;
import java.awt.*;

/**
 * Panel for adding new bugs
 */
public class AddBugPanel extends JPanel {
    private BugService bugService;
    private JFrame parentFrame;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Bug.Priority> priorityComboBox;
    private JTextField assignedToField;
    
    public AddBugPanel(BugService bugService, JFrame parentFrame) {
        this.bugService = bugService;
        this.parentFrame = parentFrame;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Bug Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(titleLbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        titleField = new JTextField(30);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleField.setPreferredSize(new Dimension(400, 35));
        panel.add(titleField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel descLbl = new JLabel("Description:");
        descLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(descLbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        descriptionArea = new JTextArea(10, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        panel.add(scrollPane, gbc);
        
        // Priority
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel priorityLbl = new JLabel("Priority:");
        priorityLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(priorityLbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        priorityComboBox = new JComboBox<>(Bug.Priority.values());
        priorityComboBox.setSelectedItem(Bug.Priority.MEDIUM);
        priorityComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priorityComboBox.setPreferredSize(new Dimension(400, 35));
        panel.add(priorityComboBox, gbc);
        
        // Assigned To
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        JLabel assignedLbl = new JLabel("Assigned To:");
        assignedLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(assignedLbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        assignedToField = new JTextField(30);
        assignedToField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        assignedToField.setPreferredSize(new Dimension(400, 35));
        panel.add(assignedToField, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton submitButton = new JButton("Submit Bug");
        submitButton.setPreferredSize(new Dimension(150, 35));
        submitButton.addActionListener(e -> submitBug());
        
        JButton clearButton = new JButton("Clear Form");
        clearButton.setPreferredSize(new Dimension(150, 35));
        clearButton.addActionListener(e -> clearForm());
        
        panel.add(submitButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    private void submitBug() {
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
        
        // Create bug
        Bug.Priority priority = (Bug.Priority) priorityComboBox.getSelectedItem();
        Bug bug = bugService.createBug(title, description, priority, assignedTo);
        
        JOptionPane.showMessageDialog(this, 
            "Bug #" + bug.getId() + " created successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        clearForm();
        if (parentFrame instanceof BugTrackerFrame) {
            ((BugTrackerFrame) parentFrame).refreshAllPanels();
        } else if (parentFrame instanceof ModernBugTrackerFrame) {
            ((ModernBugTrackerFrame) parentFrame).refreshAllPanels();
        }
    }
    
    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        priorityComboBox.setSelectedItem(Bug.Priority.MEDIUM);
        assignedToField.setText("");
        titleField.requestFocus();
    }
}
