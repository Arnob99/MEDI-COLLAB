package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Main_Menu_Controller implements Initializable {
    @FXML private JFXButton MainMenuMyProfileButton;
    @FXML private JFXButton MainMenuSignOutButton;
    @FXML private Label MainMenuMediCollabLabel;
    @FXML private JFXButton MainMenuSetAppointmentButton;
    @FXML private JFXButton MainMenuAppointmentListButton;
    @FXML private JFXButton MainMenuPrescriptionButton;
    @FXML private JFXButton MainMenuTestResultButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        MainMenuMediCollabLabel.setLayoutX(screen.getWidth()/2 - MainMenuMediCollabLabel.getPrefWidth()/2);
        try {
            MainMenuMyProfileButton.setText(Medi_collab.User_Info_Resultset.getString("FIRSTNAME") +
                    " " + Medi_collab.User_Info_Resultset.getString("LASTNAME"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (Medi_collab.role.equals("Patient")) {
            MainMenuMyProfileButton.setLayoutY(screen.getHeight() / 2.0 - 140);
            MainMenuSetAppointmentButton.setLayoutY(MainMenuMyProfileButton.getLayoutY() + MainMenuMyProfileButton.getPrefHeight() + 12);
            MainMenuAppointmentListButton.setLayoutY(MainMenuSetAppointmentButton.getLayoutY() + MainMenuSetAppointmentButton.getPrefHeight() + 12);
            MainMenuPrescriptionButton.setLayoutY(MainMenuAppointmentListButton.getLayoutY() + MainMenuAppointmentListButton.getPrefHeight() + 12);
            MainMenuTestResultButton.setLayoutY(MainMenuPrescriptionButton.getLayoutY() + MainMenuPrescriptionButton.getPrefHeight() + 12);
        }
        else if (Medi_collab.role.equals("Doctor")) {
            MainMenuMyProfileButton.setLayoutY(screen.getHeight() / 2.0 - 113);
            MainMenuSetAppointmentButton.setVisible(false);
            MainMenuAppointmentListButton.setLayoutY(MainMenuMyProfileButton.getLayoutY() + MainMenuMyProfileButton.getPrefHeight() + 12);
            MainMenuPrescriptionButton.setLayoutY(MainMenuAppointmentListButton.getLayoutY() + MainMenuAppointmentListButton.getPrefHeight() + 12);
            MainMenuTestResultButton.setLayoutY(MainMenuPrescriptionButton.getLayoutY() + MainMenuPrescriptionButton.getPrefHeight() + 12);
        }
        else {
            MainMenuMyProfileButton.setLayoutY(screen.getHeight() / 2.0 - 87);
            MainMenuSetAppointmentButton.setVisible(false);
            MainMenuAppointmentListButton.setLayoutY(MainMenuMyProfileButton.getLayoutY() + MainMenuMyProfileButton.getPrefHeight() + 12);
            MainMenuPrescriptionButton.setVisible(false);
            MainMenuTestResultButton.setLayoutY(MainMenuAppointmentListButton.getLayoutY() + MainMenuAppointmentListButton.getPrefHeight() + 12);
        }
        MainMenuSignOutButton.setLayoutX(screen.getWidth()/2.0 - MainMenuSignOutButton.getPrefWidth()/2.0);
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleMainMenuMyProfileButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("User_Profile.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/User_Profile.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleMainMenuSignOutButton(ActionEvent actionEvent) throws IOException {
        Medi_collab.User_Info_Resultset = null;

        Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

        ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/Resources/CSS/Sign_In.css");
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public void handleMainMenuSetAppointmentButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Get_Appointment.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Get_Appointment.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleMainMenuAppointmentListButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Appointment_List.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Appointment_List.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleMainMenuPrescriptionButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Prescription.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Prescription.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleMainMenuTestResultButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Test_Result.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Test_Result.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }
}
