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
    private static final String HOST = "25.44.20.209";
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
             //   server.sendMessageToAllClients("Новый участник вошёл в чат!");
                server.sendMessageToAllClients("@" + clients_count);
                break;
            }

            while (true) {//////////////////////////////////Работа Сервера
                // Если от клиента пришло сообщение
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    // если клиент отправляет данное сообщение, то цикл прерывается и
                    // клиент выходит из чата
                    if (clientMessage.equals("##session##end##")) {
                        break;
                    }
                    // выводим в консоль сообщение (для теста)
                    this.nick = clientMessage;
                    System.out.println(clientMessage);
                    // отправляем данное сообщение всем клиентам
                  //  server.sendMessageToAllClients(clientMessage);
                    server.sendallnickname();
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
            return "";
        else
        return this.nick;
    }

    public void close() {
        // удаляем клиента из списка
        server.removeClient(this);
        clients_count--;
        server.sendMessageToAllClients("@" + clients_count);
    }

}
