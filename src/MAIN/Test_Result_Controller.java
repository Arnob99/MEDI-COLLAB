package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class Test_Result_Controller implements Initializable {
    @FXML private Label TestResultLabel;
    @FXML private JFXListView<VBox> TestResultListView;
    @FXML private JFXListView<VBox> TestResultMessageListView;
    @FXML private JFXButton TestResultComposeButton;

    public static String ForwardUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        TestResultComposeButton.setVisible(Medi_collab.role.equals("Staff"));
        TestResultLabel.setLayoutX(screen.getWidth()/2 - TestResultLabel.getPrefWidth()/2);
        if (!Medi_collab.role.equals("Staff"))
            TestResultMessageListView.setLayoutY(103);
        else
            TestResultMessageListView.setLayoutY(80);

        try {
            RefreshTestResult();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleGoBackButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Main_Menu.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleComposeButton() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Writing.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Writing.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMaximized(true);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        RefreshTestResult();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage)((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    private void RefreshTestResult() throws SQLException {
        TestResultListView.getItems().clear();

        Statement statement = Objects.requireNonNull(Medi_collab.connection()).createStatement(),
                statement1 = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        ResultSet resultSet, temp;
        String user;
        Map<VBox, String> map = new HashMap<>();
        boolean done = false;

        if(Medi_collab.role.equals("Staff")) {
            resultSet = statement.executeQuery("SELECT DISTINCT RECEIVER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE SENDER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "AND SENDER_ROLE = 'Staff' AND RECEIVER_ROLE = 'Patient' " +
                    "ORDER BY TO_CHAR(SHARING_DATE, 'DD-MM-YYYY'), TO_CHAR(SHARING_DATE, 'HH24:MI') DESC " +
                    ")");
        }
        else if(Medi_collab.role.equals("Patient")){
            resultSet = statement.executeQuery("SELECT DISTINCT SENDER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE RECEIVER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "AND SENDER_ROLE = 'Staff' AND RECEIVER_ROLE = 'Patient' " +
                    "ORDER BY TO_CHAR(SHARING_DATE, 'DD-MM-YYYY'), TO_CHAR(SHARING_DATE, 'HH24:MI') DESC " +
                    ")");
        }
        else {
            resultSet = statement.executeQuery("SELECT DISTINCT SENDER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE RECEIVER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "AND SENDER_ROLE = 'Patient' AND RECEIVER_ROLE = 'Doctor' " +
                    "ORDER BY TO_CHAR(SHARING_DATE, 'DD-MM-YYYY'), TO_CHAR(SHARING_DATE, 'HH24:MI') DESC " +
                    ")");
        }

        while (resultSet.next()) {
            if(Medi_collab.role.equals("Staff"))
                user = resultSet.getString("RECEIVER");
            else
                user = resultSet.getString("SENDER");

            temp = statement1.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE WHERE USERNAME = '" + user + "'");
            temp.next();

            Label label, label1;

            label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
            label.setFont(new Font(20));
            label.setTextFill(Color.valueOf("#464646"));

            label1 = new Label(user);
            label1.setFont(new Font(14));
            label1.setTextFill(Color.valueOf("#464646"));

            VBox vBox = new VBox(5, label, label1);
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                    "       -fx-border-color: #2ed2ff;" +
                    "       -fx-border-radius: 10;                  -fx-padding: 8;");

            map.put(vBox, user);

            TestResultListView.getItems().add(TestResultListView.getItems().size(), vBox);

            if(!done) {
                showSharingFiles(user);
                done = true;
            }
        }

        TestResultListView.setOnMouseClicked(mouseEvent -> {
            String username = map.get(TestResultListView.getSelectionModel().getSelectedItem());

            try {
                showSharingFiles(username);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private void showSharingFiles(String username) throws SQLException {
        TestResultMessageListView.getItems().clear();
        Statement statement = Objects.requireNonNull(Medi_collab.connection()).createStatement(),
                statement1 = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        Map<VBox, Blob> map = new HashMap<>();
        Map<VBox, Blob> map1 = new HashMap<>();
        Map<VBox, Boolean> flags = new HashMap<>();
        String subject;

        try {
            ResultSet resultSet;
            if (Medi_collab.role.equals("Staff"))
                resultSet = statement.executeQuery("SELECT * FROM SHARED_FILES " +
                        "WHERE SENDER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                        "AND RECEIVER = '" + username + "' " +
                        "ORDER BY TO_CHAR(SHARING_DATE, 'dd-mm-yyyy') desc, TO_CHAR(SHARING_DATE, 'hh24:mi') desc ");
            else
                resultSet = statement.executeQuery("SELECT * FROM SHARED_FILES " +
                        "WHERE SENDER = '" + username + "' AND RECEIVER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                        "ORDER BY TO_CHAR(SHARING_DATE, 'dd-mm-yyyy') desc, TO_CHAR(SHARING_DATE, 'hh24:mi') desc ");

            while (resultSet.next()){
                subject = "<NO SUBJECT>";
                Label label, label1, label2;
                Blob blob = resultSet.getBlob("WRITING"),  blob1 = resultSet.getBlob("SHARING_FILE");

                if (resultSet.getString("SUBJECT") != null)
                    subject = resultSet.getString("SUBJECT");
                label = new Label(subject);
                label.setWrapText(true);
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));

                label1 = new Label(((Timestamp) resultSet.getObject("SHARING_DATE")).toString());
                label1.setFont(new Font(11));
                label1.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label, label1);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;" +
                        "       -fx-border-radius: 10;                  -fx-padding: 8;");

                if(!resultSet.getString("SENDER").equals(resultSet.getString("FROM_USERNAME"))) {
                    label2 = new Label("Forwarded from: " + resultSet.getString("FROM_USERNAME"));
                    label2.setFont(new Font(12));
                    label2.setTextFill(Color.valueOf("#464646"));
                    vBox.getChildren().add(label2);
                }

                map.put(vBox, blob);
                map1.put(vBox, blob1);
                flags.put(vBox, false);

                TestResultMessageListView.getItems().add(TestResultMessageListView.getItems().size(), vBox);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        TestResultMessageListView.setOnMouseClicked(mouseEvent -> {
            System.out.println("TEST RESULT MESSAGE CLICKED");
            VBox vBox = TestResultMessageListView.getSelectionModel().getSelectedItem();

            if (flags.get(vBox) != null && !flags.get(vBox)) {
                Label label;

                String s = null;
                Blob blob = map.get(vBox);
                byte[] bytes;
                try {
                    bytes = blob.getBytes(1, (int) blob.length());

                    File file = new File("temp.txt");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    if(bytes != null)
                        fileOutputStream.write(bytes);
                    fileOutputStream.close();

                    s = Files.readString(Paths.get("temp.txt"));

                    file.delete();
                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }

                label = new Label(s);
                label.setFont(new Font(14));
                label.setTextFill(Color.valueOf("#464646"));

                Separator separator = new Separator(Orientation.HORIZONTAL),
                        separator1 = new Separator(Orientation.HORIZONTAL);
                separator.setFocusTraversable(false);
                separator1.setFocusTraversable(false);

                vBox.getChildren().addAll(separator, separator1, label);

                Blob blob1 = null;
                if(map1.get(vBox) != null) {
                    blob1 = map1.get(vBox);
                    byte[] bytes1 = new byte[0];
                    try {
                        bytes1 = blob1.getBytes(1, (int) blob1.length());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    File file = new File("temp.jpg");
                    FileOutputStream fileOutputStream;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(bytes1);
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream;
                    Image image = null;
                    try {
                        inputStream = new FileInputStream(file);
                        image = new Image(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.fitWidthProperty().bind(TestResultMessageListView.widthProperty().subtract(50));
                    imageView.setPreserveRatio(true);
                    file.delete();

                    vBox.getChildren().add(imageView);
                }

                if (Medi_collab.role.equals("Patient")){
                    System.out.println("HERE");
                    JFXButton ForwardButton = new JFXButton("Forward");
                    ForwardButton.setStyle("-fx-pref-width: 120;        -fx-font-size: 14;" +
                            "-fx-text-fill: WHITE;                      -fx-background-color: #2ed2ff;" +
                            "-fx-background-radius: 10;                 -fx-border-color: #2ed2ff;" +
                            "-fx-border-radius: 10;");

                    vBox.getChildren().add(ForwardButton);

                    Blob finalBlob = blob1;
                    ForwardButton.setOnAction(actionEvent -> {
                        ForwardDialogue forwardDialogue = new ForwardDialogue();
                        forwardDialogue.OpenDialogue();
                        String role = null;
                        boolean alright = true;
                        try {
                            ResultSet temp = statement1.executeQuery("SELECT ROLE FROM USERS_TABLE " +
                                    "WHERE USERNAME = '" + ForwardUsername + "'");
                            if (temp.next())
                                role = temp.getString("ROLE");
                            else
                                alright = false;

                            if (alright) {
                                PreparedStatement preparedStatement = Objects.requireNonNull(Medi_collab.connection()).prepareStatement("INSERT INTO SHARED_FILES " +
                                        "VALUES (?, ?, ?, ?, ?, SYSDATE, 'Patient', ?, ?)");
                                Label label1 = (Label) vBox.getChildren().get(0);
                                preparedStatement.setString(1, Medi_collab.User_Info_Resultset.getString("USERNAME"));
                                preparedStatement.setString(2, ForwardUsername);
                                preparedStatement.setString(3, label1.getText());
                                preparedStatement.setBlob(4, blob);
                                preparedStatement.setBlob(5, finalBlob);
                                preparedStatement.setString(6, role);
                                preparedStatement.setString(7, username);

                                preparedStatement.execute();
                            }
                        }
                        catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });
                }

                TestResultMessageListView.refresh();

                flags.replace(vBox, true);
            }
            else if (flags.get(vBox) != null) {
                if (Medi_collab.role.equals("Doctor"))
                    vBox.getChildren().remove(3, vBox.getChildren().size());
                else
                    vBox.getChildren().remove(2, vBox.getChildren().size());

                TestResultMessageListView.refresh();

                flags.replace(vBox, false);
            }
        });
    }
}
