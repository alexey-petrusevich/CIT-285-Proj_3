/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import programming_project_3.panes.LoginPane;


/**
 *
 * @author Aliaksei
 */
public class Programming_Project_3 extends Application {

    @Override
    public void start(Stage primaryStage) {
        StaticData.init(); // initialize static data
        Scene scene = new Scene(LoginPane.getLoginPane(), 900, 750);
        primaryStage.setTitle("Programming project 3");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    

}
