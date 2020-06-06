package zad1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class ClientGUI extends Application {

    ClientService service;
    private TextField phraseTF;
    private TextField resultTF;
    private ComboBox languageSelectorCmb;
    private Button resetBtn;
    private int port = 8090;
    private Button translateBtn;

    public int getPort() {
        return port;
    }

    public String getSelectedLanguage() {
        String out = languageSelectorCmb.getSelectionModel().getSelectedItem().toString();
        return out;
    }

    public String getPhrase() {
        return phraseTF.getText();
    }

    public void setResultTF(String translation) {
        resultTF.setText(translation);
    }

    public static final ObservableList languages =
            FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    public ClientService getService() {
        return service;
    }

    @Override
    public void start(Stage primaryStage) {
        service = new ClientService(this);
        loadLanguages();
        languageSelectorCmb = new ComboBox(languages);
        phraseTF = new TextField("");
        resultTF = new TextField("");
        translateBtn = new Button("Translate");
        resetBtn = new Button("Reset");


        translateBtn.setOnAction(e -> {
            try {
                service.translate();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        resetBtn.setOnAction(e -> {
            loadLanguages();
            phraseTF.setText("");
            resultTF.setText("");
        });

        GridPane gridpane = new GridPane();
        gridpane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        gridpane.setVgap(10); //vertical gap in pixels
        gridpane.add(new Label("Select language"), 0, 0); // column=1 row=0
        gridpane.add(languageSelectorCmb, 1, 0);  // column=2 row=0
        gridpane.add(new Label("Enter phrase"), 0, 1);  // column=2 row=0
        gridpane.add(phraseTF, 1, 1);  // column=2 row=0

        gridpane.add(new Label("Results"), 0, 2);  // column=2 row=0
        gridpane.add(resultTF, 1, 2);  // column=2 row=0

        gridpane.add(resetBtn, 0, 3);  // column=2 row=0
        gridpane.add(translateBtn, 1, 3);  // column=2 row=0


        Scene scene = new Scene(gridpane, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    void loadLanguages() {
        languages.clear();
        try {

            String serverLangs = service.getLanguages();

            languages.addAll(serverLangs.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
