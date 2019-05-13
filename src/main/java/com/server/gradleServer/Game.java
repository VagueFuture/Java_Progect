package com.server.gradleServer;

import java.io.PrintWriter;
import java.net.Socket;

public class Game {
    public static void main(String[] args, Integer Hero, Socket fromserver, ClientUI cl) {
        TheGame game = new TheGame(Hero,fromserver,cl);
    }
}
