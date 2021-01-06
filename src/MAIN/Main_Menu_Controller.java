package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main_Menu_Controller {
    @FXML
    private JFXListView<VBox> MainMenuActionPaneMainListView;
    @FXML
    private JFXButton MainMenuComposeButton;
    @FXML
    private JFXListView<VBox> MainMenuActionPaneTestResultListView;
    @FXML
    private JFXButton RefreshButton;
    @FXML
    private JFXListView<VBox> MainMenuActionPanePrescriptionListView;
    @FXML
    private JFXButton MainMenuSetAppointmentButton;
    @FXML
    private JFXButton MainMenuAppointmentListButton;
    @FXML
    private JFXButton MainMenuPrescriptionButton;
    @FXML
    private JFXButton MainMenuTestResultButton;
    @FXML
    private Label MainMenuActionPaneSetAppointmentsLabel;
    @FXML
    private Label MainMenuActionPaneAppointmentListLabel;

    public boolean setappointmentclicked = false, appointmentlistclicked = false, prescriptionclicked = false, testresultclicked = false;

    public void init() throws SQLException {
        switch (Medi_collab.role) {
            case "Patient" -> {
                MainMenuSetAppointmentButton.setVisible(true);
                MainMenuSetAppointmentButton.setLayoutY(166);
                MainMenuAppointmentListButton.setVisible(true);
                MainMenuAppointmentListButton.setLayoutY(MainMenuSetAppointmentButton.getLayoutY() + 45);
                MainMenuPrescriptionButton.setVisible(true);
                MainMenuPrescriptionButton.setLayoutY(MainMenuAppointmentListButton.getLayoutY() + 45);
                MainMenuTestResultButton.setVisible(true);
                MainMenuTestResultButton.setLayoutY(MainMenuPrescriptionButton.getLayoutY() + 45);
                MainMenuComposeButton.setVisible(false);
            }
            case "Doctor" -> {
                MainMenuSetAppointmentButton.setVisible(false);
                MainMenuAppointmentListButton.setVisible(true);
                MainMenuAppointmentListButton.setLayoutY(188);
                MainMenuPrescriptionButton.setVisible(true);
                MainMenuPrescriptionButton.setLayoutY(MainMenuAppointmentListButton.getLayoutY() + 45);
                MainMenuTestResultButton.setVisible(true);
                MainMenuTestResultButton.setLayoutY(MainMenuPrescriptionButton.getLayoutY() + 45);
                MainMenuComposeButton.setVisible(false);
            }
            case "Staff" -> {
                MainMenuSetAppointmentButton.setVisible(false);
                MainMenuAppointmentListButton.setVisible(true);
                MainMenuAppointmentListButton.setLayoutY(211);
                MainMenuPrescriptionButton.setVisible(false);
                MainMenuTestResultButton.setVisible(true);
                MainMenuTestResultButton.setLayoutY(MainMenuAppointmentListButton.getLayoutY() + 45);
                MainMenuComposeButton.setVisible(false);
            }
            default -> System.out.println("Serious Problem!!!!!");
        }
        MainMenuActionPaneMainListView.setVisible(false);

        handleRefreshButton();
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMainMenuAppointmentListButton() {
        MainMenuSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        setappointmentclicked = false;
        appointmentlistclicked = true;
        prescriptionclicked = false;
        testresultclicked = false;

        MainMenuActionPaneAppointmentListLabel.setVisible(true);
        MainMenuActionPaneSetAppointmentsLabel.setVisible(false);
        MainMenuActionPanePrescriptionListView.setVisible(false);
        MainMenuActionPaneTestResultListView.setVisible(false);
        MainMenuComposeButton.setVisible(false);
        MainMenuActionPaneMainListView.setVisible(false);
    }

    public void handleMainMenuComposeButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Writing.fxml"));

        Parent root = fxmlLoader.load();

        Scene oldscene = ((Node)actionEvent.getSource()).getScene();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMaximized(true);

        Writing_Controller writing_controller = fxmlLoader.getController();
        writing_controller.init();

        stage.showAndWait();
    }

    public void handleMainMenuMyProfileButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("User_Profile.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();

        User_Profile_Controller user_profile_controller = fxmlLoader.getController();
        user_profile_controller.ShowUserInfo(Medi_collab.User_Info_Resultset);
    }

    public void handleMainMenuPrescriptionButton() {
        MainMenuSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        setappointmentclicked = false;
        appointmentlistclicked = false;
        prescriptionclicked = true;
        testresultclicked = false;

        MainMenuActionPaneSetAppointmentsLabel.setVisible(false);
        MainMenuActionPaneAppointmentListLabel.setVisible(false);
        MainMenuActionPanePrescriptionListView.setVisible(true);
        MainMenuActionPaneTestResultListView.setVisible(false);
        if(Medi_collab.role.equals("Doctor") || Medi_collab.role.equals("Staff"))
            MainMenuComposeButton.setVisible(true);
        else
            MainMenuComposeButton.setVisible(false);
        MainMenuActionPaneMainListView.getItems().clear();
        MainMenuActionPaneMainListView.setVisible(true);
    }

    public void handleMainMenuSetAppointmentButton() {
        MainMenuSetAppointmentButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        setappointmentclicked = true;
        appointmentlistclicked = false;
        prescriptionclicked = false;
        testresultclicked = false;

        MainMenuActionPaneSetAppointmentsLabel.setVisible(true);
        MainMenuActionPaneAppointmentListLabel.setVisible(false);
        MainMenuActionPanePrescriptionListView.setVisible(false);
        MainMenuActionPaneTestResultListView.setVisible(false);
        MainMenuComposeButton.setVisible(false);
        MainMenuActionPaneMainListView.setVisible(false);
    }

    public void handleMainMenuSignOutButton(ActionEvent actionEvent) throws IOException {
        Medi_collab.User_Info_Resultset = null;
        Medi_collab.admin_logged_in = false;

        Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

        ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Medi_collab.stylesheetaddress);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public void handleMainMenuTestResultButton() {
        MainMenuSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");

        setappointmentclicked = false;
        appointmentlistclicked = false;
        prescriptionclicked = false;
        testresultclicked = true;

        MainMenuActionPaneSetAppointmentsLabel.setVisible(false);
        MainMenuActionPaneAppointmentListLabel.setVisible(false);
        MainMenuActionPanePrescriptionListView.setVisible(false);
        MainMenuActionPaneTestResultListView.setVisible(true);
        if(Medi_collab.role.equals("Staff"))
            MainMenuComposeButton.setVisible(true);
        else
            MainMenuComposeButton.setVisible(false);
        MainMenuActionPaneMainListView.getItems().clear();
        MainMenuActionPaneMainListView.setVisible(true);
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleRefreshButton() throws SQLException {
        MainMenuActionPanePrescriptionListView.getItems().clear();
        MainMenuActionPaneTestResultListView.getItems().clear();
        MainMenuActionPaneMainListView.getItems().clear();

        Map<VBox, String> map = new HashMap<>();
        Statement statement = Medi_collab.connection().createStatement(), statement1 = Medi_collab.connection().createStatement();
        ResultSet resultSet;

        System.out.println(Medi_collab.User_Info_Resultset.getString("USERNAME"));

        if(Medi_collab.role.equals("Patient")){
            resultSet = statement.executeQuery("SELECT DISTINCT SENDER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE RECEIVER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "ORDER BY SHARING_DATE" +
                    ")");

            while (resultSet.next()){
                String sender = resultSet.getString("SENDER");

                ResultSet temp = statement1.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + sender + "'");

                Label label = null, label1 = null;
                if(temp.next())
                    label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));
                label1 = new Label(sender);
                label1.setFont(new Font(14));
                label1.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label, label1);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;              -fx-border-width: 1.5;" +
                        "       -fx-border-radius: 10;                  -fx-padding: 0 0 0 10;");

                map.put(vBox, sender);

                temp = statement1.executeQuery("SELECT ROLE FROM USERS_TABLE WHERE USERNAME = '" + sender + "'");

                if(temp.next()) {
                    if (temp.getString("ROLE").equals("Doctor"))
                        MainMenuActionPanePrescriptionListView.getItems().add(MainMenuActionPanePrescriptionListView.getItems().size(), vBox);
                    else if (temp.getString("ROLE").equals("Staff"))
                        MainMenuActionPaneTestResultListView.getItems().add(MainMenuActionPaneTestResultListView.getItems().size(), vBox);
                }
            }
        }
        else if(Medi_collab.role.equals("Doctor")){
            resultSet = statement.executeQuery("SELECT DISTINCT RECEIVER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE SENDER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "ORDER BY SHARING_DATE" +
                    ")");

            while (resultSet.next()){
                String receiver = resultSet.getString("RECEIVER");

                ResultSet temp = statement1.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + receiver + "'");

                Label label = null, label1 = null;
                if(temp.next())
                    label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));
                label1 = new Label(receiver);
                label1.setFont(new Font(14));
                label1.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label, label1);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;              -fx-border-width: 1.5;" +
                        "       -fx-border-radius: 10;                  -fx-padding: 0 0 0 10;");

                map.put(vBox, receiver);

                MainMenuActionPanePrescriptionListView.getItems().add(MainMenuActionPanePrescriptionListView.getItems().size(), vBox);
            }
        }
        else if(Medi_collab.role.equals("Staff")){
            resultSet = statement.executeQuery("SELECT DISTINCT RECEIVER FROM (" +
                    "SELECT * FROM SHARED_FILES " +
                    "WHERE SENDER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                    "ORDER BY SHARING_DATE" +
                    ")");

            while (resultSet.next()){
                String receiver = resultSet.getString("RECEIVER");

                ResultSet temp = statement1.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + receiver + "'");

                Label label = null, label1 = null;
                if(temp.next())
                    label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));
                label1 = new Label(receiver);
                label1.setFont(new Font(14));
                label1.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label, label1);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;              -fx-border-width: 1.5;" +
                        "       -fx-border-radius: 10;                  -fx-padding: 0 0 0 10;");

                map.put(vBox, receiver);

                MainMenuActionPaneTestResultListView.getItems().add(MainMenuActionPaneTestResultListView.getItems().size(), vBox);
            }
        }

        MainMenuActionPanePrescriptionListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String username = map.get(MainMenuActionPanePrescriptionListView.getSelectionModel().getSelectedItem());

                try {
                    showSharingFiles(username);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        MainMenuActionPaneTestResultListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String username = map.get(MainMenuActionPaneTestResultListView.getSelectionModel().getSelectedItem());

                try {
                    showSharingFiles(username);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    public void showSharingFiles(String username) throws SQLException {
        MainMenuActionPaneMainListView.getItems().clear();
        Statement statement = Medi_collab.connection().createStatement(), statement1 = Medi_collab.connection().createStatement();
        Map<VBox, String> map = new HashMap<>(), map1 = new HashMap<>();
        Map<VBox, Blob> map2 = new HashMap<>();
        Map<VBox, Boolean> flags = new HashMap<>();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM SHARED_FILES " +
                    "WHERE (SENDER = '" + username + "' AND RECEIVER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "') " +
                    "OR (SENDER = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' AND RECEIVER = '" + username + "') " +
                    "ORDER BY SHARING_DATE");

            while (resultSet.next()){
                Label label = null;
                if (resultSet.getString("SUBJECT") == null)
                    label = new Label("<NO SUBJECT>");
                else label = new Label(resultSet.getString("SUBJECT"));
                Label label1 = new Label(((Timestamp) resultSet.getObject("SHARING_DATE")).toString());

                label.setFont(new Font(30));
                label.setTextFill(Color.valueOf("#464646"));
                label1.setFont(new Font(12));
                label1.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label, label1);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;              -fx-border-width: 1.5;" +
                        "       -fx-border-radius: 10;                  -fx-padding: 0 0 0 10;");

                Blob blob = resultSet.getBlob("DESCRIPTION"),
                        blob1 = resultSet.getBlob("WRITING");

                byte[] bytes = null, bytes1 = null;
                if(blob != null)
                    bytes = blob.getBytes(1, (int) blob.length());
                if (blob1 != null)
                    bytes1 = blob1.getBytes(1, (int) blob1.length());

                File file = new File("tempd.txt"),
                        file1 = new File("tempw.txt");

                FileOutputStream fileOutputStream = new FileOutputStream(file),
                        fileOutputStream1 = new FileOutputStream(file1);


                if(bytes != null)
                    fileOutputStream.write(bytes);
                fileOutputStream.close();
                if(bytes1 != null)
                    fileOutputStream1.write(bytes1);
                fileOutputStream1.close();

                String s = Files.readString(Paths.get("tempd.txt")),
                        s1 = Files.readString(Paths.get("tempw.txt"));

                file.delete();
                file1.delete();

                map.put(vBox, s);
                map1.put(vBox, s1);
                map2.put(vBox, resultSet.getBlob("SHARING_FILE"));
                flags.put(vBox, false);

                MainMenuActionPaneMainListView.getItems().add(MainMenuActionPaneMainListView.getItems().size(), vBox);
            }
        }
        catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

        MainMenuActionPaneMainListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                VBox vBox = MainMenuActionPaneMainListView.getSelectionModel().getSelectedItem();

                Label label = null, label1 = null;

                String s = "", s1 = "";
                if (map.get(vBox) != null)
                    s = map.get(vBox);
                if (map1.get(vBox) != null)
                    s1 = map1.get(vBox);

                System.out.println(s + " " + s1);

                label = new Label(s);
                label.setFont(new Font(18));
                label.setTextFill(Color.valueOf("#464646"));
                label1 = new Label(s1);
                label1.setFont(new Font(18));
                label1.setTextFill(Color.valueOf("#464646"));

                if (!flags.get(vBox)) {
                    vBox.getChildren().addAll(label, label1);
                    flags.replace(vBox, true);
                }
                else{
                    vBox.getChildren().remove(2, vBox.getChildren().size());
                    flags.replace(vBox, false);
                }
            }
        });
    }

    public void MainMenuSetAppointmentButtonMouseEntered() {
        if(!setappointmentclicked)
            MainMenuSetAppointmentButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
    }

    public void MainMenuSetAppointmentButtonMouseExited() {
        if(!setappointmentclicked)
            MainMenuSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuAppointmentListButtonMouseEntered() {
        if(!appointmentlistclicked)
            MainMenuAppointmentListButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
    }

    public void MainMenuAppointmentListButtonMouseExited() {
        if(!appointmentlistclicked)
            MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuPrescriptionButtonMouseEntered() {
        if(!prescriptionclicked)
            MainMenuPrescriptionButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
    }

    public void MainMenuPrescriptionButtonMouseExited() {
        if(!prescriptionclicked)
            MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuTestResultButtonMouseEntered() {
        if(!testresultclicked)
            MainMenuTestResultButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
    }

    public void MainMenuTestResultButtonMouseExited() {
        if(!testresultclicked)
            MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT");
    }
}
