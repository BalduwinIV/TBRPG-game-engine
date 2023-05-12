package engine.panels;

import engine.tools.GameLevels;
import engine.utils.ButtonHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Toolbar class with buttons and options.
 */
public class ToolBar extends JPanel {
    private final GameLevels gameLevels;
    public ToolBar(GameLevels gameLevels, GamePanel gamePanel) {
        super();
        this.gameLevels = gameLevels;

        JPanel levelChooserPanel = new JPanel();
        levelChooserPanel.setBackground(new Color(0xb3b3b3));
        JComboBox<String> levelChooser = new JComboBox<>(gameLevels.getLevelsList());
        levelChooser.addActionListener(e -> System.out.println("Level \"" + levelChooser.getSelectedItem() + "\" has been chosen.\n"));
        levelChooserPanel.add(levelChooser);
        levelChooserPanel.setLayout(new GridLayout(1, 1));
        levelChooserPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(levelChooserPanel);

        JPanel controlButtonsPanel = new JPanel();
        controlButtonsPanel.setBackground(new Color(0xb3b3b3));

        JButton startButton = new JButton();
        startButton.setRolloverEnabled(false);
        startButton.addMouseListener(new ButtonHandler(startButton, 30, 30,
                "src/main/resources/img/gui_icons/start_button_default.png",
                "src/main/resources/img/gui_icons/start_button_hover.png",
                "src/main/resources/img/gui_icons/start_button_pressed.png",
                "src/main/resources/img/gui_icons/start_button_active.png",
                "src/main/resources/img/gui_icons/start_button_active_hover.png",
                "src/main/resources/img/gui_icons/start_button_active_pressed.png"));
        startButton.addActionListener(e -> gamePanel.startGame());
        startButton.setPreferredSize(new Dimension(30, 30));

        JButton pauseButton = new JButton();
        pauseButton.setRolloverEnabled(false);
        pauseButton.addMouseListener(new ButtonHandler(pauseButton, 30, 30,
                "src/main/resources/img/gui_icons/pause_button.png",
                "src/main/resources/img/gui_icons/pause_button_hover.png",
                "src/main/resources/img/gui_icons/pause_button_pressed.png"));
        pauseButton.setPreferredSize(new Dimension(30, 30));

        JButton stopButton = new JButton();
        stopButton.setRolloverEnabled(false);
        stopButton.addMouseListener(new ButtonHandler(stopButton, 30, 30,
                "src/main/resources/img/gui_icons/stop_button.png",
                "src/main/resources/img/gui_icons/stop_button_hover.png",
                "src/main/resources/img/gui_icons/stop_button_pressed.png"));
        stopButton.addActionListener(e -> gamePanel.stopGame());
        stopButton.setPreferredSize(new Dimension(30, 30));


        controlButtonsPanel.add(startButton);
        controlButtonsPanel.add(pauseButton);
        controlButtonsPanel.add(stopButton);
        controlButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
        this.add(controlButtonsPanel);

        JPanel settingsButtonPanel = new JPanel();
        settingsButtonPanel.setBackground(new Color(0xb3b3b3));
        JButton settingsButton = new JButton("Settings");
        settingsButtonPanel.add(settingsButton);
        settingsButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(settingsButtonPanel);


        this.setLayout(new GridLayout(1, 3, 10, 3));
    }
}
