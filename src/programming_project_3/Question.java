/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3;

import java.io.Serializable;

/**
 *
 * @author Aliaksei
 */
public class Question implements Serializable{

    // datafield
    private int id; // question id
    private int difficulty; // 1-4 (1-easy, 2-medium, 3-hard, 4-advanced)
    private String question; // contents of the question
    private Answer[] answers; // set of options 
    private Answer correctAnswer;
    private Answer selectedAnswer = null;

    public void setSelectedAnswer(Answer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Answer getSelectedAnswer() {
        return selectedAnswer;
    }

    // constructors
    public Question() {

    }

    public Question(int id, int difficulty, String question, Answer[] answers, Answer correctAnswer) {
        setId(id);
        setDifficulty(difficulty);
        setQuestion(question);
        setAnswers(answers);
        setCorrectAnswer(correctAnswer);
    }
    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // getters
    public int getId() {
        return id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

}
