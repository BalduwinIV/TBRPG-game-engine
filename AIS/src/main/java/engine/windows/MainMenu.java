package engine.windows;

import engine.tools.GUIComponents;
import engine.tools.ModelControlComponents;
import engine.utils.*;

import javax.swing.*;
import java.awt.*;

/**
 *  Main menu window class.
 */
public class MainMenu extends JFrame {
    private final Logger logger;
    public MainMenu(Logger logger) {
        super();
        this.logger = logger;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("AIS menu");
        setSize(250, 275);
        setPreferredSize(getSize());
    }

    /**
     *  Show main menu window.
     */
    public void start() {
        logger.info(this, "Creating AIS menu window.");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(0xb3b3b3));
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel label = new JLabel(new ImageIcon("src/main/resources/img/gui_icons/title_label.png"));

        GUIComponents guiComponents = new GUIComponents();
        guiComponents.setMainWindow(this);
        ModelControlComponents modelControlComponents = new ModelControlComponents();
        modelControlComponents.getSaveDataEditorLogger().startLogging();
        modelControlComponents.getEngineLogger().startLogging();
        modelControlComponents.getGameLogger().startLogging();

        JButton startGameButton = new JButton();
        startGameButton.addMouseListener(new ButtonHandler(startGameButton, 150, 50,
                "src/main/resources/img/gui_icons/start_game.png",
                "src/main/resources/img/gui_icons/start_game_hover.png",
                "src/main/resources/img/gui_icons/start_game_pressed.png"));
        startGameButton.setRolloverEnabled(false);
        startGameButton.setPreferredSize(new Dimension(150, 50));
        startGameButton.addActionListener(e -> {
            logger.info(this, "Starting game window creation...");
            ChooseSaveDataWindow chooseSaveDataWindow = new ChooseSaveDataWindow(guiComponents, modelControlComponents);
            modelControlComponents.getGameLogger().info(chooseSaveDataWindow, "Creating save data chooser window...");
            chooseSaveDataWindow.start();
            logger.info(this, "Game has been started. Closing menu...");
            logger.stopLogging();
            dispose();
        });

        JButton startEngineButton = new JButton();
        startEngineButton.addMouseListener(new ButtonHandler(startEngineButton, 150, 50,
                "src/main/resources/img/gui_icons/start_engine.png",
                "src/main/resources/img/gui_icons/start_engine_hover.png",
                "src/main/resources/img/gui_icons/start_engine_pressed.png"));
        startEngineButton.setRolloverEnabled(false);
        startEngineButton.setPreferredSize(new Dimension(150, 50));
        startEngineButton.addActionListener(e -> {
            logger.info(this, "Starting engine window creation...");
            MainWindow mainWindow = new MainWindow(guiComponents, modelControlComponents);
            modelControlComponents.getEngineLogger().info(mainWindow, "Creating MainWindow.");
            mainWindow.start();
            logger.info(this, "Engine window has been created. Closing menu...");
            logger.stopLogging();
            dispose();
        });

        JButton editDataButton = new JButton();
        editDataButton.addMouseListener(new ButtonHandler(editDataButton, 150, 50,
                "src/main/resources/img/gui_icons/edit_data.png",
                "src/main/resources/img/gui_icons/edit_data_hover.png",
                "src/main/resources/img/gui_icons/edit_data_pressed.png"));
        editDataButton.setRolloverEnabled(false);
        editDataButton.setPreferredSize(new Dimension(150, 50));
        editDataButton.addActionListener(e -> {
            logger.info(this, "Starting save data template file editor window creation...");
            EditSaveData editSaveData = new EditSaveData(guiComponents ,modelControlComponents);
            editSaveData.start();
            logger.info(this, "Editor window has been created. Closing menu...");
            logger.stopLogging();
            dispose();
        });

        menuPanel.add(label);
        menuPanel.add(startGameButton);
        menuPanel.add(startEngineButton);
        menuPanel.add(editDataButton);

        add(menuPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
