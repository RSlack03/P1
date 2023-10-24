package main;
/**
 * Supplied Main.java
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import java.util.Objects;

/**
 * @author Ryan.Slack
 * RUNTIME ERROR: To Prevent runtime errors, Try/ Catch blocks were added to prevent the program from failing
 * FUTURE ENHANCEMENT: Add drop down list of category to  better categorize and sort parts and products.
 *
 * Javadoc: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/main/Main.html
 *
 *
 */

public class Main extends Application {
    @Override

    /**
     * loads main screen when application starts
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainScreen.fxml")));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root, 1000, 500));
        stage.show();
    }


    /**
     *Adds example data to columns
     * @param args Sample input data
     */
    public static void main(String[] args){

        InHouse part1 = new InHouse(11, "Front Tire", 16.99, 26, 1, 123, 99);
        InHouse part2 = new InHouse(22, "Back Tire", 17.99, 24, 1, 100, 98);
        InHouse part3 = new InHouse(33, "Chain", 18.99, 68, 1, 100, 97);
        Outsourced part4 = new Outsourced(44, "Pedals", 19.99, 44, 1, 100, "Bike Feet");
        Outsourced part5 = new Outsourced(55, "Handle Grips", 20.99, 15, 1, 100, "Bike Hands");

        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        Inventory.addPart(part5);

        Product product1 = new Product(9000, "Bike", 100.00, 20, 1, 99);
        Product product2 = new Product(9001, "Trike",150.00, 10,1,99);
        Product product3 = new Product(9002, "Unicycle",50.00, 50,1,99);
        Product product4 = new Product(9003, "Scooter",200.00, 30,1,99);
        Product product5 = new Product(9004, "Skateboard",99.00, 40,1,99);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);
        Inventory.addProduct(product5);


        launch(args);

    }
}
