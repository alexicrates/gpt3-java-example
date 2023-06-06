package com.example.gui;

import com.example.gptlogsspringbootstarter.model.entities.ChatMessage;
import com.example.gptlogsspringbootstarter.model.repositories.PostgresRepository;
import com.example.web.clients.ListenerClient;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.gptlogsspringbootstarter.model.entities.ChatMessage.MessageType.INPUT;
import static com.example.gui.ImageUtils.getBufferedImage;
import static com.example.gui.ImageUtils.getResizedImageIcon;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

@Component
public class MainFrame extends JFrame {

    @Autowired
    private ListenerClient listenerClient;

    @Autowired
    private PostgresRepository repository;

    private boolean isListening = true;

    private JPanel mainPanel;
    private JButton microphoneControl;
    private JScrollPane scrollPane1;
    private JLabel recordIndicator;
    private JTextArea textArea1;

    final String recordIndicatorImagePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("red_circle.png")).getPath();
    final String unmutedMicroImagePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("unmuted.png")).getPath();
    final String mutedMicroImagePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("muted.png")).getPath();
    ImageIcon unmutedMicroImage = getResizedImageIcon(unmutedMicroImagePath, 50, 60);
    ImageIcon mutedMicroImage = getResizedImageIcon(mutedMicroImagePath, 50, 60);

    public MainFrame() throws IOException {
        $$$setupUI$$$();

        this.setTitle("Voice App");

        microphoneControl.setFocusable(false);
        microphoneControl.setMargin(new Insets(0, 0, 0, 0));
        microphoneControl.setFocusPainted(true);
        microphoneControl.setContentAreaFilled(false);
        microphoneControl.setBorder(BorderFactory.createEmptyBorder());
        microphoneControl.setIcon(unmutedMicroImage);

        setRecording(false);
        setupMenu();

        microphoneControl.addActionListener(e -> {
            setMicrophoneListening(isListening);
            isListening = !isListening;
            JButton source = (JButton) e.getSource();
            if (!isListening) {
                source.setIcon(mutedMicroImage);
            } else {
                source.setIcon(unmutedMicroImage);
            }
        });

        this.setSize(new Dimension(700, 700));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setContentPane(mainPanel);
    }

    @PostConstruct
    public void loadMessagesToFrame(){
        List<ChatMessage> messages = repository.findAll();
        for (ChatMessage message : messages) {
            Role role = message.getMessageType() == INPUT ? Role.YOU : Role.BOT;
            this.appendMessage(role, message.getPrompt());
        }
    }

    public void setMicrophoneListening(boolean shouldListen){
        try {
            if (shouldListen) {
                listenerClient.turnMicroOn();
            } else {
                listenerClient.turnMicroOff();
            }
        } catch (Exception e) {
            showErrorMessage("Can't detect speech-listener module");
            throw new RuntimeException(e);
        }
    }

    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void appendMessage(Role role, String text) {
        textArea1.append(role + ": " + text + "\n\n");
    }
    public void clearMessageWindow(){
        textArea1.setText("");
    }

    public void setRecording(boolean recording) throws IOException {
        recordIndicator.setIcon(getResizedImageIcon(
                getBufferedImage(recordIndicatorImagePath, recording), 50, 50));
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        Font font = new Font("", 0, 20);

        JMenuItem authorItem = new JMenuItem("Автор");
        authorItem.setFont(font);
        authorItem.addActionListener(
                e -> JOptionPane.showMessageDialog(
                        null,
                        "Выполнил студент группы ИИ-18 Малейчук Александр",
                        "Автор",
                        INFORMATION_MESSAGE));


        JMenuItem helpItem = new JMenuItem("Помошь");
        helpItem.setFont(font);
        helpItem.addActionListener(
                e -> JOptionPane.showMessageDialog(
                        null,
                        "----",
                        "Помощь",
                        INFORMATION_MESSAGE));


        JMenu menu = new JMenu("Справка");
        menu.setFont(font);
        menu.add(authorItem);
        menu.add(helpItem);

        JMenuItem deleteLogsItem = new JMenuItem("Очистить историю");
        deleteLogsItem.setFont(font);
        deleteLogsItem.addActionListener(e -> {
            try {
                repository.deleteAll();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            clearMessageWindow();
        });

        JMenu actionMenu = new JMenu("Действия");
        actionMenu.setFont(font);
        actionMenu.add(deleteLogsItem);

        menuBar.add(actionMenu);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        textArea1.setEditable(false);
        textArea1.setMaximumSize(new Dimension(500, 500));

        scrollPane1 = new JScrollPane(textArea1, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setMinimumSize(new Dimension(500, 500));
        scrollPane1.setMaximumSize(new Dimension(500, 500));
    }

    public enum Role {
        BOT {
            @Override
            public String toString() {
                return "Bot";
            }
        },
        YOU {
            @Override
            public String toString() {
                return "You";
            }
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(6, 7, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBackground(new Color(-1246977));
        mainPanel.setEnabled(true);
        mainPanel.setForeground(new Color(-1246977));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 0, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(0, 6, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        mainPanel.add(scrollPane1, new GridConstraints(1, 1, 2, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        recordIndicator = new JLabel();
        recordIndicator.setText("");
        mainPanel.add(recordIndicator, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        mainPanel.add(spacer6, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        mainPanel.add(spacer7, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        microphoneControl = new JButton();
        microphoneControl.setBackground(new Color(-197121));
        microphoneControl.setForeground(new Color(-1));
        microphoneControl.setText("");
        mainPanel.add(microphoneControl, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
