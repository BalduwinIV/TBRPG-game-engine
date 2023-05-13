package engine.panels;

import engine.utils.ButtonHandler;
import engine.utils.Logger;
import game.objects.BaseObject;
import tilemap_editor.Tile;
import tilemap_editor.TileMap;

import javax.swing.*;
import java.awt.*;

/**
 * Objects panel class that is used for putting objects to the game field.
 */
public class ObjectsPanel extends JPanel {
    private Logger logger;
    private final TileMap tileMap;
    private final JTabbedPane objectsTabbedPane;
    private final JScrollPane tileMapScrollPane;
    private final JPanel tileMapPanel;
    private final JScrollPane charactersScrollPane;
    private final JPanel charactersPanel;

    public ObjectsPanel(TileMap tileMap) {
        this.tileMap = tileMap;
        objectsTabbedPane = new JTabbedPane();
        tileMapPanel = new JPanel();
        tileMapScrollPane = new JScrollPane(tileMapPanel);
        charactersPanel = new JPanel();
        charactersScrollPane = new JScrollPane(charactersPanel);

        tileMapPanel.setBackground(Color.GRAY);
        charactersPanel.setBackground(Color.DARK_GRAY);

        objectsTabbedPane.add("tiles", tileMapScrollPane);
        objectsTabbedPane.add("characters", charactersScrollPane);

        tileMapPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        for (int tileI = 0; tileI < tileMap.getTilesCount(); tileI++) {
            Tile tile = tileMap.getTile(tileI);
            ImageIcon tileButtonIcon = new ImageIcon(tile.getSprites().getImage().getScaledInstance(48, 48, Image.SCALE_FAST));
            JButton tileButton = new JButton(tileButtonIcon);
            tileButton.setPreferredSize(new Dimension(48, 48));
            tileMapPanel.add(tileButton);
        }
        JButton newObjectButton = new JButton();
        newObjectButton.setRolloverEnabled(false);
        newObjectButton.addMouseListener(new ButtonHandler(newObjectButton, 48, 48,
                "src/main/resources/img/gui_icons/new_object_button.png",
                "src/main/resources/img/gui_icons/new_object_button_hover.png",
                "src/main/resources/img/gui_icons/new_object_button_pressed.png"));
        newObjectButton.setPreferredSize(new Dimension(48, 48));
        tileMapPanel.add(newObjectButton);

        this.setLayout(new GridLayout(1, 1));
        this.add(objectsTabbedPane);
    }

    public ObjectsPanel() {
        this(new TileMap());
    }


    public ObjectsPanel(String tileMapFileLocation) {
        this(new TileMap(tileMapFileLocation));
    }


    /**
     * Connects logger to current object.
     * @param   logger  Logger, that will be used for logging current class actions.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Adds object to objects panel, so that user can easily add a new object to the game field whenever he wants.
     * @param   object  Object, to add to objects panel.
     */
    public void addObject(BaseObject object) {
        /*
        *  Code for GUI implementation.
        *  */
    }

    /**
     * Removes object from objects panel, so that it would not be possible to add this object to the game field, but the rest of the objects will not be affected by these changes.
     * @param   object  Object to remove from ObjectsPanel.
     */
    public void removeObject(BaseObject object) {
        /*
        * Find an object and remove it from the objectsList.
        * ...
        * Remove from GUI.
        * */
    }
}
