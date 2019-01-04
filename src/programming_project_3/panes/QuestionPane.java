/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import programming_project_3.*;

/**
 *
 * @author Aliaksei
 */
public class QuestionPane {

    // get random difficulty level upon loading the pane
    private static byte currentDiffLevel = (byte) (Math.random() * 4 + 1);
    // arraylist of questions to be answer by user; populated as user answer questions
    private static ArrayList<Question> questions = new ArrayList<>(20);

    public static ArrayList<Question> getQuestions() {
        return questions;
    }

    public static void setQuestions(ArrayList<Question> questions) {
        QuestionPane.questions = questions;
    }

    private static RadioButton[] radioButtons = new RadioButton[5];

    public static RadioButton[] getRadioButtons() {
        return radioButtons;
    }
    private static TextArea text;
    private static int current = 0; // reference to the current question; 0 is first element

    public static void setCurrentDiffLevel(byte currentDiffLevel) {
        QuestionPane.currentDiffLevel = currentDiffLevel;
    }

    public static void setCurrent(int current) {
        QuestionPane.current = current;
    }
    private static int selected = 0;
    private static Button nextButton = new Button("Next question");

    public static Button getNextButton() {
        return nextButton;
    }
    private static Button previousButton = new Button("Previous question");
    // label
    private static Label quenstionNumLabel = new Label(); // question number

    public static Pane getQuestionPane() {

        // constants
        final int VBOX_GAP = 10;
        final int HBOX_GAP = 20;
        // constants
        final Font DEF_FONT = Font.font("Times", FontWeight.MEDIUM, 14);
        // containers
        VBox vBox1 = new VBox(VBOX_GAP);
        VBox vBox2 = new VBox(VBOX_GAP);
        HBox hBox = new HBox(HBOX_GAP * 10);
        HBox hBox1 = new HBox(HBOX_GAP);

        // text
        text = new TextArea();
        text.setFont(DEF_FONT);
        text.setEditable(false);
        text.setPrefSize(500, 300);

        // add listeners to radioButtons
        quenstionNumLabel.setFont(DEF_FONT);

        quenstionNumLabel.setText("#" + (current + 1));

        // buttons
        previousButton.setFont(DEF_FONT);
        previousButton.disableProperty().set(true); // disable by default
        previousButton.setOnAction(e -> {

            // enable next button
            // even if current = 19 if disabled
            if (nextButton.disableProperty().get() == true) {
                nextButton.disableProperty().set(false);
            }
            // get the element from the arraylist of answered questions
            // and decrement current by 1
            StaticData.getEXECUTOR().execute(getQuestionTask(--current));

            //System.out.println("Current: " + current);
            // if there are question exist before current questions
            // if current is 1 (2nd question), the maximum the user can move is to position 0 (1st question)
            if (current == 0) {
                previousButton.disableProperty().set(true);
            } else if (current > 0) {
                // enable previous button if disabled
                if (previousButton.disableProperty().get() == true) {
                    previousButton.disableProperty().set(false);
                }

            }

        });

        nextButton.setFont(DEF_FONT);
        // create a toggle group for radio buttons
        ToggleGroup tgGroup = new ToggleGroup();

        nextButton.setOnAction(e -> {
            class RadioNotSelectedException extends Exception {
            }
            try {
                // check if user selected any radiobutton
                // if all selected is equal to false, then throw an exception
                if (radioButtons[0].isSelected() == false
                        && radioButtons[1].isSelected() == false
                        && radioButtons[2].isSelected() == false
                        && radioButtons[3].isSelected() == false
                        && radioButtons[4].isSelected() == false) {
                    // if at least one button is selected, exception will not be thrown
                    throw new RadioNotSelectedException();
                }

                // if there are any more questions following
                // if current = 18 (last question), if user presses next, current = 19
                // is the farthest he can go; only previos can be pressed
                if (current < (questions.size())) {
                    // enable previous button
                    previousButton.disableProperty().set(false);

                    // check if selected answer is correct; increase difficulty level if true
                    if (questions.get(current).getSelectedAnswer().getAnswer().equals(questions.get(current).getCorrectAnswer().getAnswer())) {
                        // if difficulty level is less than 4 (advanced level)
                        if (currentDiffLevel < 4) {
                            // increment difficulty
                            currentDiffLevel++;
                        }
                    } else {
                        // the answer is incorrect, decrement difficulty level
                        if (currentDiffLevel > 1) {
                            currentDiffLevel--;
                        }
                    }

                    // get the element from the arraylist of answered questions
                    // and increment current by 1
                    if (current == questions.size() - 1) {
                        // if there are no questions following
                        StaticData.getEXECUTOR().execute(getQuestionTask());
                        current++;
                    } else {
                        // else get the follwoing question from questions arraylist
                        StaticData.getEXECUTOR().execute(getQuestionTask(++current));
                    }
                    if (current == 19) { // 19 is 20th (last question); disable next button
                        nextButton.disableProperty().set(true);
                    }

                }

            } catch (RadioNotSelectedException ex) {
                Alerts.alertError("You must select an answer!");
            }

        });

        // instantiate radio buttons with possible answers
        for (int i = 0; i < 5; i++) {
            final int j = i;

            radioButtons[i] = new RadioButton();
            final RadioButton selectedRadio = radioButtons[i];
            radioButtons[i].setFont(DEF_FONT);
            radioButtons[i].setToggleGroup(tgGroup);
            // add a listener to each radiobutton if selected, then set selected marker to j (i)
            radioButtons[i].selectedProperty().addListener(e -> {
                if (selectedRadio.isSelected() == true) {
                    selected = j;
                }
            });
            vBox1.getChildren().add(radioButtons[i]); // add radio button to the vbox
        }
        // instantiate every question in the arraylist of questions with null
        questions.forEach(e -> {
            e = null;
        });
        // add all elements in containers
        hBox.getChildren().addAll(previousButton, nextButton);
        hBox1.getChildren().addAll(quenstionNumLabel, text);
        vBox2.getChildren().addAll(hBox1, vBox1, hBox);
        vBox2.setTranslateX(-80); // translate an entire pane in x direction 
        // return vbox2
        return new Pane(vBox2);
    }

