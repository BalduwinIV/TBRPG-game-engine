package engine.tools.itemsManager.WeaponManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  Window for changing weapon stats.
 */
public class ChangeWeaponCharacteristics extends JDialog implements ActionListener {
    private final ModelControlComponents modelControlComponents;
    private final Weapon weapon;
    private final JRadioButton swordType;
    private final JRadioButton axeType;
    private final JRadioButton lanceType;
    private final JRadioButton bowType;
    private final JRadioButton magicType;
    private final JSpinner mightSpinner;
    private final JSpinner hitRateSpinner;
    private final JSpinner criticalHitRateSpinner;
    private final JSpinner rangeSpinner;

    public ChangeWeaponCharacteristics(GUIComponents guiComponents, ModelControlComponents modelControlComponents, Weapon weapon) {
        super(guiComponents.getMainWindow(), "Change weapon characteristics", true);
        this.modelControlComponents = modelControlComponents;
        this.weapon = weapon;

        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(400, 350));

        JLabel label = new JLabel(weapon.getName());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        add(label, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        add(contentPanel, BorderLayout.CENTER);

        JLabel weaponImageLabel = new JLabel(new ImageIcon(weapon.getSprite().getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        weaponImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(weaponImageLabel);

        JPanel itemTypePanel = new JPanel();
        itemTypePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        swordType = new JRadioButton("sword");
        axeType = new JRadioButton("axe");
        lanceType = new JRadioButton("lance");
        bowType = new JRadioButton("bow");
        magicType = new JRadioButton("magic");

        ButtonGroup typeGroup = new ButtonGroup();

        typeGroup.add(swordType);
        typeGroup.add(axeType);
        typeGroup.add(lanceType);
        typeGroup.add(bowType);
        typeGroup.add(magicType);

        itemTypePanel.add(swordType);
        itemTypePanel.add(axeType);
        itemTypePanel.add(lanceType);
        itemTypePanel.add(bowType);
        itemTypePanel.add(magicType);

        contentPanel.add(itemTypePanel);

        JPanel mightSpinnerPanel = new JPanel();
        mightSpinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel mightLabel = new JLabel("might: ");
        mightSpinner = new JSpinner(new SpinnerNumberModel(weapon.getMight(), 0, 100, 1));
        mightSpinnerPanel.add(mightLabel);
        mightSpinnerPanel.add(mightSpinner);

        JPanel hitRateSpinnerPanel = new JPanel();
        hitRateSpinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel hitRateLabel = new JLabel("hitRate: ");
        hitRateSpinner = new JSpinner(new SpinnerNumberModel(weapon.getHitRate(), 0, 100, 1));
        hitRateSpinnerPanel.add(hitRateLabel);
        hitRateSpinnerPanel.add(hitRateSpinner);

        JPanel criticalHitRateSpinnerPanel = new JPanel();
        criticalHitRateSpinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel criticalHitRateLabel = new JLabel("criticalHitRate: ");
        criticalHitRateSpinner = new JSpinner(new SpinnerNumberModel(weapon.getCriticalHitRate(), 0, 100, 1));
        criticalHitRateSpinnerPanel.add(criticalHitRateLabel);
        criticalHitRateSpinnerPanel.add(criticalHitRateSpinner);

        JPanel rangeSpinnerPanel = new JPanel();
        rangeSpinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel rangeLabel = new JLabel("range: ");
        rangeSpinner = new JSpinner(new SpinnerNumberModel(weapon.getRange(), 0, 100, 1));
        rangeSpinnerPanel.add(rangeLabel);
        rangeSpinnerPanel.add(rangeSpinner);

        contentPanel.add(mightSpinnerPanel);
        contentPanel.add(hitRateSpinnerPanel);
        contentPanel.add(criticalHitRateSpinnerPanel);
        contentPanel.add(rangeSpinnerPanel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);

        add(confirmButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(guiComponents.getMainWindow());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (swordType.isSelected()) {
            weapon.setWeaponClass("sword");
        } else if (axeType.isSelected()) {
            weapon.setWeaponClass("axe");
        } else if (lanceType.isSelected()) {
            weapon.setWeaponClass("lance");
        } else if (bowType.isSelected()) {
            weapon.setWeaponClass("bow");
        } else if (magicType.isSelected()) {
            weapon.setWeaponClass("magic");
        } else {
            JOptionPane.showMessageDialog(this, "Item type has not been chosen.", "Item type warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        weapon.setStats((int)mightSpinner.getValue(), (int)hitRateSpinner.getValue(), (int)criticalHitRateSpinner.getValue(), (int)rangeSpinner.getValue());
        modelControlComponents.getItemManager().saveJSONFile();
        dispose();
    }
}
