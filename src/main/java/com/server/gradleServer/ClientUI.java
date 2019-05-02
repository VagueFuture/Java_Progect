package com.server.gradleServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ClientUI extends JFrame{

    private JPanel panel1;
    private JButton button1;
    private JTextField nickname;
    private JComboBox comboBox1;
    private JLabel numberofclient;
    private JTextArea textArea1;

    private PrintWriter out;
    private Scanner in;

    private String clientName = "";
    // получаем имя клиента
    public String getClientName() {
        return this.clientName;
    }

    public ClientUI() {
        Socket fromserver = null;
        DefaultListModel dlm = new DefaultListModel();
        try {
            fromserver = new Socket("25.44.20.209", 2620);
            in = new Scanner(fromserver.getInputStream());
            out = new PrintWriter(fromserver.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("App");
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
                    clientName = nickname.getText();
                    sendMsg();
                }
            }
        });


        new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                // бесконечный цикл
                while (true) {
                    // если есть входящее сообщение
                    if (in.hasNext()) {
                        // считываем его
                        String inMes = in.nextLine();
                        String clientsInChat = "Количество человек в лобби:";
                        if (inMes.indexOf(clientsInChat) == 0) {
                            numberofclient.setText("Количество человек в лобби:"+inMes);
                        } else {
                            // выводим сообщение
                            textArea1.append(inMes);
                            // добавляем строку перехода
                            textArea1.append("\n");
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }).start();
    }
    /*addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            try {
                // здесь проверяем, что имя клиента непустое и не равно значению по умолчанию
                if (!clientName.isEmpty() && clientName != "Введите ваше имя: ") {
                    outMessage.println(clientName + " вышел из чата!");
                } else {
                    outMessage.println("Участник вышел из чата, так и не представившись!");
                }
                // отправляем служебное сообщение, которое является признаком того, что клиент вышел из чата
                outMessage.println("##session##end##");
                outMessage.flush();
                outMessage.close();
                inMessage.close();
                clientSocket.close();
            } catch (IOException exc) {

            }
        }
    });
    // отображаем форму
    setVisible(true);
}*/

    public void sendMsg() {
        String messageStr = nickname.getText() + ": " + textArea1.getText();
        out.println(messageStr);
        out.flush();
        textArea1.setText("");
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

