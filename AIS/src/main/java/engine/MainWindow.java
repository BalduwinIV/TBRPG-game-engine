package engine;

import game.GamePanel;
import game.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    JFrame window;
    public MainWindow() {
        this.window = new JFrame();
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setResizable(true);
        this.window.setTitle("AIS Engine");
        this.window.setSize(1280, 720);
        this.window.setPreferredSize(this.window.getSize());
    }

    public void start() {
        Button button;
        GamePanel gameWindow = new GamePanel(1280,720);
        gameWindow.setPreferredSize(new Dimension(1280, 720));
        JPanel toolBar = new JPanel();
        GridLayout toolBarGrid = new GridLayout(1, 3);
        toolBar.setLayout(toolBarGrid);
        toolBar.add(new Button("Menu"));
        button = new Button("Compile");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameWindow.isGameThreadIsRunning()) {
                    gameWindow.stopGameThread();
                } else {
                    gameWindow.startGameThread();
                }
            }
        });
        toolBar.add(button);
        toolBar.add(new Button("Options"));

        window.setLayout(new BorderLayout(5, 5));

        window.add(toolBar, BorderLayout.NORTH);

        button = new Button("Objects");
        button.setPreferredSize(new Dimension(250, 720));
        window.add(button, BorderLayout.WEST);

        window.add(gameWindow, BorderLayout.CENTER);

        button = new Button("Parameters");
        button.setPreferredSize(new Dimension(320, 720));
        window.add(button, BorderLayout.EAST);

        button = new Button("Console");
        button.setPreferredSize(new Dimension(1850, 300));
        window.add(button, BorderLayout.SOUTH);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
