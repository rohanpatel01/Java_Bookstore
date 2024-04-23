package Client;

import java.io.*;
import java.net.Socket;

import Shared.Book;

public class Client {

    public Socket clientSocket;

    public static void main(String[] args) {
        Thread clientNetworkingThread = new Thread(() -> {
            new Client().setupNetworking();
        });
        clientNetworkingThread.start();
    }

     public void setupNetworking() {
        try {
            clientSocket = new Socket("11.21.69.196", 1024);
            System.out.println("Client Socket Created: " + clientSocket);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}