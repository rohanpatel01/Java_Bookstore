package Server.connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import Shared.Book;
import Shared.TestSend;

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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                Book book = (Book)(new ObjectInputStream(clientSocket.getInputStream()).readObject());
//                System.out.println("server got the book: " + book);
//                TestSend testSend = (TestSend)  (new ObjectInputStream(clientSocket.getInputStream()).readObject());
                System.out.println("recieved testSend from server");
                System.out.println("Book: " + book);
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("RECEIVED: " + message);
                    System.out.println(clientSocket.getInetAddress());
                    writer.println(message);
                    writer.flush();
                }
            } catch (Exception e) {
                System.out.println("Exception in Server - Client Handler");
                e.printStackTrace();
            }
        }
    }
}
