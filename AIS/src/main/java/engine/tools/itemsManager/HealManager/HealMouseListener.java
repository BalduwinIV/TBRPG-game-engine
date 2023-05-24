package engine.tools.itemsManager.HealManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.itemsManager.Item;
import engine.tools.itemsManager.ItemRemoveButtonController;
import engine.tools.itemsManager.ItemRenameButtonController;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HealMouseListener implements MouseListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final JButton healButton;
    private final boolean isInCharactersInventory;
    private final Item item;

    public HealMouseListener(GUIComponents guiComponents, ModelControlComponents modelControlComponents, JButton healButton, Item item, boolean isInCharactersInventory) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.healButton = healButton;
        this.item = item;
        this.isInCharactersInventory = isInCharactersInventory;
    }

    private void showCharacterPopupMenu(int xPos, int yPos) {
        JPopupMenu characterPopupMenu = new JPopupMenu("Manage heal");
        JMenuItem renameOption = new JMenuItem("Rename");
        JMenuItem changeOption = new JMenuItem("Change");
        JMenuItem removeOption = new JMenuItem("Remove");
        characterPopupMenu.add(renameOption);
        characterPopupMenu.add(changeOption);
        characterPopupMenu.add(removeOption);

        renameOption.addActionListener(new ItemRenameButtonController(guiComponents, modelControlComponents, healButton, item.getName()));
        changeOption.addActionListener(e -> new ChangeHealCharacteristics(guiComponents, modelControlComponents, (Heal) modelControlComponents.getItemManager().getItem(item.getName())));
        removeOption.addActionListener(new ItemRemoveButtonController(guiComponents, modelControlComponents, healButton, item.getName(), isInCharactersInventory));

        characterPopupMenu.show(healButton.getParent(), xPos, yPos);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            showCharacterPopupMenu(healButton.getLocation().x + healButton.getWidth(), healButton.getLocation().y + healButton.getHeight());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
