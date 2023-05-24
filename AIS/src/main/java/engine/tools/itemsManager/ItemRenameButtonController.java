package engine.tools.itemsManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ItemRenameButtonController implements ActionListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final JButton itemButton;
    private String itemName;

    public ItemRenameButtonController(GUIComponents guiComponents, ModelControlComponents modelControlComponents, JButton itemButton, String itemName) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.itemButton = itemButton;
        this.itemName = itemName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object optionPaneOption = JOptionPane.showInputDialog(guiComponents.getMainWindow(), "Enter " + modelControlComponents.getItemManager().getItem(itemName).getItemType() + " new name:", "New " + modelControlComponents.getItemManager().getItem(itemName).getItemType() + " name window", JOptionPane.QUESTION_MESSAGE, null, null, itemName);
        if (Objects.nonNull(optionPaneOption)) {
            String newName = optionPaneOption.toString();
            if (modelControlComponents.getItemManager().renameItem(itemName, newName)) {
                modelControlComponents.getSaveDataManager().saveJSONFile();
                itemButton.setToolTipText(newName);
                itemName = newName;
            } else {
                JOptionPane.showMessageDialog(guiComponents.getMainWindow(), "New name is not valid.", "Rename error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
