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


public class ClientUI {

    private JPanel panel1;
    private JButton button1;
    private JTextField textField1;
    private JList list1;
    private JComboBox comboBox1;

    public static void main(String[] args){
        JFrame frame=new JFrame("App");
        Dimension size=new Dimension(800,600);
        frame.setPreferredSize(size);
        frame.setContentPane(new ClientUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void SayMyName(String text){
        Socket fromserver = null;
        DefaultListModel dlm=new DefaultListModel();
        try {
            fromserver = new Socket("25.44.20.209",2620);
            BufferedReader in  = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
            PrintWriter out = new PrintWriter(fromserver.getOutputStream(),true);

            String fserver;

                out.println(text);
                out.flush();
                fserver = in.readLine();
            System.out.println(fserver);
                dlm.addElement(fserver);
                list1.setModel(dlm);

            out.close();
            in.close();
            fromserver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ClientUI() {

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SayMyName(textField1.getText());
            }
        });
    }
}

