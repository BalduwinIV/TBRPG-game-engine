package engine.panels;

import engine.GUIColors;
import engine.tools.ProgramState;
import engine.tools.itemsManager.AddNewKnownItemWindow;
import engine.tools.itemsManager.HealManager.HealMouseListener;
import engine.tools.itemsManager.WeaponManager.WeaponMouseListener;
import engine.tools.characterManager.Character;
import engine.tools.itemsManager.InventoryItem;
import engine.tools.itemsManager.Item;
import engine.utils.ButtonHandler;
import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 *  Panel for viewing items in inventory.
 */
public class InventoryPanel extends JPanel {
    private final ModelControlComponents modelControlComponents;
    private final GUIComponents guiComponents;
    private final Character character;
    private final JPanel characterItems;
    private JPanel knownItems;
    public InventoryPanel(ModelControlComponents modelControlComponents, GUIComponents guiComponents, Character character) {
        this.modelControlComponents = modelControlComponents;
        this.guiComponents = guiComponents;
        guiComponents.setInventoryPanel(this);
        this.character = character;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        if (modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
            setPreferredSize(new Dimension(400, 64));
        } else {
            setPreferredSize(new Dimension(400, 200));
        }

        characterItems = new JPanel();

        JScrollPane characterItemsScrollPane = new JScrollPane(characterItems);

        characterItems.setBackground(new Color(GUIColors.color(GUIColors.Colors.INVENTORY_CHARACTER)));

        for (int itemI = 0; itemI < character.getInventory().getInventorySize(); itemI++) {
            addItemToCharacterInventory(character.getInventory().getInventoryItem(itemI));
        }

        add(characterItemsScrollPane);

        if (!modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
            knownItems = new JPanel();
            JScrollPane knownItemsScrollPane = new JScrollPane(knownItems);
            knownItems.setBackground(new Color(GUIColors.color(GUIColors.Colors.INVENTORY_KNOWN_ITEMS)));
            JButton addNewKnownItem = new JButton();
            addNewKnownItem.addMouseListener(new ButtonHandler(addNewKnownItem, 48, 48,
                    "src/main/resources/img/gui_icons/new_object_button.png",
                    "src/main/resources/img/gui_icons/new_object_button_hover.png",
                    "src/main/resources/img/gui_icons/new_object_button_pressed.png"));
            addNewKnownItem.setToolTipText("Add a new known item.");
            addNewKnownItem.addActionListener(e -> new AddNewKnownItemWindow(guiComponents, modelControlComponents));
            knownItems.add(addNewKnownItem);
            for (int itemI = 0; itemI < modelControlComponents.getItemManager().getItemsAmount(); itemI++) {
                addItemToKnownItems(modelControlComponents.getItemManager().getItem(itemI));
            }
            add(knownItemsScrollPane);
        }
    }

    /**
     *  Adds item from known items panel, to characters inventory.
     * @param inventoryItem Known item.
     */
    public void addItemToCharacterInventory(InventoryItem inventoryItem) {
        JButton itemButton = new JButton();
        itemButton.setIcon(new ImageIcon(inventoryItem.getItem().getSprite().getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
        itemButton.setBackground(new Color(GUIColors.color(GUIColors.Colors.INVENTORY_CHARACTER)));
        itemButton.setPreferredSize(new Dimension(48, 48));
        itemButton.setRolloverEnabled(false);
        itemButton.setToolTipText(inventoryItem.getItem().getName());
        if (inventoryItem.getItem().getItemType().equals("weapon")) {
            if (modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
                itemButton.addActionListener(e -> guiComponents.getGamePanel().prepareItemToUse(inventoryItem));
            } else {
                itemButton.addMouseListener(new WeaponMouseListener(guiComponents, modelControlComponents, itemButton, inventoryItem.getItem(), true));
            }
        } else if (inventoryItem.getItem().getItemType().equals("heal")) {
            if (modelControlComponents.getProgramState().equals(ProgramState.GAME)) {
                itemButton.addActionListener(e -> guiComponents.getGamePanel().prepareItemToUse(inventoryItem));
            } else {
                itemButton.addMouseListener(new HealMouseListener(guiComponents, modelControlComponents, itemButton, inventoryItem.getItem(), true));
            }
        }

        characterItems.add(itemButton);
        characterItems.revalidate();
    }

    /**
     *  Adds item to known items panel.
     * @param item  New item.
     */
    public void addItemToKnownItems(Item item) {
        JButton itemButton = new JButton();
        itemButton.setIcon(new ImageIcon(item.getSprite().getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
        itemButton.setBackground(new Color(GUIColors.color(GUIColors.Colors.INVENTORY_KNOWN_ITEMS)));
        itemButton.setPreferredSize(new Dimension(48, 48));
        itemButton.setRolloverEnabled(false);
        itemButton.setToolTipText(item.getName());
        if (item.getItemType().equals("weapon")) {
            itemButton.addMouseListener(new WeaponMouseListener(guiComponents, modelControlComponents, itemButton, item, false));
        } else if (item.getItemType().equals("heal")) {
            itemButton.addMouseListener(new HealMouseListener(guiComponents, modelControlComponents, itemButton, item, false));
        }
        itemButton.addActionListener(e -> {
            character.getInventory().addItem(item, 1);
            addItemToCharacterInventory(character.getInventory().getInventoryItem(character.getInventory().getInventorySize()-1));
        });

        knownItems.add(itemButton, knownItems.getComponentCount()-1);
        knownItems.revalidate();
    }

    /**
     *  Removes item from characters inventory panel.
     * @param itemButton Character items button.
     */
    public void removeItemFromCharacterInventory(JButton itemButton, String itemName) {
        InventoryItem inventoryItem = character.getInventory().getInventoryItem(itemName);
        if (Objects.nonNull(inventoryItem)) {
            character.getInventory().remove(inventoryItem);
        }
        characterItems.remove(itemButton);
        characterItems.revalidate();
    }

    /**
     *  Removes item from known items panel.
     * @param itemButton Known items button.
     */
    public void removeItemButtonFromKnownItems(JButton itemButton, String itemName) {
        modelControlComponents.getItemManager().removeItem(itemName);
        knownItems.remove(itemButton);
        knownItems.revalidate();
    }
}
