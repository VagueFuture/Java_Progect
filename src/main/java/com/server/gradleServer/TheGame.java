package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TheGame extends JFrame {
    private JPanel jpanel1;
    private JLabel room;
    private JTextArea HeroView;
    private JButton left;
    private JButton right;
    private JButton down;
    private JButton up;
    private JLabel hero;
    private JTextArea textmap;
    private JFrame frame;
    private Icon icon;
    private Image img;
    private FileReader reader;

    private ClientUI cl;

    private PrintWriter out;
    private Scanner in;

    private int c;
    private String status="";
    private int[] currentpos;
    private String msg;

    private final int mapsize=10;
    private int[][] map=new int[mapsize][mapsize];

    public TheGame(Integer Hero, Socket fromserver,ClientUI cl) {
        frame = new JFrame("Game");
        Dimension size = new Dimension(1920, 1080);
        frame.setPreferredSize(size);
        frame.setContentPane(jpanel1);
        currentpos=cl.getpos();
        try {
            //img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Hero\\"+ Hero +".png"));
            //img = img.getScaledInstance(400, 200,  java.awt.Image.SCALE_SMOOTH);
            //icon = new ImageIcon(img);
            //hero.setIcon(icon);

            cl.getdMsg();//Получаю координаты стартовые
            msg="Client_posit"+currentpos[0]+"@"+currentpos[1]+"@"+'2';
            //msg+='@';
            //}
            System.out.println(msg);
            cl.sendMsg(msg);
            paintMap();
            reader = new FileReader("src\\main\\resources\\Database\\bd.csv");
            while ((c = reader.read()) != '\n') {
                if (c != '1')
                    status += Character.toString(c);
                System.out.print((char) c);
            }
            HeroView.setText(status);
            status = "";
        } catch (IOException e) {
            System.out.println(e);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        HeroView.setLineWrap(true);
        frame.setVisible(true);
        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentpos[0]-1>=0){
                    currentpos[0]-=1;
                    paint();
                }
                else{
                    HeroView.setText("Нельзя двигаться на юг");
                }
            }
        });
        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(currentpos[0]+1);
                if (currentpos[0]+1<map.length){
                currentpos[0]+=1;
                paint();
            }
                else{
                HeroView.setText("Нельзя двигаться на север");
            }
            }
        });
        left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentpos[1]-1>=0) {
                    currentpos[1]-=1;
                    paint();
                }
                else{
                    HeroView.setText("Нельзя двигаться на запад");
                }
            }
        });
        right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentpos[1]+1<map.length) {
                    currentpos[1] += 1;
                    paint();
                }
                else{
                    HeroView.setText("Нельзя двигаться на восток");
                }
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                cl.close();
            }
        });

    }
    public void paintMap(){
        textmap.setText("");
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                textmap.append(map[i][j] + " ");
            }
            textmap.append("\n");
        }
    }


    public void paint(){////НЕ ОТСЫЛАЕТСЯ КОРРЕКТНО ПОЗИЦИЯ ВЫБИВАЕТ ОШИБКУ НА СЕНДМЕСАГЕ
        int a = 2 + (int) ( Math.random() * 4 );
        if (map[currentpos[0]][currentpos[1]]==0) {
        //System.out.println(currentpos[0]+" "+currentpos[1]);
            map[currentpos[0]][currentpos[1]] = a;
            paintMap();
        }
        //for (int j = 0; j < 2; j++) {
        msg="Client_posit"+currentpos[0]+"@"+currentpos[1]+"@"+a;
            //msg+='@';
        //}
        System.out.println(msg);
        cl.sendMsg(msg);
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
        HeroView.setText(status);
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

