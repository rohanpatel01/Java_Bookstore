package Shared;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    public Map<String, LibraryItem> cartItems;

    public Cart() {
        this.cartItems = new HashMap<>();
    }

    public void add(LibraryItem libraryItem) {
       cartItems.put(libraryItem.title, libraryItem);
    }


}
