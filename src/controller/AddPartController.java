package controller;
/**
 * Supplied controller AddPartController.java
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Ryan.Slack
 * JavaDoc route: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/index.html
 */

public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField addPartIdTxt;
    @FXML
    private TextField addPartNameTxt;
    @FXML
    private TextField addPartInvTxt;
    @FXML
    private TextField addPartCostTxt;
    @FXML
    private TextField addPartMaxTxt;
    @FXML
    private TextField addPartMinTxt;
    @FXML
    private TextField addPartMachineIdTxt;
    @FXML
    private Label originLabel;
    @FXML
    private ToggleGroup addPartSourceTG;
    @FXML
    private RadioButton addPartInHouseRadio;

    private static int id = 6;


    /**
     * @param actionEvent onSave
     * Logic errors and Number Format Exceptions are checked
     * In House vs Outsourced radio buttons will toggle the last text field to either save companyName or machineId
     * To Prevent runtime errors, Try/ Catch blocks were added to prevent the program from failing
     * @throws IOException OnSave
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            String name = addPartNameTxt.getText();
            double price = Double.parseDouble(addPartCostTxt.getText());
            int stock = Integer.parseInt(addPartInvTxt.getText());

            int max = Integer.parseInt(addPartMaxTxt.getText());
            int min = Integer.parseInt(addPartMinTxt.getText());

            if (Integer.parseInt(addPartMinTxt.getText().trim()) >
                    Integer.parseInt(addPartMaxTxt.getText().trim())) {
                AlertMessage(1);
                return;
            }
            if (Integer.parseInt(addPartInvTxt.getText().trim()) <
                    Integer.parseInt(addPartMinTxt.getText().trim())) {
                AlertMessage(2);
                return;
            }
            if (Integer.parseInt(addPartInvTxt.getText().trim()) >
                    Integer.parseInt(addPartMaxTxt.getText().trim())) {
                AlertMessage(3);
                return;
            }
            if (Double.parseDouble(addPartCostTxt.getText().trim()) < 0) {
                AlertMessage(4);
                return;
            }

            if (addPartInHouseRadio.isSelected()) {
                int machineId = Integer.parseInt(addPartMachineIdTxt.getText());
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
            } else {
                String companyName = addPartMachineIdTxt.getText();
                Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
            }

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
            id++;


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
        }
    }
    /** @param actionEvent onCancel
     * When cancel button is clicked program returns to main screen
     * and does not save a new part
     * @throws IOException OnCancel
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();

    }


    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * Toggle group listener changes text box label
         * ID - disabled
         * new ID is set
         */
        addPartSourceTG.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (addPartInHouseRadio.isSelected())
                originLabel.setText("Machine ID");
            else
                originLabel.setText("Company Name");
        });
        addPartIdTxt.setEditable(false);
        addPartIdTxt.setText(String.valueOf(id));
    }
}


