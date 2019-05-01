package com.server.gradleServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



@Controller
public class ClientUI {
    private JButton button1;
    private JPanel panel1;
    private JTextField textField1;

    public  String str="lol";


    public static void main(String[] args){
        JFrame frame=new JFrame("App");
        frame.setContentPane(new ClientUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public ClientUI() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                str="Hai";
                JOptionPane.showMessageDialog(null,"Clicked");
                JOptionPane.showMessageDialog(null,str);
                textField1.setText(str);
            }
        });
    }
    @RequestMapping("/")
    @ResponseBody
    String hello(){
        return str;
    }
}
