/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import programming_project_3.StaticData;

/**
 *
 * @author Aliaksei
 */
public class TimePane {

    //global constants
    private static final Label TIMER_LBL = new Label();
    private static final long SLEEP_TIME = 1000; // 1 sec
    // set default time
    private static int minutes = 10, seconds = 0;
    private static Button dummyButton = new Button();

    public static void setMinutes(int minutes) {
        TimePane.minutes = minutes;
    }

    public static void setSeconds(int seconds) {
        TimePane.seconds = seconds;
    }

    public static Pane getTimePane() {

        // constants
        final Font DEF_FONT = Font.font("Times", FontWeight.BOLD, 20);
        final int VBOX_GAP = 10;
        // labels
        final Label TIME_LEFT_LBL = new Label("Time left:");
        TIME_LEFT_LBL.setFont(DEF_FONT);

        TIMER_LBL.setFont(DEF_FONT);
        // containers
        VBox vBox = new VBox(VBOX_GAP);
        vBox.setTranslateX(50);
        dummyButton.setOnAction(e->{
            NavigationPane.updateNumTakes();
            StaticData.getEXECUTOR().execute(ResultsPane.getResultsTask());
            StaticData.loadScene(e, ResultsPane.getResultsPane());
        });
        dummyButton.setVisible(false);
        vBox.getChildren().addAll(TIME_LEFT_LBL, TIMER_LBL, dummyButton);
        // return vBox
        return vBox;
    }

    // method returns a runnable task
    public static Runnable getTimerTask() {

        class TimerTask implements Runnable {

            @Override
            public void run() {

                try {
                    while (!StaticData.isSubmitted) {
                        
                        // decrement time
                        // if seconds are not 0 decrement seconds
                        if (seconds != 0) {
                            seconds--;
                        } // else if seconds are 0 and minutes are not 0, decrement minutes
                        // and reset seconds to 59;
                        else if (seconds == 0 && minutes != 0) {
                            minutes--;
                            seconds = 59;
                        } // else seconds and minutes are 0; interrupt thread.
                        else {
                            // set time up to true
                            StaticData.setTimeUp(Boolean.TRUE);
                            // complete the task
                            StaticData.isSubmitted = true;
                            Platform.runLater(() -> dummyButton.fire());
                            
                            
                            
                        }
                        Platform.runLater(() -> updateTimer());// update label with time
                        //StaticData.getLock().unlock(); // unlock the lock
                        // put thread to sleep
                        Thread.sleep(SLEEP_TIME);
                        
                    }
                    
                } catch (InterruptedException ex) {
                    // if thread was interrupted, load the results                    
                    StaticData.getEXECUTOR().execute(ResultsPane.getResultsTask());
                } 
            }

        }

        return new TimerTask();
    }
    // method updates the timer

    private static void updateTimer() {
        String time = "";
        // update time only if minutes or seconds are not 0
        if (minutes >= 0 || seconds >= 0) {
            // check if minutes or seconds are less than 10, and add leading 0 if so
            if (minutes < 10) {
                time += "0".concat(Integer.toString(minutes));
            } else {
                time += Integer.toString(minutes);
            }
            time += ":";
            if (seconds < 10) {
                time += "0".concat(Integer.toString(seconds));
            } else {
                time += Integer.toString(seconds);
            }
            TIMER_LBL.setText(time);
        }

    }
}
