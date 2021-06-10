package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {
    private JButton apagarButton;
    private JPanel rootPanel;
    private JLabel Text;

    public Gui() {
        setTitle("BET");
        add(rootPanel);
        setSize(200, 200);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        apagarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
