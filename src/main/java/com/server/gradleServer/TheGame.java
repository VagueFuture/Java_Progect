package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

public class TheGame extends JFrame {
    private JPanel jpanel1;
    private JFrame frame;
    private JLabel jLabel1;

    public TheGame(){
        frame = new JFrame("Game");
        Dimension size = new Dimension(800, 600);
        frame.setPreferredSize(size);
        frame.setContentPane(jpanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        try {

        }
        catch (IOError e){
            System.out.println(e);
        }
    }
}
