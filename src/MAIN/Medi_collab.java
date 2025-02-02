package MAIN;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Medi_collab extends Application {
    public static ResultSet User_Info_Resultset = null;
    public static String role = "";
    public static StringConverter<LocalDate> localDateStringConverter = new StringConverter<>() {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        @Override
        public String toString(LocalDate localDate) {
            if (localDate != null)
                return dateTimeFormatter.format(localDate);
            else
                return "";
        }

        @Override
        public LocalDate fromString(String s) {
            if (s != null && !s.isEmpty())
                return LocalDate.parse(s, dateTimeFormatter);
            else
                return null;
        }
    };

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sign_In.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Sign_In.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.show();
    }

    public static Connection connection(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "system", "2580");
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("DATABASE CONNECTION COULD NOT BE ESTABLISHED!");

            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) throws SQLException {
        DatabaseMetaData databaseMetaData = Objects.requireNonNull(connection()).getMetaData();

        ResultSet resultSet = databaseMetaData.getTables(null, null, "USERS_TABLE", null);

        if(!resultSet.next()){
            System.out.println("INITIALIZING FOR THE FIRST TIME!");
            Statement statement = Objects.requireNonNull(connection()).createStatement();

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

            resultSet = databaseMetaData.getTables(null, null, "SHARED_FILES", null);

            if(resultSet.next())
                statement.executeQuery("DROP TABLE SHARED_FILES");

            statement.executeQuery("CREATE TABLE SHARED_FILES (" +
                    "    SENDER VARCHAR2(20), " +
                    "    RECEIVER VARCHAR2(20)," +
                    "    SUBJECT VARCHAR2(250)," +
                    "    DESCRIPTION BLOB," +
                    "    WRITING BLOB," +
                    "    SHARING_FILE BLOB," +
                    "    SHARING_DATE DATE)");
        }

        launch(args);
    }
}
