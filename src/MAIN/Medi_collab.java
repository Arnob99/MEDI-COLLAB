package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;
import java.util.Optional;

public class Medi_collab extends Application {
    public static boolean admin_logged_in = false;
    public static ResultSet User_Info_Resultset = null;
    public static String stylesheetaddress = "/Resources/Medi_collab_Stylesheet.css";
//    public static String dbusername, dbpassword;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    public static Connection connection(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "system", "2580");
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) {
        try {
            DatabaseMetaData databaseMetaData = connection().getMetaData();

            ResultSet resultSet = databaseMetaData.getTables(null, null, "ADMIN_TABLE", null);

            if(!resultSet.next()){
                System.out.println("INITIALIZING FOR THE FIRST TIME!");
                try {
                    Statement statement = connection().createStatement();

                    statement.executeQuery("CREATE TABLE ADMIN_TABLE(" +
                            "USERNAME VARCHAR2(30), " +
                            "PASSWORD VARCHAR2(30))");

                    statement.executeQuery("INSERT INTO ADMIN_TABLE " +
                            "VALUES ('admin', 'password')");

                    statement.executeQuery("DROP TABLE USERS_TABLE");

                    statement.executeQuery("CREATE TABLE USERS_TABLE (" +
                            "USERNAME VARCHAR2(20)," +
                            "PASSWORD VARCHAR2(30)," +
                            "FIRSTNAME VARCHAR2(20)," +
                            "LASTNAME VARCHAR2(15)," +
                            "DATEOFBIRTH DATE," +
                            "EMAIL VARCHAR2(30)," +
                            "ROLE VARCHAR2(15)," +
                            "ADDRESS VARCHAR2(100)," +
                            "CONTACT VARCHAR2(15)" +
                            ")");

//                    statement.executeQuery("CREATE TABLE PENDING_USERS_TABLE(" +
//                            ")");
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        launch(args);

//        try {
//            PreparedStatement preparedStatement = connection().prepareStatement("insert into pic values (?, ?)");
//
//            InputStream inputStream = new FileInputStream("E:\\Pictures\\sample.jpg");
//
//            preparedStatement.setString(1, "sample image");
//            preparedStatement.setBlob(2, inputStream);
//
//            preparedStatement.execute();
//
//            System.out.println("Done!!!!");
//        }
//        catch (SQLException | FileNotFoundException throwable) {
//            throwable.printStackTrace();
//        }

//        try {
//            Statement statement = connection().createStatement();
//
//            ResultSet resultSet = statement.executeQuery("select * from PIC");
//
//            while(resultSet.next()){
//                Blob blob = resultSet.getBlob(2);
//                byte b[] = blob.getBytes(1, (int) blob.length());
//
//                FileOutputStream fileOutputStream = new FileOutputStream("E:\\hehe.jpg");
//                fileOutputStream.write(b);
//                fileOutputStream.close();
//            }
//            System.out.println("Done!!!");
//            connection().close();
//        }
//        catch (SQLException | FileNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//
//            Connection connection = DriverManager.getConnection(url, username, password);
//
//            Statement statement = connection.createStatement();
//
//            System.out.println("Connection to Database Successful!");
//
//            ResultSet resultSet = statement.executeQuery(sql);
//
//            while (resultSet.next())
//                average = resultSet.getDouble(1);
//
//            System.out.println("Average age: " + average);
//
//            connection.close();
//            statement.close();
//            resultSet.close();
//        }
//        catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
    }
}
