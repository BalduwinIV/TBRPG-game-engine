package engine.panels;

import engine.tools.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.ArrayList;

public class LoggerPanel extends JPanel {
    ArrayList<Logger> loggers;
    private final JTabbedPane tabbedPane;
    private final ArrayList<JTextPane> textAreas;
    private final ArrayList<Document> documents;
    private final SimpleAttributeSet errorMessageStyle;
    private final SimpleAttributeSet infoMessageStyle;
    private final SimpleAttributeSet warningMessageStyle;
    private JScrollPane scrollTextArea;

    public LoggerPanel () {
        loggers = new ArrayList<>(1);
        documents = new ArrayList<>(1);
        textAreas = new ArrayList<>(1);

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(234, 234, 234));

        tabbedPane = new JTabbedPane();

        JTextPane new_textArea = new JTextPane();
        new_textArea.setEditable(false);
        new_textArea.setBackground(new Color(47, 45, 45));
        textAreas.add(new_textArea);

        Document new_document = new_textArea.getStyledDocument();
        new_textArea.setDocument(new_document);
        documents.add(new_document);

        errorMessageStyle = new SimpleAttributeSet();
        StyleConstants.setBold(errorMessageStyle, true);
        StyleConstants.setForeground(errorMessageStyle, new Color(255, 33, 83));

        infoMessageStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(infoMessageStyle, new Color(16, 231, 226));

        warningMessageStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(warningMessageStyle, new Color(249, 235, 15));

        scrollTextArea = new JScrollPane(new_textArea);
        scrollTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tabbedPane.add("<empty>", scrollTextArea);

        this.add(tabbedPane);
        this.setVisible(true);
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
        logger.connectLoggerPanel(this);

        if (tabbedPane.getTabCount() == loggers.size()) {
            tabbedPane.setTitleAt(0, logger.getLogFileName());
        } else {
            JTextPane new_textArea = new JTextPane();
            new_textArea.setEditable(false);
            new_textArea.setBackground(new Color(47, 45, 45));
            textAreas.add(new_textArea);

            Document new_document = new_textArea.getStyledDocument();
            documents.add(new_document);
            new_textArea.setDocument(new_document);

            scrollTextArea = new JScrollPane(new_textArea);
            scrollTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            tabbedPane.add(logger.getLogFileName(), scrollTextArea);
        }
    }

    public void addErrorMessage(Logger logger, String message) {
        try {
            for (int i = 0; i < loggers.size(); i++) {
                if (loggers.get(i) == logger) {
                    documents.get(i).insertString(documents.get(i).getLength(), message, errorMessageStyle);
                    textAreas.get(i).setCaretPosition(documents.get(i).getLength());
                    break;
                }
            }
        } catch (BadLocationException e) {
            System.err.println("An error occurred on writing error message in LoggerPanel.");
            e.printStackTrace();
        }
    }

    public void addInfoMessage(Logger logger, String message) {
        try {
            for (int i = 0; i < loggers.size(); i++) {
                if (loggers.get(i) == logger) {
                    documents.get(i).insertString(documents.get(i).getLength(), message, infoMessageStyle);
                    textAreas.get(i).setCaretPosition(documents.get(i).getLength());
                }
            }
        } catch (BadLocationException e) {
            logger.error(this, "An error occurred on writing info message.");
            e.printStackTrace();
        }
    }

    public void addWarningMassage(Logger logger, String message) {
        try {
            for (int i = 0; i < loggers.size(); i++) {
                if (loggers.get(i) == logger) {
                    documents.get(i).insertString(documents.get(i).getLength(), message, warningMessageStyle);
                    textAreas.get(i).setCaretPosition(documents.get(i).getLength());
                }
            }
        } catch (BadLocationException e) {
            logger.error(this, "An error occurred on writing warning message.");
            e.printStackTrace();
        }
    }
}
