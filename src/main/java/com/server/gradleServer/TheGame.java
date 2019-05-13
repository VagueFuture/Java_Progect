package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TheGame extends JFrame {
    private JPanel jpanel1;
    private JLabel room=new JLabel();
    private JTextArea HeroView=new JTextArea();
    private JButton left=new JButton("На Запад");
    private JButton right=new JButton("На Восток");
    private JButton down=new JButton("На Север");
    private JButton up=new JButton("На Юг");
    private JLabel hero=new JLabel();
    private JTextArea textmap=new JTextArea();
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
        Dimension size = new Dimension(1200, 700);
        jpanel1.setLayout(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        frame.setPreferredSize(size);
        frame.setContentPane(jpanel1);
        currentpos=cl.getpos();
        f.fill = GridBagConstraints.BOTH;
        f.gridx = 0;
        f.gridy = 0;
        f.weightx =1;
        f.weighty =1;
        f.gridheight=2;
        f.anchor=GridBagConstraints.FIRST_LINE_START;
        jpanel1.add(room, f);


        f.gridx = 0;
        f.gridy = 2;
        f.weighty =0.25;
        f.gridheight=1;
        f.anchor=GridBagConstraints.LAST_LINE_START;
        HeroView.setEditable(false);
        HeroView.setLineWrap(true);
        //HeroView.setSize(5,2);// setMaximumSize(new Dimension(100,50));
        jpanel1.add(HeroView, f);

        f.fill = GridBagConstraints.HORIZONTAL;
        f.gridx=1;
        f.gridy=0;
        f.weightx =1;
        f.weighty =0.5;
        f.gridwidth=4;
        textmap.setEditable(false);
        textmap.setLineWrap(true);
        //textmap.setSize(5,2);
        f.anchor=GridBagConstraints.PAGE_START;
        jpanel1.add(textmap,f);

        f.fill = GridBagConstraints.HORIZONTAL;////////ФИКСАНУТЬ
        f.gridx=1;
        f.gridy=1;
        f.weightx =1;
        f.weighty =0.1;
        f.anchor=GridBagConstraints.NORTH;
        f.insets=new Insets(0,0,500,0);
        jpanel1.add(hero,f);

        f.gridx=1;
        f.gridy=2;
        f.gridwidth=1;
        f.weightx =0.2;
        f.insets=new Insets(0,0,0,0);
        f.anchor=GridBagConstraints.SOUTHWEST;
        jpanel1.add(down,f);

        f.gridx=2;
        f.gridy=2;
        jpanel1.add(left,f);

        f.gridx=3;
        f.gridy=2;
        jpanel1.add(right,f);

        f.gridx=4;
        f.gridy=2;
        jpanel1.add(up,f);

        try {
            img = ImageIO.read(new File("D:\\Documents\\javaproject\\src\\main\\resources\\Drawable\\Rooms\\1.png"));
            img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            room.setIcon(icon);
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Hero\\"+ Hero +".png"));
            img = img.getScaledInstance(400, 200,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            hero.setIcon(icon);
            jpanel1.add(hero, f);
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
        int a = 2 + (int) ( Math.random() * 10 );
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
        //cl.sendMsg(msg);
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

