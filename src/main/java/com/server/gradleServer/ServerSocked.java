package com.server.gradleServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocked {

    public static void main(String[] args) {

        ServerSocket servers = null;

        try {
            servers = new ServerSocket(2620);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 2620");
            System.exit(-1);
        }


        Socket Client;
        try {
            System.out.print("Waiting for a client...");
            Client = servers.accept();
            System.out.println("Client connected");
        } catch (IOException e) {

            System.out.println("Can't accept");
            System.exit(-1);
        }


    }
}