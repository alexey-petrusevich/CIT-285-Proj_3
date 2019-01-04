/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import programming_project_3.Alerts;
import programming_project_3.Answer;
import programming_project_3.StaticData;

/**
 *
 * @author Aliaksei
 */
public class NavigationPane {

    private static Button[] questionButtons = new Button[20]; // buttons 1-20 

    public static Button getSUBMIT_BTN() {
        return SUBMIT_BTN;
    }
    private static final Button SUBMIT_BTN = new Button("Submit");

    public static Button[] getQuestionButtons() {
        return questionButtons;
    }

    public static Pane getNavigationPane() {

        // constants
        final Font DEF_FONT = Font.font("Times", FontWeight.MEDIUM, 14);
        final double BTN_PREF_W = 50;
        final double BTN_PREF_H = 50;
        final int VBOX_GAP = 10;
        final int HBOX_GAP = 10;
        // containers
        VBox vBox = new VBox(VBOX_GAP);
        HBox[] hBoxes = new HBox[7]; // array of hboxes for question buttons
        for (int i = 0; i < 7; i++) {
            hBoxes[i] = new HBox(HBOX_GAP);
        }
        // buttons
        
        SUBMIT_BTN.setFont(DEF_FONT);
        SUBMIT_BTN.setPrefSize(BTN_PREF_W * 3, BTN_PREF_H);
        
        SUBMIT_BTN.setOnAction(e -> {
            class RadioNotSelectedException extends Exception {
            }
            try {
                // check if user selected any radiobutton
                // if all selected is equal to false, then throw an exception

                // check if user left any unanswered questions
                for (int i = 0; i < QuestionPane.getQuestions().size(); i++) {
                    for (int j = 0; j < 5; j++) {
                        // if NullPointerException or IndexOutOfBoundsException is thrown here
                        // then the user left a question unasnwered
                        Answer a = QuestionPane.getQuestions().get(i).getAnswers()[j];
                        
                    }
                }

                if (QuestionPane.getRadioButtons()[0].isSelected() == false
                        && QuestionPane.getRadioButtons()[1].isSelected() == false
                        && QuestionPane.getRadioButtons()[2].isSelected() == false
                        && QuestionPane.getRadioButtons()[3].isSelected() == false
                        && QuestionPane.getRadioButtons()[4].isSelected() == false) {
                    // if at least one button is selected, exception will not be thrown
                    throw new RadioNotSelectedException();
                }

                if (Alerts.submitAlert() == 1) {
                    // user pressed OK
                    // end all running threads
                    StaticData.isSubmitted = true;
                    // load results pane
                    // start results task
                    // update number of takes for this user
                    updateNumTakes();
                    StaticData.getEXECUTOR().execute(ResultsPane.getResultsTask());
                    StaticData.loadScene(e, ResultsPane.getResultsPane());
                }

            } catch (RadioNotSelectedException ex) {
                Alerts.alertError("You must select an answer before submitting!");
            } catch (IndexOutOfBoundsException | NullPointerException ex) {
                // if catched, then the user did not answer at least one question
                Alerts.alertError("One or more questions were left unanswered!");
            }

        });
        // array of 20 buttons for 20 questions

        // instantiate each array member
        for (int i = 0; i < 20; i++) {
            questionButtons[i] = new Button(Integer.toString(i + 1));
            questionButtons[i].setPrefSize(BTN_PREF_W, BTN_PREF_H);
            questionButtons[i].setFont(DEF_FONT);
            // disable all question buttons
            questionButtons[i].disableProperty().set(true);
        }
        // add triplets of buttons to corresponding hboxes
        hBoxes[0].getChildren().addAll(questionButtons[0], questionButtons[1], questionButtons[2]);
        hBoxes[1].getChildren().addAll(questionButtons[3], questionButtons[4], questionButtons[5]);
        hBoxes[2].getChildren().addAll(questionButtons[6], questionButtons[7], questionButtons[8]);
        hBoxes[3].getChildren().addAll(questionButtons[9], questionButtons[10], questionButtons[11]);
        hBoxes[4].getChildren().addAll(questionButtons[12], questionButtons[13], questionButtons[14]);
        hBoxes[5].getChildren().addAll(questionButtons[15], questionButtons[16], questionButtons[17]);
        hBoxes[6].getChildren().addAll(questionButtons[18], questionButtons[19]);
        // add buttons onto the vbox pane
        vBox.setTranslateX(25);
        vBox.getChildren().addAll(hBoxes[0], hBoxes[1], hBoxes[2], hBoxes[3], hBoxes[4], hBoxes[5], hBoxes[6], SUBMIT_BTN);
        // return vbox
        return vBox;
    }

    // method updates number of entries in userinfo table of project3 database
    public static void updateNumTakes() {
        final String DRIVER = "com.mysql.jdbc.Driver";
        final String DATABASE = "jdbc:mysql://localhost/project3";
        final String LOGIN = "root";
        final String PASSWORD = "mysql";
        int numTakes = 0;
        try {
            // 1) connect a driver
            Class.forName(DRIVER);
            // 2) create a connection
            try (Connection connection = DriverManager.getConnection(DATABASE, LOGIN, PASSWORD)) {
                // 3) prepare a statement
                PreparedStatement prepStatement = connection.prepareStatement("SELECT * FROM userinfo WHERE "
                        + "username = ?;"); // user entry does exist
                // get old num_takes
                prepStatement.setString(1, LoginPane.getUserName());
                ResultSet rSet = prepStatement.executeQuery();
                while (rSet.next()) {
                    numTakes = rSet.getInt("num_takes");
                }
                // increment numTakes
                numTakes++;
                // update numTakes
                prepStatement = connection.prepareStatement("UPDATE userinfo SET num_takes = " + numTakes + " WHERE "
                        + "username = ?;");
                prepStatement.setString(1, LoginPane.getUserName());
                prepStatement.execute();

            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
