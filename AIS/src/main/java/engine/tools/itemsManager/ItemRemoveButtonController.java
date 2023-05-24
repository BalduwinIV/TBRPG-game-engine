package engine.tools.itemsManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemRemoveButtonController implements ActionListener {
    private final GUIComponents guiComponents;
    private final JButton itemButton;
    private final String itemName;
    private final boolean isInCharactersInventory;

    public ItemRemoveButtonController(GUIComponents guiComponents, ModelControlComponents modelControlComponents, JButton itemButton, String itemName, boolean isInCharactersInventory) {
        this.guiComponents = guiComponents;
        this.itemButton = itemButton;
        this.itemName = itemName;
        this.isInCharactersInventory = isInCharactersInventory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        guiComponents.getInventoryPanel().removeItemFromCharacterInventory(itemButton, itemName);
        if (!isInCharactersInventory) {
            guiComponents.getInventoryPanel().removeItemButtonFromKnownItems(itemButton, itemName);
        }
    }
}
