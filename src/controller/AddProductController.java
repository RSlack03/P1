
package controller;
/**
 * Supplied controller AddProductController.java
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Ryan.Slack
 * JavaDoc route: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/index.html
 */

public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;
    /**
     * References for Parts table on modify screen (upper table)
     */
    @FXML
    public TableView<Part> partsTable;
    @FXML
    public TableColumn<Part, Integer> partIDCol;
    @FXML
    public TableColumn<Part, String> partNameCol;
    @FXML
    public TableColumn<Part, Integer> partInvLevelCol;
    @FXML
    public TableColumn<Part, Double> partPriceCol;
    @FXML
    private TextField searchPartTxt;
    /**
     * References for Parts table on modify screen (lower table)
     */
    @FXML
    public TableView<Part> prodPartTbl;
    @FXML
    public TableColumn<Part, Integer> prodPartIDCol;
    @FXML
    public TableColumn<Part, String> prodPartNameCol;
    @FXML
    public TableColumn<Part, Integer> prodPartInvLevelCol;
    @FXML
    public TableColumn<Part, Double> prodPartPriceCol;

    /**
     * Product Labels
     */
    @FXML
    private TextField prodID;
    @FXML
    private TextField prodName;
    @FXML
    private TextField prodInv;
    @FXML
    private TextField prodPrice;
    @FXML
    private TextField prodMax;
    @FXML
    private TextField prodMin;
    //private final ObservableList<Part> associatedPartList = FXCollections.observableArrayList();
    Product NewProd = new Product(9000, "Bike", 100.00, 20, 1, 99);
    private static int id = 9005;


    /**@param actionEvent  onSearchPartBtn
     * this gets text from search field and matches it against part ID or part Name in the associated parts table (Upper table)
     * @throws IOException OnSearchPartBtn
     * @return true
     */
    public boolean onSearchPartBtn(ActionEvent actionEvent) throws IOException {
        if (searchPartTxt.getText().isEmpty()) {

            partsTable.setItems(allParts());
        } else {

            String q = searchPartTxt.getText();
            ObservableList<Part> searchPartsTable = lookupPart(q);
            if (searchPartsTable.size() == 0) {
                partsTable.setItems(allParts());
            } else {
                partsTable.setItems(searchPartsTable);
            }
        }
        return true;
    }

    /** @return  allParts
     * this does the search for part by ID
     */
    private ObservableList<Part> allParts() {
        ObservableList<Part> namedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Part pt : allParts) {
            namedPart.add(pt);
        }
        return namedPart;
    }

    /** @return lookupPart
     * this does the search for part by Name.
     * I also left the old way of adding alerts in the code.
     * After entering these alerts several times I changed my method to allow for future ease/ scalability.*/
    private ObservableList<Part> lookupPart(String partialName) {
        ObservableList<Part> namedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Part pt : allParts) {
            if (pt.getName().contains(partialName) || (pt.getId() + "").contains(partialName)) {
                namedPart.add(pt);
            }
        }
        if (namedPart.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search Error");
            alert.setContentText("No part found");
            alert.showAndWait();
        }
        return namedPart;
    }
    /**
     * @param actionEvent onSave
     * Logic errors and Number Format Exceptions are checked
     * a new product is created as NewProd
     * To Prevent runtime errors, Try/ Catch blocks were added to prevent the program from failing
     * @throws IOException OnSave
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            id++;
            int id = Integer.parseInt(prodID.getText());
            String name = prodName.getText();
            double price = Double.parseDouble(prodPrice.getText());
            int stock = Integer.parseInt(prodInv.getText());
            int max = Integer.parseInt(prodMax.getText());
            int min = Integer.parseInt(prodMin.getText());

            NewProd.setId(id);
            NewProd.setName(name);
            NewProd.setPrice(price);
            NewProd.setStock(stock);
            NewProd.setMin(min);
            NewProd.setMax(max);
            NewProd.getAllAssociatedParts();

            if (Integer.parseInt(prodMin.getText().trim()) >
                    Integer.parseInt(prodMax.getText().trim())) {
                AlertMessage(1);
                return;
            }
            if (Integer.parseInt(prodInv.getText().trim()) <
                    Integer.parseInt(prodMin.getText().trim())) {
                AlertMessage(2);
                return;
            }
            if (Integer.parseInt(prodInv.getText().trim()) >
                    Integer.parseInt(prodMax.getText().trim())) {
                AlertMessage(3);
                return;
            }
            if (Double.parseDouble(prodPrice.getText().trim()) < 0) {
                AlertMessage(4);
                return;
            }
            Inventory.addProduct(NewProd);
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (NumberFormatException e)  {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Dialog");
            alert.setContentText("Please enter valid values for each text field!");
            alert.setContentText(" The following " + e + "is an incorrect input. Please correct to proceed");
            alert.showAndWait();
        }

    }
    /**
     * @param alertType AlertMessage
     * Alert message with case numbers were created for future scalability, and to make it easier to read/ write
     */
    private void AlertMessage (int alertType) {
        Alert alert = new Alert (Alert.AlertType.ERROR);
        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Max / Min");
                alert.setContentText("The Minimum exceeds Maximum");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Inventory");
                alert.setContentText("Inventory is less than Min value");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Inventory");
                alert.setContentText("Inventory is greater than Max value");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Entry");
                alert.setContentText("Price can NOT be less than 0");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("None Selected");
                alert.setContentText("Please select a part to be added");
                alert.showAndWait();
        }
    }

    /** @param actionEvent  onAdd
     * selected part from the upper table and it's attributes are saved to associated parts list
     * @throws IOException OnAdd
     */
    public void onAdd(ActionEvent actionEvent) throws IOException {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        if (part == null) { AlertMessage(5);
        } else {
            NewProd.addAssociatedPart(part);
            prodPartTbl.setItems(NewProd.getAllAssociatedParts());
        }
    }

    /** @param actionEvent onRemove
     * selected part from the lower table and it's attributes are removed from associated parts list
     */
    public void onRemove(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Select OK to delete associated part. Continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part parts = prodPartTbl.getSelectionModel().getSelectedItem();
            NewProd.deleteAssociatedPart(parts);
        }
    }

    /** @param actionEvent onCancel
     * When cancel button is clicked program returns to main screen
     * and does not save a new product
     @throws IOException OnCancelProd
     */
    public void onCancelProd(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "By canceling, this will not save any updated values. Continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodID.setEditable(false);
        prodID.setText(String.valueOf(id));

        prodPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodPartInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}


