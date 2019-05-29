package com.server.gradleServer;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        int ok;
        String[] ip=new String[2];
        String temp;
        String[] subStr;
        JTextArea info=new JTextArea();
        info.setLineWrap(false);
        UIManager.put("OptionPane.yesButtonText"   , "Создать сервер"    );
        UIManager.put("OptionPane.noButtonText"    , "Подключиться к серверу"   );
        ok= JOptionPane.showOptionDialog(null,
                new Object[] { "Введите ip сервера и порт:\n", info },
                "Dungeon Quest",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        temp=info.getText();
        subStr =temp.split(":");
        for(int i=0;i<subStr.length;i++){
            ip[i]=subStr[i];
            System.out.println(ip[i]);
        }
        if(ok==JOptionPane.YES_OPTION){
            Server server=new Server();
            Server.main(ip);
            //ClientUI client=new ClientUI(ip);
        }
        if(ok==JOptionPane.NO_OPTION){
            ClientUI client=new ClientUI(ip);
        }
        System.out.println(info.getText());
       // ClientUI clientWindow = new ClientUI();
    }
}
