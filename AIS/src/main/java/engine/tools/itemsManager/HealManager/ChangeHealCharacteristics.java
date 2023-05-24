package engine.tools.itemsManager.HealManager;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  Window for changing heal item stats.
 */
public class ChangeHealCharacteristics extends JDialog implements ActionListener {
    private final ModelControlComponents modelControlComponents;
    private final Heal heal;
    private final JSpinner healAmountSpinner;
    private final JSpinner healRangeSpinner;

    public ChangeHealCharacteristics(GUIComponents guiComponents, ModelControlComponents modelControlComponents, Heal heal) {
        super(guiComponents.getMainWindow(), "Change heal characteristics", true);
        this.modelControlComponents = modelControlComponents;
        this.heal = heal;

        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(200, 250));

        JLabel label = new JLabel(heal.getName());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        add(label, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        add(contentPanel, BorderLayout.CENTER);

        JLabel healImageLabel = new JLabel(new ImageIcon(heal.getSprite().getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        healImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(healImageLabel);

        JPanel healAmountSpinnerPanel = new JPanel();
        healAmountSpinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel healAmountLabel = new JLabel("healAmount: ");
        healAmountSpinner = new JSpinner(new SpinnerNumberModel(heal.getHealAmount(), 0, 30, 1));
        healAmountSpinnerPanel.add(healAmountLabel);
        healAmountSpinnerPanel.add(healAmountSpinner);

        JPanel healRangeSpinnerPanel = new JPanel();
        healRangeSpinnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel healRangeLabel = new JLabel("healRange: ");
        healRangeSpinner = new JSpinner(new SpinnerNumberModel(heal.getHealRange(), 1, 4, 1));
        healRangeSpinnerPanel.add(healRangeLabel);
        healRangeSpinnerPanel.add(healRangeSpinner);

        contentPanel.add(healAmountSpinnerPanel);
        contentPanel.add(healRangeSpinnerPanel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);

        add(confirmButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(guiComponents.getMainWindow());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        heal.setHealAmount((int) healAmountSpinner.getValue());
        heal.setHealRange((int) healRangeSpinner.getValue());
        modelControlComponents.getItemManager().saveJSONFile();
        dispose();
    }
}
