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
                System.out.println("client connected");

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
//                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                String message;
                Object recievedObject = null;
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                while ( ( recievedObject = objectInputStream.readObject() ) != null ){
                    if (recievedObject instanceof Book) {
                        System.out.println("Server got book");
                        Book book = (Book) recievedObject;
                        System.out.println("Book: " + book);
                    }
                    System.out.println("Server recieved object");
                }
            } catch (IOException ioe) { ioe.printStackTrace(); }
            catch (ClassNotFoundException classNotFoundException) { classNotFoundException.printStackTrace(); }
        }
    }
}
