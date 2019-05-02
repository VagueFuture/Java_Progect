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

    public void sendallnickname() {
        String str = "";
        for (MyThread o : serverList) {
            str =str + o.getnick()+" \n";
        }
        System.out.println("str = " + str);
        sendMessageToAllClients(str);
    }

    public void removeClient(MyThread client) {
        serverList.remove(client);
    }


}