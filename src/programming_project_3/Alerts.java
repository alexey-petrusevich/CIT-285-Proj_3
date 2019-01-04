/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3;

import javafx.scene.control.*;

/**
 *
 * @author Aliaksei
 */
public class Alerts {

    /**
     * Method displays an alert with a message passed as a string parameter.
     *
     * @param message message to be displayed.
     */
    public static void alertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void timeUpAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Time up!");
        alert.setHeaderText("Time up!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void quitAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Quit program");
        alert.setContentText("Are you sure?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            StaticData.getEXECUTOR().shutdown(); // shut down the executor
            System.exit(0);

        }
    }

    public static void successAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("Success!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static int submitAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Submit results");
        alert.setHeaderText("Submit results");
        alert.setContentText("Are you sure?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            return 1;

        } else {
            return 0;
        }

    }

}
