/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming_project_3;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Converts binary file to MySQL table entries
 *
 * @author Aliaksei
 */
public class DatabaseConverter {

    private static int counter = 0; // for counting number of readings; equal to 4 after all files were processed
    // method writes the content of binary files into database

    public static void parseData() {
        final File EASY = new File("easy.dat");
        final String EASY_TABLE = "easy";
        final File MEDIUM = new File("medium.dat");
        final String MEDIUM_TABLE = "medium";
        final File HARD = new File("hard.dat");
        final String HARD_TABLE = "hard";
        final File ADVANCED = new File("advanced.dat");
        final String ADVANCED_TABLE = "advanced";

        // read data from the file
        // 3) prepare statement
        while (counter < 4) {
            // write data from each file to corresponding table
            writeData(EASY, EASY_TABLE);
            writeData(MEDIUM, MEDIUM_TABLE);
            writeData(HARD, HARD_TABLE);
            writeData(ADVANCED, ADVANCED_TABLE);
        }

    }

    private static void writeData(File file, String table) {
        final String DRIVER = "com.mysql.jdbc.Driver";
        final String DATABASE = "jdbc:mysql://localhost/project3";
        final String LOGIN = "root";
        final String PASSWORD = "mysql";
        try {
            // 1) connect driver
            Class.forName(DRIVER);
            PreparedStatement prepStatement;
            // 2) make a connections
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
                    Connection connection = DriverManager.getConnection(DATABASE, LOGIN, PASSWORD)) {
                // read arraylist of questions from corresponding binary file
                ArrayList<Question> questions = null;
                questions =  (ArrayList<Question>) input.readObject();
                for(int i = 0; i < questions.size(); i++) {                    
                    
                    // write question into database
                    prepStatement = connection.prepareStatement("INSERT INTO " + table + " (id, question) VALUES (?, ?);");
                    prepStatement.setInt(1, questions.get(i).getId());
                    // cast a question into a byte array before writing to blob datatype in DB
                    // 1) create baos
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    // 2) object output stream with baos as output stream
                    ObjectOutputStream out = new ObjectOutputStream(baos);
                    // 3) write a question into ouput stream, and into baos
                    out.writeObject(questions.get(i));
                    // 4) get byte data from baos
                    byte[] bArray = baos.toByteArray();
                    // 5) assign byte array as blob object in prepared statement ( setBytes() )
                    prepStatement.setBytes(2, bArray);
                    // execute statement
                    prepStatement.execute();
                    // here question is written as blob to database and loop starts over
                    // 1) object is taken from the file
                    // 2) placed into temporary question variable
                    // 3) object is written into database
                    // the process continues until an EOFException is thrown - end of file is reached
                }

            } catch (EOFException ex) {
                ex.printStackTrace();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        // increment counter to show the end for reading process
        counter++;
    }
}
