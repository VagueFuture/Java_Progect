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
    private JLabel[][] minimap =new JLabel[11][11];

    private JPanel jpanel1;
    private JPanel jpanel_minimap = new JPanel();
    private JLabel room=new JLabel();
    private JTextArea HeroView=new JTextArea();
    private JButton left=new JButton("На Запад");
    private JButton right=new JButton("На Восток");
    private JButton down=new JButton("На Север");
    private JButton up=new JButton("На Юг");
    private JLabel hero=new JLabel();
    private JLabel enemy=new JLabel();
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
    private int[][] currentallpos;
    private String msg;

    private final int mapsize=11;
    private final int roomcount=11;

    private int[][] map=new int[mapsize][mapsize];

    public TheGame(Integer Hero, Socket fromserver,ClientUI tcl) {
        frame = new JFrame("Game");
        Dimension size = new Dimension(1200, 700);
        jpanel1.setLayout(new GridBagLayout());
        jpanel_minimap.setLayout(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        GridBagConstraints ff = new GridBagConstraints();
        frame.setPreferredSize(size);
        frame.setContentPane(jpanel1);

        cl=tcl;

        currentpos=cl.getpos();
        /////////////////////Противник
        f.fill = GridBagConstraints.LAST_LINE_START;
        f.gridx=0;
        f.gridy=1;
        f.gridwidth = 4;

        //f.anchor=GridBagConstraints.NORTH;
        //  f.insets=new Insets(0,0,500,0);
        jpanel1.add(enemy,f);
        f.gridwidth = 1;
/////////////////////Противник

/////////////////////////////Комната
        f.anchor=GridBagConstraints.FIRST_LINE_START;
     //   f.fill = GridBagConstraints.BOTH;
        f.gridx = 0;
        f.gridy = 0;
        f.gridheight=2;
        jpanel1.add(room, f);
        f.gridheight=1;
////////////////////////////Комната

/////////////////////Текстовое поле
        f.gridx = 0;
        f.gridy = 2;
        f.fill = GridBagConstraints.BOTH;
        HeroView.setEditable(false);
        HeroView.setLineWrap(true);
        jpanel1.add(HeroView, f);
/////////////////////Текстовое поле
///////////////////////////Кароточки на карте

        f.gridwidth = 2;
        //f.fill = GridBagConstraints.VERTICAL;
        f.gridx=1;
        f.gridy=0;
        f.gridwidth = 3;
        jpanel1.add(jpanel_minimap,f);
        ff.fill = GridBagConstraints.FIRST_LINE_START;
        for(int i=0; i<minimap.length;i++)
            for(int j=0;j<minimap.length;j++){
                ff.gridx = i;
                ff.gridy = j;
                minimap[i][j] = new JLabel();
                jpanel_minimap.add(minimap[i][j],ff);
            }

//////////////////////////Карточки на карте


/////////////////Герой
        f.fill = GridBagConstraints.FIRST_LINE_START;
        f.gridx=1;
        f.gridy=1;
        f.gridwidth = 4;
        jpanel1.add(hero,f);
        f.gridwidth = 1;
/////////////////////Герой

/////////////////////Кнопки
        f.fill = GridBagConstraints.BOTH;
        f.gridwidth = 1;
        f.gridx=1;
        f.gridy=2;
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

/////////////////////Кнопки

        try {
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Rooms\\1.png"));
            img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            room.setIcon(icon);
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Hero\\"+ Hero +".png"));
            img = img.getScaledInstance(390, 250,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            hero.setIcon(icon);

            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\fon.png"));
            img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            for(int i=0; i<minimap.length;i++)
                for(int j=0;j<minimap.length;j++) {
                    minimap[i][j].setIcon(icon);
                }
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\12.png"));
            img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            minimap[5][5].setIcon(icon);
            map[5][5]=12;
            //jpanel1.add(hero, f);
            cl.getdMsg();//Получаю координаты стартовые
            msg="Client_posit"+currentpos[0]+"@"+currentpos[1]+"@"+'2';
            //msg+='@';
            //}
            System.out.println(msg);
            cl.sendMsg(msg);
            //paintMap();
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
    public void paintMap(Integer i,Integer j,Integer k){
                if(map[i][j]!=roomcount+1) {
                    try {
                        img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\"+k+".png"));
                        img = img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                        minimap[j][i].setIcon(icon);
                    }
                    catch (IOException e){
                        System.out.println(e);
                    }

                }
    }


    public void paint(){
        int a= 2 + (int) ( Math.random() * roomcount-1);
        currentallpos=cl.getallpospos();
        String temp="";
        if (map[currentpos[0]][currentpos[1]]==0 && map[currentpos[0]][currentpos[1]]!=roomcount+1) {
        //System.out.println(currentpos[0]+" "+currentpos[1]);
            map[currentpos[0]][currentpos[1]] = a;
        }
        else{
            a=map[currentpos[0]][currentpos[1]];
        }
        //for (int j = 0; j < 2; j++) {
        msg="Client_posit"+currentpos[0]+"@"+currentpos[1]+"@"+a;
            //msg+='@';
        //}
        for (int i=0;i<currentallpos.length;i++)
        paintMap(currentallpos[1][i],currentallpos[0][i],currentallpos[2][i]);
        System.out.println(msg);
        try {
            cl.sendMsg(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
        try(Reader reader= new FileReader("src\\main\\resources\\Database\\bd.csv")){
            while((c=reader.read())!=-1){
                  temp+=Character.toString(c);
                if(c=='\n'){
                    temp="";
                }
                //System.out.println(temp);
                if(temp.equals(Integer.toString(a))) {
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

