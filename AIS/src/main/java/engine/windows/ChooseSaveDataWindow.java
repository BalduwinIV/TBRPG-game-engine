package engine.windows;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.ProgramState;
import engine.tools.SaveDataMouseListener;
import engine.utils.ImageStorage;
import engine.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChooseSaveDataWindow extends JFrame {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final Logger logger;
    private JPanel saveDataChooser;

    public ChooseSaveDataWindow(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
        super();
        this.guiComponents = guiComponents;
        this.modelControlComponents = modelControlComponents;
        modelControlComponents.setProgramState(ProgramState.GAME);
        this.logger = modelControlComponents.getGameLogger();

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
        setTitle("Choose Save Data");
        setSize(new Dimension(300, 400));
        setPreferredSize(getSize());
        setLocationRelativeTo(null);
    }

    public void start() {
        getContentPane().setBackground(new Color(0x808080));
        getContentPane().setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel(new ImageIcon(new ImageStorage("src/main/resources/img/gui_icons/choose_save_data_label.png").getImage()));

        saveDataChooser = new JPanel();
        saveDataChooser.setBackground(new Color(0x808080));
        saveDataChooser.setLayout(new BoxLayout(saveDataChooser, BoxLayout.Y_AXIS));
        JScrollPane saveDataChooserScrollPane = new JScrollPane(saveDataChooser);

        updateSaveDataChooser();

        add(label, BorderLayout.NORTH);
        add(saveDataChooserScrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateSaveDataChooser() {
        saveDataChooser.removeAll();
        JPanel createNewSaveDataPanel = new JPanel();
        createNewSaveDataPanel.setLayout(new GridLayout(1, 1));
        JButton createNewSaveDataButton = new JButton("CREATE A NEW SAVE DATA");
        createNewSaveDataButton.setBackground(new Color(0x0097ff));
        createNewSaveDataButton.setForeground(Color.WHITE);
        createNewSaveDataButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        createNewSaveDataButton.addActionListener(e -> {
            modelControlComponents.getSaveDataManager().addSaveData();
            modelControlComponents.getSaveDataManager().saveJSONFile();
            addSaveDataButton();
        });
        createNewSaveDataPanel.add(createNewSaveDataButton);
        saveDataChooser.add(createNewSaveDataPanel);

        for (int saveDataI = 1; saveDataI < modelControlComponents.getSaveDataManager().getSaveDataCount(); saveDataI++) {
            addSaveDataButton();
        }
        revalidate();
    }

    public void addSaveDataButton() {
        JPanel saveDataPanel = new JPanel();
        saveDataPanel.setLayout(new GridLayout(1, 1));
        int saveDataIndex = saveDataChooser.getComponentCount();
        JButton saveDataButton = new JButton("SAVE DATA #" + saveDataChooser.getComponentCount());
        saveDataButton.setBackground(new Color(0x0097ff));
        saveDataButton.setForeground(Color.WHITE);
        saveDataButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        saveDataButton.addActionListener(e -> {
            modelControlComponents.getSaveDataManager().loadSaveData(saveDataIndex);
            logger.info(this, "Save Data #" + saveDataIndex + " has been chosen.");
            ChooseLevelWindow chooseLevelWindow = new ChooseLevelWindow(guiComponents, modelControlComponents);
            logger.info(this, "Creating choosing level window...");
            chooseLevelWindow.start();
            logger.info(this, "Choosing level window has been created.");
            logger.info(this, "Closing Save Data Chooser...");
            dispose();
        });
        saveDataButton.addMouseListener(new SaveDataMouseListener(modelControlComponents, this, saveDataButton, saveDataIndex));
        saveDataPanel.add(saveDataButton);
        saveDataChooser.add(saveDataPanel, saveDataIndex-1);
        revalidate();
    }
}
