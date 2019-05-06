package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TheGame extends JFrame {
    private JPanel jpanel1;
    private JLabel room;
    private JTextArea TextArea;
    private JButton left;
    private JButton right;
    private JButton down;
    private JButton up;
    private JLabel hero;
    private JFrame frame;
    private Icon icon;
    private Image img;
    private FileReader reader;

    private int c;
    private String status="";

    public TheGame(Integer Hero) {
        frame = new JFrame("Game");
        Dimension size = new Dimension(1920, 1080);
        frame.setPreferredSize(size);
        frame.setContentPane(jpanel1);

        try {
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Hero\\"+ Hero +".png"));
            img = img.getScaledInstance(400, 200,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            hero.setIcon(icon);
            reader= new FileReader("src\\main\\resources\\Database\\bd.csv");
            while((c=reader.read())!='\n') {
                status+=Character.toString(c);
                System.out.print((char) c);
            }
            TextArea.setText(status);
            status="";
        } catch (IOException e) {
            System.out.println(e);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        TextArea.setLineWrap(true);
        frame.setVisible(true);
        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint();
            }
        });
    }

    public void paint(){
        int a = 2 + (int) ( Math.random() * 4 );
        System.out.println(a);
        try(Reader reader= new FileReader("src\\main\\resources\\Database\\bd.csv");){
            while((c=reader.read())!=-1){
                if(Character.toString(c).equals(Integer.toString(a))) {
                    while((c=reader.read())!='\n') {
                        status+=Character.toString(c);
                        System.out.print((char) c);
                    }
                    break;
                }
            }
            //reader.close();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        TextArea.setText(status);
        status="";
        try {
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Rooms\\"+ a +".png"));
            img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            room.setIcon(icon);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

