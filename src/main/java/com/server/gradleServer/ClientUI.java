package com.server.gradleServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JFrame frame;
    private Socket fromserver;

    private PrintWriter out;
    private Scanner in;

    private String clientName = "";
    // получаем имя клиента
    public String getClientName() {
        return this.clientName;
    }

    public ClientUI() {
        fromserver = null;
        DefaultListModel dlm = new DefaultListModel();
        try {
            fromserver = new Socket("25.44.20.209", 2620);
            in = new Scanner(fromserver.getInputStream());
            out = new PrintWriter(fromserver.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        frame = new JFrame("App");
        Dimension size = new Dimension(800, 600);
        frame.setPreferredSize(size);
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nickname.getText().trim().isEmpty()) {
                    textArea1.setText("");
                    clientName = "Client_nick=";
                    clientName += nickname.getText();
                    clientName += "@GoR";
                    sendMsg(clientName);
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
            try {
                // отправляем служебное сообщение, которое является признаком того, что клиент вышел из чата
                out.println("##session##end##");
                out.flush();
                out.close();
                in.close();
                fromserver.close();
            } catch (IOException exc) {

            }
        }
    });
}

    public void sendMsg(String msg) {
        out.println(msg);
        out.flush();
    }

    public void getdMsg() {/////////////НЕ РАБОТАЕТ ТОЛКОМ ВЫВОД ИГРОКОВ, НАХОДЯЩИХСЯ В ЛОББИ
        String ch="";
        if (in.hasNext()) {

          //  for (int i = 0; i < 4; i++) {
                // считываем его
                String inMes = in.next();
            if (inMes.startsWith("Client_nick")) {
                textArea1.setText("");
                inMes = inMes.substring(11,inMes.length());
                String[] subStr;
                subStr = inMes.split("-");
                for(int j=0;j<subStr.length;j++){
                    textArea1.append(subStr[j] + "\n");
                }

            }
                //ch+=inMes+"\n";
            if (inMes.indexOf('@') != -1) {
                jlNumberOfClients.setText("Человек в лобби:" + inMes.charAt(1));
            }else{
                //textArea1.append(inMes + "\n");
                }
          //  }
        }
        textArea1.append(ch);
        System.out.println(ch);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

