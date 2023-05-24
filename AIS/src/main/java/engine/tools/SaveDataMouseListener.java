package engine.tools;

import engine.windows.ChooseSaveDataWindow;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SaveDataMouseListener implements MouseListener {
    private final ModelControlComponents modelControlComponents;
    private final ChooseSaveDataWindow chooseSaveDataWindow;
    private final JButton saveDataButton;
    private final int saveDataIndex;

    public SaveDataMouseListener(ModelControlComponents modelControlComponents, ChooseSaveDataWindow chooseSaveDataWindow, JButton saveDataButton, int saveDataIndex) {
        this.modelControlComponents = modelControlComponents;
        this.chooseSaveDataWindow = chooseSaveDataWindow;
        this.saveDataButton = saveDataButton;
        this.saveDataIndex = saveDataIndex;
    }

    private void showSaveDataPopupMenu(int xPos, int yPos) {
        JPopupMenu saveDataPopupMenu = new JPopupMenu("Manage weapon");
        JMenuItem removeOption = new JMenuItem("Remove");
        saveDataPopupMenu.add(removeOption);

        removeOption.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(chooseSaveDataWindow, "Are you sure that you want to remove Save Data #" + saveDataIndex + "?", "Remove Confirmation", JOptionPane.YES_NO_OPTION) == 0) {
                if (modelControlComponents.getSaveDataManager().removeSaveData(saveDataIndex)) {
                    modelControlComponents.getSaveDataManager().saveJSONFile();
                    chooseSaveDataWindow.updateSaveDataChooser();
                }
            }
        });

        saveDataPopupMenu.show(saveDataButton.getParent(), xPos, yPos);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            showSaveDataPopupMenu(saveDataButton.getLocation().x, saveDataButton.getLocation().y);
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
