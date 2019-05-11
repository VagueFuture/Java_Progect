package com.server.gradleServer;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class ServerSocked {

    public static ArrayList<MyThread> serverList = new ArrayList<>();


    public ServerSocked() {

        ServerSocket servers = null;

        Socket Client = null;

        try {
            servers = new ServerSocket(2620);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 2620");
            System.exit(-1);
        }

        try {

        while (true) {



                System.out.print("Waiting for a client...");
                Client = servers.accept();
                //serverList.add(new MyThread(Client));
                MyThread client = new MyThread(Client, this);
                serverList.add(client);
                System.out.println("Client connected");
                new Thread(client).start();
            }
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(-1);
            } finally {
                try {
                    Client.close();
                    System.out.println("Сервер остановлен");
                    servers.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

    }
        public void sendMessageToAllClients(String msg) {
            for (MyThread o : serverList) {
                o.sendMsg(msg);
            }
        }

    public void sendAllNicknameAndHero() {
        String str = "Client_nick";
        for (MyThread o : serverList) {
            str +=o.getnick();
            str +=">"+ o.gethero()+"-";
        }
        sendMessageToAllClients(str);
    }

    public void CheckAllClientReady() {
        int i= 0;
        boolean ready;
        int client = 0;
        for (MyThread o : serverList) {
             ready=o.getready();
             if(ready)
                 i++;
             else
                 i=0;

             client = o.getAllClients();
        }
        if(i == client){
            sendMessageToAllClients("Start_Game");
            int[] x = new int[4];
            int[] y = new int[4];
            x[0] = -1; x[1] = 10; x[2] = 0; x[3] = 9;//Не Забыть проверить правильность расположения ВСЕХ игроков!
            y[0] = 0; y[1] = 0; y[2] = 10; y[3] = 10;
            int j=0;
            String str;
            for (MyThread o : serverList) {
                str ="Client_posit";
                o.setAmIn(x[j],y[j]);
                str += o.getAmInX()+"@"+o.getAmInY();
                o.sendMsg(str);
                j++;
            }
            System.out.println("All_ready!!");
        }else
        System.out.println("Not_All_ready");

    }


    public void removeClient(MyThread client) {
        serverList.remove(client);
    }


}