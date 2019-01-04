/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import java.sql.*;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import programming_project_3.Alerts;
import programming_project_3.StaticData;

/**
 *
 * @author Aliaksei
 */
public class LoginPane {
    private static String userName = "";

    public static String getUserName() {
        return userName;
    }
    public static Pane getLoginPane() {
        // constants
        final int VGAP = 10;
        final int GRID_HGAP = 10;
        final int HBOX_HGAP = 30;
        final double BTN_PREF_W = 75;
        final double BTN_PREF_H = 15;
        final Font HEAD_FONT = Font.font("Times", FontWeight.BOLD, 26);
        final Font DEF_FONT = Font.font("Times", FontWeight.MEDIUM, 14);
        // labels
        final Label JAVA_EXAM_LABEL = new Label("Java Certification Exam");
        JAVA_EXAM_LABEL.setFont(HEAD_FONT);
        final Label USERNAME_LABEL = new Label("Username:");
        USERNAME_LABEL.setFont(DEF_FONT);
        final Label ID_LABEL = new Label("ID:");
        ID_LABEL.setFont(DEF_FONT);
        final Label ERROR_LABEL = new Label("");
        ERROR_LABEL.setFont(DEF_FONT);
        // textfields and passwordfield
        TextField userNameTF = new TextField();
        userNameTF.setFont(DEF_FONT);
        PasswordField idPF = new PasswordField();
        idPF.setFont(DEF_FONT);
        // buttons
        final Button LOGIN_BUTTON = new Button("Log in");
        LOGIN_BUTTON.setPrefSize(BTN_PREF_W, BTN_PREF_H);
        LOGIN_BUTTON.setFont(DEF_FONT);
        
        LOGIN_BUTTON.setOnAction(e -> {
            userName = userNameTF.getText();
            // check if user exists or add new entry
            checkUser(userNameTF.getText(), idPF.getText(), e);
        });
        final Button QUIT_BUTTON = new Button("Quit");
        QUIT_BUTTON.setPrefSize(BTN_PREF_W, BTN_PREF_H);
        QUIT_BUTTON.setFont(DEF_FONT);
        QUIT_BUTTON.setOnAction(e -> {
            Alerts.quitAlert();
        });
        // containers
        GridPane gridPane = new GridPane();
        gridPane.setVgap(VGAP);
        gridPane.setHgap(GRID_HGAP);
        gridPane.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(HBOX_HGAP);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(VGAP);
        vBox.setAlignment(Pos.CENTER);
        // add elements into containers
        gridPane.add(USERNAME_LABEL, 0, 0);
        gridPane.add(userNameTF, 1, 0);
        gridPane.add(ID_LABEL, 0, 1);
        gridPane.add(idPF, 1, 1);
        hBox.getChildren().addAll(LOGIN_BUTTON, QUIT_BUTTON);
        vBox.getChildren().addAll(JAVA_EXAM_LABEL, gridPane, hBox, ERROR_LABEL);
        // return vbox
        return vBox;
    }

    // method submits a query to the userinfo database and if username not found, 
    // creates a new entry
    private static void checkUser(String userName, String id, Event event) {
        class UserInfoMismatchException extends Exception {
        }
        class IDLengthMismatchException extends Exception {
        }
        class NonNumericIDException extends Exception {
        }
        class UserNamelengthException extends Exception {
        }
        class MaxTrialsReachedException extends Exception {
        }
        final String DRIVER = "com.mysql.jdbc.Driver";
        final String DATABASE = "jdbc:mysql://localhost/project3";
        final String LOGIN = "root";
        final String PASSWORD = "mysql";
        try {
            if (userName.length() == 0) {
                throw new UserNamelengthException();
            }
            if (id.length() != 4) {
                throw new IDLengthMismatchException();
            }
            for (int i = 0; i < id.length(); i++) {
                if (!Character.isDigit(id.charAt(i))) {
                    throw new NonNumericIDException();
                }
            }
            // 1) connect a driver
            Class.forName(DRIVER);
            // 2) create a connection
            try (Connection connection = DriverManager.getConnection(DATABASE, LOGIN, PASSWORD)) {
                // 3) prepare a statement
                PreparedStatement prepStatement = connection.prepareStatement("SELECT * FROM userinfo WHERE "
                        + "username = ?;");
                prepStatement.setString(1, userName);
                ResultSet rSet = prepStatement.executeQuery();
                if (rSet.next()) {
                    // check if password matched username
                    String userID = rSet.getString("id");
                    if (!userID.equals(id)) {
                        throw new UserInfoMismatchException();
                    }
                    if (rSet.getInt("num_takes") > 1) throw new MaxTrialsReachedException();
                    Alerts.successAlert("Logged in!");
                } else {

                    // create new user
                    prepStatement = connection.prepareStatement("INSERT INTO userinfo VALUES (?, ?, ?);");
                    prepStatement.setString(1, userName);
                    prepStatement.setString(2, id);
                    prepStatement.setInt(3, 0); // set number of takes to 0 for new user
                    prepStatement.execute();
                    // display an alert that entry has been created
                    Alerts.successAlert("You account has been created!");
                }
                StaticData.loadScene(event, PreTestPane.getPreTestPane()); // load pretest pane if no exceptions were thrown
            }
        } catch (ClassNotFoundException ex) {
            Alerts.alertError("Driver not found!");
        } catch (SQLException ex) {
            Alerts.alertError("Error accessing database!");
        } catch (UserInfoMismatchException ex) {
            Alerts.alertError("User id is incorrect!");
        } catch (IDLengthMismatchException ex) {
            Alerts.alertError("ID must 4 numbers!");
        } catch (NonNumericIDException ex) {
            Alerts.alertError("ID must have numbers only!");
        } catch (UserNamelengthException ex) {
            Alerts.alertError("Username must have at least one character!");
        } catch (MaxTrialsReachedException ex){
            Alerts.alertError("This user has exceeded the maximum number of trials!");
        }

    }

}
