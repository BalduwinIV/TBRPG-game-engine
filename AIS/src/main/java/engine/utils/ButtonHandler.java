package engine.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonHandler implements MouseListener {
    private final JButton button;
    private boolean isActive;
    private final ImageIcon defaultIcon;
    private final ImageIcon hoverIcon;
    private final ImageIcon pressedIcon;
    private final ImageIcon activeDefaultIcon;
    private final ImageIcon activeHoverIcon;
    private final ImageIcon activePressedIcon;

    public ButtonHandler(JButton button, int width, int height, String defaultIcon, String hoverIcon, String pressedIcon) {
        this.button = button;
        isActive = false;

        button.setRolloverEnabled(false);
        button.setPreferredSize(new Dimension(width, height));

        this.defaultIcon = new ImageIcon(new ImageIcon(defaultIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.hoverIcon = new ImageIcon(new ImageIcon(hoverIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.pressedIcon = new ImageIcon(new ImageIcon(pressedIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.activeDefaultIcon = this.defaultIcon;
        this.activeHoverIcon = this.hoverIcon;
        this.activePressedIcon = this.pressedIcon;

        button.setIcon(this.defaultIcon);
    }

    public ButtonHandler(JButton button, int width, int height, String defaultIcon, String hoverIcon, String pressedIcon, String activeDefaultIcon, String activeHoverIcon, String activePressedIcon) {
        this.button = button;
        isActive = false;

        button.setRolloverEnabled(false);
        button.setPreferredSize(new Dimension(width, height));

        this.defaultIcon = new ImageIcon(new ImageIcon(defaultIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.hoverIcon = new ImageIcon(new ImageIcon(hoverIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.pressedIcon = new ImageIcon(new ImageIcon(pressedIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.activeDefaultIcon = new ImageIcon(new ImageIcon(activeDefaultIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.activeHoverIcon = new ImageIcon(new ImageIcon(activeHoverIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        this.activePressedIcon = new ImageIcon(new ImageIcon(activePressedIcon).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));

        button.setIcon(this.defaultIcon);
    }

    public void setState(String state) {
        switch (state) {
            case "default" -> {
                isActive = false;
                button.setIcon(defaultIcon);
            }
            case "hover" -> {
                isActive = false;
                button.setIcon(hoverIcon);
            }
            case "pressed" -> {
                isActive = false;
                button.setIcon(pressedIcon);
            }
            case "active" -> {
                isActive = true;
                button.setIcon(activeDefaultIcon);
            }
            case "activeHover" -> {
                isActive = true;
                button.setIcon(activeHoverIcon);
            }
            case "activePressed" -> {
                isActive = true;
                button.setIcon(activePressedIcon);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isActive) {
            isActive = false;
            button.setIcon(hoverIcon);
        } else {
            isActive = true;
            button.setIcon(activeHoverIcon);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isActive) {
            button.setIcon(activePressedIcon);
        } else {
            button.setIcon(pressedIcon);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isActive) {
            button.setIcon(activeHoverIcon);
        } else {
            button.setIcon(hoverIcon);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (isActive) {
            button.setIcon(activeDefaultIcon);
        } else {
            button.setIcon(defaultIcon);
        }
    }
}
