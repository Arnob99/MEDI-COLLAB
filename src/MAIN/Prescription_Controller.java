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
import javafx.scene.text.TextAlignment;
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

public class Prescription_Controller implements Initializable {
    @FXML private Label PrescriptionLabel;
    @FXML private JFXButton PrescriptionComposeButton;
    @FXML private JFXListView<VBox> PrescriptionListView;
    @FXML private JFXListView<VBox> PrescriptionMessageListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        PrescriptionComposeButton.setVisible(!Medi_collab.role.equals("Patient"));
        PrescriptionLabel.setLayoutX(screen.getWidth()/2 - PrescriptionLabel.getPrefWidth()/2);
        if (Medi_collab.role.equals("Patient"))
            PrescriptionMessageListView.setLayoutY(80);
        else
            PrescriptionMessageListView.setLayoutY(103);

        try {
            RefreshPrescription();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
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

        RefreshPrescription();
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

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void RefreshPrescription() throws SQLException {
        PrescriptionListView.getItems().clear();

        Statement statement = Objects.requireNonNull(Medi_collab.connection()).createStatement(),
                statement1 = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        ResultSet resultSet, temp;
        String user;
        Map<VBox, String> map = new HashMap<>();
        boolean done = false;

        if(Medi_collab.role.equals("Patient")) {
            resultSet = statement.executeQuery("SELECT DISTINCT SENDER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE RECEIVER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "AND SENDER_ROLE = 'Doctor' AND RECEIVER_ROLE = 'Patient' " +
                    "ORDER BY TO_CHAR(SHARING_DATE, 'DD-MM-YYYY'), TO_CHAR(SHARING_DATE, 'HH24:MI') DESC " +
                    ")");
        }
        else {
            resultSet = statement.executeQuery("SELECT DISTINCT RECEIVER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE SENDER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "AND SENDER_ROLE = 'Doctor' AND RECEIVER_ROLE = 'Patient' " +
                    "ORDER BY TO_CHAR(SHARING_DATE, 'DD-MM-YYYY'), TO_CHAR(SHARING_DATE, 'HH24:MI') DESC " +
                    ")");
        }

        while (resultSet.next()) {
            if (Medi_collab.role.equals("Patient"))
                user = resultSet.getString("SENDER");
            else
                user = resultSet.getString("RECEIVER");

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

            PrescriptionListView.getItems().add(PrescriptionListView.getItems().size(), vBox);

            if(!done) {
                showSharingFiles(user);
                done = true;
            }
        }

        PrescriptionListView.setOnMouseClicked(mouseEvent -> {
            String username = map.get(PrescriptionListView.getSelectionModel().getSelectedItem());

            try {
                showSharingFiles(username);
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void showSharingFiles(String username) throws SQLException {
        PrescriptionMessageListView.getItems().clear();
        Statement statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        Map<VBox, String> map = new HashMap<>();
        Map<VBox, Blob> map1 = new HashMap<>();
        Map<VBox, Boolean> flags = new HashMap<>();

        try {
            ResultSet resultSet;
            if (Medi_collab.role.equals("Patient"))
                resultSet = statement.executeQuery("SELECT * FROM SHARED_FILES " +
                        "WHERE SENDER = '" + username + "' AND RECEIVER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                        "ORDER BY TO_CHAR(SHARING_DATE, 'dd-mm-yyyy'), TO_CHAR(SHARING_DATE, 'hh24:mi') desc ");
            else
                resultSet = statement.executeQuery("SELECT * FROM SHARED_FILES " +
                        "WHERE SENDER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' AND RECEIVER = '" + username + "' " +
                        "ORDER BY TO_CHAR(SHARING_DATE, 'dd-mm-yyyy'), TO_CHAR(SHARING_DATE, 'hh24:mi') desc ");

            while (resultSet.next()){
                Label label, label1;
                if (resultSet.getString("SUBJECT") == null)
                    label = new Label("<NO SUBJECT>");
                else label = new Label(resultSet.getString("SUBJECT"));
                label1 = new Label(((Timestamp) resultSet.getObject("SHARING_DATE")).toString());

                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));
                label1.setFont(new Font(11));
                label1.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label, label1);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;" +
                        "       -fx-border-radius: 10;                  -fx-padding: 8;");

                Blob blob = resultSet.getBlob("WRITING");

                byte[] bytes = null;
                if(blob != null)
                    bytes = blob.getBytes(1, (int) blob.length());

                File file = new File("temp.txt");

                FileOutputStream fileOutputStream = new FileOutputStream(file);


                if(bytes != null)
                    fileOutputStream.write(bytes);
                fileOutputStream.close();

                String s = Files.readString(Paths.get("temp.txt"));

                file.delete();

                map.put(vBox, s);
                map1.put(vBox, resultSet.getBlob("SHARING_FILE"));
                flags.put(vBox, false);

                PrescriptionMessageListView.getItems().add(PrescriptionMessageListView.getItems().size(), vBox);
            }
        }
        catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

        PrescriptionMessageListView.setOnMouseClicked(mouseEvent -> {
            System.out.println("PRESCRIPTION MESSAGE CLICKED");
            VBox vBox = PrescriptionMessageListView.getSelectionModel().getSelectedItem();

            if (flags.get(vBox) != null && !flags.get(vBox)) {
                Label label;

                String s = "";
                if (!map.get(vBox).equals(s))
                    s = map.get(vBox);

                label = new Label(s);
                label.setFont(new Font(14));
                label.setTextFill(Color.valueOf("#464646"));
                label.setTextAlignment(TextAlignment.JUSTIFY);

                Separator separator = new Separator(Orientation.HORIZONTAL),
                        separator1 = new Separator(Orientation.HORIZONTAL);
                separator.setFocusTraversable(false);
                separator1.setFocusTraversable(false);

                vBox.getChildren().addAll(separator, separator1, label);

                if(map1.get(vBox) != null) {
                    Blob blob = map1.get(vBox);
                    byte[] bytes = new byte[0];
                    try {
                        bytes = blob.getBytes(1, (int) blob.length());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    File file = new File("temp.jpg");
                    FileOutputStream fileOutputStream;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(bytes);
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
                    imageView.fitWidthProperty().bind(PrescriptionMessageListView.widthProperty().subtract(50));
                    imageView.setPreserveRatio(true);
                    file.delete();

                    vBox.getChildren().add(imageView);
                }

                PrescriptionMessageListView.refresh();

                flags.replace(vBox, true);
            }
            else if (flags.get(vBox) != null) {
                vBox.getChildren().remove(2, vBox.getChildren().size());

                PrescriptionMessageListView.refresh();

                flags.replace(vBox, false);
            }
        });
    }
}
