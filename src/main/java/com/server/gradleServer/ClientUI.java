package com.server.gradleServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ClientUI extends JFrame{

    private JPanel panel1;
    private JButton button1;
    private JTextField nickname;
    private JComboBox comboBox1;
    private JLabel jlNumberOfClients;
    private JTextArea textArea1;
    private JButton button2;
    private JFrame frame;
    private Socket fromserver;

    private PrintWriter out;
    private Scanner in;
    private int PlayerCount=0;
    private int[] allpos;
    private int mynumber;

    private Integer ChosenHero;
    private String clientName = "";
    private String  PlayerStatusNotReady= "Client_not_ready";
    private String PlayerStatusReady = "Client_ready";

    public int[] getallpospos(){
        return this.allpos;
    }

    public int getmynumber(){
        return this.mynumber;
    }

    public int getPlayerCount(){
        return this.PlayerCount;
    }

    public ClientUI() {
            fromserver = null;
            DefaultListModel dlm = new DefaultListModel();
            try {
                fromserver = new Socket("25.44.20.209", 2620);//25.44.20.209
                in = new Scanner(fromserver.getInputStream());
                out = new PrintWriter(fromserver.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            frame = new JFrame("ClientUI");
            Dimension size = new Dimension(800, 600);
            frame.setPreferredSize(size);
            frame.setContentPane(panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            comboBox1.addItem("Линделл");
            comboBox1.addItem("Крутцбек");
            comboBox1.addItem("Брат_Геринн");
            comboBox1.addItem("Хьюго_Великолепный");

            textArea1.setLineWrap(true);
            frame.setVisible(true);

            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!nickname.getText().trim().isEmpty()) {
                        textArea1.setText("");
                        clientName = "Client_nick=";
                        clientName += nickname.getText();
                        clientName += "@";
                        clientName += comboBox1.getSelectedItem();
                        ChosenHero = comboBox1.getSelectedIndex();
                        button2.setEnabled(true);
                        sendMsg(clientName);
                        sendMsg(PlayerStatusNotReady);
                    }
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // бесконечный цикл
                        while (true) {
                            getdMsg();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e);

                    }
                }
            }).start();

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    close();
                }
            });
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMsg(PlayerStatusReady);
                    button2.setEnabled(false);
                }
            });
    }

    public void close(){
        try {
            // отправляем служебное сообщение, которое является признаком того, что клиент вышел из лобби
            sendMsg("##session##end##");
            out.close();
            in.close();
            fromserver.close();
        } catch (IOException exc) {

        }
    }

    public void sendMsg(String msg) {
        try{
        out.println(msg);
    } catch (IOError exc) {
        System.out.println(exc);
    }
    }

    public void getdMsg() {
        if (in.hasNext()) {
                String inMes = in.next();
            if(inMes.startsWith("Client_numbr")){
                inMes = inMes.substring(12,inMes.length());
                String[] subStr;
                subStr = inMes.split("@");
                this.mynumber=Integer.valueOf(subStr[0]);
                //System.out.println("mynumber "+mynumber);
            }
            if(inMes.startsWith("Start_Game")){
                Game.main(null,ChosenHero,fromserver,this);
                frame.setVisible(false);
            }
            if (inMes.startsWith("Client_nick")) {
                textArea1.setText("");
                inMes = inMes.substring(11,inMes.length());
                String[] subStr;
                subStr = inMes.split("-");
                for(int j=0;j<subStr.length;j++){
                    textArea1.append(subStr[j] + "\n");
                }
            }
            if (inMes.indexOf('%') != -1) {
                jlNumberOfClients.setText("Человек в лобби:" + inMes.charAt(1));
                //PlayerCount++;

                this.PlayerCount=Character.getNumericValue(inMes.charAt(1));
                System.out.println(PlayerCount);
                allpos = new int[PlayerCount*3];
            }
            if(inMes.startsWith("Clients_post")){
                inMes = inMes.substring(12,inMes.length());
                String[] subStr;
                subStr = inMes.split("@");
                //int k=0;
                //System.out.println("Poluchil clientui");
                //System.out.println(PlayerCount);
                //System.out.println("mas client ui"+allpos.length);
                for(int i=0;i<subStr.length;i++){
                    allpos[i]=Integer.valueOf(subStr[i]);
                    //System.out.println(subStr[i]);
                    //System.out.println(allpos[i]);
                }
                this.allpos=allpos;
            }
            else{
                }
        }
    }
}

