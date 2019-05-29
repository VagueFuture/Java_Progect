package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
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
    private JLabel compas = new JLabel();
    private JTextArea HeroView=new JTextArea();
    private JTextArea Herohp=new JTextArea();
    private JTextArea day=new JTextArea();
    private JTextArea Herogold=new JTextArea();
    private JButton left=new JButton("На Запад");
    private JButton right=new JButton("На Восток");
    private JButton down=new JButton("На Север");
    private JButton up=new JButton("На Юг");
    private JButton endturn =new JButton("Конец хода");
    private JButton find = new JButton("Исследовать");
    private JButton dream = new JButton("Отдохнуть");
    private JLabel hero=new JLabel();
    private JLabel enemy=new JLabel();
    private JFrame frame;
    private Icon icon;
    private BufferedImage img;
    private FileReader reader;

    private ClientUI cl;

    private int my_x,my_y;
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

    private int hero_helf = 20;
    private int enemy_helf = 0;
    private int[] stats = new int[4];
    private String figt_dam;
    private boolean secret_hod = false;
    private int gold = 0;
    private boolean gameover=false;
    private boolean first=  true;
    private int day_count = 24;

    public TheGame(Integer Hero, Socket fromserver,ClientUI tcl){
        frame  = new JFrame("Game");
        Dimension size = new Dimension(1300, 800);
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
        f.gridheight=3;
        HeroView.setEditable(false);
        HeroView.setLineWrap(true);
        jpanel1.add(HeroView, f);
        f.gridheight=1;


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
        jpanel1.add(dream,f);

        f.gridx=1;
        f.gridy=3;
        jpanel1.add(find,f);


        f.gridx=4;
        f.gridy=3;
        jpanel1.add(endturn,f);

/////////////////////Кнопки
///////////////////Hp и Gold///////
        f.gridx=2;
        f.gridy=3;
        Herohp.setEditable(false);
        jpanel1.add(Herohp,f);
        f.gridx=3;
        f.gridy=3;
        Herogold.setEditable(false);
        jpanel1.add(Herogold,f);

        f.gridx=2;
        f.gridy=2;
        day.setEditable(false);
        jpanel1.add(day,f);
///////////////////Hp и Gold и day///////
//////////////////compas////////
        f.gridx = 3;
        f.gridy = 1;
        f.gridheight=4;
        jpanel1.add(compas, f);
//////////////////compas////////
        try {
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Rooms\\1.png"));
            //img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            img = ChangeImage(img,0,800,600,1,1);
            icon = new ImageIcon(img);
            room.setIcon(icon);
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Hero\\"+ Hero +".png"));
            fillStats(Hero);
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
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\8.png"));
            img = ChangeImage(img,0,25,25,0.5,0.5);
            icon = new ImageIcon(img);
            minimap[0][0].setIcon(icon);
            minimap[10][10].setIcon(icon);
            minimap[0][10].setIcon(icon);
            minimap[10][0].setIcon(icon);

            map[0][0]=8;
            map[10][10]=8;
            map[0][10]=8;
            map[10][0]=8;

            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\compas.png"));
            img = ChangeImage(img,0,150,150,0.5,0.5);
            icon = new ImageIcon(img);
            compas.setIcon(icon);
            update_hp_gold();
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        dream.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int othil;
                othil=(int)( Math.random() * 5)+1;
                hero_helf =hero_helf+othil;
                HeroView.setText("Вы решили развести костер и немного отдохнуть. Успокаивающие потрескивание костра оказывает на вас исцеляющий эффект(+"+othil+"). Немного отдохнув вы продолжаете свой путь...\n");
                not_Activ();
            }
        });

        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HeroView.setText("");
                Card(1);
                find.setEnabled(false);
                not_Activ();
            }
        });

        endturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameover){
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    System.exit(0);
                }else {
                    Card(0);
                    not_Activ();
                    cl.sendMsg("End_of_the_turn");
                    cl.setstate();
                    while (true) {
                        if (cl.getstate()) {
                            newturn();
                            System.out.println(cl.getstate());
                            break;
                        }
                        System.out.println(cl.getstate());

                        int i = 0;
                        currentallpos = cl.getallpospos();
                        for (int k = 0; k < PlayerCount; k++) {
                            map[currentallpos[i]][currentallpos[i + 1]] = currentallpos[i + 2];
                            angles[currentallpos[i]][currentallpos[i + 1]] = currentallpos[i + 3];
                            System.out.println("Отрисовал игрока = " + (i - 4) + " " + currentallpos[i] + " " + currentallpos[i + 1] + " " + currentallpos[i + 2] + " " + currentallpos[i + 3]);
                            paintMap(currentallpos[i], currentallpos[i + 1], currentallpos[i + 2], currentallpos[i + 3]);
                            i += 4;
                        }

                    }
                }
            }
        });

        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mynumber=cl.getmynumber();
                System.out.println("mynumber ="+mynumber);
                  thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                  thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key south = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle south = "+thisangle);
                if(key != 12 && key != 32 &&key != 42&&key != 2341 &&key != 23 &&key != 2 &&key != 21 &&key != 24&& secret_hod !=true) {
                    HeroView.append("Нельзя двигаться на юг\n");
                }else
                    if (currentallpos[0 + 4 * (mynumber - 1)] - 1 >= 0) {
                        currentallpos[0 + 4 * (mynumber - 1)] -= 1;
                        test_game_win();
                        paint(0);
                        not_Activ();
                        thisroom = currentallpos[2+4*(mynumber-1)];
                        Card(1);
                    } else {
                        HeroView.append("Нельзя двигаться на юг\n");
                    }


            }
        });
        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mynumber=cl.getmynumber();
                System.out.println("mynumber ="+mynumber);
                thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key sever = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle sever = "+thisangle);
                if(key != 21 && key != 31 && key != 41&&key != 2341 &&key != 13 &&key != 1 &&key != 12 &&key != 14 && secret_hod !=true){
                    HeroView.append("Нельзя двигаться на север\n");
                }else
                if (currentallpos[0+4*(mynumber-1)]+1<map.length){
                    currentallpos[0+4*(mynumber-1)]+=1;
                    test_game_win();
                    paint(-180);
                    not_Activ();
                    thisroom = currentallpos[2+4*(mynumber-1)];
                    Card(1);
            }
                else{
                HeroView.append("Нельзя двигаться на север\n");
            }
            }
        });
        left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mynumber=cl.getmynumber();
                System.out.println("mynumber ="+mynumber);
                thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key west = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle west = "+thisangle);
                if(key != 43 && key != 23 &&key != 13&&key != 2341 &&key != 31 &&key != 3 &&key != 32 &&key != 34&& secret_hod !=true){
                    HeroView.append("Нельзя двигаться на запад\n");
                }else
                if(currentallpos[1+4*(mynumber-1)]-1>=0) {
                    currentallpos[1+4*(mynumber-1)]-=1;
                    test_game_win();
                    paint(-90);
                    not_Activ();
                    thisroom = currentallpos[2+4*(mynumber-1)];
                    Card(1);
                }
                else{
                    HeroView.append("Нельзя двигаться на запад\n");
                }
            }
        });
        right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                        mynumber=cl.getmynumber();
                System.out.println("mynumber ="+mynumber);
                thisangle =angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
                thisroom = currentallpos[2+4*(mynumber-1)];
                int key = test_room(thisangle);
                System.out.println("key east = "+key);
                System.out.println("room = "+thisroom);
                System.out.println("angle east = "+thisangle);
                if(key != 34 && key != 24 &&key != 14 &&key != 2341&&key != 43 &&key != 4 &&key != 42 &&key != 41&& secret_hod !=true){
                    HeroView.append("Нельзя двигаться на восток\n");
                }else
                        if(currentallpos[1+4*(mynumber-1)]+1<map.length) {
                            currentallpos[1+4*(mynumber-1)]+= 1;
                            test_game_win();
                            paint(90);
                            not_Activ();
                            thisroom = currentallpos[2+4*(mynumber-1)];
                            Card(1);
                        }
                        else{
                            HeroView.append("Нельзя двигаться на восток\n");
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


if(first) {
    currentallpos = cl.getallpospos();
    mynumber = cl.getmynumber();
    System.out.println("Мой номер ="+mynumber);
    my_x = currentallpos[0 + 4 * (mynumber )];
    my_y = currentallpos[1 + 4 * (mynumber )];
    first = false;
}

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
        secret_hod = false;
        String temp="";
        int a= 2 + (int) ( Math.random() * roomcount-1);
        cl.getallpospos();
        System.out.print("angle? = "+currentallpos[3]+" "+(3+4*(mynumber-1)));
        if (map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]==0 && map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]!=roomcount+1) {
            map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]] = a;
            currentallpos[2+4*(mynumber-1)]=a;
            angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]=angle;
           // msg="Client_posit"+currentallpos[0+4*(mynumber-1)]+"@"+currentallpos[1+4*(mynumber-1)]+"@"+currentallpos[2+4*(mynumber-1)]+"@"+angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
            msg="Client_posit"+currentallpos[0+4*(mynumber-1)]+"@"+currentallpos[1+4*(mynumber-1)]+"@"+currentallpos[2+4*(mynumber-1)]+"@"+angle;
            paintMap(currentallpos[0+4*(mynumber-1)],currentallpos[1+4*(mynumber-1)],currentallpos[2+4*(mynumber-1)],angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]]);
        }
        else{
            currentallpos[2+4*(mynumber-1)]=map[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
            msg="Client_posit"+currentallpos[0+4*(mynumber-1)]+"@"+currentallpos[1+4*(mynumber-1)]+"@"+currentallpos[2+4*(mynumber-1)]+"@"+angles[currentallpos[0+4*(mynumber-1)]][currentallpos[1+4*(mynumber-1)]];
        }


        System.out.print("\n");
        System.out.print("\n");
        for(int i=0;i<11;i++){
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
        HeroView.setText(status+"\n");
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

///////////////1-только север и юг /2-только запад и юг/3-только восток и юг/4-запад и север и юг
// /////////////5-на восток и север/0-тупик и назад
    public int test_room(int angle){
  /////////////////////////////////////////////////////////////////
        int dmg =0;
        if(thisroom == 3){
            dmg =skillchec(2);
            if(dmg>0){
                HeroView.append("ПРОВАЛ: Потеряв равновесие вы падаете с досточки в густую тьму. При падении факел вылетел из рук. Придется в кромешной тьме на ощупь выбираться обратно. Вы получили "+dmg+" урона\n");
                not_Activ();}
            else{}

        }
        if(thisroom == 4){
            dmg =skillchec(2);
            if(dmg>0){
                HeroView.append("ПРОВАЛ: При попытке пролезть через обвал в туннеле вас придавило. Вы получили "+dmg+" урона\n");
                not_Activ();}
            else{}

        }
        if(thisroom == 5){
            gold = gold+5;
        }
        if(thisroom == 12){
            gold = gold+100;
        }
        if(thisroom == 10){
            dmg =skillchec(1);
            if(dmg>0){
                HeroView.append("ПРОВАЛ: У вас не получилось прорваться сквозь паутину и вы только запутались. Своими действиями вы привлекли внимание огромного паука. К счастью, у вас получилось освободить одну руку," +
                        " но все равно предстоит тяжелый бой. Вы получили "+dmg+" урона\n");
                not_Activ();}
            else{}

        }
//////////////////////////////////////////////////////////////////////
        update_hp_gold();
        if(dmg==0) {
            if (angle == 90) {
                if (thisroom == 2 || thisroom == 3 || thisroom == 4 || thisroom == 12) {
                    return 34;
                }
                if (thisroom == 6 || thisroom == 9) {
                    return 31;
                }
                if (thisroom == 7) {
                    return 32;
                }
                if (thisroom == 8 || thisroom == 10) {
                    return 2341;
                }
                if (thisroom == 5) {
                    return 3;
                }
                if (thisroom == 11) {
                    return 4;
                }
            }

            if (angle == -180) {
                if (thisroom == 2 || thisroom == 3 || thisroom == 4 || thisroom == 12) {
                    return 21;
                }
                if (thisroom == 6 || thisroom == 9) {
                    return 23;
                }
                if (thisroom == 7) {
                    return 24;
                }
                if (thisroom == 8 || thisroom == 10) {
                    return 2341;
                }
                if (thisroom == 5) {
                    return 2;
                }
                if (thisroom == 11) {
                    return 1;
                }
            }
            if (angle == -90) {
                if (thisroom == 2 || thisroom == 3 || thisroom == 4 || thisroom == 12) {
                    return 43;
                }
                if (thisroom == 6 || thisroom == 9) {
                    return 42;
                }
                if (thisroom == 7) {
                    return 41;
                }
                if (thisroom == 8 || thisroom == 10) {
                    return 2341;
                }
                if (thisroom == 5) {
                    return 4;
                }
                if (thisroom == 11) {
                    return 3;
                }
            }
            if (angle == 0) {
                if (thisroom == 2 || thisroom == 3 || thisroom == 4 || thisroom == 12) {
                    return 12;
                }
                if (thisroom == 6 || thisroom == 9) {
                    return 14;
                }
                if (thisroom == 7) {
                    return 13;
                }
                if (thisroom == 8 || thisroom == 10) {
                    return 2341;
                }
                if (thisroom == 5) {
                    return 1;
                }
                if (thisroom == 11) {
                    return 2;
                }
            }
            if (thisroom == 0) {
                return 2341;
            }

        }
            return 0;
    }

    public void Card(int key){
        try {
            if(key == 1) {
                if(thisroom != 3 && thisroom != 4 && thisroom != 10 && thisroom != 12) {
                    int a = 0 + (int) (Math.random() * 10);

                    img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Events\\" + a + ".png"));
                    img = ChangeImage(img, 0, 177, 250, 1, 1);
                    icon = new ImageIcon(img);
                    enemy.setIcon(icon);
                    int dmg;
                    switch (a) {
                        case 0: {
                            HeroView.append("Быстро пробежавшись глазами по сторонам, не нашлось ничего, что стоило бы вашего внимания\n");
                            break;
                        }
                        case 1: {
                            HeroView.append("Перед вами огромное ядовитое членистоногое существо. Искать обходной путь нет премени, к бою!\nПройдите проверку на защиту\n");
                            dmg =skillchec(3);
                            if(dmg>0){
                                HeroView.append("ПРОВАЛ: Паук бросается на вас, попытавшись прокусить вашу броню. Похоже тряпки, которые вы нацепили на себя и назвали 'Броней' не так предназначены для защиты от укусов огромного паука. Свозь боль прокалываете его брюхо. Вы получили "+dmg+" урона\n");}////СМЕРТЬ???????????????
                            else{HeroView.append("УСПЕХ: Паук бросается на вас, попытавшись прокусить вашу броню. Тщетно. Вы без проблем прокалываете его брюхо\n");}
                            break;
                        }
                        case 2: {
                            HeroView.append("Идя по туннелю, кости перед вами оживают. Скелеты! Судя по снаряжению бывшие охотниками за сокровищями. Они заслоняют проход и требуют от вас пароль. \nПройдите проверку на удачу\n");
                            dmg =skillchec(4);
                            if(dmg>0){
                                HeroView.append("ПРОВАЛ: Фраза 'кхм... Темницы рухнут и свобода...кхм' явно не устроила скелетов, они выхватили мечи и набросились на вас. Вы получили "+dmg+" урона\n");}
                            else{HeroView.append("УСПЕХ: От безысходности вы произносите первое пришедшее слово на ум 'МОРОЖЕНКА'. На удивление это сработало! Скелеты обратно превратились в груду костей, осмотрев которую вы находите 5 монет.\n");
                            gold =gold+5;
                            }
                            break;
                        }
                        case 3: {
                            HeroView.append("Ловушка!\n");
                            itsatrap ();
                            break;
                        }
                        case 4: {
                            HeroView.append("Перед вами непроходимая каменная стена, хотя постойте это же Каменный Голем! Огрмная цельнрая груда камней быстро приближается к вам\nСражайтесь!\n");
                            enemy_helf = 8;
                            fight("Голем ХП=");
                            break;
                        }
                        case 5: {
                            HeroView.append("На вашем пути встретился человек в плаще, который стоит к вам спиной. Пока вы решали, что делать, незнакомец разворачивается. К сожалению, это не человек, а Лич, опаснейший соперник, который ко всему прочему еще и владеет древней магией.\nСражайтесь!\n");
                            enemy_helf = 6;
                            fight("Лич ХП=");
                            break;
                        }
                        case 6: {
                            HeroView.append("Последние 10 минут, что вы идете, в воздухе чувствуется противный запах, который еще и усиливается. Похоже, что вы приближаетесь к логову троля. Запах уже стал совсем сильным, судя по всему Троль рядом.\nСражайтесь!\n");
                            enemy_helf = 5;
                            fight("Троль ХП=");
                            break;
                        }
                        case 7: {
                            HeroView.append("Только вам решили, что перед вами свежий труп, как он встал и побрел на вас. Предстоит сражение с Зомби\nСражайтесь!\n");
                            enemy_helf = 2;
                            fight("Зомби ХП=");
                            break;
                        }
                        case 8: {
                            HeroView.append("Вы нашли тайный проход\n ");
                            secret_hod = true;
                            break;
                        }
                        case 9:{
                            int g=20 + (int) (Math.random() * 100);
                            gold+=g;
                            HeroView.append("Вы нашли на полу мешочек с золотом, похоже, что кто-то обронил в спешке и не заметил.+\n "+g);
                        }
                        case 10: {
                            break;
                        }
                    }

                }else
                    key =0;
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
        update_hp_gold();
    }

    private void newturn(){
        day_count--;
        find.setEnabled(true);
        up.setEnabled(true);
        down.setEnabled(true);
        left.setEnabled(true);
        right.setEnabled(true);
        dream.setEnabled(true);
      //  endturn.setEnabled(true);
        endturn.setEnabled(false);
     //   paint(currentallpos[2+4*(mynumber-1)]);
    }

    private void not_Activ(){
        find.setEnabled(false);
        up.setEnabled(false);
        down.setEnabled(false);
        left.setEnabled(false);
        right.setEnabled(false);
        dream.setEnabled(false);
        endturn.setEnabled(true);
    }

    private void fight(String enemy){
        int chooze;
        int card_enem, card_hero;
        Icon icon_en, icon_he;
        BufferedImage img_en, img_he;

        UIManager.put("OptionPane.yesButtonText"   , "Бить"    );
        UIManager.put("OptionPane.noButtonText"    , "Бежать"   );
        UIManager.put("OptionPane.okButtonText"    , "В БОЙ"   );
        UIManager.put("OptionPane.background",new ColorUIResource(255,255,255));

        chooze = JOptionPane.showConfirmDialog(
                this,
                "Сражаемся?",
                "Бой против "+enemy,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,icon);
        if(chooze == JOptionPane.YES_OPTION){

                    try {

                        img_en = ImageIO.read(new File("src\\main\\resources\\Drawable\\Attacks\\" + 10 + ".png"));
                        img_en = ChangeImage(img_en, 0, 177, 250, 1, 1);
                        icon_en = new ImageIcon(img_en);
                        JLabel JLabel_heroCh = new JLabel("", icon_en, JLabel.LEFT);

                        img_he = ImageIO.read(new File("src\\main\\resources\\Drawable\\Attacks\\" + 10 + ".png"));
                        img_he = ChangeImage(img_he, 0, 177, 250, 1, 1);
                        icon_he = new ImageIcon(img_he);

                        JOptionPane.showConfirmDialog(
                                this,
                                JLabel_heroCh,
                                "Бой против " + enemy,
                                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, icon_he);

                        while (enemy_helf > 0) {

                            ////////////////////////////////Бой/////////////////
                            int a = (int) (Math.random() * 3);
                            card_hero = a;
                            img_he = ImageIO.read(new File("src\\main\\resources\\Drawable\\Attacks\\" + a + ".png"));
                            img_he = ChangeImage(img_he, 0, 177, 250, 1, 1);
                            icon_he = new ImageIcon(img_he);

                            a = (int) (Math.random() * 3);
                            card_enem = a;
                            damage( card_hero, card_enem);

                            img_en = ImageIO.read(new File("src\\main\\resources\\Drawable\\Attacks\\" + a + ".png"));
                            img_en = ChangeImage(img_en, 0, 177, 250, 1, 1);
                            icon_en = new ImageIcon(img_en);
                            JLabel_heroCh = new JLabel(figt_dam, icon_en, JLabel.RIGHT);

                            chooze = JOptionPane.showConfirmDialog(
                                    this,
                                    JLabel_heroCh,
                                    "Бой против " + enemy+""+enemy_helf+" Здоровье Героя = "+hero_helf,
                                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, icon_he);
                            ////////////////////////////////Бой////////////////////
                            if(chooze == JOptionPane.YES_OPTION){
                            }

                            if(chooze == JOptionPane.NO_OPTION){
                                if(damage( 10, 10) == 0){
                                    break;
                                }
                            }
                        }

                } catch(IOException e){
                    System.out.println(e);
                }

        }

    }

    private int damage(int card_hero, int card_enem) {

        int e = enemy_helf;
        int h = hero_helf;
        if(card_hero == 0 && card_enem == 0)
        { enemy_helf = enemy_helf-2;
          hero_helf = hero_helf - 2;
        }
        if(card_hero == 0 && card_enem == 1)
        {
            hero_helf = hero_helf - 2;
        }
        if(card_hero == 0 && card_enem == 2)
        {
            enemy_helf = enemy_helf-1;
        }
        if(card_hero == 1 && card_enem == 0)
        {
            hero_helf = hero_helf - 1;
        }
        if(card_hero == 2 && card_enem == 0)
        {
            hero_helf = hero_helf - 1;
        }

        if(card_hero == 1 && card_enem == 1)
        {
            hero_helf = hero_helf - 1;
            enemy_helf = enemy_helf-1;
        }
        if(card_hero == 1 && card_enem == 2)
        {
            enemy_helf = enemy_helf-1;
        }
        if(card_hero == 2 && card_enem == 2)
        {
            hero_helf = hero_helf - 1;
            enemy_helf = enemy_helf-1;
        }
        if(card_hero == 2 && card_enem == 1)
        {
            hero_helf = hero_helf - 1;
        }

        if(card_hero == 10 && card_enem == 10) {
            int chec = skillchec(2);
            if (chec > 0)
                hero_helf = hero_helf - chec;
            else
                return 0;
        }

        figt_dam = "Вы бъете на "+ (e - enemy_helf)+". Противник бъет на "+(h-hero_helf);
        update_hp_gold();
        return 1;
    }
//1-сила 2-ловкость 3-защита 4-удача
    private int skillchec(int stat) {
        int a = (int) (Math.random() * 12);
        System.out.println(a);
       switch(stat){
           case 1:{
                if(stats[0] < a){
                    hero_helf = hero_helf - (a - stats[0]);
                    return a - stats[0];
                }
                else
                {
                    return 0;
                }
           }
           case 2:{
               if(stats[1] < a){
                   hero_helf = hero_helf - (a - stats[1]);
                   return a - stats[1];
               }
               else
               {
                   return 0;
               }
           }
           case 3:{
               if(stats[2] < a){
                   hero_helf = hero_helf - (a - stats[2]);
                   return a - stats[2];
               }
               else
               {
                   return 0;
               }
           }
           case 4:{
               if(stats[3] < a){
                   hero_helf = hero_helf - (a - stats[3]);
                   return a - stats[3];
               }
               else
               {
                   return 0;
               }
           }
       }

        update_hp_gold();
        return 1;
    }

    private void fillStats(int hero) {

        switch(hero){
            case 0:{
                stats[0] = 4;
                stats[1] = 9;
                stats[2] = 4;
                stats[3] = 6;

                break;
            }
            case 1:{
                stats[0] = 9;
                stats[1] = 4;
                stats[2] = 5;
                stats[3] = 4;
                break;
            }
            case 2:{
                stats[0] = 3;
                stats[1] = 5;
                stats[2] = 4;
                stats[3] = 9;
                break;
            }
            case 3:{
                stats[0] = 7;
                stats[1] = 5;
                stats[2] = 9;
                stats[3] = 4;
                break;
            }
            case 4:{
                stats[0] = 9;
                stats[1] = 9;
                stats[2] = 9;
                stats[3] = 9;
                hero_helf=40;
                break;
            }
        }

    }

    private void itsatrap () {
        int a = (int) (Math.random() * 4);
        int dmg=0;
        switch(a){
            case 0:{
                HeroView.setText("С грохотом с потолка падает огромный камень, все что вам остается попробовать его поймайть и откинуть подальше.\nНужно пройти проверку на силу.\n");
                dmg = skillchec(0);
                if(dmg>0){
                    HeroView.append("ПРОВАЛ: Вам не удалось поймать камень и отшвырнуть его.  Вы получили "+dmg+"урона\n");
                }
                else
                    HeroView.append("УСПЕХ: Вы поймали камень и отшвырнули его в сторону. Это оказалось проще, чем вы думали\n");

                break;
            }
            case 1:{
                HeroView.setText("Вы задеваете веревку, низко весящую над полом, после чего на вас со свистом полетело огромное бревно. Вы пытаетесь кувырком резко перекатиться в сторону.\nНужно пройти проверку на ловкость.\n");
                dmg = skillchec(1);
                if(dmg>0){
                    HeroView.append("ПРОВАЛ: Вам не удалось быстро перекатиться, из-за чего бревно прилетает вам в грудь. Вы получили "+dmg+"урона\n");
                }
                else
                    HeroView.append("УСПЕХ: Молниейностно среагировав, вы перекатились в сторону, ловко избежав столкновения.\n");

                break;
            }
            case 2:{
                HeroView.setText("Заходя в комнату, вы наступили на нажимную плиту и откуда из скрытых отверстий в стене в вас летят стрелы.\nНужно пройти проверку на защиту.\n");
                dmg = skillchec(2);
                if(dmg>0){
                    HeroView.append("ПРОВАЛ: Ваша броня не смогла спасти вас от всех стрел. Вы получили "+dmg+"урона\n");
                }
                else
                    HeroView.append("УСПЕХ: Все стрелы отскачили от вашей брони, что вы почти ничего не почувствовали, кромие слабых ударов по защите.\n");

                break;
            }
            case 3:{
                HeroView.setText("Из под пола резко выдвигаются колья, что вы даже не успеваете на это среагировать\nНужно пройти проверку на удачу.\n");
                dmg = skillchec(3);
                if(dmg>0){
                    HeroView.append("ПРОВАЛ: Выдвинутый кол под вами пробивает вам колено. Вы получили "+dmg+"урона\n");
                }
                else
                    HeroView.append("УСПЕХ: Со свистом из пола выдвинулось 1000 кольев, но вот удача, именно в том месте где вы стояли, кол был сломан.\n");
                break;
            }
        }
        update_hp_gold();
    }

    private void update_hp_gold(){
        if(hero_helf>20){
            hero_helf = 20;
        }
        if(hero_helf<1 || day_count<=0){HeroView.setText("Подземелья поглотили вас. Тьма посмертия — всё, что ждёт вас в будущем. Может быть, загробный мир будет более дружелюбным к вам…\nВы собрали "+gold+" Золота\n");
            cl.sendMsg("End_of_the_turn");
        cl.sendMsg("##session##end##");
            endturn.setText("Конец Игры");
            gameover = true;
        }
        else {
            Color col = new Color(255, 185, 0);
            Herohp.setBackground(col);
            Herogold.setBackground(col);
            day.setBackground(col);
            Herohp.setFont(new Font("Dialog", Font.PLAIN, 20));
            Herogold.setFont(new Font("Dialog", Font.PLAIN, 20));
            day.setFont(new Font("Dialog", Font.PLAIN, 20));
            Herohp.setText("ХП " + hero_helf + "");
            Herogold.setText("$" + gold + "");
            day.setText("Осталось часов: "+day_count+" /24");
        }
    }

    private int test_game_win(){
        int ok=9999999;
        System.out.println("myx= "+my_x+"myy= "+my_y);
        UIManager.put("OptionPane.yesButtonText"   , "Сбежать из подземелья"    );
        UIManager.put("OptionPane.noButtonText"    , "Остаться в подземелье"   );
        currentallpos = cl.getallpospos();
        //if(currentallpos[1+4*(mynumber-1)]!=my_x && currentallpos[0+4*(mynumber-1)]!=my_y){
            if(currentallpos[0+4*(mynumber-1)]==0 && currentallpos[1+4*(mynumber-1)]==10){
                 ok =JOptionPane.showConfirmDialog(
                        this,
                        "Победа?\n Вы собрали "+gold+" Монет!",
                        "Выжил"+enemy,
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,hero.getIcon());
            }
            if(currentallpos[0+4*(mynumber-1)]==10 && currentallpos[1+4*(mynumber-1)]==10){
                ok =JOptionPane.showConfirmDialog(
                        this,
                        "Победа?\n Вы собрали "+gold+" Монет!",
                        "Выжил"+enemy,
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,hero.getIcon());
            }
            if(currentallpos[0+4*(mynumber-1)]==10 && currentallpos[1+4*(mynumber-1)]==0){
                ok =JOptionPane.showConfirmDialog(
                        this,
                        "Победа?\n Вы собрали "+gold+" Монет!",
                        "Выжил"+enemy,
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,hero.getIcon());
            }
            if(currentallpos[0+4*(mynumber-1)]==0 && currentallpos[1+4*(mynumber-1)]==0){
                ok =JOptionPane.showConfirmDialog(
                        this,
                        "Победа?\n Вы собрали "+gold+" Монет!",
                        "Выжил"+enemy,
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,hero.getIcon());
            }
            if(ok == JOptionPane.YES_OPTION){
            //cl.sendMsg("End_of_the_turn");
            cl.sendMsg("##session##end##");
            gameover = true;
            cl.close();
            return 1;
            }
            else{
                cl.sendMsg("End_of_the_turn");
                return 1;
                //gameover = true;
            }
       // }
    }
}
