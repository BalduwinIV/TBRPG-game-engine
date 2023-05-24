package engine.windows;

import engine.panels.CharactersPanel;
import engine.panels.LoggerPanel;
import engine.panels.ParametersPanel;
import engine.tools.ProgramState;
import engine.utils.ButtonHandler;
import engine.tools.GUIComponents;
import engine.utils.Logger;
import engine.tools.ModelControlComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditSaveData extends JFrame {
    private final Logger logger;
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;

    public EditSaveData(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        super();
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        modelControlComponents.setProgramState(ProgramState.SAVE_DATA_EDITOR);
        this.logger = modelControlComponents.getSaveDataEditorLogger();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                modelControlComponents.getEngineLogger().stopLogging();
                modelControlComponents.getGameLogger().stopLogging();
                modelControlComponents.getSaveDataEditorLogger().stopLogging();

                Logger startLevelLogger = new Logger("StartWindowLogger.log");
                startLevelLogger.startLogging();
                MainMenu mainMenu = new MainMenu(startLevelLogger);
                mainMenu.start();

                super.windowClosing(e);
            }
        });
        setResizable(true);
        setTitle("Save Data Editor");
        setSize(new Dimension(1280, 720));
        setPreferredSize(getSize());
        setLocationRelativeTo(null);
    }

    public void start() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(0xcccccc));

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new GridLayout(1, 3, 10, 3));

        JPanel saveDataSelectorPanel = new JPanel();
        saveDataSelectorPanel.setLayout(new BorderLayout(5, 5));

        JComboBox<String> saveDataSelector = new JComboBox<>();
        saveDataSelector.addItem("Save Data template");
        for (int saveDataI = 1; saveDataI < modelControlComponents.getSaveDataManager().getSaveDataCount(); saveDataI++) {
            saveDataSelector.addItem(Integer.toString(saveDataI));
        }
        saveDataSelector.addItem("-- create new save data --");

        JButton saveButton = new JButton();
        saveButton.addActionListener(e -> modelControlComponents.getSaveDataManager().saveJSONFile());
        saveButton.addMouseListener(new ButtonHandler(saveButton, 30, 30,
                "src/main/resources/img/gui_icons/save_button.png",
                "src/main/resources/img/gui_icons/save_button_hover.png",
                "src/main/resources/img/gui_icons/save_button_pressed.png"));
        saveButton.setRolloverEnabled(false);
        saveButton.setPreferredSize(new Dimension(30, 30));
        saveButton.setToolTipText("save");

        JButton removeButton = new JButton();
        removeButton.addActionListener(e -> {
            if (saveDataSelector.getSelectedIndex() > 0) {
                int optionPaneOption = JOptionPane.showConfirmDialog(this, "Are you sure that you want to remove save data #" + saveDataSelector.getSelectedItem().toString() + "?");
                if (optionPaneOption == JOptionPane.YES_OPTION) {
                    if (modelControlComponents.getSaveDataManager().removeSaveData(saveDataSelector.getSelectedIndex())) {
                        modelControlComponents.getSaveDataManager().saveJSONFile();
                        saveDataSelector.removeItemAt(saveDataSelector.getSelectedIndex());
                    } else {
                        JOptionPane.showMessageDialog(this, "An error occurred while removing save data #" + saveDataSelector.getSelectedIndex() + ".");
                    }
                }
            }
        });
        removeButton.addMouseListener(new ButtonHandler(removeButton, 30, 30,
                "src/main/resources/img/gui_icons/remove_button.png",
                "src/main/resources/img/gui_icons/remove_button_hover.png",
                "src/main/resources/img/gui_icons/remove_button_pressed.png"));
        removeButton.setRolloverEnabled(false);
        removeButton.setPreferredSize(new Dimension(30, 30));
        removeButton.setToolTipText("remove");
        removeButton.setEnabled(saveDataSelector.getSelectedIndex() > 0);

        saveDataSelector.addActionListener(e -> {
            removeButton.setEnabled(saveDataSelector.getSelectedIndex() > 0);
            if (saveDataSelector.getSelectedItem().toString().equals("-- create new save data --")) {
                logger.info(this, "Creating save data #" + (saveDataSelector.getItemCount()-1) + ".");
                modelControlComponents.getSaveDataManager().addSaveData();
                modelControlComponents.getSaveDataManager().saveJSONFile();
                modelControlComponents.getSaveDataManager().loadSaveData(saveDataSelector.getItemCount()-1);
                saveDataSelector.insertItemAt(Integer.toString(saveDataSelector.getItemCount()-1), saveDataSelector.getItemCount()-1);
                saveDataSelector.setSelectedIndex(saveDataSelector.getItemCount()-2);
            } else {
                if (saveDataSelector.getSelectedIndex() == 0) {
                    logger.info(this, "Loading template save data.");
                } else {
                    logger.info(this, "Loading save data #" + saveDataSelector.getSelectedIndex() + ".");
                }
                modelControlComponents.getSaveDataManager().loadSaveData(saveDataSelector.getSelectedIndex());
                guiComponents.getCharactersPanel().updatePanel();
            }
            guiComponents.getParametersPanel().clearDisplay();
        });

        JPanel saveDataSelectorButtonsPanel = new JPanel();
        saveDataSelectorButtonsPanel.setLayout(new GridLayout(1, 2, 0, 0));

        saveDataSelectorButtonsPanel.add(saveButton);
        saveDataSelectorButtonsPanel.add(removeButton);

        saveDataSelectorPanel.add(saveDataSelector, BorderLayout.CENTER);
        saveDataSelectorPanel.add(saveDataSelectorButtonsPanel, BorderLayout.EAST);

        toolBar.add(saveDataSelectorPanel);

        LoggerPanel loggerPanel = new LoggerPanel();
        loggerPanel.addLogger(modelControlComponents.getGameLogger());
        loggerPanel.addLogger(modelControlComponents.getEngineLogger());
        loggerPanel.addLogger(modelControlComponents.getSaveDataEditorLogger());
        loggerPanel.setPreferredSize(new Dimension(250, 720));

        CharactersPanel charactersPanel = new CharactersPanel(guiComponents, modelControlComponents);
        JScrollPane charactersScrollPane = new JScrollPane(charactersPanel);
        charactersScrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        ParametersPanel parametersPanel = new ParametersPanel(guiComponents, modelControlComponents);

        toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        loggerPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
        charactersScrollPane.setBorder(new EmptyBorder(0, 0, 5, 0));
        parametersPanel.setBorder(new EmptyBorder(0, 5, 5, 5));

        modelControlComponents.getSaveDataManager().loadSaveData(saveDataSelector.getSelectedIndex());
        guiComponents.getCharactersPanel().updatePanel();

        add(toolBar, BorderLayout.NORTH);
        add(loggerPanel, BorderLayout.WEST);
        add(charactersScrollPane, BorderLayout.CENTER);
        add(parametersPanel, BorderLayout.EAST);

        pack();
        setVisible(true);
    }
}
