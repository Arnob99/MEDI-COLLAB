package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Writing_Controller implements Initializable {

    @FXML private AnchorPane WritingProfilePane;
    @FXML private Label ProfileNameLabel;
    @FXML private Label ProfileEmailLabel;
    @FXML private Label ProfileContactLabel;
    @FXML private Label ProfileAddress;
    @FXML private Label ProfileAddressLabel;
    @FXML private JFXButton CancelButton;
    @FXML private JFXButton WritingSendButton;
    @FXML private JFXTextArea WritingWriteHereTextArea;
    @FXML private JFXTextArea WritingDescriptionTextArea;
    @FXML private JFXTextField WritingSubjectTextField;
    @FXML private Label WritingNotifyLabel;
    @FXML private JFXTextField WritingToTextField;
    @FXML private Label WritingChooseFileLabel;

    public File file = null;

    Statement statement, statement1;
    ContextMenu contextMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contextMenu = new ContextMenu();

        try {
            statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();
            statement1 = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        WritingProfilePane.setVisible(false);

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        WritingSendButton.setLayoutX(screen.getWidth()/2.0 - 130);
        CancelButton.setLayoutX(screen.getWidth()/2.0 + 10);
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleWritingSendButton(ActionEvent actionEvent) {
        String sender;
        boolean alright = true;
        try {
            sender = Medi_collab.User_Info_Resultset.getString("USERNAME");
            String receiver = WritingToTextField.getText();

            PreparedStatement preparedStatement = Objects.requireNonNull(Medi_collab.connection()).prepareStatement("INSERT INTO SHARED_FILES " +
                    "VALUES (?, ?, ?, ?, ?, SYSDATE, ?, ?, ?)");

            FileInputStream fileInputStream;

            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, receiver);
            preparedStatement.setString(3, WritingSubjectTextField.getText());

            File temp = new File("temp.txt");
            FileWriter fileWriter = new FileWriter(temp, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            if(WritingDescriptionTextArea.getText().strip().length() != 0){
                printWriter.write(WritingDescriptionTextArea.getText().strip() + "\n\n");
            }

            printWriter.write(WritingWriteHereTextArea.getText().strip());
            printWriter.close();

            fileInputStream = new FileInputStream(temp);
            preparedStatement.setBlob(4, fileInputStream);
            fileInputStream.close();

            temp.delete();

            if(file != null) {
                fileInputStream = new FileInputStream(file);
                preparedStatement.setBlob(5, fileInputStream);
                fileInputStream.close();

                file.delete();
            }
            else
                preparedStatement.setBlob(5, InputStream.nullInputStream());

            preparedStatement.setString(6, Medi_collab.role);
            preparedStatement.setString(7, "Patient");
            preparedStatement.setString(8, sender);

            ResultSet resultSet = statement.executeQuery("SELECT USERNAME FROM USERS_TABLE " +
                    "WHERE USERNAME = '" + receiver + "' AND ROLE = 'Patient'");

            if(!resultSet.next()) {
                alright = false;
                WritingNotifyLabel.setText("Invalid Receiver Username!");
                WritingNotifyLabel.setTextFill(Color.RED);
            }

            if(WritingWriteHereTextArea.getText().strip().length() == 0){
                alright = false;
                WritingNotifyLabel.setText("Main Writing Field Cannot Be Empty!");
                WritingNotifyLabel.setTextFill(Color.RED);
            }

            if(alright) {
                preparedStatement.execute();
                ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
            }
        }
        catch (SQLException | IOException throwables) {
            throwables.printStackTrace();

            WritingNotifyLabel.setText("NOT SENT!");
            WritingNotifyLabel.setTextFill(Color.RED);
        }
    }

    public void handleWritingChooseFileButton() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);

        if(file != null){
            WritingChooseFileLabel.setText(file.getName());
        }
    }

    public void handleWritingToTextField() throws SQLException {
        if (contextMenu.isShowing())
            contextMenu.hide();

        contextMenu.getItems().clear();

        String s = WritingToTextField.getText();
        if(!s.isEmpty()) {
            ResultSet resultSet = statement.executeQuery("SELECT USERNAME FROM USERS_TABLE " +
                    "WHERE USERNAME LIKE '%" + s + "%' AND ROLE = 'Patient' " +
                    "ORDER BY USERNAME");

            List<String> stringList = new LinkedList<>();

            while (resultSet.next())
                stringList.add(resultSet.getString("USERNAME"));

            List<CustomMenuItem> customMenuItemList = new LinkedList<>();

            for (String s1 : stringList) {
                Label label = new Label(s1);
                label.setPrefWidth(WritingToTextField.getPrefWidth());
                label.setFont(new Font(13));
                label.setPadding(new Insets(5));

                CustomMenuItem customMenuItem = new CustomMenuItem(label, true);

                customMenuItemList.add(customMenuItem);

                customMenuItem.setOnAction(actionEvent -> {
                    WritingToTextField.setText(s1);
                    WritingToTextField.positionCaret(s1.length());

                    contextMenu.hide();

                    ResultSet resultSet1;
                    try {
                        resultSet1 = statement1.executeQuery("SELECT * FROM USERS_TABLE " +
                                "WHERE USERNAME = '" + s1 + "'");
                        resultSet1.next();

                        WritingProfilePane.setVisible(true);
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
            contextMenu.show(WritingToTextField, Side.BOTTOM, 0, 0);
        }
    }
}
