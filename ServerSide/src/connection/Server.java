package connection;
import Items.Inventory;
import Items.Book;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        new Server().setupNetworking();
    }

    private void setupNetworking() {
        try {
            ServerSocket server = new ServerSocket(1024);
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("incoming transmission");

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
            }
        } catch (IOException ioe) {}
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;
//        private Inventory inventory;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
//            inventory = new Inventory();
        }


        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = reader.readLine()) != null) {

                    try {
                        Book book = (Book)(new ObjectInputStream(clientSocket.getInputStream()).readObject());
                        System.out.println("GOT THE BOOK " + book);
                    } catch (IOException ioe) {

                    } catch (ClassNotFoundException cnfe) {

                    }
                    System.out.println("RECEIVED: " + message);
                    System.out.println(clientSocket.getInetAddress());
                    writer.println(message);
                    writer.flush();
                }
            } catch (IOException ioe) {}
        }
    }
}
