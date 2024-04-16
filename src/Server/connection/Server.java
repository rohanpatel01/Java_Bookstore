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

        private Socket socket;

        ClientHandler(Socket clientSocket) {
            this.socket = clientSocket;
        }
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String recievedString = (String )(new ObjectInputStream(socket.getInputStream()).readObject());
                System.out.println("recieved string gotten: " + recievedString);

                String message = null;
                Object readObject = null;
                while (((message = reader.readLine()) != null ) ) { // || ((readObject = objectInputStream.readObject()) != null)

//                    if (readObject != null){
//                        System.out.println("Object Recieved");
//                        // hardcoding a book but need to be able to read any type of library item
//                        Book newBook = (Book) readObject;
//                        System.out.println("new book: " + newBook);
//                    }

//                    if (message != null) {
                        System.out.println("RECEIVED: " + message);
                        System.out.println(socket.getInetAddress());
                        writer.println(message);
                        writer.flush();
//                    }
                }
            } catch (Exception e) {
                System.out.println("Exception in Server - Client Handler");
                e.printStackTrace();
            }
        }
    }
}
