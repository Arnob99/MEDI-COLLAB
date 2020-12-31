package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main_Menu_Doctor_Controller {
    @FXML
    private JFXButton MainMenuAppointmentListButton;
    @FXML
    private JFXButton MainMenuPrescriptionButton;
    @FXML
    private JFXButton MainMenuTestResultButton;

    public boolean appointmentlistclicked = false, prescriptionclicked = false, testresultclicked = false;

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMainMenuAppointmentListButton(ActionEvent actionEvent) {
        MainMenuAppointmentListButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        appointmentlistclicked = true;
        prescriptionclicked = false;
        testresultclicked = false;
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

    public void handleMainMenuPrescriptionButton(ActionEvent actionEvent) throws IOException {
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");
        MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");

        appointmentlistclicked = false;
        prescriptionclicked = true;
        testresultclicked = false;

//        Parent root = FXMLLoader.load(getClass().getResource("Writing.fxml"));
//
//        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
//
//        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
//        scene.getStylesheets().add(Medi_collab.stylesheetaddress);
//        scene.setFill(Color.TRANSPARENT);
//
//        stage.setScene(scene);
//        stage.show();
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

    public void handleMainMenuTestResultButton(ActionEvent actionEvent) {
        MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: #464646");
        MainMenuTestResultButton.setStyle("-fx-background-color: #2ed2ff; -fx-text-fill: WHITE");

        appointmentlistclicked = false;
        prescriptionclicked = false;
        testresultclicked = true;
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void MainMenuAppointmentListButtonMouseEntered(MouseEvent mouseEvent) {
        if(!appointmentlistclicked)
            MainMenuAppointmentListButton.setStyle("-fx-background-color: #a7fafa");
    }

    public void MainMenuAppointmentListButtonMouseExited(MouseEvent mouseEvent) {
        if(!appointmentlistclicked)
            MainMenuAppointmentListButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuPrescriptionButtonMouseEntered(MouseEvent mouseEvent) {
        if(!prescriptionclicked)
            MainMenuPrescriptionButton.setStyle("-fx-background-color: #a7fafa");
    }

    public void MainMenuPrescriptionButtonMouseExited(MouseEvent mouseEvent) {
        if(!prescriptionclicked)
            MainMenuPrescriptionButton.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void MainMenuTestResultButtonMouseEntered(MouseEvent mouseEvent) {
        if(!testresultclicked)
            MainMenuTestResultButton.setStyle("-fx-background-color: #a7fafa");
    }

    public void MainMenuTestResultButtonMouseExited(MouseEvent mouseEvent) {
        if(!testresultclicked)
            MainMenuTestResultButton.setStyle("-fx-background-color: TRANSPARENT");
    }
}
