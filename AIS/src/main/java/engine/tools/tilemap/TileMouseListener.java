package engine.tools.tilemap;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class TileMouseListener implements MouseListener {
    private final JPanel invoker;
    private final JButton tileButton;
    private final TileMap tileMap;
    private String tileName;
    public TileMouseListener(JPanel invoker, JButton tileButton, TileMap tileMap, String tileName) {
        this.invoker = invoker;
        this.tileButton = tileButton;
        this.tileMap = tileMap;
        this.tileName = tileName;
    }

    private void showTilePopupMenu(int xPos, int yPos) {
        JPopupMenu tilePopupMenu = new JPopupMenu("Manage tile");
        JMenuItem renameOption = new JMenuItem("Rename");
        JMenuItem removeOption = new JMenuItem("Remove");
        tilePopupMenu.add(renameOption);
        tilePopupMenu.add(removeOption);

        renameOption.addActionListener(e -> {
            Object optionPaneOption = JOptionPane.showInputDialog(invoker, "Enter tiles new name:", "New tiles name window", JOptionPane.QUESTION_MESSAGE, null, null, tileName);
            if (Objects.nonNull(optionPaneOption)) {
                String newName = optionPaneOption.toString();
                if (tileMap.renameTile(tileName, newName)) {
                    tileMap.saveJSONFile();
                    tileButton.setToolTipText(newName);
                    tileName = newName;
                } else {
                    JOptionPane.showMessageDialog(invoker, "New name is not valid.", "Rename error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        removeOption.addActionListener(e -> {
            if (tileMap.removeTile(tileName)) {
                tileMap.saveJSONFile();
                invoker.remove(tileButton);
            }
        });

        tilePopupMenu.show(invoker, xPos, yPos);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            showTilePopupMenu(tileButton.getLocation().x + tileButton.getWidth(), tileButton.getLocation().y + tileButton.getHeight());
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
