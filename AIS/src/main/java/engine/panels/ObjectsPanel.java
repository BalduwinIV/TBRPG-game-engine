package engine.panels;

import engine.utils.ButtonHandler;
import engine.tools.GUIComponents;
import engine.utils.Logger;
import engine.tools.ModelControlComponents;
import engine.tools.tilemap.AddNewTileWindow;
import engine.tools.tilemap.Tile;
import engine.tools.tilemap.TileMouseListener;

import javax.swing.*;
import java.awt.*;

/**
 * Objects panel class that is used for putting objects to the game field.
 */
public class ObjectsPanel extends JPanel {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private Logger logger;
    private final JPanel tileMapPanel;

    public ObjectsPanel(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        super();
        this.guiComponents = guiComponents;
        guiComponents.setObjectsPanel(this);
        this.modelControlComponents = modelControlComponents;
        setLogger(modelControlComponents.getEngineLogger());
        JTabbedPane objectsTabbedPane = new JTabbedPane();
        tileMapPanel = new JPanel();
        JScrollPane tileMapScrollPane = new JScrollPane(tileMapPanel);
        CharactersPanel charactersPanel = new CharactersPanel(guiComponents, modelControlComponents);
        JScrollPane charactersScrollPane = new JScrollPane(charactersPanel);

        tileMapPanel.setBackground(Color.GRAY);

        objectsTabbedPane.add("tiles", tileMapScrollPane);
        objectsTabbedPane.add("characters", charactersScrollPane);

        tileMapPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton newObjectButton = new JButton();
        newObjectButton.setRolloverEnabled(false);
        newObjectButton.addMouseListener(new ButtonHandler(newObjectButton, 48, 48,
                "src/main/resources/img/gui_icons/new_object_button.png",
                "src/main/resources/img/gui_icons/new_object_button_hover.png",
                "src/main/resources/img/gui_icons/new_object_button_pressed.png"));
        newObjectButton.setPreferredSize(new Dimension(48, 48));
        newObjectButton.setToolTipText("Add a new tile");
        newObjectButton.addActionListener(e -> new AddNewTileWindow(guiComponents, modelControlComponents));
        tileMapPanel.add(newObjectButton);

        for (int tileI = 0; tileI < modelControlComponents.getTileMap().getTilesCount(); tileI++) {
            addTileToPanel(modelControlComponents.getTileMap().getTileByIndex(tileI));
        }
        logger.info(this, "Total tiles count: " + modelControlComponents.getTileMap().getTilesCount() + ".");

        this.setLayout(new GridLayout(1, 1));
        this.add(objectsTabbedPane);
    }

    /**
     * Connects logger to current object.
     * @param   logger  Logger, that will be used for logging current class actions.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     *  Adds a new tile to panel.
     * @param tile  Tile to add.
     */
    public void addTileToPanel(Tile tile) {
        ImageIcon tileButtonIcon = new ImageIcon(tile.getSprite().getImage().getScaledInstance(48, 48, Image.SCALE_FAST));
        JButton tileButton = new JButton(tileButtonIcon);
        tileButton.setPreferredSize(new Dimension(48, 48));
        tileButton.setToolTipText(tile.getName());
        tileButton.addMouseListener(new TileMouseListener(tileMapPanel, tileButton, modelControlComponents.getTileMap(), tile.getName()));
        tileButton.addActionListener(e -> {
            if (modelControlComponents.getGameLevels().levelIsLoaded()) {
                modelControlComponents.getGameLevels().addTileToLevel(tile);
                guiComponents.getGamePanel().updateSpritesOnCamera();
            } else {
                JOptionPane.showMessageDialog(this, "No level has been chosen!", "Add tile to level error", JOptionPane.ERROR_MESSAGE);
                logger.warning(this, "No level has been chosen!");
            }
        });
        tileMapPanel.add(tileButton, tileMapPanel.getComponentCount()-1);
        logger.info(this, "A new tile \"" + tile.getName() + "\" has been added to objects panel.");
        revalidate();
    }
}
