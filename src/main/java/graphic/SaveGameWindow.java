package graphic;

import control.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class SaveGameWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    Controller controller;
    String firstMsg = "Opcion de guardado 1";
    File firstFile;
    String secondMsg = "Opción de guardado 2";
    File secondFile;
    String thirdMsg = "Opción de guardado 3";
    File thirdFile;

    public SaveGameWindow(Controller control) {
        this.controller = control;
        firstFile = new File("firstGame.json");
        secondFile = new File("secondGame.json");
        thirdFile = new File("thirdGame.json");
        initGUI();
    }

    public void initGUI() {
        this.setPreferredSize(new Dimension(500, 300));
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        //Panel Superior
        JPanel firstPanel = new JPanel();
        firstPanel.setBackground(Color.WHITE);
        firstPanel.setPreferredSize(new Dimension(150, 85));
        JButton opt1 = new JButton(firstMsg);
        opt1.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt1.setFocusable(false);
        opt1.setBorderPainted(true);
        opt1.setFocusPainted(false);
        opt1.setContentAreaFilled(false);
        opt1.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    controller.saveGame(firstFile);
                    setVisible(false);
                }
            }
        );
        firstPanel.add(opt1, BorderLayout.CENTER);

        //Panel Medio
        JPanel secondPanel = new JPanel();
        secondPanel.setBackground(Color.WHITE);
        secondPanel.setPreferredSize(new Dimension(150, 85));
        JButton opt2 = new JButton(secondMsg);
        opt2.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt2.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt2.setFocusable(false);
        opt2.setBorderPainted(true);
        opt2.setFocusPainted(false);
        opt2.setContentAreaFilled(false);
        opt2.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    controller.saveGame(secondFile);
                    setVisible(false);
                }
            }
        );
        secondPanel.add(opt2, BorderLayout.CENTER);

        //Panel bajo
        JPanel thirdPanel = new JPanel();
        thirdPanel.setBackground(Color.WHITE);
        thirdPanel.setPreferredSize(new Dimension(150, 85));
        JButton opt3 = new JButton(thirdMsg);
        opt3.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt3.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt3.setFocusable(false);
        opt3.setBorderPainted(true);
        opt3.setFocusPainted(false);
        opt3.setContentAreaFilled(false);
        opt3.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    controller.saveGame(thirdFile);
                    setVisible(false);
                }
            }
        );
        thirdPanel.add(opt3, BorderLayout.CENTER);

        this.add(firstPanel, BorderLayout.NORTH);
        this.add(secondPanel, BorderLayout.CENTER);
        this.add(thirdPanel, BorderLayout.SOUTH);
    }

    public void open() {
        setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
        pack();
        setVisible(true);
    }
}
