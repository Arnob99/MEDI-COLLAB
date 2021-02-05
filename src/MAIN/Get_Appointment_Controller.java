package MAIN;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Get_Appointment_Controller implements Initializable {
    @FXML private Label ProfileAddressLabel;
    @FXML private AnchorPane GetAppointmentActionPane;
    @FXML private AnchorPane GetAppointmentProfilePane;
    @FXML private ImageView ProfilePicImageView;
    @FXML private Label ProfileNameLabel;
    @FXML private Label ProfileEmailLabel;
    @FXML private Label ProfileContactLabel;
    @FXML private Label GetAppointmentLabel;
    @FXML private Label GetAppointmentNotifyLabel;
    @FXML private JFXButton GetAppointmentSubmitButton;
    @FXML private JFXButton GetAppointmentClearButton;
    @FXML private JFXTextField GetAppointmentDoctorUsernameTextField;
    @FXML private JFXDatePicker GetAppointmentDateDatePicker;
    @FXML private JFXTimePicker GetAppointmentTimeFromTimePicker;
    @FXML private JFXTimePicker GetAppointmentTimeToTimePicker;
    @FXML private JFXTextArea GetAppointmentPurposeTextArea;

    Statement statement, statement1;
    ContextMenu contextMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contextMenu = new ContextMenu();

        try {
            statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();
            statement1 = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        int AppointmentActionPaneWidth, AppointmentUsernameDatePaneWidth;

        GetAppointmentLabel.setLayoutX(screen.getWidth()/2.0 - GetAppointmentLabel.getPrefWidth()/2.0);
        AppointmentActionPaneWidth = (int) (screen.getWidth() - GetAppointmentProfilePane.getPrefWidth() - 30);
        AppointmentUsernameDatePaneWidth = AppointmentActionPaneWidth - 120;
        GetAppointmentActionPane.setPrefWidth(AppointmentActionPaneWidth);
        GetAppointmentDoctorUsernameTextField.setPrefWidth(AppointmentUsernameDatePaneWidth/2.0 - 50);
        GetAppointmentDateDatePicker.setPrefWidth(GetAppointmentDoctorUsernameTextField.getPrefWidth());
        GetAppointmentDateDatePicker.setConverter(Medi_collab.localDateStringConverter);
        GetAppointmentTimeFromTimePicker.setPrefWidth(GetAppointmentDoctorUsernameTextField.getPrefWidth());
        GetAppointmentTimeToTimePicker.setPrefWidth(GetAppointmentDoctorUsernameTextField.getPrefWidth());
        GetAppointmentSubmitButton.setLayoutX(AppointmentActionPaneWidth/2.0 - 145);
        GetAppointmentClearButton.setLayoutX(AppointmentActionPaneWidth/2.0 + 25);
        ProfilePicImageView.setLayoutX(GetAppointmentProfilePane.getPrefWidth()/2.0 - ProfilePicImageView.getFitHeight()/2.0);
        GetAppointmentProfilePane.setVisible(false);

        restrictDatePicker(LocalDate.now().plusDays(1), LocalDate.now().plusDays(8));
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleGetAppointmentClearButton() {
        GetAppointmentDoctorUsernameTextField.clear();
        GetAppointmentDateDatePicker.getEditor().clear();
        GetAppointmentPurposeTextArea.clear();
        GetAppointmentTimeFromTimePicker.setValue(new LocalTimeStringConverter().fromString(""));
        GetAppointmentTimeToTimePicker.setValue(new LocalTimeStringConverter().fromString(""));
        GetAppointmentNotifyLabel.setText("");
        GetAppointmentProfilePane.setVisible(false);
    }

    public void handleGetAppointmentSubmitButton() throws SQLException, IOException, InterruptedException {
        boolean alright = true;

        PreparedStatement preparedStatement = Objects.requireNonNull(Medi_collab.connection()).prepareStatement("INSERT INTO APPOINTMENT_TABLE " +
                "VALUES (?, ?, ?, TO_DATE(?, 'dd-mm-yyyy'), ?, ?, null, ?)");

        String doctor = GetAppointmentDoctorUsernameTextField.getText().strip(),
                app_date = (GetAppointmentDateDatePicker.getValue() != null) ? GetAppointmentDateDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : null,
                time_from = (GetAppointmentTimeFromTimePicker.getValue() != null) ? GetAppointmentTimeFromTimePicker.getValue().toString() : null,
                time_to = (GetAppointmentTimeToTimePicker.getValue() != null) ? GetAppointmentTimeToTimePicker.getValue().toString() : null,
                purpose = GetAppointmentPurposeTextArea.getText().strip(),
                id;
        int intID = 0;

        ResultSet resultSet = statement.executeQuery("SELECT MAX(ID) AS MAX FROM APPOINTMENT_TABLE " +
                "WHERE APPOINTMENT_DATE = TO_DATE('" + app_date + "', 'dd-mm-yyyy')");
        if(resultSet.next())
            intID = resultSet.getInt("MAX");
        id = String.valueOf(intID + 1);

        System.out.println(app_date);
        System.out.println(time_from);
        System.out.println(time_to);

        if (doctor.length() == 0) {
            GetAppointmentNotifyLabel.setText("Doctor Every Field Must Be Filled Up!");
            alright = false;
        }
        else {
            resultSet = statement.executeQuery("SELECT USERNAME FROM USERS_TABLE WHERE USERNAME = '" + doctor + "' AND (ROLE = 'Doctor' OR ROLE = 'Staff')");
            if(!resultSet.next()){
                GetAppointmentNotifyLabel.setText("Invalid Username.");
                alright = false;
            }
            else if(resultSet.getString("USERNAME").equals(Medi_collab.User_Info_Resultset.getString("USERNAME"))){
                GetAppointmentNotifyLabel.setText("Invalid Username.");
                alright = false;
            }
        }
        if (app_date == null || app_date.length() == 0){
            GetAppointmentNotifyLabel.setText("Date Every Field Must Be Filled Up!");
            alright = false;
        }
        if (time_from == null || time_from.length() == 0){
            GetAppointmentNotifyLabel.setText("Time from Every Field Must Be Filled Up!");
            alright = false;
        }
        if (time_to == null || time_to.length() == 0){
            GetAppointmentNotifyLabel.setText("Time to Every Field Must Be Filled Up!");
            alright = false;
        }
        if (GetAppointmentTimeFromTimePicker.getValue().isAfter(GetAppointmentTimeToTimePicker.getValue())) {
            GetAppointmentNotifyLabel.setText("Visiting Time Range Error");
            alright = false;
        }

        FileInputStream fileInputStream;
        File temp;
        FileWriter fileWriter;

        if(purpose.length() != 0){
            temp = new File("temp.txt");
            fileWriter = new FileWriter(temp);
            fileWriter.write(purpose);
            fileWriter.close();

            fileInputStream = new FileInputStream(temp);
            preparedStatement.setBlob(3, fileInputStream);
            fileInputStream.close();

            temp.delete();
        }
        else {
            GetAppointmentNotifyLabel.setText("Every Field Must Be Filled Up!");
            alright = false;
        }

        preparedStatement.setString(1, doctor);
        preparedStatement.setString(2, Medi_collab.User_Info_Resultset.getString("USERNAME"));
        preparedStatement.setString(4, app_date);
        preparedStatement.setString(5, time_from);
        preparedStatement.setString(6, time_to);
        preparedStatement.setString(7, id);

        if(alright){
            preparedStatement.executeQuery();

            ApprovalDialogue approvalDialogue = new ApprovalDialogue();
            approvalDialogue.OpenDialogue();

            GetAppointmentNotifyLabel.setText("APPOINTMENT PENDING FOR APPROVAL");
            GetAppointmentNotifyLabel.setTextFill(Color.valueOf("2ed2ff"));
        }
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

    public void handleGetAppointmentDoctorUsernameTextField() throws SQLException {
        if (contextMenu.isShowing())
            contextMenu.hide();

        contextMenu.getItems().clear();

        String s = GetAppointmentDoctorUsernameTextField.getText();
        if(!s.isEmpty()) {
            ResultSet resultSet = statement.executeQuery("SELECT USERNAME FROM USERS_TABLE " +
                    "WHERE USERNAME LIKE '%" + s + "%' AND (ROLE = 'Doctor' OR ROLE = 'Staff') " +
                    "AND USERNAME != '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "'" +
                    "ORDER BY USERNAME");

            List<String> stringList = new LinkedList<>();

            while (resultSet.next())
                stringList.add(resultSet.getString("USERNAME"));

            List<CustomMenuItem> customMenuItemList = new LinkedList<>();

            for (String s1 : stringList) {
                Label label = new Label(s1);
                label.setPrefWidth(GetAppointmentDoctorUsernameTextField.getPrefWidth());
                label.setFont(new Font(13));
                label.setPadding(new Insets(5));

                CustomMenuItem customMenuItem = new CustomMenuItem(label, true);

                customMenuItemList.add(customMenuItem);

                customMenuItem.setOnAction(e -> {
                    GetAppointmentDoctorUsernameTextField.setText(s1);
                    GetAppointmentDoctorUsernameTextField.positionCaret(s1.length());

                    contextMenu.hide();

                    ResultSet resultSet1;
                    try {
                        resultSet1 = statement1.executeQuery("SELECT * FROM USERS_TABLE " +
                                "WHERE USERNAME = '" + s1 + "'");
                        resultSet1.next();

                        GetAppointmentProfilePane.setVisible(true);
                        ProfileNameLabel.setText(resultSet1.getString("FIRSTNAME") + " " + resultSet1.getString("LASTNAME"));
                        ProfileEmailLabel.setText(resultSet1.getString("EMAIL"));
                        ProfileContactLabel.setText(resultSet1.getString("CONTACT"));
                        ProfileAddressLabel.setText(resultSet1.getString("ADDRESS"));
                        ProfileAddressLabel.setWrapText(true);
                    }
                    catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }

            contextMenu.getItems().addAll(customMenuItemList);
            contextMenu.show(GetAppointmentDoctorUsernameTextField, Side.BOTTOM, 0, 0);
        }
    }

    public void restrictDatePicker (LocalDate minDate, LocalDate maxDate) {
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-text-fill: #2ed2ff");
                        }
                        else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-text-fill: #2ed2ff");
                        }
                    }
                };
            }
        };
        GetAppointmentDateDatePicker.setDayCellFactory(dayCellFactory);
    }
}
