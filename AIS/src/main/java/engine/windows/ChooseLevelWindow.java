package engine.windows;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.tools.ProgramState;
import engine.utils.ImageStorage;
import engine.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChooseLevelWindow extends JFrame {
    private final GUIComponents guiComponents;
    private final ModelControlComponents modelControlComponents;
    private final Logger logger;
    private JPanel levelChooser;

    public ChooseLevelWindow(GUIComponents guiComponents, ModelControlComponents modelControlComponents) {
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
        setTitle("Choose Level");
        setSize(new Dimension(300, 400));
        setPreferredSize(getSize());
        setLocationRelativeTo(null);
    }

    public void start() {
        getContentPane().setBackground(new Color(0x808080));
        getContentPane().setLayout(new BorderLayout(5, 5));

        JLabel label = new JLabel(new ImageIcon(new ImageStorage("src/main/resources/img/gui_icons/choose_level_label.png").getImage()));

        levelChooser = new JPanel();
        levelChooser.setBackground(new Color(0x808080));
        levelChooser.setLayout(new BoxLayout(levelChooser, BoxLayout.Y_AXIS));
        JScrollPane levelChooserScrollPane = new JScrollPane(levelChooser);

        updateLevelChooser();

        add(label, BorderLayout.NORTH);
        add(levelChooserScrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateLevelChooser() {
        levelChooser.removeAll();
        for (String levelName : modelControlComponents.getGameLevels().getLevelNamesList()) {
            addLevelButton(levelName);
        }
        revalidate();
    }

    public void addLevelButton(String levelName) {
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(1, 1));
        JButton levelButton = new JButton(levelName);
        if (modelControlComponents.getSaveDataManager().levelIsCleared(levelName)) {
            levelButton.setBackground(new Color(0x00ff36));
        } else {
            levelButton.setBackground(new Color(0xf64e5b));
        }
        levelButton.setForeground(Color.WHITE);
        levelButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        levelButton.addActionListener(e -> {
            logger.info(this, "Chosen level: " + levelName + ".");
            modelControlComponents.getGameLevels().loadLevel(levelName);
            GameWindow gameWindow = new GameWindow(guiComponents, modelControlComponents);
            logger.info(this, "Starting game...");
            gameWindow.start();
            logger.info(this, "Game is running.");
            logger.info(this, "Closing level choosing window...");
            dispose();
        });
        levelPanel.add(levelButton);
        levelChooser.add(levelPanel);
        revalidate();
    }
}
