/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import programming_project_3.Alerts;
import programming_project_3.Answer;
import programming_project_3.Question;
import programming_project_3.StaticData;

/**
 * Class is used for insertion/reading of questions. Due to shortage of time, it was 
 * implemented considering that the programmer will not enter any data that might
 * cause errors or exceptions.
 * @author Aliaksei
 */
public class InsertionPane {

    private static int questionID = 1; // used in
    private static int difficulty = 4; // change this to select difficulty level
    // method reads 25 questions, writes them into arraylist, and then write an arraylist into a file
    private static Button readButton = new Button("Read"); // read button to check if all questions were written to a file
    private static Button nextQuestionButton = new Button("Next question"); // for reading
    private static Button writeButton = new Button("Write"); // button for writing
    private static Button addButton = new Button("Add");
    private static ArrayList<Question> arrQuestions = new ArrayList<>();

    public static Pane getInsertionPane() {
        StaticData.init();
        // binary files
        final String EASY = "easy.dat";
        final String MEDIUM = "medium.dat";
        final String HARD = "hard.dat";
        final String ADVANCED = "advanced.dat";
        // constants
        final int VBOX_SPACING = 10;
        final int HBOX_SPACING = 10;
        // variables

        // labels
        Label qIDLabel = new Label("QID:");
        Label qDifLabel = new Label("QDif:");
        Label questionLabel = new Label("Question:");
        Label ans1Label = new Label("Answer 1:");
        Label ans2Label = new Label("Answer 2:");
        Label ans3Label = new Label("Answer 3:");
        Label ans4Label = new Label("Answer 4:");
        Label ans5Label = new Label("Answer 5:");
        // textfields
        TextField questionIDTF = new TextField(Integer.toString(questionID));
        questionIDTF.setEditable(false);
        TextField difficultyTF = new TextField(Integer.toString(difficulty));
        difficultyTF.setEditable(false);
        TextField answer1TF = new TextField();
        TextField answer2TF = new TextField();
        TextField answer3TF = new TextField();
        TextField answer4TF = new TextField();
        TextField answer5TF = new TextField();
        // radiobuttons
        ToggleGroup tg = new ToggleGroup();
        RadioButton radio1 = new RadioButton("Correct?");
        RadioButton radio2 = new RadioButton("Correct?");
        RadioButton radio3 = new RadioButton("Correct?");
        RadioButton radio4 = new RadioButton("Correct?");
        RadioButton radio5 = new RadioButton("Correct?");
        radio1.setToggleGroup(tg);
        radio2.setToggleGroup(tg);
        radio3.setToggleGroup(tg);
        radio4.setToggleGroup(tg);
        radio5.setToggleGroup(tg);
        // textarea
        TextArea questionArea = new TextArea();
        questionArea.setPrefWidth(500);
        // actions for buttons
        // write
        
        writeButton.setOnAction(e -> {
            // save arraylist into corresponding file
            String outputFile = null;
            int diffLevel = Integer.parseInt(difficultyTF.getText());
            // select diff level
            switch (diffLevel) {
                case 1:
                    outputFile = EASY;
                    break;
                case 2:
                    outputFile = MEDIUM;
                    break;
                case 3:
                    outputFile = HARD;
                    break;
                case 4:
                    outputFile = ADVANCED;
                    break;
            }
            // write questions arraylist into corresponding file
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outputFile));) {
                out.writeObject(arrQuestions); // write array of questions
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        // add
        addButton.setPrefWidth(75);
        addButton.setOnAction(e -> {
            class SelectionException extends Exception {
            } // for radio button selection
            try {
                if (radio1.isSelected() == true
                        || radio2.isSelected() == true
                        || radio3.isSelected() == true
                        || radio4.isSelected() == true
                        || radio5.isSelected() == true) {

                    String outputFile = null;
                    int diffLevel = Integer.parseInt(difficultyTF.getText());
                    // select diff level
                    switch (diffLevel) {
                        case 1:
                            outputFile = EASY;
                            break;
                        case 2:
                            outputFile = MEDIUM;
                            break;
                        case 3:
                            outputFile = HARD;
                            break;
                        case 4:
                            outputFile = ADVANCED;
                            break;
                    }
                    // write question into file
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outputFile));) {
                        // populate answers array with answers
                        Answer[] answers = new Answer[5];
                        answers[0] = new Answer(answer1TF.getText(), radio1.isSelected());
                        answers[1] = new Answer(answer2TF.getText(), radio2.isSelected());
                        answers[2] = new Answer(answer3TF.getText(), radio3.isSelected());
                        answers[3] = new Answer(answer4TF.getText(), radio4.isSelected());
                        answers[4] = new Answer(answer5TF.getText(), radio5.isSelected());
                        Answer correctAnswer;
                        // find correct answer
                        if (radio1.isSelected()) { // 1
                            correctAnswer = answers[0];
                        } else if (radio2.isSelected()) { // 2
                            correctAnswer = answers[1];
                        } else if (radio3.isSelected()) { // 3
                            correctAnswer = answers[2];
                        } else if (radio4.isSelected()) { // 4
                            correctAnswer = answers[3];
                        } else { // 5
                            correctAnswer = answers[4];
                        }
                        // create a question object
                        // int id, byte difficulty, String question, Answer[] answers, Answer correctAnswer
                        Question question = new Question(Integer.parseInt(questionIDTF.getText()),
                                Integer.parseInt(difficultyTF.getText()), questionArea.getText(), answers, correctAnswer);
                        // write question object into appropriate file
                        arrQuestions.add(question);
                        // increment question id
                        questionIDTF.setText(Integer.toString(++questionID));
                        // clear textarea and textfields
                        questionArea.setText("");
                        answer1TF.setText("");
                        answer2TF.setText("");
                        answer3TF.setText("");
                        answer4TF.setText("");
                        answer5TF.setText("");
                        // reset radiobutton
                        radio1.selectedProperty().set(false);
                        radio2.selectedProperty().set(false);
                        radio3.selectedProperty().set(false);
                        radio4.selectedProperty().set(false);
                        radio5.selectedProperty().set(false);

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    // if none selected throw an exception
                    throw new SelectionException();

                }
            } catch (SelectionException ex) {
                Alerts.alertError("You must select the correct answer!");
            }

        });
        // read
        readButton.setOnAction(e -> {
            // reset question id
            questionID = 1;
            questionIDTF.setText(Integer.toString(questionID));
            // determine the name of the input file
            String inputFile = null;
            int diffLevel = Integer.parseInt(difficultyTF.getText());
            // select diff level
            switch (diffLevel) {
                case 1:
                    inputFile = EASY;
                    break;
                case 2:
                    inputFile = MEDIUM;
                    break;
                case 3:
                    inputFile = HARD;
                    break;
                case 4:
                    inputFile = ADVANCED;
                    break;
            }
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(inputFile))) {
                // read from file into arrayList
                arrQuestions = (ArrayList<Question>) in.readObject();

                // put first question onto the pane
                Question question = arrQuestions.get(questionID - 1);
                Answer[] answers = question.getAnswers();
                questionArea.setText(question.getQuestion());
                // put answers into textfields
                answer1TF.setText(answers[0].getAnswer());
                answer2TF.setText(answers[1].getAnswer());
                answer3TF.setText(answers[2].getAnswer());
                answer4TF.setText(answers[3].getAnswer());
                answer5TF.setText(answers[4].getAnswer());
                // select corresponding radiobutton
                if (answers[0].getIsCorrect()) {
                    radio1.selectedProperty().set(true);
                } else if (answers[1].getIsCorrect()) {
                    radio2.selectedProperty().set(true);
                } else if (answers[2].getIsCorrect()) {
                    radio3.selectedProperty().set(true);
                } else if (answers[3].getIsCorrect()) {
                    radio4.selectedProperty().set(true);
                } else if (answers[4].getIsCorrect()) {
                    radio5.selectedProperty().set(true);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        // next question

        nextQuestionButton.setOnAction(e -> {
            questionIDTF.setText(Integer.toString(++questionID)); // increment id
            Question question = arrQuestions.get(questionID - 1); // get next question
            Answer[] answers = question.getAnswers();
            questionArea.setText(question.getQuestion());
            // put answers into textfields
            answer1TF.setText(answers[0].getAnswer());
            answer2TF.setText(answers[1].getAnswer());
            answer3TF.setText(answers[2].getAnswer());
            answer4TF.setText(answers[3].getAnswer());
            answer5TF.setText(answers[4].getAnswer());
            // select corresponding radiobutton
            if (answers[0].getIsCorrect()) {
                radio1.selectedProperty().set(true);
            } else if (answers[1].getIsCorrect()) {
                radio2.selectedProperty().set(true);
            } else if (answers[2].getIsCorrect()) {
                radio3.selectedProperty().set(true);
            } else if (answers[3].getIsCorrect()) {
                radio4.selectedProperty().set(true);
            } else if (answers[4].getIsCorrect()) {
                radio5.selectedProperty().set(true);
            }

        });
        // containers
        VBox vBox = new VBox(VBOX_SPACING);
        HBox hBox = new HBox(HBOX_SPACING);
        HBox hBox1 = new HBox(HBOX_SPACING);
        HBox hBox2 = new HBox(HBOX_SPACING);
        HBox hBox3 = new HBox(HBOX_SPACING);
        HBox hBox4 = new HBox(HBOX_SPACING);
        HBox hBox5 = new HBox(HBOX_SPACING);
        HBox hBox6 = new HBox(HBOX_SPACING);
        // add all elements onto the pane
        hBox.getChildren().addAll(qIDLabel, questionIDTF, qDifLabel, difficultyTF);
        hBox1.getChildren().addAll(ans1Label, answer1TF, radio1);
        hBox2.getChildren().addAll(ans2Label, answer2TF, radio2);
        hBox3.getChildren().addAll(ans3Label, answer3TF, radio3);
        hBox4.getChildren().addAll(ans4Label, answer4TF, radio4);
        hBox5.getChildren().addAll(ans5Label, answer5TF, radio5);
        hBox6.getChildren().addAll(addButton, nextQuestionButton, writeButton, readButton);
        vBox.getChildren().addAll(hBox, questionLabel, questionArea, hBox1, hBox2, hBox3, hBox4, hBox5, hBox6);
        vBox.setTranslateX(50);
        vBox.setTranslateY(25);
        // return pane
        return new Pane(vBox);
    }

}
