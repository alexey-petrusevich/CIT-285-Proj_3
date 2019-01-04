/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3.panes;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import programming_project_3.StaticData;


/**
 *
 * @author Aliaksei
 */
public class PreTestPane {

    public static Pane getPreTestPane() {
        // constant
        final Font HEAD_FONT = Font.font("Times", FontWeight.BOLD, 26);
        final Font HEAD1_FONT = Font.font("Times", FontWeight.SEMI_BOLD, 20);
        final Font DEF_FONT = Font.font("Times", FontWeight.MEDIUM, 14);
        final int VGAP = 25;
        // text do be displayed
        final Text READY_TXT = new Text();
        READY_TXT.setFont(HEAD_FONT);
        READY_TXT.setText("Test is about to begin!");
        final Text DESC_TXT = new Text();
        DESC_TXT.setFont(DEF_FONT);
        DESC_TXT.setLineSpacing(15);
        DESC_TXT.setText(""
                + "This test is designed to test your knowledge of Java.\n"
                + "The test will cover the majority of topics from\n"
                + "data types and operations to MySQL and JSP. You will have\n"
                + "10 minutes to answer 20 questions. When you are ready\n"
                + "press \"Enter\" key to start evaluation. Good luck!");
        final Text PRESS_ENTER_TXT = new Text();
        PRESS_ENTER_TXT.setFont(HEAD1_FONT);
        PRESS_ENTER_TXT.setText("Press \"Enter\" key to start");
        // containers
        VBox vBox = new VBox(VGAP);
        vBox.setAlignment(Pos.CENTER);
        // add text elements in the container
        vBox.getChildren().addAll(READY_TXT, DESC_TXT, PRESS_ENTER_TXT);
        vBox.setOnKeyPressed(e->{
            switch(e.getCode()){
                case ENTER:
                    StaticData.reset();
                    // load testing scene
                    StaticData.loadScene(e, TestingPane.getTestingPane());
                    break;
            }
        });
        // return vbox
        return vBox;
    }
}
