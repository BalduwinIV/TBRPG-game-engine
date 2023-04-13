package engine.panels;

import engine.utils.Logger;
import game.objects.BaseObject;

import javax.swing.*;

/**
 * Parameters panel that shows a specific objects parameters.
 */
public class ParametersPanel extends JPanel {
    private Logger logger;
    private BaseObject currentObject;

    public ParametersPanel() {
        /*
        * Initialising new class object.
        * ...
        * Starting GUI for this class.
        * */
    }

    /**
     * Connects logger to current object.
     * @param   logger  Logger, that will be used for logging current class actions.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Find parameter of given name, change its value and write these changes to the data file.
     * @param   parameterName   Name of parameter, which value should be changed.
     * @param   newValue        New parameters value.
     */
    public void changeParameter(String parameterName, Double newValue) {
        return;
    }

    /**
     * Find parameter of given name, change its value and write these changes to the data file.
     * @param   parameterName   Name of parameter, which value should be changed.
     * @param   newValue        New parameters value.
     */
    public void changeParameter(String parameterName, Boolean newValue) {
        return;
    }

    /**
     * Find parameter of given name, change its value and write these changes to the data file.
     * @param   parameterName   Name of parameter, which value should be changed.
     * @param   newValue        New parameters value.
     */
    public void changeParameter(String parameterName, String newValue) {
        return;
    }

    /**
     * Sets current object to show all its parameters in panel.
     * @param   new_object  New object, which parameters is going to be changed.
     */
    public void setCurrentObject(BaseObject new_object) {
        this.currentObject = new_object;
        /*
        * Show new_object parameters in GUI.
        * */
    }

    /**
     * Returns an object which parameters is being showed.
     * @return  An object which parameters is being showed.
     */
    public BaseObject getCurrentObject() {
        return this.currentObject;
    }

    /**
     * Hide currentObject parameters and clear parameters panel.
     */
    public void resetCurrentObject() {
        this.currentObject = null;
        /*
        * Clear panel from previous objects parameters.
        * */
    }
}
