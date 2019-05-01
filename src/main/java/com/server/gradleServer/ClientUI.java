package com.server.gradleServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;


@Controller
public class ClientUI {

    private JPanel panel1;


    private String str="lol";


    public static void main(String[] args){
        JFrame frame=new JFrame("App");
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




