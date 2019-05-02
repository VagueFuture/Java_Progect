package com.server.gradleServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class ServerSocked {

    public static LinkedList<MyThread> serverList = new LinkedList<>();


    public static void main(String[] args) {



        ServerSocket servers = null;

        while(true) {


            try {
                servers = new ServerSocket(2620);
            } catch (IOException e) {
                System.out.println("Couldn't listen to port 2620");
                System.exit(-1);
            }


            Socket Client = null;
            try {
                System.out.print("Waiting for a client...");
                Client = servers.accept();
                serverList.add(new MyThread(Client));
                System.out.println("Client connected");
            } catch (IOException e) {

                System.out.println("Can't accept");
                System.exit(-1);
            }

            Runnable r =new MyThread(Client);
            Thread t = new Thread(r);

            t.start();
            t.run();

            BufferedReader in = null;
            PrintWriter out = null;



            try {

                in = new BufferedReader(new InputStreamReader(Client.getInputStream()));
                out = new PrintWriter(Client.getOutputStream(), true);

                String input, output;

                System.out.println("Wait for messages");

                while ((input = in.readLine()) != null) {
                    if (input.equalsIgnoreCase("exit")) break;


                    for (MyThread vr : ServerSocked.serverList) {
                        Client = vr.getClient();
                        out.println(input);

                        // отослать принятое сообщение с
                        // привязанного клиента всем остальным включая его
                    }

                    System.out.println(input);
                }

                out.close();

                in.close();
                Client.close();
                servers.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}