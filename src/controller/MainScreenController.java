package controller;
/**
 * Supplied controller MainScreenController.java
 */

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.awt.*;
import java.awt.Label;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Ryan.Slack
 * JavaDoc route: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/index.html
 */

public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;


    /**
     Associated with the Parts Table
     **/
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partIDCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvLevelCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;


    /**
     * Associated with Products Table
     */
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> prodIDCol;
    @FXML
    private TableColumn<Product, String> prodNameCol;
    @FXML
    private TableColumn<Product, Integer> prodInventoryLevelCol;
    @FXML
    private TableColumn<Product, Double> prodPriceCol;
    @FXML
    private Label partsLabel;
    @FXML
    private TextField searchPartTxt;
    @FXML
    private Button searchPartBtn;
    @FXML
    private Label productsLabel;
    @FXML
    public TextField searchProdTxt;
    @FXML
    private Button searchProdBtn;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** @param actionEvent onAddPart
     * takes you to the add part screen and auto generates a disabled ID
     * @throws IOException OnAddPart
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddPartForm.fxml"));
        loader.load();
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** @param actionEvent onModifyPart
     * takes you to the modify screen and populates attributes into the textfields
     * try/catch block was added to prevent runtime errors
     * @throws IOException OnModifyPart
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartForm.fxml"));
            loader.load();

            ModifyPartController MPController = loader.getController();
            MPController.sendPart(partsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Please select a part to be modified!");
            alert.showAndWait();
        }

    }

    /** @param actionEvent  onDeletePart
     * removes the selected part from the parts list
     */
    public void onDeletePart(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete current item. Continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part part = partsTable.getSelectionModel().getSelectedItem();
            Inventory.getAllParts().remove(part);
        }
    }

    /** @param actionEvent onAddProd
     * takes you to the add product screen and auto generates a disabled ID
     * @throws IOException OnAddProd
     */
    public void onAddProd(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddProductForm.fxml"));
        loader.load();
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** @param actionEvent onModifyProd
     * takes you to the modify screen and populates attributes into the textfields
     * try/catch block was added to prevent runtime errors
     * @throws IOException OnModifyProd
     */
    public void onModifyProd(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductForm.fxml"));
            loader.load();

            ModifyProductController MPdController = loader.getController();
            MPdController.sendProduct(productsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Please select a product to be modified!");
            alert.showAndWait();
        }
    }

    /** @param actionEvent onDeleteProduct
     * this deleted a selected Product with no associated parts. If parts exist, error messages are shown.
     * @throws IOException OnDeleteProduct
     */
    // button to delete the product from main page
    public void onDeleteProduct(ActionEvent actionEvent) throws IOException {
        ObservableList<Part> associatedParts = FXCollections.observableArrayList();
        boolean deleted = false;
        Product deleteProduct = productsTable.getSelectionModel().getSelectedItem();
        if (productsTable.getItems().isEmpty()) {
            errorWindow(1);
            return;
        }
        if (!productsTable.getItems().isEmpty() && deleteProduct == null) {
            errorWindow(2);
            return;
        }
        if (deleteProduct.associatedParts.size() == 0) {
            boolean confirm = confirmDelete(deleteProduct.getName());
            if (!confirm) {
                return;
            }

        } else {
            infoWindow(deleteProduct.getName());
            return;
        }
        Product product = productsTable.getSelectionModel().getSelectedItem();
        Inventory.getAllProducts().remove(product);}

    /** @param code errorWindow
     * code used when multiple runtime errors exist
     */
    private void errorWindow(int code) {
        if (code == 1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("there is nothing to select!");
            alert.showAndWait();
        }
        if (code == 2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("you must select an item");
            alert.showAndWait();
        }
    }
    /** @param name confirmDelete
     * simple error message built to simplofy code for the delete product action
     */
    private boolean confirmDelete (String name){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to delete: "+ name);
        alert.setContentText("Click OK to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }
    /** @param name infoWindow
     * simple error message built to simplofy code for the delete product action
     */
    private void infoWindow(String name){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmed");
        alert.setContentText(name + " still has parts assigned to it and has NOT been deleted!");
        alert.showAndWait();
    }

    /** @param actionEvent onExit
     * Clicking this button will exit the program
     * also, i left the old way/ less scaleable way of adding an alert to demonstrate how my program changed oer time.
     */
    //exits the program
    public void onExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);}
    }

    /**@param actionEvent  onSearchPartBtn
     * this gets text from search field and matches it against part ID or part Name in the parts table
     * @throws IOException onSearchPartBtn
     * @return true
     */
    public boolean onSearchPartBtn(ActionEvent actionEvent) throws IOException {
        if (searchPartTxt.getText().isEmpty()) {
            partsTable.setItems(lookupPart());
        } else {
            String q = searchPartTxt.getText();
            ObservableList<Part> searchPartsTable = lookupPart(q);
            if (searchPartsTable.size() == 0){
                partsTable.setItems(lookupPart());
            } else {
                partsTable.setItems(searchPartsTable);
            }
        }
        return true;
    }
    public ObservableList<Part> lookupPart() {
        ObservableList<Part> namedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.lookupPart();

        for (Part pt : allParts) {
            namedPart.add(pt);
        } return namedPart;
    }

    /** @return lookupPart
     * this does the search for part by Name.
     * I also left the old way of adding alerts in the code.
     * After entering these alerts several times I changed my method to allow for future ease/ scalability.
     * @param partialName lookupPart
     */
    public ObservableList<Part> lookupPart(String partialName) {
        ObservableList<Part> namedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Part pt : allParts) {
            if (pt.getName().contains(partialName) || (pt.getId() + "").contains(partialName)) {
                namedPart.add(pt);
            }
        }
        if (namedPart.size() == 0 ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search Error");
            alert.setContentText("No part found");
            alert.showAndWait();
        }
        return namedPart;
    }

    /** @param partId q
     * @return  allParts
     * this does the search for part by ID
     */
    public Part searchPartId(int partId){
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(int i= 0; i < allParts.size(); i++){
            Part q = allParts.get(i);
            if(q.getId() == partId){
                return q;
            }
        } return null;
    }

    /**@param actionEvent  onSearchProdBtn
     * this gets text from search field and matches it against product ID or product Name in the products table
     * @throws IOException OnSearchProdBtn
     * @return true
     */
    //Here I am doing the PRODUCT searches
    public boolean onSearchProdBtn(ActionEvent actionEvent) throws IOException {
        if (searchProdTxt.getText().isEmpty()) {
            productsTable.setItems(lookupProduct());
        } else {
            String qq = searchProdTxt.getText();
            ObservableList<Product> searchProdsTable = lookupProduct(qq);
            if (searchProdsTable.size() == 0) {
                productsTable.setItems(lookupProduct());
            } else {
                productsTable.setItems((searchProdsTable));
            }
        }
        return true;
    }
    private ObservableList<Product> lookupProduct(){
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.lookupProduct();

        for (Product pd : allProducts){
            namedProduct.add(pd);
        } return namedProduct;
    }

    /** @return lookupProduct
     * this does the search for product by Name.
     * I also left the old way of adding alerts in the code.
     * After entering these alerts several times I changed my method to allow for future ease/ scalability.*/
    private ObservableList<Product> lookupProduct(String partialName) {
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (Product pd : allProducts) {
            if (pd.getName().contains(partialName) || (pd.getId() + "").contains((partialName))) {
                namedProduct.add(pd);
            }
        }
        if (namedProduct.size() == 0 ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search Error");
            alert.setContentText("No product found");
            alert.showAndWait();
        }
        return namedProduct;
    }

    /**
     * this does the search for part by ID
     */
    private Product searchProdId(int prodId){
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(int i= 0; i < allProducts.size(); i++){
            Product pd = allProducts.get(i);
            if(pd.getId() == prodId){
                return pd;
            }
        }
        return null;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         *    Displays all parts in parts table -excluding max, min, source
         */

        partsTable.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        /**
         *      Displays all Products in product table - excluding max, min, source
         */

        productsTable.setItems(Inventory.getAllProducts());
        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        ObservableList<Part> allParts = Inventory.getAllParts();
        partsTable.setItems(allParts);
    }



}
