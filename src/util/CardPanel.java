package util;

import javax.swing.*;
import java.awt.*;

/**
 * Modern styled panel with shadow effect
 */
public class CardPanel extends JPanel {
    private Color backgroundColor = ColorScheme.SURFACE;
    
    public CardPanel() {
        this(new FlowLayout());
    }
    
    public CardPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBackground(backgroundColor);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
        
        // Draw background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 10, 10);
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    public void setCardBackground(Color color) {
        this.backgroundColor = color;
        repaint();
    }
}
