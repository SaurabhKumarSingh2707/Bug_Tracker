package main;

import ui.ModernLoginFrame;
import database.DatabaseManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Enumeration;

/**
 * Main entry point for the Bug Tracker application
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseManager.initializeDatabase();
        
        // Set modern font globally
        setUIFont(new FontUIResource("Segoe UI", Font.PLAIN, 12));
        
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ModernLoginFrame frame = new ModernLoginFrame();
            frame.setVisible(true);
        });
    }
    
    // Set default font for all Swing components
    private static void setUIFont(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
