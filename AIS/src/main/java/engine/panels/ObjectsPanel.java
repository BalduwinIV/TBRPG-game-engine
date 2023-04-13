package engine.panels;

import engine.utils.Logger;
import game.objects.BaseObject;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Objects panel class that is used for putting objects to the game field.
 */
public class ObjectsPanel extends JPanel {
    private Logger logger;
    private ArrayList<BaseObject> objectsList;

    public ObjectsPanel() {
        objectsList = new ArrayList<>();
        /*
          Initializing ObjectsPanel object.
         */
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
        objectsList.add(object);
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
