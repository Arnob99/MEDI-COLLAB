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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Main_Menu_Controller {
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

        handleRefreshButton();

//        Label label1 = new Label("Tausif Khan Arnob");
//        label1.setFont(new Font(20));
//        Label label2 = new Label("Date: 31/03/2000");
//        label2.setFont(new Font(11));
//        Label label3 = new Label("Is it working?");
//        label3.setFont(new Font(14));
//
//        VBox buttonbox1 = new VBox(5, label1), buttonbox2 = new VBox(5, label2), buttonbox3 = new VBox(5, label3);
//        buttonbox1.setAlignment(Pos.CENTER_LEFT);
//        buttonbox1.setStyle("-fx-background-color: TRANSPARENT; -fx-background-radius: 10; -fx-padding: 0 0 0 10;" +
//                "-fx-border-color: #2ed2ff; -fx-border-width: 1.5; -fx-border-radius: 10;");
//        buttonbox2.setAlignment(Pos.CENTER_LEFT);
//        buttonbox2.setStyle("-fx-background-color: TRANSPARENT; -fx-background-radius: 10; -fx-padding: 0 0 0 10;" +
//                "-fx-border-color: #2ed2ff; -fx-border-width: 1.5; -fx-border-radius: 10;");
//        buttonbox3.setAlignment(Pos.CENTER_LEFT);
//        buttonbox3.setStyle("-fx-background-color: TRANSPARENT; -fx-background-radius: 10; -fx-padding: 0 0 0 10;" +
//                "-fx-border-color: #2ed2ff; -fx-border-width: 1.5; -fx-border-radius: 10;");
//
//        MainMenuActionPanePrescriptionListView.getItems().addAll(buttonbox1, buttonbox2, buttonbox3);
//
//        Map<VBox, String> mp = new HashMap<>();
//        mp.put(buttonbox1, "NAME");
//        mp.put(buttonbox2, "DOB");
//        mp.put(buttonbox3, "WORKING");
//
//        MainMenuActionPanePrescriptionListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                System.out.println(mp.get(MainMenuActionPanePrescriptionListView.getSelectionModel().getSelectedItem()));
//            }
//        });
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
    }

    public void handleMainMenuSignOutButton(ActionEvent actionEvent) throws IOException {
        Medi_collab.User_Info_Resultset = null;
        Medi_collab.admin_logged_in = false;

        Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(Medi_collab.stylesheetaddress);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
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
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleMainMenuComposeButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Writing.fxml"));

        Parent root = fxmlLoader.load();

        Scene oldscene = ((Node)actionEvent.getSource()).getScene();

        Scene scene = new Scene(root, oldscene.getWidth(), oldscene.getHeight());
        scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void handleRefreshButton() throws SQLException {
        MainMenuActionPanePrescriptionListView.getItems().clear();
        MainMenuActionPaneTestResultListView.getItems().clear();

        Map<VBox, String> map = new HashMap<>();
        Statement statement = Medi_collab.connection().createStatement(), tempstatement = Medi_collab.connection().createStatement();
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

                ResultSet temp = tempstatement.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + sender + "'");

                Label label = null;
                if(temp.next())
                    label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;              -fx-border-width: 1.5;" +
                        "       -fx-pref-height: 50;                    -fx-padding: 0 0 0 10;" +
                        "       -fx-border-radius: 10;");

                map.put(vBox, sender);

                temp = tempstatement.executeQuery("SELECT ROLE FROM USERS_TABLE WHERE USERNAME = '" + sender + "'");

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

                ResultSet temp = tempstatement.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + receiver + "'");

                Label label = null;
                if(temp.next())
                    label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(5, label);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;              -fx-border-width: 1.5;" +
                        "       -fx-pref-height: 50;                    -fx-padding: 0 0 0 10;" +
                        "       -fx-border-radius: 10;");

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

                ResultSet temp = tempstatement.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + receiver + "'");

                Label label = null;
                if(temp.next())
                    label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));

                VBox vBox = new VBox(label);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                        "       -fx-border-color: #2ed2ff;              -fx-border-width: 1.5;" +
                        "       -fx-pref-height: 50;                    -fx-padding: 0 0 0 10;" +
                        "       -fx-border-radius: 10;");

                map.put(vBox, receiver);

                MainMenuActionPaneTestResultListView.getItems().add(MainMenuActionPaneTestResultListView.getItems().size(), vBox);
            }
        }

        MainMenuActionPanePrescriptionListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String username = map.get(MainMenuActionPanePrescriptionListView.getSelectionModel().getSelectedItem());

                try {
                    ResultSet temp = tempstatement.executeQuery("SELECT * FROM USERS_TABLE WHERE USERNAME = '" + username + "'");
                    if(temp.next())
                        System.out.println(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        MainMenuActionPaneTestResultListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String username = map.get(MainMenuActionPaneTestResultListView.getSelectionModel().getSelectedItem());

                try {
                    ResultSet temp = tempstatement.executeQuery("SELECT * FROM USERS_TABLE WHERE USERNAME = '" + username + "'");
                    if(temp.next())
                        System.out.println(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
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
