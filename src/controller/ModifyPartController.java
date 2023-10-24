package controller;
/**
 * Supplied controller ModifyPartController.java
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
import model.Outsourced;
import model.InHouse;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Ryan.Slack
 * JavaDoc route: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/index.html
 */

public class ModifyPartController implements Initializable {
    private Part part;

    Stage stage;
    Parent scene;

    @FXML
    private TextField modPartIdTxt;
    @FXML
    private TextField modPartNameTxt;
    @FXML
    private TextField modPartInvTxt;
    @FXML
    private TextField modPartCostTxt;
    @FXML
    private TextField modPartMaxTxt;
    @FXML
    private TextField modPartMinTxt;
    @FXML
    private TextField modPartMachineIdTxt;
    @FXML
    private RadioButton modPartInHouseRadio;
    @FXML
    private Label originLabel;
    @FXML
    private ToggleGroup modPartSourceTG;
    @FXML
    private RadioButton modPartOutsourcedRadio;

    /**
     * @param newPart  sendPart
     * takes selected part and populates the individual attributes in the modify part screen text fields*/
    public void sendPart(Part newPart) {

        part =newPart;
        modPartIdTxt.setText(String.valueOf(part.getId()));
        modPartNameTxt.setText(part.getName());
        modPartInvTxt.setText(String.valueOf(part.getStock()));
        modPartCostTxt.setText(String.valueOf(part.getPrice()));
        modPartMaxTxt.setText(String.valueOf(part.getMax()));
        modPartMinTxt.setText(String.valueOf(part.getMin()));

        if (part instanceof InHouse) {
            modPartMachineIdTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
            modPartInHouseRadio.setSelected(true);
        }
        else {
            modPartMachineIdTxt.setText(((Outsourced) part).getCompanyName());
            modPartOutsourcedRadio.setSelected(true);
        }
    }


    /**
     * @param actionEvent onSave
     * Logic errors and Number Format Exceptions are checked
     * In House vs Outsourced radio buttons will toggle the last text field to either save companyName or machineId
     * To Prevent runtime errors, Try/ Catch blocks were added to prevent the program from failing
     * @throws IOException OnSave
     */
    public void onSave(ActionEvent actionEvent)  throws IOException {
        try {
            int id = Integer.parseInt(modPartIdTxt.getText());
            String name = modPartNameTxt.getText();
            double price = Double.parseDouble(modPartCostTxt.getText());
            int stock = Integer.parseInt(modPartInvTxt.getText());
            int max = Integer.parseInt(modPartMaxTxt.getText());
            int min = Integer.parseInt(modPartMinTxt.getText());

            part.setId(id);
            part.setName(name);
            part.setPrice(price);
            part.setStock(stock);
            part.setMax(max);
            part.setMin(min);

            if (Integer.parseInt(modPartMinTxt.getText().trim()) >
                    Integer.parseInt(modPartMaxTxt.getText().trim())) {
                AlertMessage(1);
                return;
            }
            if (Integer.parseInt(modPartInvTxt.getText().trim()) <
                    Integer.parseInt(modPartMinTxt.getText().trim())) {
                AlertMessage(2);
                return;
            }
            if (Integer.parseInt(modPartInvTxt.getText().trim()) >
                    Integer.parseInt(modPartMaxTxt.getText().trim())) {
                AlertMessage(3);
                return;
            }
            if (Double.parseDouble(modPartCostTxt.getText().trim()) < 0) {
                AlertMessage(4);
                return;
            }


            if (part instanceof InHouse) {
                if (modPartOutsourcedRadio.isSelected()) {
                    Outsourced newOutsourced = new Outsourced(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(), part.getMax(), modPartMachineIdTxt.getText());
                    Inventory.deletePart(part);
                    Inventory.updatePart(newOutsourced);
                } else {
                    ((InHouse) part).setMachineId(Integer.parseInt(modPartMachineIdTxt.getText()));
                }

            } else {
                if (modPartInHouseRadio.isSelected()) {
                    InHouse newInHouse = new InHouse(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(), part.getMax(), Integer.parseInt(modPartMachineIdTxt.getText()));
                    Inventory.deletePart(part);
                    Inventory.updatePart(newInHouse);
                } else {
                    ((Outsourced) part).setCompanyName(modPartMachineIdTxt.getText());
                }
            }

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (NumberFormatException a) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter valid values for each text field!");
            alert.setContentText(" The following " + a + "is an incorrect input. Please correct to proceed");
            alert.showAndWait();
        }
    }
    /**
     * @param alertType AlertMessage
     * Alert message with case numbers were created for future scalability, and to make it easier to read/ write
     */
    private void AlertMessage (int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
        /**
         * Toggle group listener changes text box label
         * ID - disabled
         * new ID is set
         */
        modPartSourceTG.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (modPartInHouseRadio.isSelected())
                originLabel.setText("Machine ID");

            else
                originLabel.setText("Company Name");

        });
        modPartIdTxt.setEditable(false);
        modPartInHouseRadio.setSelected(false);
    }
}
