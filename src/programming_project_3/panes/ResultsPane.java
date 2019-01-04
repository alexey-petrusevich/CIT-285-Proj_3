/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import programming_project_3.Answer;
import programming_project_3.Question;
import programming_project_3.StaticData;
import static programming_project_3.panes.ResultsPane.getResultLabel;
import static programming_project_3.panes.ResultsPane.getScoreLabel;
import static programming_project_3.panes.ResultsPane.getTitleLabel;

/**
 *
 * @author Alex
 */
public class ResultsPane {

    private static Label resultLabel = new Label("");
    private static Label titleLabel = new Label("");
    private static Label scoreLabel = new Label("");

    public static Label getTitleLabel() {
        return titleLabel;
    }

    public static Label getResultLabel() {
        return resultLabel;
    }

    public static Pane getResultsPane() {
        // constants
        final int VBOX_GAP = 50;
        final Font DEF_FONT = Font.font("Times", FontWeight.MEDIUM, 14);
        final Font HEAD_FONT = Font.font("Times", FontWeight.BOLD, 20);
        // containers
        VBox vBox = new VBox(VBOX_GAP);

        // labels
        resultLabel.setFont(DEF_FONT);
        titleLabel.setFont(HEAD_FONT);
        // button
        Button OKButton = new Button("OK");
        OKButton.setPrefSize(80, 40);
        OKButton.setFont(DEF_FONT);
        OKButton.setOnAction(e -> {
            StaticData.loadScene(e, LoginPane.getLoginPane());
        });
        vBox.getChildren().addAll(resultLabel, titleLabel, OKButton);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public static Label getScoreLabel() {
        return scoreLabel;
    }

    public static Runnable getResultsTask() {
        return new ResultsTask();
    }

}
// task will calculate ONLY CORRECT answers; if test taker submits his results before
// answering the remaining questions, the remaining questions count as omited

class ResultsTask implements Runnable {

    @Override
    public void run() {
        final double ARCHITECT_LOW = 85;
        final double DEVELOPER_HIGH = 84.9;
        final double DEVELOPER_LOW = 75;
        final double PROGRAMMER_HIGH = 74.9;
        final double PROGRAMMER_LOW = 65;

        // calculate the score
        ArrayList<Question> questions = QuestionPane.getQuestions();
        int totalQuestions = questions.size();
        int numCorrect = 0;
        int numIncorrect = 0;
        double result = 0;

        for (int i = 0; i < totalQuestions; i++) {
            Answer selectedAnswer = questions.get(i).getSelectedAnswer();
            Answer correctAnswer = questions.get(i).getCorrectAnswer();
            if (selectedAnswer != null) {
                if (selectedAnswer.getAnswer().equals(correctAnswer.getAnswer())) {

                    // if selected answer and correct answers match, increment numCorrect
                    numCorrect++;
                } else {
                    // else question was answered incorrectly; increment numIncorrect
                    numIncorrect++;
                }
            }

        }
        // numCorrect and numIncorrect are calculated here
        // follow the instructions in the results calculation description
        double a = (numCorrect - numIncorrect) / ((double) totalQuestions); // (20 - 5) / 20 = 0.75
        a = 1 - a; // 1 - 0.75 = 0.25
        a = a * numIncorrect; // 0.25 * 5 = 1.25
        a = a + numIncorrect; // 1.25 + 5 = 6.25
        result = numCorrect * 5 - a; // 15 * 5 - 6.25 - final result
        if (result < 1E-14) result = 0; // in case if result is negative
        //System.out.println("Result: " + Double.toString(result));
        // determine the mastery level based on the result
        String title = "";
        if (result > ARCHITECT_LOW) {
            title = "Java Certified Architect";
        } else if (result >= DEVELOPER_LOW && result < DEVELOPER_HIGH) {
            title = "Java Certified Developer";
        } else if (result >= PROGRAMMER_LOW && result < PROGRAMMER_HIGH) {
            title = "Java Certified Programmer";
        }
        if (result < PROGRAMMER_LOW) {
            // display "sorry, try again" message
            Platform.runLater(() -> {
                getResultLabel().setText("Sorry you did not pass, try again!");
            });
        } else {
            // the user earned at least one title
            final String TITLE = title;
            final String RESULTS = "You have earned a title of ";
            final String SCORE = String.format("%.2f", result);
            Platform.runLater(() -> {
                getResultLabel().setText(RESULTS);
                getTitleLabel().setText(TITLE);
                getScoreLabel().setText(SCORE);
            });
        }
    }

}