    // method is overloaded version of getQuestionTask() that gets a question from the array list with specified index
    // no exception may be thrown because current validation is performed inside button listeners
    public static Runnable getQuestionTask(int index) {
        class QuestionTask implements Runnable {

            @Override
            public void run() {

                // if time is up, interrupt current thread
                if (StaticData.getTimeUp() == true) {
                    Thread.currentThread().interrupt();
                } else {
                    // else get the element from the arraylist of questions

                    Question question = getQuestion(index); // cannot be null

                    Platform.runLater(() -> {
                        // set text of the question

                        text.setText(question.getQuestion());
                        quenstionNumLabel.setText("#" + Integer.toString(index + 1));
                    });
                    // set contents of each radiobutton
                    for (int i = 0; i < 5; i++) {
                        final int J = i;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                // read an answer (String)
                                String radioText = question.getAnswers()[J].getAnswer();
                                // if radioText is longer than 60 characters
                                String finalText = formatString(radioText); // text to be inserted into radiobutton

                                radioButtons[J].setText(finalText);
                                // select a radiobutton that was chosen by the user
                                // if next question was pressed, then user must have selected an answer
                                if (questions.get(index).getAnswers()[J].equals(questions.get(index).getSelectedAnswer())) {
                                    radioButtons[J].setSelected(true);
                                } else {
                                    if (radioButtons[J].isSelected() == true) {
                                        radioButtons[J].setSelected(false); // reset button if selected
                                    }
                                }

                                radioButtons[J].setOnAction(e -> {
                                    // set a selected answer
                                    questions.get(index).setSelectedAnswer(questions.get(index).getAnswers()[J]);
                                });
                            }

                            // method accepts a string and formats it so there are no more than 60 characters per one line
                            public String formatString(String string) {
                                String finalText = "";
                                if (string.length() > 60) {
                                    int length = string.length();
                                    while (length > 60) {
                                        for (int k = 60; k < string.length(); k++) {
                                            if (Character.isSpaceChar(string.charAt(k))) {
                                                String subStr = string.substring(k, string.length());
                                                if (subStr.length() > 60) {
                                                    subStr = formatString(subStr); // use recursion
                                                }
                                                finalText = string.substring(0, k).concat("\n").concat(subStr);
                                                break;
                                            } else if (k == string.length() - 1) { // last character
                                                finalText = string;
                                            }
                                        }
                                        length = finalText.length() - string.length();
                                    }
                                } else {
                                    // else text is less than 60 characters
                                    finalText = string;
                                }
                                return finalText;
                            }
                        });
                        // select a radio button that was selected by the user
                        if (question.getCorrectAnswer().equals(question.getAnswers()[i].getAnswer())) {
                            radioButtons[i].setSelected(true);
                        }
                    }

                }
            }

            // method returns an elemenent at index index; data validation is not required
            // it is performed in button event handlers
            private Question getQuestion(int index) {
                return questions.get(index);
            }

        }
        return new QuestionTask();
    }

    // task gets a question from corresponding arraylist
    public static Runnable getQuestionTask() {
        class QuestionTask implements Runnable {

            @Override
            public void run() {
                // custom exception
                class QuestionException extends Exception {
                }
                // check if time is up
                if (StaticData.getTimeUp() == true) {
                    // interrup this thread if time is up
                    Thread.currentThread().interrupt();
                } else {
                    // else  get a random question from corresponding arraylist
                    try {
                        Question question = getQuestion();
                        if (question == null) {
                            throw new QuestionException();
                        }
                        // set text of the question
                        Platform.runLater(() -> {
                            text.setText(question.getQuestion());
                            quenstionNumLabel.setText("#" + Integer.toString(current + 1));
                        });
                        // set contents of each radiobutton
                        for (int i = 0; i < 5; i++) {
                            final int J = i;

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    // read an answer (String)
                                    String radioText = question.getAnswers()[J].getAnswer();
                                    // if radioText is longer than 60 characters
                                    String finalText = formatString(radioText); // text to be inserted into radiobutton

                                    radioButtons[J].setText(finalText);
                                    if (radioButtons[J].isSelected() == true) {
                                        radioButtons[J].setSelected(false); // reset button if selected
                                    }
                                    radioButtons[J].setOnAction(e -> {
                                        // set a selected answer
                                        questions.get(current).setSelectedAnswer(questions.get(current).getAnswers()[J]);
                                    });
                                }

                                // method accepts a string and formats it so there are no more than 60 characters per one line
                                public String formatString(String string) {
                                    String finalText = "";
                                    if (string.length() > 60) {
                                        int length = string.length();
                                        while (length > 60) {
                                            for (int k = 60; k < string.length(); k++) {
                                                if (Character.isSpaceChar(string.charAt(k))) {
                                                    String subStr = string.substring(k, string.length());
                                                    if (subStr.length() > 60) {
                                                        subStr = formatString(subStr); // use recursion
                                                    }
                                                    finalText = string.substring(0, k).concat("\n").concat(subStr);
                                                    break;
                                                } else if (k == string.length() - 1) { // last character
                                                    finalText = string;
                                                }
                                            }
                                            length = finalText.length() - string.length();
                                        }
                                    } else {
                                        // else text is less than 60 characters
                                        finalText = string;
                                    }
                                    return finalText;
                                }
                            });

                        }
                    } catch (QuestionException ex) {
                        Alerts.alertError("Question error! Check if DB is properly connected!");
                    }
                }
            }

            // method picks a question based on user's current difficulty level
            private Question getQuestion() {

                Question question = null; // question to be returned
                ArrayList<Question> arr = null;

                switch (currentDiffLevel) {
                    case 1: // easy
                        arr = StaticData.getEasyQuestions();
                        break;
                    case 2: // medium
                        arr = StaticData.getMediumQuestions();
                        break;
                    case 3: // hard
                        arr = StaticData.getHardQuestions();
                        break;
                    case 4: // advanced
                        arr = StaticData.getAdvancedQuestions();
                        break;
                }
                // get a random question from 0th to 24th element in the appropriate group of questions
                question = arr.get((int) (Math.random() * arr.size()));
                while (questions.contains(question)) {
                    int nextElement = (arr.indexOf(question) + 1);
                    if (nextElement >= 25) {
                        nextElement = (int) (Math.random() * arr.size()); // generate new random number of end of the array has been reached
                    }
                    question = arr.get(nextElement); // increment position by 1 in the selected arraylist (of current difficulty)
                }
                // add element into questions arraylist
                questions.add(question);
                //System.out.println(question.getCorrectAnswer().getAnswer());
                //System.out.println(currentDiffLevel);
                // enable button when a new question is taken
                // if size of the arraylist is 1 (1st question) 0th button (that points to 1st question) 
                // will be enabled

                NavigationPane.getQuestionButtons()[questions.size() - 1].disableProperty().set(true);
                // enable current button however
                if (NavigationPane.getQuestionButtons()[current].disableProperty().get() == true) {
                    NavigationPane.getQuestionButtons()[current].disableProperty().set(false);
                }
                // add a listener to this button

                final int J = questions.size() - 1;
                NavigationPane.getQuestionButtons()[J].setOnAction(e -> {
                    current = J;
                    if (current == 0) {
                        // disable previous button
                        previousButton.disableProperty().set(true);
                        // if next button is disabled, enable it
                        if (nextButton.disableProperty().get() == true) {
                            nextButton.disableProperty().set(false);
                        }
                    } else if (current == 19) {
                        // disable next button
                        nextButton.disableProperty().set(true);
                        // if previous button is disabled, enable it
                        if (previousButton.disableProperty().get() == true) {
                            previousButton.disableProperty().set(false);
                        }
                    } else {
                        // else the question is neither first nor last
                        // enable both next and previous if they are disabled
                        if (previousButton.disableProperty().get() == true) {
                            previousButton.disableProperty().set(false);
                        }
                        if (nextButton.disableProperty().get() == true) {
                            nextButton.disableProperty().set(false);
                        }
                    }
                    // run new thread if button pressed - call overloaded task with an index
                    StaticData.getEXECUTOR().execute(getQuestionTask(J));

                });
                // return question
                return question;
            }

        }
        return new QuestionTask();
    }

}
