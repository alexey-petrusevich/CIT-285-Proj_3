/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.event.Event;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import programming_project_3.panes.QuestionPane;
import programming_project_3.panes.TimePane;

/**
 *
 * @author Aliaksei
 */
public class StaticData {
    public static boolean isSubmitted = false;
    private static ArrayList<Question> easyQuestions = new ArrayList<>();
    private static ArrayList<Question> mediumQuestions = new ArrayList<>();
    private static ArrayList<Question> hardQuestions = new ArrayList<>();
    private static ArrayList<Question> advancedQuestions = new ArrayList<>();
    private static Boolean timeUp = false;
    private static final Lock LOCK = new ReentrantLock();
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static ExecutorService getEXECUTOR() {
        return EXECUTOR;
    }

    public static Lock getLock() {
        return LOCK;
    }

    public static void setTimeUp(Boolean timeUp) {
        StaticData.timeUp = timeUp;
    }

    public static Boolean getTimeUp() {
        return timeUp;
    }
    public static void reset(){
        isSubmitted = false;
        timeUp = false;
        QuestionPane.setQuestions(new ArrayList<>(20));
        TimePane.setMinutes(10);
        TimePane.setSeconds(0);
        if(QuestionPane.getNextButton().disableProperty().get() == true){
            QuestionPane.getNextButton().disableProperty().set(false);
        }
        QuestionPane.setCurrentDiffLevel((byte) (Math.random() * 4 + 1));
        QuestionPane.setCurrent(0);
    }

    // upload contents of database into corresponding arraylists upon staring the application
    public static void init() {

        //**************************************************************************************
        // UNCOMMENT THE LINE OF CODE BELOW IF DATABASE HAS NO QUESTIONS
        //DatabaseConverter.parseData();
        //**************************************************************************************
        // initialize arraylists with data from database (faster than querying every question)


        loadData(easyQuestions, "easy");
        loadData(mediumQuestions, "medium");
        loadData(hardQuestions, "hard");
        loadData(advancedQuestions, "advanced");
    }

    // method accesses database and tries reading data from it and writing to corresponsind arrayList
    public static void loadData(ArrayList<Question> arr, String table) {
        // constants
        final String DRIVER = "com.mysql.jdbc.Driver";
        final String DATABASE = "jdbc:mysql://localhost/project3";
        final String LOGIN = "root";
        final String PASSWORD = "mysql";
        // connect to db
        try {
            // 1) connect a driver
            Class.forName(DRIVER);
            // 2) create conection
            try (Connection connection = DriverManager.getConnection(DATABASE, LOGIN, PASSWORD)) {
                // 3) prepare a statement
                PreparedStatement prepStatement = connection.prepareStatement("SELECT * FROM "
                        + table + ";");
                ResultSet rSet = prepStatement.executeQuery();
                while (rSet.next()) {

                    // use blob reading technique using byte arrays
                    // 1) create a byte array and populate it with blob from database
                    byte[] bArray1 = (byte[]) rSet.getObject("question");
                    // 2) create a bais with byte array as its parameter
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(bArray1);
                            // 3) create object input stream with bais as its stream, so as byte array (automatically)
                            ObjectInputStream in = new ObjectInputStream(bais);) {
                        // 4) ready object from object input stream
                        Question question = (Question) in.readObject();
                        // add question into corresponding arraylist                    
                        arr.add(question);
                        // repeat the process while there is data to read from rSet
                    }

                }
            }
        } catch (ClassNotFoundException ex) {
            Alerts.alertError("Driver for DB not found!");
        } catch (SQLException ex) {
            Alerts.alertError("Error accessing DB!");
            ex.printStackTrace();
        } catch (IOException ex) {
            Alerts.alertError("Error while reading from input stream of byte array!");
            ex.printStackTrace();
        }

    }

    public static void loadScene(Event event, Pane pane) {
        // get the stage and the scene of the window
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        Scene scene = stage.getScene();
        // hide the stage
        stage.hide();
        // put new pane onto the scene
        scene.setRoot(pane);
        // put new scene on the stage
        stage.setScene(scene);
        // show the stage
        stage.show();
        scene.getRoot().requestFocus(); // request focus on the scene
    }
    

    public static void setEasyQuestions(ArrayList<Question> easyQuestions) {
        StaticData.easyQuestions = easyQuestions;
    }

    public static void setMediumQuestions(ArrayList<Question> mediumQuestions) {
        StaticData.mediumQuestions = mediumQuestions;
    }

    public static void setHardQuestions(ArrayList<Question> hardQuestions) {
        StaticData.hardQuestions = hardQuestions;
    }

    public static void setAdvancedQuestions(ArrayList<Question> advancedQuestions) {
        StaticData.advancedQuestions = advancedQuestions;
    }

    public static ArrayList<Question> getEasyQuestions() {
        return easyQuestions;
    }

    public static ArrayList<Question> getMediumQuestions() {
        return mediumQuestions;
    }

    public static ArrayList<Question> getHardQuestions() {
        return hardQuestions;
    }

    public static ArrayList<Question> getAdvancedQuestions() {
        return advancedQuestions;
    }

}
