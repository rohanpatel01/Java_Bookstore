package Client;

import Shared.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class AdminLibraryController {

    private Stage stage;
    private Scene scene;
    private Parent root;


    Client client;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    Inventory inventory;
    Object objectRecievedFromServer;

    @FXML
    Button bookButton;
    @FXML
    Button movieButton;
    @FXML
    Button gameButton;
    @FXML
    Button audiobookButton;
    @FXML
    Button exitButton;

    @FXML
    TextField bookTitle;
    @FXML
    TextField bookAuthor;
    @FXML
    TextField bookDescription;
    @FXML
    TextField bookNumPages;
    @FXML
    TextField bookNumCopies;

    @FXML
    TextField movieTitle;
    @FXML
    TextField movieDirector;
    @FXML
    TextField movieDescription;
    @FXML
    TextField movieRuntime;
    @FXML
    TextField movieNumCopies;

    @FXML
    TextField gameTitle;
    @FXML
    TextField gameDeveloper;
    @FXML
    TextField gameDescription;
    @FXML
    TextField gameNumCopies;


    @FXML
    TextField audiobookTitle;
    @FXML
    TextField audiobookNarrator;
    @FXML
    TextField audiobookDescription;
    @FXML
    TextField audiobookNumCopies;

    @FXML
    HBox booksHBox;
    @FXML
    HBox moviesHBox;
    @FXML
    HBox gamesHBox;
    @FXML
    HBox audiobooksHBox;


    public AdminLibraryController() {
        client = new Client();
        client.setupNetworking();
        inventory = new Inventory();

        System.out.println("admin socket created");

        try {
            objectOutputStream = new ObjectOutputStream(client.clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(client.clientSocket.getInputStream());
        } catch (IOException ioException) { ioException.printStackTrace(); }



        Thread t = new Thread(new AdminLibraryController.ObjectReader()); //, objectOutputStream
        t.start();



        // create and start thread to listen for objects coming in from server
//        Thread objectListenerThread = new Thread(() -> {
//
//            try {
//                while (true) {
//                    if ((objectRecievedFromServer = (objectInputStream.readObject())) != null ) {
//                        // TODO: make sure admin can populate inventory on start
////                        inventory.bookList.put(((Book) objectRecievedFromServer).title, (Book) objectRecievedFromServer);
//                        inventory.printInventory();
//
//                    }
//
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//
//
//        });
//        objectListenerThread.start();
    }


    class ObjectReader implements Runnable{

        @Override
        public void run() {
            while (true) {
                try {
                    // allow for all types of items
                    // based on what type of item - have method to determine type then make index correct one for inventory big
                    // need to change what hbox we target based on what type of item it is

                    if ((objectRecievedFromServer = objectInputStream.readObject()) != null) {

                        if (objectRecievedFromServer instanceof LibraryItem) {
                            System.out.println("recieved library item");
                            int itemIndex = inventory.determineItemType((LibraryItem)objectRecievedFromServer);
                            HBox itemHbox = determineItemHbox((LibraryItem)objectRecievedFromServer);

                            inventory.inventoryLists.get(itemIndex).put(((LibraryItem) objectRecievedFromServer).title,(LibraryItem) objectRecievedFromServer);
                            inventory.printInventory();
//                       currentUser.printCart();

                            // handle card creation and modification
                            int currentItemCount = inventory.inventoryLists.get(itemIndex).get(((LibraryItem) objectRecievedFromServer).title).numCopies;
                            if (currentItemCount == 0) {
                                boolean foundCard = false;
                                for (Node node : itemHbox.getChildren()) {
                                    if (node.getId() != null && node.getId().equals(((LibraryItem) objectRecievedFromServer).title)) {
                                        foundCard = true;
                                        // found card - means card was checked out and thus needs to decrease in count and disable button
                                        Platform.runLater(() -> {
                                            String newButtonName = (((LibraryItem) objectRecievedFromServer).title) + (((LibraryItem)objectRecievedFromServer).numCopies)  + "";
                                            ((Button) node.lookup(".button")).setText(newButtonName);
                                            node.setDisable(true);
                                        });
                                    }
                                }
                                // card does not exist - means loaded into from inventory and need to create card for it
                                if (!foundCard) {
                                    System.out.println("create card");
                                    HBox createdBookCard = new HBox();
                                    createdBookCard.setId(((LibraryItem) objectRecievedFromServer).title);
                                    Button checkoutButton = new Button( ((LibraryItem) objectRecievedFromServer).title + ((LibraryItem) objectRecievedFromServer).numCopies );
                                    checkoutButton.setUserData(itemIndex);
                                    // give the button some user data so we can tell later what type of object the button corresponds to
//                                    checkoutButton.setOnAction(event -> itemSelected(event));  // , (Book) objectRecievedFromServer)

                                    if (((LibraryItem)objectRecievedFromServer).numCopies == 0) {
                                        checkoutButton.setDisable(true);
                                    }

                                    Platform.runLater(() -> {
                                        createdBookCard.getChildren().add(checkoutButton);
                                        itemHbox.getChildren().add(createdBookCard);
                                    });
                                }

                            } else { // item is greater or less than 0 - just update the count and update


                                // if already there update, else create
                                boolean foundCard = false;
                                for (Node node : itemHbox.getChildren()) {
                                    if (node.getId() != null && node.getId().equals(((LibraryItem) objectRecievedFromServer).title)) {
                                        foundCard = true;
                                        Platform.runLater(() -> {
                                            String newButtonName = (((LibraryItem) objectRecievedFromServer).title) +(( (LibraryItem)objectRecievedFromServer).numCopies)  + "";
                                            ((Button) node.lookup(".button")).setText(newButtonName);
                                            node.setDisable(false); // shouldn't need this
                                        });
                                    }
                                }
                                // adding new item that was not in inventory before - create card
                                if (!foundCard) {
                                    System.out.println("adding new item to inventory that was not created before");
                                    HBox createdBookCard = new HBox();
                                    createdBookCard.setId(( (LibraryItem)objectRecievedFromServer).title);
                                    Button checkoutButton = new Button( ( (LibraryItem)objectRecievedFromServer).title + ( (LibraryItem)objectRecievedFromServer).numCopies );
                                    checkoutButton.setUserData(itemIndex);

                                    Platform.runLater(() -> {
                                        createdBookCard.getChildren().add(checkoutButton);
                                        itemHbox.getChildren().add(createdBookCard);
                                    });
                                }


                            }

                        }


                    }

                }  catch (IOException| ClassNotFoundException  exception) {exception.printStackTrace(); }
            }
        }
    }




    private HBox determineItemHbox(LibraryItem item) {

        if (item instanceof Book) {
            return booksHBox;
        } else if (item instanceof Movie) {
            return moviesHBox;
        } else if (item instanceof Game) {
            return gamesHBox;
        } else if (item instanceof AudioBook){
            return audiobooksHBox;
        }

        return null; // will never happen
    }

    @FXML
    public void exit(ActionEvent event) {
        System.out.println("exit");
        try {
            root = FXMLLoader.load(getClass().getResource("ExitPage.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioException) { ioException.printStackTrace(); }
    }

    public void AddBook() {
        System.out.println("book selected");
        Book item = new Book(bookTitle.getText(), bookDescription.getText(), bookAuthor.getText(), Integer.parseInt(bookNumPages.getText()), Integer.parseInt(bookNumCopies.getText()) );
        try {
            objectOutputStream.writeObject(item);
            objectOutputStream.flush();
            System.out.println("sending: " + item);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }        // send server the book and have it store in inventory
    }

    public void AddMovie() {

        System.out.println("book selected");
//        Movie item = new Movie("Your Name", "Great Movie", "1:00", "Some japanese dude", 1);
        Movie item = new Movie(movieTitle.getText(), movieDescription.getText(), movieRuntime.getText(), movieDirector.getText(),Integer.parseInt(movieNumCopies.getText()) );

        try {
            objectOutputStream.writeObject(item);
            objectOutputStream.flush();
            System.out.println("sending: " + item);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }        // send server the book and have it store in inventory
    }

    public void AddGame() {

        System.out.println("book selected");
//        Game item = new Game("League of Legends", "dont solo q", "Riot", 1);
        Game item = new Game(gameTitle.getText(), gameDescription.getText(), gameDeveloper.getText(), Integer.parseInt(gameNumCopies.getText() ));

        try {
            objectOutputStream.writeObject(item);
            objectOutputStream.flush();
            System.out.println("sending: " + item);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }        // send server the book and have it store in inventory
    }

    public void AddAudiobook() {

        System.out.println("book selected");
//        AudioBook item = new AudioBook("A Court of Thorns and Roses", "Something Julian's GF would like I think", "some narrator", 1);
        AudioBook item = new AudioBook(audiobookTitle.getText(), audiobookDescription.getText(), audiobookNarrator.getText(), Integer.parseInt(audiobookNumCopies.getText() ));

        try {
            objectOutputStream.writeObject(item);
            objectOutputStream.flush();
            System.out.println("sending: " + item);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }        // send server the book and have it store in inventory
    }
}
