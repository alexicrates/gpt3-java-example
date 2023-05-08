package com.example.gui;

import com.example.web.clients.ListenerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static com.example.gui.ImageUtils.getBufferedImage;
import static com.example.gui.ImageUtils.getResizedImageIcon;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

@Component
public class MainFrame extends JFrame {

    @Autowired
    private ListenerClient listenerClient;

    private boolean isMute = false;

    private JPanel mainPanel;
    private JButton microphoneControl;
    private JScrollPane scrollPane1;
    private JLabel recordIndicator;
    private JTextArea textArea1;

    ImageIcon unmutedMicroImage = getResizedImageIcon("swing-gui/src/main/resources/unmuted.png", 50, 60);
    ImageIcon mutedMicroImage = getResizedImageIcon("swing-gui/src/main/resources/muted.png", 50, 60);
    final String recordIndicatorImagePath = "/home/abobus/Downloads/red_circle.png";

    public MainFrame() throws IOException {
        microphoneControl.setFocusable(false);
        microphoneControl.setMargin(new Insets(0, 0, 0, 0));
        microphoneControl.setFocusPainted(true);
        microphoneControl.setContentAreaFilled(false);
        microphoneControl.setBorder(BorderFactory.createEmptyBorder());
        microphoneControl.setIcon(unmutedMicroImage);

        setRecording(false);

        microphoneControl.addActionListener(e -> {
            isMute = !isMute;
            JButton source = (JButton) e.getSource();
            if (isMute) {
                source.setIcon(mutedMicroImage);
                listenerClient.turnMicroOff();
            } else {
                source.setIcon(unmutedMicroImage);
                listenerClient.turnMicroOn();
            }
        });

        this.setSize(new Dimension(700, 700));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setContentPane(mainPanel);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        scrollPane1 = new JScrollPane(textArea1, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setMinimumSize(new Dimension(500, 500));
    }

    public void appendMessage(Role role, String text) {
        textArea1.append(role + ": " + text + "\n\n");
    }

    public void setRecording(boolean recording) throws IOException {
        recordIndicator.setIcon(getResizedImageIcon(
                getBufferedImage(recordIndicatorImagePath, recording), 50, 50));
    }

    public enum Role {
        BOT{
            @Override
            public String toString() {
                return "Bot";
            }
        },
        YOU{
            @Override
            public String toString() {
                return "You";
            }
        }
    }
}
