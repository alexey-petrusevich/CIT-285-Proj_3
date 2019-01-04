/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import programming_project_3.StaticData;

/**
 *
 * @author Aliaksei
 */
public class TestingPane {

    public static Pane getTestingPane() {

        BorderPane borderPane = new BorderPane();

        // set borderpane components
        borderPane.setLeft(NavigationPane.getNavigationPane());
        borderPane.setTop(TimePane.getTimePane());
        borderPane.setRight(QuestionPane.getQuestionPane());
        BorderPane.setAlignment(borderPane.getRight(), Pos.CENTER_LEFT);
        // start timer thread and question thread
        StaticData.getEXECUTOR().execute(TimePane.getTimerTask());
        StaticData.getEXECUTOR().execute(QuestionPane.getQuestionTask());
        // return borderPane
        return borderPane;
    }
}
