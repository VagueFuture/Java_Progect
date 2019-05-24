package com.server.gradleServer;

import org.apache.catalina.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyThread implements Runnable {
    private ServerSocked server;
    private PrintWriter outMessage;
    private Scanner inMessage;
    private String nick;
    private String hero;
    private int x;
    private int number;
    private int y;
    private int room;
    private int angle;
    private boolean turn;
    private boolean ready;
    private static final String HOST = "25.44.20.209";//25.44.20.209
    private static final int PORT = 2620;
    Socket Client = null;
    private static int clients_count = 0;

    public MyThread(Socket socket, ServerSocked server) {
        try {
            clients_count++;
            this.server = server;
            this.Client = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // сервер отправляет сообщение
                server.sendMessageToAllClients("%" + clients_count);
                this.number = clients_count;
                break;
            }

            while (true) {//////////////////////////////////Работа Сервера
                // Если от клиента пришло сообщение
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    // если клиент отправляет данное сообщение, то цикл прерывается и
                    // клиент выходит
                    if (clientMessage.equals("##session##end##")) {
                        System.out.println("Клиент остановлен!");
                        break;
                    }
                    //Получаем никнейм от клиента
                    System.out.println("Client message = "+clientMessage);

                    if (clientMessage.startsWith("Client_nick")) {
                        clientMessage = clientMessage.substring(12,clientMessage.length());
                        String[] subStr;
                        subStr = clientMessage.split("@");
                        this.nick = subStr[0];
                        this.hero = subStr[1];
                        server.sendAllNicknameAndHero();
                        System.out.println("Nick get! "+this.nick);
                        System.out.println("Hero get! "+this.hero);
                    }

                    if (clientMessage.startsWith("Client_ready")) {
                        this.ready =true;
                        server.CheckAllClientReady();
                        System.out.println("Ready! = "+this.getready());
                    }

                    if (clientMessage.startsWith("Client_not_ready")) {
                        this.ready =false;
                        server.CheckAllClientReady();
                        System.out.println("Ready! = "+this.getready());
                    }

                    if (clientMessage.startsWith("Client_posit")) {
                        clientMessage = clientMessage.substring(12,clientMessage.length());
                        String[] subStr;
                        subStr = clientMessage.split("@");
                        this.x = Integer.valueOf(subStr[0]);
                        this.y = Integer.valueOf(subStr[1]);
                        this.room = Integer.valueOf(subStr[2]);
                        this.angle = Integer.valueOf(subStr[3]);
                        server.sendAllClientsPosition();
                        System.out.println("get! x y = "+this.x+" "+this.y);
                        System.out.println("get! room = "+this.room);
                        System.out.println("get! angle = "+this.angle);
                    }

                    if (clientMessage.startsWith("End_of_the_turn")) {
                        this.turn = true;
                        server.CheckAllTurn();
                    }



                }
                // останавливаем выполнение потока на 100 мс
                Thread.sleep(100);
            }///////////////////////////////////////////////////////
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.close();
        }
    }

    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getnick() {
        if(this.nick == null)
            return "Не_готов";
        else
        return this.nick;
    }

    public String gethero() {
        if(this.nick == null)
            return "Ожидание";
        else
            return this.hero;
    }

    public boolean getready() {
            return this.ready;
    }

    public int getAllClients() {
        return this.clients_count;
    }

    public int getClientNumber() {
        return this.number;
    }

    public void setAmIn(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public int getAmInX() {
        return this.x;
    }

    public int getAmInY() {
        return this.y;
    }

    public boolean getTurn() {
        return this.turn;
    }

    public void setgetTurnFalse() {
        this.turn = false;
    }

    public int getAmInRoomAngle() {
        return this.angle;
    }

    public int getAmInRoom() {
        return this.room;
    }

    public void close() {
        // удаляем клиента из списка
        server.removeClient(this);
        clients_count--;
        server.sendMessageToAllClients("@" + clients_count);
    }

}
