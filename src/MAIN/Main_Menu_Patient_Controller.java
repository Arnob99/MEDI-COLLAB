package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main_Menu_Patient_Controller {
    @FXML
    private JFXButton MainMenuPatientSetAppointmentButton;
    @FXML
    private JFXButton MainMenuAppointmentListButton;
    @FXML
    private JFXButton MainMenuPrescriptionButton;
    @FXML
    private JFXButton MainMenuTestResultButton;
    @FXML
    private Label MainMenuPatientSetAppointmentsPane;
    @FXML
    private Label MainMenuPatientAppointmentListPane;

    public boolean setappointmentclicked = false, appointmentlistclicked = false, prescriptionclicked = false, testresultclicked = false;

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMainMenuAppointmentListButton() {
        MainMenuPatientSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        setappointmentclicked = false;
        appointmentlistclicked = true;
        prescriptionclicked = false;
        testresultclicked = false;

        MainMenuPatientAppointmentListPane.setVisible(true);
        MainMenuPatientSetAppointmentsPane.setVisible(false);
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
        MainMenuPatientSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        setappointmentclicked = false;
        appointmentlistclicked = false;
        prescriptionclicked = true;
        testresultclicked = false;
    }

    public void handleMainMenuSetAppointmentButton() {
        MainMenuPatientSetAppointmentButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        setappointmentclicked = true;
        appointmentlistclicked = false;
        prescriptionclicked = false;
        testresultclicked = false;

        MainMenuPatientSetAppointmentsPane.setVisible(true);
        MainMenuPatientAppointmentListPane.setVisible(false);
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
        MainMenuPatientSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");

        setappointmentclicked = false;
        appointmentlistclicked = false;
        prescriptionclicked = false;
        testresultclicked = true;
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void MainMenuPatientSetAppointmentButtonMouseEntered() {
        if(!setappointmentclicked)
            MainMenuPatientSetAppointmentButton.setStyle("-fx-background-color: #a7fafa");
    }

    public void MainMenuPatientSetAppointmentButtonMouseExited() {
        if(!setappointmentclicked)
            MainMenuPatientSetAppointmentButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuAppointmentListButtonMouseEntered() {
        if(!appointmentlistclicked)
            MainMenuAppointmentListButton.setStyle("-fx-background-color: #a7fafa");
    }

    public void MainMenuAppointmentListButtonMouseExited() {
        if(!appointmentlistclicked)
            MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuPrescriptionButtonMouseEntered() {
        if(!prescriptionclicked)
            MainMenuPrescriptionButton.setStyle("-fx-background-color: #a7fafa");
    }

    public void MainMenuPrescriptionButtonMouseExited() {
        if(!prescriptionclicked)
            MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuTestResultButtonMouseEntered() {
        if(!testresultclicked)
            MainMenuTestResultButton.setStyle("-fx-background-color: #a7fafa");
    }

    public void MainMenuTestResultButtonMouseExited() {
        if(!testresultclicked)
            MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT");
    }
}
