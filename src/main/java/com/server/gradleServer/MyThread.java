package com.server.gradleServer;

import java.net.Socket;

public class MyThread implements Runnable {
    Socket Client = null;
    public void run() {
    }
    public MyThread (Socket s){
        this.Client = s;
    }

    public Socket getClient() {
        return this.Client;
    }

}
