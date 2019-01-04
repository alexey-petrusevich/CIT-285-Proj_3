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
public class Answer implements Serializable{
    private boolean isCorrect;
    private String answer;
    public Answer(String answer, boolean isCorrect){
        setAnswer(answer);
        setIsCorrect(isCorrect);
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }
    public void setIsCorrect(boolean isCorrect){
        this.isCorrect = isCorrect;
    }
    public String getAnswer(){
        return answer;
    }
    public boolean getIsCorrect(){
        return isCorrect;
    }
}
