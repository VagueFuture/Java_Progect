package com.server.gradleServer;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class ServerSocked {

    public static ArrayList<MyThread> serverList = new ArrayList<>();


    public ServerSocked(String[] ip) {

        ServerSocket servers = null;

        Socket Client = null;

        try {
            //port p;
            servers = new ServerSocket(Integer.valueOf(ip[1]));
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 2620");
            System.exit(-1);
        }

        try {

        while (true) {

                System.out.print("Waiting for a client...");
                Client = servers.accept();
                //serverList.add(new MyThread(Client));
                MyThread client = new MyThread(Client, this,ip);
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

    public void sendAllClientsPosition() {
        String str = "Clients_post";
        for (MyThread o : serverList) {
                str +=o.getAmInX()+"@";
                str +=o.getAmInY()+"@";
                str +=o.getAmInRoom()+"@";
                str +=o.getAmInRoomAngle()+"@";
        }
        sendMessageToAllClients(str);
        System.out.println("Send = " + str);
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
            x[0] = 0; x[1] = 10; x[2] = 0; x[3] = 10;//Не Забыть проверить правильность расположения ВСЕХ игроков!
            y[0] = 0; y[1] = 0; y[2] = 10; y[3] = 10;
            int j=0;
            String str = "Clients_post";
            for (MyThread o : serverList) {
                o.setAmIn(x[j], y[j]);
                str += x[j] + "@" + y[j] + "@" + 0+"@"+ 0+"@";
                j++;
            }
            sendMessageToAllClients(str);
                System.out.println("ready pos = "+ str);

            for (MyThread o : serverList) {
                str ="Client_numbr";
                str += o.getClientNumber();
                o.sendMsg(str);
            }
            System.out.println("All_ready!!");
        }else
        System.out.println("Not_All_ready");

    }


    public void removeClient(MyThread client) {
        serverList.remove(client);
    }

    public void CheckAllTurn() {
        int i= 0;
        boolean ready;
        int client = 0;
        for (MyThread o : serverList) {
            ready=o.getTurn();
            if(ready)
                i++;
            else
                i=0;

            client = o.getAllClients();
        }
        if(i == client){
            sendMessageToAllClients("New_turn");
            for (MyThread o : serverList) {
                o.setgetTurnFalse();
            }
        }else
            System.out.println("Not_All_End_Turn");

    }

}