package engine;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Console implements Runnable {
    private JFrame consoleFrame;
    private JTextArea textArea;
    private PrintStream consoleOutput;

    public Console () {
        this.consoleFrame = new JFrame();
        this.consoleFrame.add(new JLabel("Console"), BorderLayout.NORTH);

        this.textArea = new JTextArea();
//        OutputStream outputStream = new OutputStream() {
//            @Override
//            public void write(int b) throws IOException {
//
//            }
//        }
//        this.consoleOutput = new PrintStream();
    }
    @Override
    public void run() {

    }
}
