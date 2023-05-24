package engine.tools.itemsManager.WeaponManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.itemsManager.Item;
import engine.tools.itemsManager.ItemRemoveButtonController;
import engine.tools.itemsManager.ItemRenameButtonController;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WeaponMouseListener implements MouseListener {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final JButton weaponButton;
    private final boolean isInCharactersInventory;
    private final Item item;

    public WeaponMouseListener(GUIComponents guiComponents, ModelControlComponents modelControlComponents, JButton weaponButton, Item item, boolean isInCharactersInventory) {
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        this.weaponButton = weaponButton;
        this.item = item;
        this.isInCharactersInventory = isInCharactersInventory;
    }

    private void showCharacterPopupMenu(int xPos, int yPos) {
        JPopupMenu characterPopupMenu = new JPopupMenu("Manage weapon");
        JMenuItem renameOption = new JMenuItem("Rename");
        JMenuItem changeOption = new JMenuItem("Change");
        JMenuItem removeOption = new JMenuItem("Remove");
        characterPopupMenu.add(renameOption);
        characterPopupMenu.add(changeOption);
        characterPopupMenu.add(removeOption);

        renameOption.addActionListener(new ItemRenameButtonController(guiComponents, modelControlComponents, weaponButton, item.getName()));
        changeOption.addActionListener(e -> new ChangeWeaponCharacteristics(guiComponents, modelControlComponents, (Weapon) modelControlComponents.getItemManager().getItem(item.getName())));
        removeOption.addActionListener(new ItemRemoveButtonController(guiComponents, modelControlComponents, weaponButton, item.getName(), isInCharactersInventory));

        characterPopupMenu.show(weaponButton.getParent(), xPos, yPos);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            showCharacterPopupMenu(weaponButton.getLocation().x + weaponButton.getWidth(), weaponButton.getLocation().y + weaponButton.getHeight());
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
