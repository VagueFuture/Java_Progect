package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class TheGame extends JFrame {
    private JLabel[][] minimap =new JLabel[11][11];

    private JPanel jpanel1;
    private JPanel jpanel_minimap = new JPanel();
    private JLabel room=new JLabel();
    private JLabel card=new JLabel();
    private JTextArea HeroView=new JTextArea();
    private JButton left=new JButton("На Запад");
    private JButton right=new JButton("На Восток");
    private JButton down=new JButton("На Север");
    private JButton up=new JButton("На Юг");
    private JButton endturn =new JButton("Конец хода");
    private JButton find = new JButton("Исследовать");
    private JButton skill = new JButton("Проверка");
    private JLabel hero=new JLabel();
    private JLabel enemy=new JLabel();
    private JFrame frame;
    private Icon icon;
    private BufferedImage img;
    private FileReader reader;

    private ClientUI cl;

    private int thisangle = -180;
    private int thisroom = 2;
    private int c;
    private String status="";
    private int[] currentallpos;
    private String msg;
    private int mynumber;
    private int PlayerCount;

    private final int mapsize=11;
    private final int roomcount=11;

    private int[][] map=new int[mapsize][mapsize];
    private int[][] angles=new int[mapsize][mapsize];

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
        PlayerCount=cl.getPlayerCount();
        /////////////////////Противник
        f.fill = GridBagConstraints.BOTH;
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
        f.gridheight=4;
        jpanel1.add(room, f);
        f.gridheight=1;
////////////////////////////Комната

/////////////////////Текстовое поле
        f.gridx = 0;
        f.gridy = 4;
        f.fill = GridBagConstraints.BOTH;
        HeroView.setEditable(false);
        HeroView.setLineWrap(true);
        jpanel1.add(HeroView, f);
/////////////////////Текстовое поле
///////////////////////////Кароточки на карте

        //f.gridwidth = 2;
        //f.fill = GridBagConstraints.VERTICAL;
        f.gridx=1;
        f.gridy=0;
        f.gridwidth = 3;
        Color col = new Color(255,185,0);
        jpanel_minimap.setBackground(col);
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

// ////////////////////Исследование
        f.fill = GridBagConstraints.FIRST_LINE_START;
        f.gridx=3;
        f.gridy=1;
        f.gridwidth = 4;
        jpanel1.add(card,f);
        f.gridwidth = 1;
/////////////////////Исследование

/////////////////////Кнопки
        f.fill = GridBagConstraints.BOTH;
        f.gridwidth = 1;
        f.gridx=1;
        f.gridy=4;
        jpanel1.add(down,f);

        f.gridx=2;
        f.gridy=4;
        jpanel1.add(left,f);

        f.gridx=3;
        f.gridy=4;
        jpanel1.add(right,f);

        f.gridx=4;
        f.gridy=4;
        jpanel1.add(up,f);

        f.gridx=1;
        f.gridy=2;
        jpanel1.add(skill,f);

        f.gridx=1;
        f.gridy=3;
        jpanel1.add(find,f);


        f.gridx=4;
        f.gridy=3;
        jpanel1.add(endturn,f);

/////////////////////Кнопки

        try {
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Rooms\\1.png"));
            //img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            img = ChangeImage(img,0,800,600,1,1);
            icon = new ImageIcon(img);
            room.setIcon(icon);
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Hero\\"+ Hero +".png"));
            //img = img.getScaledInstance(390, 250,  java.awt.Image.SCALE_SMOOTH);
            img = ChangeImage(img,0,390,250,1,1);
            icon = new ImageIcon(img);
            hero.setIcon(icon);

            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\fon.png"));
            img = ChangeImage(img,0,25,25,0.5,0.5);
            //img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            for(int i=0; i<minimap.length;i++)
                for(int j=0;j<minimap.length;j++) {
                    minimap[i][j].setIcon(icon);
                }
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\12.png"));
            //img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
            img = ChangeImage(img,0,25,25,0.5,0.5);
            icon = new ImageIcon(img);
            minimap[5][5].setIcon(icon);
            map[5][5]=12;
            //jpanel1.add(hero, f);
            cl.getdMsg();//Получаю координаты стартовые
            reader = new FileReader("src\\main\\resources\\Database\\bd.csv");
            while ((c = reader.read()) != '\n') {
                if (c != '1')
                    status += Character.toString(c);
                //System.out.print((char) c);
            }
            HeroView.setText(status);
            status = "";
        } catch (IOException e) {
            System.out.println(e);
        }
        currentallpos=cl.getallpospos();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card(1);
                find.setEnabled(false);
                not_Activ();
            }
        });

        endturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card(0);
                not_Activ();
                cl.sendMsg("End_of_the_turn");
                cl.setstate();
                while(true){
                    if(cl.getstate()){
                        newturn();
                        break;
                    }
                }
            }
        });

        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mynumber=cl.getmynumber();
                  thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                  thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key south = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle south = "+thisangle);
                if(key != 12 && key != 32 &&key != 42&&key != 2341 &&key != 23 &&key != 2 &&key != 21 &&key != 24) {
                    HeroView.setText("Нельзя двигаться на юг");
                }else
                    if (currentallpos[0 + 4 * (mynumber - 1)] - 1 >= 0) {
                        currentallpos[0 + 4 * (mynumber - 1)] -= 1;
                        paint(0);
                        not_Activ();
                        Card(1);
                    } else {
                        HeroView.setText("Нельзя двигаться на юг");
                    }


            }
        });
        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mynumber=cl.getmynumber();
                thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key sever = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle sever = "+thisangle);
                if(key != 21 && key != 31 && key != 41&&key != 2341 &&key != 13 &&key != 1 &&key != 12 &&key != 14){
                    HeroView.setText("Нельзя двигаться на север");
                }else
                if (currentallpos[0+4*(mynumber-1)]+1<map.length){
                    currentallpos[0+4*(mynumber-1)]+=1;
                paint(-180);
                    not_Activ();
                    Card(1);
            }
                else{
                HeroView.setText("Нельзя двигаться на север");
            }
            }
        });
        left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mynumber=cl.getmynumber();
                thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key west = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle west = "+thisangle);
                if(key != 43 && key != 23 &&key != 13&&key != 2341 &&key != 31 &&key != 3 &&key != 32 &&key != 34){
                    HeroView.setText("Нельзя двигаться на запад");
                }else
                if(currentallpos[1+4*(mynumber-1)]-1>=0) {
                    currentallpos[1+4*(mynumber-1)]-=1;
                    paint(-90);
                    not_Activ();
                    Card(1);
                }
                else{
                    HeroView.setText("Нельзя двигаться на запад");
                }
            }
        });
        right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                        mynumber=cl.getmynumber();
                thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key east = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle east = "+thisangle);
                if(key != 34 && key != 24 &&key != 14 &&key != 2341&&key != 43 &&key != 4 &&key != 42 &&key != 41){
                    HeroView.setText("Нельзя двигаться на восток");
                }else
                        if(currentallpos[1+4*(mynumber-1)]+1<map.length) {
                            currentallpos[1+4*(mynumber-1)]+= 1;
                            paint(90);
                            not_Activ();
                            Card(1);
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
    public void paintMap(Integer i,Integer j,Integer k,int angle){
                if(map[i][j]!=roomcount+1) {
                    try {
                        img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\"+k+".png"));
                        //img = img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
                        img = ChangeImage(img,angle,25,25,0.5,0.5);
                        icon = new ImageIcon(img);
                        minimap[j][i].setIcon(icon);
                    }
                    catch (IOException e){
                        System.out.println(e);
                    }

                }
    }


    public void paint(int angle){
        String temp="";
        int a= 2 + (int) ( Math.random() * roomcount-1);
        cl.getallpospos();
        if (map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]==0 && map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]!=roomcount+1) {
            map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]] = a;
            currentallpos[2+4*(mynumber-1)]=a;
            angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]=angle;
            msg="Client_posit"+currentallpos[0+4*(mynumber-1)]+"@"+currentallpos[1+4*(mynumber-1)]+"@"+currentallpos[2+4*(mynumber-1)]+"@"+angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
            paintMap(currentallpos[0+4*(mynumber-1)],currentallpos[1+4*(mynumber-1)],currentallpos[2+4*(mynumber-1)],angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]);
        }
        else{
            currentallpos[2+4*(mynumber-1)]=map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
            msg="Client_posit"+currentallpos[0+4*(mynumber-1)]+"@"+currentallpos[1+4*(mynumber-1)]+"@"+currentallpos[2+4*(mynumber-1)]+"@"+angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
        }

        /////////////////////////////////Проверка комнаты

        /////////////////////////////////Проверка комнаты
        int i=0;
        System.out.print("\n");
        System.out.println(PlayerCount);
        for(int k=0;k<PlayerCount;k++){
            map[currentallpos[i]][currentallpos[i+1]] = currentallpos[i+2];
            System.out.println(currentallpos[i]+" "+currentallpos[i+1]+" "+ currentallpos[i+2]);
            paintMap(currentallpos[i],currentallpos[i+1],currentallpos[i+2],angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]);
            i+=4;
        }
        System.out.print("\n");
        System.out.print("\n");
        for(i=0;i<11;i++){
            for(int j=0;j<11;j++){
                System.out.print(map[i][j]);
            }
            System.out.print("\n");
        }

        try(Reader reader= new FileReader("src\\main\\resources\\Database\\bd.csv")){
            while((c=reader.read())!=-1){
                  temp+=Character.toString(c);
                if(c=='\n'){
                    temp="";
                }
                if(temp.equals(Integer.toString(currentallpos[2+4*(mynumber-1)]))) {
                    while((c=reader.read())!='\n') {
                        status+=Character.toString(c);
                        System.out.print((char) c);
                    }
                    break;
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        HeroView.setText(status);
        status="";
        try {
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Rooms\\"+ currentallpos[2+4*(mynumber-1)] +".png"));
            img = ChangeImage(img,0,800,600,1,1);
            icon = new ImageIcon(img);
            room.setIcon(icon);
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            cl.sendMsg(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public BufferedImage ChangeImage(BufferedImage buffImage, double angle,int width1,int height1, double sx, double sy) {
        BufferedImage Image = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = Image.createGraphics();
        g.scale(sx,sy);
        g.drawImage(buffImage, 0, 0,null);
        double radian = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));
        int width  = Image.getWidth();
        int height = Image.getHeight();
        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);
        BufferedImage ChangeImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = ChangeImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
        graphics.drawImage(Image, 0, 0,null);
        graphics.dispose();
        return ChangeImage;
    }


    public void Card(int key){
        try {
        if(key == 1) {
            int a = 0 + (int) (Math.random() * 9);

                img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Events\\" + a + ".png"));
                img = ChangeImage(img, 0, 177, 250, 1, 1);
                icon = new ImageIcon(img);
                enemy.setIcon(icon);



        }
        if(key == 0){
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Events\\" + 10 + ".png"));
            img = ChangeImage(img, 0, 177, 250, 1, 1);
            icon = new ImageIcon(img);
            enemy.setIcon(icon);

            find.setEnabled(true);
        }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
///////////////1-только север и юг /2-только запад и юг/3-только восток и юг/4-запад и север и юг
// /////////////5-на восток и север/0-тупик и назад
    public int test_room(int angle){
        if(angle == 90){
            if(thisroom == 2 || thisroom == 3 ||thisroom == 4 ||thisroom == 12){
                return 34;
            }
            if(thisroom == 6 || thisroom == 9){
                return 31;
            }
            if(thisroom == 7){
                return 32;
            }
            if(thisroom == 8 || thisroom == 10){
                return 2341;
            }
            if(thisroom == 5){
                return 3;
            }
            if(thisroom == 11){
                return 4;
            }
        }

        if(angle == -180){
            if(thisroom == 2 || thisroom == 3 ||thisroom == 4 ||thisroom == 12){
                return 21;
            }
            if(thisroom == 6 || thisroom == 9){
                return 23;
            }
            if(thisroom == 7){
                return 24;
            }
            if(thisroom == 8 || thisroom == 10){
                return 2341;
            }
            if(thisroom == 5){
                return 2;
            }
            if(thisroom == 11){
                return 1;
            }
        }
        if(angle == -90){
            if(thisroom == 2 || thisroom == 3 ||thisroom == 4 ||thisroom == 12){
                return 43;
            }
            if(thisroom == 6 || thisroom == 9){
                return 42;
            }
            if(thisroom == 7){
                return 41;
            }
            if(thisroom == 8 || thisroom == 10){
                return 2341;
            }
            if(thisroom == 5){
                return 4;
            }
            if(thisroom == 11){
                return 3;
            }
        }
        if(angle == 0){
            if(thisroom == 2 || thisroom == 3 ||thisroom == 4 ||thisroom == 12){
                return 12;
            }
            if(thisroom == 6 || thisroom == 9){
                return 14;
            }
            if(thisroom == 7){
                return 13;
            }
            if(thisroom == 8 || thisroom == 10){
                return 2341;
            }
            if(thisroom == 5){
                return 1;
            }
            if(thisroom == 11){
                return 2;
            }
        }
        if(thisroom == 0){
            return 2341;
        }
            return 0;
    }
    private void newturn(){
        find.setEnabled(true);
        up.setEnabled(true);
        down.setEnabled(true);
        left.setEnabled(true);
        right.setEnabled(true);
        skill.setEnabled(true);
        endturn.setEnabled(false);
     //   paint(currentallpos[2+4*(mynumber-1)]);
    }

    private void not_Activ(){
        find.setEnabled(false);
        up.setEnabled(false);
        down.setEnabled(false);
        left.setEnabled(false);
        right.setEnabled(false);
        skill.setEnabled(false);
        endturn.setEnabled(true);
    }
}
