package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

public class TheGame extends JFrame {
    private JPanel jpanel1;
    private JButton button1;
    private JLabel jlabel1;
    private JFrame frame;
    private Icon icon;


    public TheGame() {
        frame = new JFrame("Game");
        //Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = new Dimension(1280, 720);
        frame.setPreferredSize(size);
        frame.setContentPane(jpanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        try {
            Image img = ImageIO.read(new File("src\\main\\resources\\Drawable\\5tTy0R9GGJg.jpg"));
            img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        } catch (IOException e) {
            System.out.println(e);
        }
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jlabel1.setIcon(icon);
                //frame.repaint();
            }
        });
        frame.setVisible(true);
    }
}

