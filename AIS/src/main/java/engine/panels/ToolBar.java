package engine.panels;

import engine.GUIColors;
import engine.tools.gameLevelsManager.GameLevelChooser;
import engine.tools.toolBarControllers.ToolBarGameLevelChooserController;
import engine.tools.toolBarControllers.ToolBarRemoveButtonController;
import engine.tools.toolBarControllers.ToolBarRenameButtonController;
import engine.tools.toolBarControllers.ToolBarSaveButtonController;
import engine.utils.ButtonHandler;
import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.utils.ImageStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Toolbar class with buttons and options.
 */
public class ToolBar extends JPanel {
    private final AtomicBoolean collisionMode;
    private final AtomicBoolean allyPlaceholderMode;
    public ToolBar(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        super();
        guiComponents.setToolBar(this);
        setBackground(new Color(GUIColors.color(GUIColors.Colors.BACKGROUND)));

        JPanel levelChooserPanel = new JPanel();
        levelChooserPanel.setLayout(new BorderLayout(5, 5));
        levelChooserPanel.setBackground(new Color(GUIColors.color(GUIColors.Colors.BACKGROUND)));

        GameLevelChooser levelChooser = new GameLevelChooser(modelControlComponents.getGameLevels());

        JButton saveButton = new JButton();
        saveButton.addActionListener(new ToolBarSaveButtonController(modelControlComponents, levelChooser));
        saveButton.addMouseListener(new ButtonHandler(saveButton, 30, 30,
                "src/main/resources/img/gui_icons/save_button.png",
                "src/main/resources/img/gui_icons/save_button_hover.png",
                "src/main/resources/img/gui_icons/save_button_pressed.png"));
        saveButton.setToolTipText("save");

        JButton renameButton = new JButton();
        renameButton.addActionListener(new ToolBarRenameButtonController(guiComponents, modelControlComponents, levelChooser));
        renameButton.addMouseListener(new ButtonHandler(renameButton, 30, 30,
                "src/main/resources/img/gui_icons/rename_button.png",
                "src/main/resources/img/gui_icons/rename_button_hover.png",
                "src/main/resources/img/gui_icons/rename_button_pressed.png"));
        renameButton.setToolTipText("rename");

        JButton removeButton = new JButton();
        removeButton.addActionListener(new ToolBarRemoveButtonController(guiComponents, modelControlComponents, levelChooser));
        removeButton.addMouseListener(new ButtonHandler(removeButton, 30, 30,
                "src/main/resources/img/gui_icons/remove_button.png",
                "src/main/resources/img/gui_icons/remove_button_hover.png",
                "src/main/resources/img/gui_icons/remove_button_pressed.png"));
        removeButton.setToolTipText("remove");

        collisionMode = new AtomicBoolean(false);
        JToggleButton collisionModeButton = new JToggleButton(new ImageIcon(new ImageStorage("src/main/resources/img/tileMaps/collision_tile.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        collisionModeButton.setPreferredSize(new Dimension(30, 30));
        collisionModeButton.setToolTipText("Collision Mode");
        collisionModeButton.addActionListener(e -> collisionMode.set(collisionModeButton.isSelected()));

        allyPlaceholderMode = new AtomicBoolean(false);
        JToggleButton allyPlaceholderModeButton = new JToggleButton(new ImageIcon(new ImageStorage("src/main/resources/img/tileMaps/ally_tile_filter.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        allyPlaceholderModeButton.setPreferredSize(new Dimension(30, 30));
        allyPlaceholderModeButton.setToolTipText("Ally Placeholder Mode");
        allyPlaceholderModeButton.addActionListener(e -> allyPlaceholderMode.set(allyPlaceholderModeButton.isSelected()));

        AbstractButton[] controlButtons = new AbstractButton[5];
        controlButtons[0] = saveButton;
        controlButtons[1] = renameButton;
        controlButtons[2] = removeButton;
        controlButtons[3] = collisionModeButton;
        controlButtons[4] = allyPlaceholderModeButton;

        for (AbstractButton button : controlButtons) {
            button.setEnabled(!Objects.requireNonNull(levelChooser.getSelectedItem()).toString().equals("-- choose the level --") && !levelChooser.getSelectedItem().toString().equals("-- add new level --"));
        }

        levelChooser.addActionListener(new ToolBarGameLevelChooserController(guiComponents, modelControlComponents, levelChooser, controlButtons));

        JPanel levelChooserButtonsPanel = new JPanel();
        levelChooserButtonsPanel.setLayout(new GridLayout(1, 3, 0, 0));
        levelChooserButtonsPanel.add(saveButton);
        levelChooserButtonsPanel.add(renameButton);
        levelChooserButtonsPanel.add(removeButton);

        levelChooserPanel.add(levelChooser, BorderLayout.CENTER);
        levelChooserPanel.add(levelChooserButtonsPanel, BorderLayout.EAST);
        levelChooserPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        levelChooserPanel.setToolTipText("Select a level");
        this.add(levelChooserPanel);

        JPanel controlButtonsPanel = new JPanel();
        controlButtonsPanel.setBackground(new Color(GUIColors.color(GUIColors.Colors.BACKGROUND)));
        controlButtonsPanel.add(collisionModeButton);
        controlButtonsPanel.add(allyPlaceholderModeButton);

        controlButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
        this.add(controlButtonsPanel);

        this.setLayout(new GridLayout(1, 3, 10, 3));
    }

    public boolean collisionModeIsEnabled() {
        return collisionMode.get();
    }

    public boolean allyPlaceholderModeIsEnabled() {
        return allyPlaceholderMode.get();
    }
}
