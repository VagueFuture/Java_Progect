package com.server.gradleServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;


public class ClientUI {

    private JPanel panel1;

    private JTextField textField1;
    private JList list1;

    public static void main(String[] args){
        JFrame frame=new JFrame("App");
        Dimension size=new Dimension(800,600);
        frame.setPreferredSize(size);
        frame.setContentPane(new ClientUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        Socket fromserver = null;
        try {
            fromserver = new Socket("localhost",2620);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }

    }

    private ClientUI() {
        DefaultListModel dlm=new DefaultListModel();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlm.addElement(textField1.getText());
                list1.setModel(dlm);
            }
        });
    }
}

