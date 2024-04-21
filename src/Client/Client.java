package Client;

import java.io.*;
import java.net.Socket;

import Shared.Book;

public class Client {

    public Socket clientSocket;
    PrintWriter writer;
    BufferedReader reader;


    public static void main(String[] args) {
        Thread clientNetworkingThread = new Thread(() -> {
            new Client().setupNetworking();
        });
        clientNetworkingThread.start();
    }

     public void setupNetworking() {
        try {
            clientSocket = new Socket("11.21.22.194", 1024);
            clientSocket.getOutputStream().flush(); // so server can connect?
            System.out.println("Client Socket Created: " + clientSocket);

            writer = new PrintWriter(clientSocket.getOutputStream());
            reader = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())));

            Thread readerThread = new Thread(() -> {
                String message;
                try {
                    if ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            });

            readerThread.start();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}